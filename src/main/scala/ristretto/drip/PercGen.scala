package ristretto.drip

// Generate Drip from Ristretto ASTs
object PercGen {
  import ristretto.perc.{PercSyntax => P}
  import ristretto.drip.{DripSyntax => D}

  type Label = String
  type Temp = String

  type TempName = String
  type Name = String

  def translate(t: D.Root): P.Root = t match {
    case D.Root(dprocs) =>
      val procs = dprocs map {
        case D.Proc(f, params, body) =>
          val t = newTemp()
          P.Proc(f, params.map {
            case x => x
          }, translate(body, t) :+ P.Ret(P.Temp(t)))
      }
      P.Root(procs)
  }

  object d2p {
    def unapply(op: D.Operator): Option[P.Comparison] = op match {
      case D.EQ() => Some(P.EQ())
      case D.LT() => Some(P.LT())
      case D.GT() => Some(P.GT())
      case D.LE() => Some(P.LE())
      case D.GE() => Some(P.GE())
      case D.NE() => Some(P.NE())
      case _ => None
    }
  }

  def translate(e: D.Stm): List[P.Stm] = e match {
    case D.Nop() => P.Nop()::Nil
    case D.ErrorStm(n) =>
      P.ErrorStm(n)::Nil

    case D.CJmp(D.BinOp(d2p(cmp), D.Temp(t1), D.Temp(t2)), tgt) =>
      val t3 = newTemp()
      P.Bin(t3, P.SUB(), P.Temp(t1), P.Temp(t2)) ::
      P.CJmp(cmp, P.Temp(t3), tgt) ::
      Nil
    case D.CJmp(D.BinOp(d2p(cmp), D.Temp(t1), e2), tgt) =>
      val t2 = newTemp()
      val t3 = newTemp()
      translate(e2, t2) :+
      P.Bin(t3, P.SUB(), P.Temp(t1), P.Temp(t2)) :+
      P.CJmp(cmp, P.Temp(t3), tgt)
    case D.CJmp(D.BinOp(d2p(cmp), e1, D.Temp(t2)), tgt) =>
      val t1 = newTemp()
      val t3 = newTemp()
      translate(e1, t1) :+
      P.Bin(t3, P.SUB(), P.Temp(t1), P.Temp(t2)) :+
      P.CJmp(cmp, P.Temp(t3), tgt)
    case D.CJmp(D.BinOp(d2p(cmp), e1, e2), tgt) =>
      val t1 = newTemp()
      val t2 = newTemp()
      val t3 = newTemp()
      (translate(e1, t1) ++ translate(e2, t2)) :+
      P.Bin(t3, P.SUB(), P.Temp(t1), P.Temp(t2)) :+
      P.CJmp(cmp, P.Temp(t3), tgt)

    case D.CJmp(e, tgt) =>
      val t = newTemp()
      translate(e, t) :+ P.CJmp(P.NE(), P.Temp(t), tgt)

    case D.Jmp(tgt) => P.Jmp(tgt)::Nil

    case D.Store(offset, D.Temp(ta), D.Temp(tv)) =>
      P.Store(P.Address(offset, P.Temp(ta)), P.Temp(tv)) :: Nil
    case D.Store(offset, address, value) =>
      val ta = newTemp()
      val tv = newTemp()
      translate(address, ta) ++ (
        translate(value, tv) :+ (
          P.Store(P.Address(offset, P.Temp(ta)), P.Temp(tv))
        )
      )
    case D.Move(t, e) =>
      translate(e, t)
    case D.LabelStm(l) => P.LabelStm(l)::Nil
  }

  def translateExp(e: D.Exp): P.Exp = e match {
    case D.Temp(x) => P.Temp(x)
    case D.Lit(n) => P.Lit(n)
    case D.Global(x) => P.Global(x)
    case _ => throw new RuntimeException("bad exp " + e)
  }

  def translate(e: D.Exp, t: TempName): List[P.Stm] = e match {
    case D.Begin(stms, e) =>
      val ps = stms flatMap {
        s => translate(s)
      }
      ps ++ translate(e, t)

    case D.Call(f, es) =>
      // Be careful to evaluate all arguments into temporaries *and then*
      // set all the argument registers. Otherwise a later argument
      // might overwrite one of the earlier arguments.
      val ts = es map { _ => newTemp() }
      val evalArgs = (es zip ts) flatMap {
        case (e, t) => translate(e, t)
      }
      val setArgs = ts.zipWithIndex map {
        case (t, i) => P.SetArg(i, P.Temp(t))
      }
      evalArgs ++ (setArgs :+ P.Call(t, translateExp(f)))

    case D.Alloc(sz) =>
      val tsz = newTemp();
      translate(sz, tsz) :+ P.SetArg(0, P.Temp(tsz)) :+ P.Call(t, P.Global("ristretto_alloc"))

    case D.Load(offset, D.Temp(t0)) =>
      P.Load(t, P.Address(offset, P.Temp(t0))) :: Nil

    case D.Load(offset, addr) =>
      val t0 = newTemp()
      translate(addr, t0) :+
      P.Load(t, P.Address(offset, P.Temp(t0)))

    case D.BinOp(D.EQ(), e1, e2) =>
      val t1 = newTemp()
      val t2 = newTemp()
      val t3 = newTemp()
      val fLabel = newLabel()
      translate(e1, t1) ++ (
        translate(e2, t2) ++ (
          P.Bin(t3, P.SUB(), P.Temp(t1), P.Temp(t2))::
          P.Move(t, P.Lit(0))::
          P.CJmp(P.NE(), P.Temp(t3), fLabel)::
          P.Move(t, P.Lit(1))::
          P.LabelStm(fLabel)::
          Nil
        )
      )
    case D.BinOp(D.NE(), e1, e2) =>
      val t1 = newTemp()
      val t2 = newTemp()
      val t3 = newTemp()
      val fLabel = newLabel()
      translate(e1, t1) ++ (
        translate(e2, t2) ++ (
          P.Bin(t3, P.SUB(), P.Temp(t1), P.Temp(t2))::
          P.Move(t, P.Lit(0))::
          P.CJmp(P.EQ(), P.Temp(t3), fLabel)::
          P.Move(t, P.Lit(1))::
          P.LabelStm(fLabel)::
          Nil
        )
      )
    case D.BinOp(D.LT(), e1, e2) =>
      val t1 = newTemp()
      val t2 = newTemp()
      val t3 = newTemp()
      val fLabel = newLabel()
      translate(e1, t1) ++ (
        translate(e2, t2) ++ (
          P.Bin(t3, P.SUB(), P.Temp(t1), P.Temp(t2))::
          P.Move(t, P.Lit(0))::
          P.CJmp(P.GE(), P.Temp(t3), fLabel)::
          P.Move(t, P.Lit(1))::
          P.LabelStm(fLabel)::
          Nil
        )
      )
    case D.BinOp(D.GT(), e1, e2) =>
      val t1 = newTemp()
      val t2 = newTemp()
      val t3 = newTemp()
      val fLabel = newLabel()
      translate(e1, t1) ++ (
        translate(e2, t2) ++ (
          P.Bin(t3, P.SUB(), P.Temp(t1), P.Temp(t2))::
          P.Move(t, P.Lit(0))::
          P.CJmp(P.LE(), P.Temp(t3), fLabel)::
          P.Move(t, P.Lit(1))::
          P.LabelStm(fLabel)::
          Nil
        )
      )
    case D.BinOp(D.LE(), e1, e2) =>
      val t1 = newTemp()
      val t2 = newTemp()
      val t3 = newTemp()
      val fLabel = newLabel()
      translate(e1, t1) ++ (
        translate(e2, t2) ++ (
          P.Bin(t3, P.SUB(), P.Temp(t1), P.Temp(t2))::
          P.Move(t, P.Lit(0))::
          P.CJmp(P.GT(), P.Temp(t3), fLabel)::
          P.Move(t, P.Lit(1))::
          P.LabelStm(fLabel)::
          Nil
        )
      )
    case D.BinOp(D.GE(), e1, e2) =>
      val t1 = newTemp()
      val t2 = newTemp()
      val t3 = newTemp()
      val fLabel = newLabel()
      translate(e1, t1) ++ (
        translate(e2, t2) ++ (
          P.Bin(t3, P.SUB(), P.Temp(t1), P.Temp(t2))::
          P.Move(t, P.Lit(0))::
          P.CJmp(P.LT(), P.Temp(t3), fLabel)::
          P.Move(t, P.Lit(1))::
          P.LabelStm(fLabel)::
          Nil
        )
      )

    case D.BinOp(D.ADD(), D.Temp(t1), D.Temp(t2)) =>
          P.Bin(t, P.ADD(), P.Temp(t1), P.Temp(t2))::Nil
    case D.BinOp(D.SUB(), D.Temp(t1), D.Temp(t2)) =>
          P.Bin(t, P.SUB(), P.Temp(t1), P.Temp(t2))::Nil
    case D.BinOp(D.MUL(), D.Temp(t1), D.Temp(t2)) =>
          P.Bin(t, P.MUL(), P.Temp(t1), P.Temp(t2))::Nil
    case D.BinOp(D.DIV(), D.Temp(t1), D.Temp(t2)) =>
          P.Bin(t, P.DIV(), P.Temp(t1), P.Temp(t2))::Nil
    case D.BinOp(D.REM(), D.Temp(t1), D.Temp(t2)) =>
          P.Bin(t, P.REM(), P.Temp(t1), P.Temp(t2))::Nil

    case D.BinOp(D.ADD(), D.Temp(n1), D.Lit(n2)) =>
          P.Bin(t, P.ADD(), P.Temp(n1), P.Lit(n2))::Nil
    case D.BinOp(D.SUB(), D.Temp(n1), D.Lit(n2)) =>
          P.Bin(t, P.SUB(), P.Temp(n1), P.Lit(n2))::Nil
    case D.BinOp(D.MUL(), D.Temp(n1), D.Lit(n2)) =>
          P.Bin(t, P.MUL(), P.Temp(n1), P.Lit(n2))::Nil
    case D.BinOp(D.DIV(), D.Temp(n1), D.Lit(n2)) if n2 != 0 =>
          P.Bin(t, P.DIV(), P.Temp(n1), P.Lit(n2))::Nil
    case D.BinOp(D.REM(), D.Temp(n1), D.Lit(n2)) if n2 != 0 =>
          P.Bin(t, P.REM(), P.Temp(n1), P.Lit(n2))::Nil

    case D.BinOp(D.ADD(), D.Lit(n1), D.Lit(n2)) =>
          P.Bin(t, P.ADD(), P.Lit(n1), P.Lit(n2))::Nil
    case D.BinOp(D.SUB(), D.Lit(n1), D.Lit(n2)) =>
          P.Bin(t, P.SUB(), P.Lit(n1), P.Lit(n2))::Nil
    case D.BinOp(D.MUL(), D.Lit(n1), D.Lit(n2)) =>
          P.Bin(t, P.MUL(), P.Lit(n1), P.Lit(n2))::Nil
    case D.BinOp(D.DIV(), D.Lit(n1), D.Lit(n2)) if n2 != 0 =>
          P.Bin(t, P.DIV(), P.Lit(n1), P.Lit(n2))::Nil
    case D.BinOp(D.REM(), D.Lit(n1), D.Lit(n2)) if n2 != 0 =>
          P.Bin(t, P.REM(), P.Lit(n1), P.Lit(n2))::Nil

    case D.BinOp(D.ADD(), D.Lit(n1), D.Temp(t2)) =>
          P.Bin(t, P.ADD(), P.Lit(n1), P.Temp(t2))::Nil
    case D.BinOp(D.SUB(), D.Lit(n1), D.Temp(t2)) =>
          P.Bin(t, P.SUB(), P.Lit(n1), P.Temp(t2))::Nil
    case D.BinOp(D.MUL(), D.Lit(n1), D.Temp(t2)) =>
          P.Bin(t, P.MUL(), P.Lit(n1), P.Temp(t2))::Nil
    case D.BinOp(D.DIV(), D.Lit(n1), D.Temp(t2)) =>
          P.Bin(t, P.DIV(), P.Lit(n1), P.Temp(t2))::Nil
    case D.BinOp(D.REM(), D.Lit(n1), D.Temp(t2)) =>
          P.Bin(t, P.REM(), P.Lit(n1), P.Temp(t2))::Nil

    case D.BinOp(D.ADD(), e1, e2) =>
      val t1 = e1 match { case D.Temp(t1) => t1 ; case _ => newTemp() }
      val t2 = e2 match { case D.Temp(t2) => t2 ; case _ => newTemp() }
      translate(e1, t1) ++ (
        translate(e2, t2) ++ (
          P.Bin(t, P.ADD(), P.Temp(t1), P.Temp(t2))::
      Nil))

    case D.BinOp(D.SUB(), e1, e2) =>
      val t1 = e1 match { case D.Temp(t1) => t1 ; case _ => newTemp() }
      val t2 = e2 match { case D.Temp(t2) => t2 ; case _ => newTemp() }
      translate(e1, t1) ++ (
        translate(e2, t2) ++ (
          P.Bin(t, P.SUB(), P.Temp(t1), P.Temp(t2))::
      Nil))
    case D.BinOp(D.MUL(), e1, e2) =>
      val t1 = e1 match { case D.Temp(t1) => t1 ; case _ => newTemp() }
      val t2 = e2 match { case D.Temp(t2) => t2 ; case _ => newTemp() }
      translate(e1, t1) ++ (
        translate(e2, t2) ++ (
          P.Bin(t, P.MUL(), P.Temp(t1), P.Temp(t2))::
      Nil))
    case D.BinOp(D.DIV(), e1, e2) =>
      val t1 = e1 match { case D.Temp(t1) => t1 ; case _ => newTemp() }
      val t2 = e2 match { case D.Temp(t2) => t2 ; case _ => newTemp() }
      translate(e1, t1) ++ (
        translate(e2, t2) ++ (
          P.Bin(t, P.DIV(), P.Temp(t1), P.Temp(t2))::
      Nil))
    case D.BinOp(D.REM(), e1, e2) =>
      val t1 = e1 match { case D.Temp(t1) => t1 ; case _ => newTemp() }
      val t2 = e2 match { case D.Temp(t2) => t2 ; case _ => newTemp() }
      translate(e1, t1) ++ (
        translate(e2, t2) ++ (
          P.Bin(t, P.REM(), P.Temp(t1), P.Temp(t2))::
      Nil))

    case D.Temp(x) if x == t =>
      Nil

    case D.Temp(x) =>
      P.Move(t, P.Temp(x))::Nil

    case D.Lit(n) =>
      P.Move(t, P.Lit(n))::Nil

    case D.Global(x) =>
      P.Move(t, P.Global(x))::Nil
  }

  def newLabel(): Label = {
    ristretto.main.FreshId.freshId("Lperc")
  }

  def newTemp(): Temp = {
    ristretto.main.FreshId.freshId("tperc")
  }
}
