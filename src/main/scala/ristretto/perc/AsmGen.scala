package ristretto.perc

// Generate Asm with Pseudo register from Perc
object AsmGen {
  import ristretto.perc.{PercSyntax => P}
  import ristretto.asm.AsmSyntax._
  import ristretto.main.{Compiler => Errors}

  type Label = String
  type Temp = String

  type TempName = String
  type Name = String

  val WORDSIZE = 8

  // Pop pseudo-instruction
  object Pop {
    def apply() = Add(Immediate(WORDSIZE), SP())
  }

  def translate(t: P.Root, linux: Boolean): Root = new Translator(linux).translate(t)

  class Translator(linux: Boolean) {
    def translate(t: P.Root): Root = t match {
      case P.Root(procs) =>
        val aprocs = procs map {
          case proc @ P.Proc(label, params, stms) =>
            if (linux)
              Proc(label, translate(stms, params))
            else
              Proc(s"_$label", translate(stms, params))
        }
        Root(aprocs)
    }

    def translate(stms: List[P.Stm], params: List[TempName]): List[Insn] = {
      // Copy the parameters into the function.
      val saveParams = params.zipWithIndex map {
        case (x, i) => Mov(Param(i), Pseudo(x))
      }

      val insns = for {
          s <- stms
          insn <- translate(s)
        } yield insn

      saveParams ++ peephole(insns)
    }

    def usesOneAddress(op1: Operand, op2: Operand): Boolean = ! (isAddress(op1) && isAddress(op2))

    def isAddress(op: Operand): Boolean = op match {
      case Address(_, _) => true
      // On x86-64 the firt 6 parameters are passed in registers, the rest on the stack.
      case Arg(i) if (i >= 6 || WORDSIZE == 4) => true
      case Param(i) if (i >= 6 || WORDSIZE == 4) => true
      case Pseudo(_) => false
      case _ => false
    }

    // Peephole optimizations
    def peephole(insns: List[Insn]): List[Insn] = insns match {
      // collapse jump to next instruction
      case Jmp(label1)::Label(label2)::rest if label1 == label2 =>
        peephole(Label(label2)::rest)

      // collapse consequtive jumps
      case Jmp(label1)::Jmp(label2)::rest =>
        peephole(Jmp(label1)::rest)

      // Otherwise do nothing
      case insn::insns => insn::peephole(insns)
      case Nil => Nil
    }

    def cjump(cmp: P.Comparison, label: String): Insn = cmp match {
      case P.LT() => JL(label)
      case P.GT() => JG(label)
      case P.LE() => JLE(label)
      case P.GE() => JGE(label)
      case P.EQ() => JE(label)
      case P.NE() => JNE(label)
    }

    def binop(operator: P.Operator, op1: Operand, op2: Operand, dst: Operand): List[Insn] = operator match {
      case P.ADD() => Mov(op1, dst) :: Add(op2, dst) :: Nil
      case P.SUB() => Mov(op1, dst) :: Sub(op2, dst) :: Nil
      case P.MUL() => Mov(op1, dst) :: Mul(op2, dst) :: Nil
      case P.DIV() =>
        // Divide instruction divides DX:AX by BX, putting the quotient in AX and the remainder in DX.
        Mov(Immediate(0), DX()) ::
        Mov(op1, AX()) ::
        Mov(op2, BX()) ::
        Div(BX()) ::
        Mov(AX(), dst) ::
        Nil
      case P.REM() =>
        // Divide instruction divides DX:AX by BX, putting the quotient in AX and the remainder in DX.
        Mov(Immediate(0), DX()) ::
        Mov(op1, AX()) ::
        Mov(op2, BX()) ::
        Div(BX()) ::
        Mov(DX(), dst) ::
        Nil
    }

    def translate(s: P.Stm): List[Insn] = CommentInsn(s"$s") :: translateStm(s)

    def translateStm(s: P.Stm): List[Insn] = s match {
      case P.Nop() =>
        Mov(AX(), AX()) :: Nil
      case P.ErrorStm(n) =>
        Mov(Immediate(n), Arg(0)) :: Call(Name("_ristretto_die")) :: Nil
      case P.CJmp(cmp, e, label) =>
        val (insns, op) = translate(e)
        insns :+ Cmp(Immediate(0), op) :+ cjump(cmp, label)
      case P.Jmp(label) =>
        Jmp(label) :: Nil
      case P.Store(addr, value) =>
        val (insns1, op1) = translate(addr)
        val (insns2, op2) = translate(value)
        insns1 ++ insns2 :+ Mov(op2, op1)
      case P.Call(t, P.Global(label)) =>
        // The return value is in AX
        // On linux we don't need an _ in front of the name.
        if (linux)
          Call(Name(label)) :: Mov(AX(), Pseudo(t)) :: Nil
        else
          Call(Name(s"_$label")) :: Mov(AX(), Pseudo(t)) :: Nil
      case P.Call(t, e) =>
        val (insns, op) = translate(e)
        // The return value is in AX
        insns :+ Call(op) :+ Mov(AX(), Pseudo(t))
      case P.Load(t, addr) =>
        val (insns, op) = translate(addr)
        insns :+ Mov(op, Pseudo(t))
      case P.Move(t, e) =>
        val (insns, op) = translate(e)
        insns :+ Mov(op, Pseudo(t))
      case P.Bin(t, op, e1, e2) =>
        val (insns1, op1) = translate(e1)
        val (insns2, op2) = translate(e2)
        insns1 ++ insns2 ++ binop(op, op1, op2, Pseudo(t))

      case P.SetArg(i, e) =>
        val (insns, op) = translate(e)
        insns :+ Mov(op, Arg(i))

      case P.Ret(e) =>
        val (insns, op) = translate(e)
        insns :+ Mov(op, AX()) :+ Ret()

      case P.LabelStm(label) =>
        Label(label)::Nil
    }

    // translate an expression, returning a register, pseudo reg, or immediate
    def translate(e: P.Exp): (List[Insn], Operand) = e match {
      case P.Temp(t) =>
        (Nil, Pseudo(t))
      case P.Global(label) =>
        val t = newTemp()
        (Mov(Name(label), Pseudo(t))::Nil, Pseudo(t))
      case P.Lit(v) =>
        (Nil, Immediate(v))
      case P.Address(offset, base) =>
        val (insns, op) = translate(base)
        // The base should be a register (or pseudo)
        op match {
          case op @ Immediate(v) =>
            (insns, Immediate(offset + v))
          case op =>
            (insns, Address(offset, op))
        }
    }

    def newLabel(): Label = {
      ristretto.main.FreshId.freshId("Lasm")
    }

    def newTemp(): Temp = {
      ristretto.main.FreshId.freshId("tasm")
    }
  }
}
