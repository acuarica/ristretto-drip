	.section	__TEXT,__text,regular,pure_instructions
	.macosx_version_min 10, 11
	.globl _main
	.p2align 4, 0x90
_main:
	 pushq %rbp
	 movq %rsp, %rbp
	 subq $368, %rsp
	 # Move(tperc11,Lit(8))
	 movq $8, %r14
	 movq %r14, -8(%rbp)
	 # Bin(tperc12,MUL(),Lit(8),Lit(12))
	 movq $8, %r14
	 movq %r14, -16(%rbp)
	 movq $12, %r14
	 movq -16(%rbp), %r15
	 imulq %r14, %r15
	 movq %r15, -16(%rbp)
	 # Bin(tperc10,ADD(),Temp(tperc11),Temp(tperc12))
	 movq -8(%rbp), %r14
	 movq %r14, -24(%rbp)
	 movq -16(%rbp), %r14
	 movq -24(%rbp), %r15
	 addq %r14, %r15
	 movq %r15, -24(%rbp)
	 # SetArg(0,Temp(tperc10))
	 movq -24(%rbp), %r14
	 movq %r14, %rdi
	 # Call(tdrip6,Global(ristretto_alloc))
	 callq _ristretto_alloc
	 movq %rax, %r14
	 movq %r14, -32(%rbp)
	 # Move(tperc13,Temp(tdrip6))
	 movq -32(%rbp), %r14
	 movq %r14, -40(%rbp)
	 # Move(tperc14,Lit(12))
	 movq $12, %r14
	 movq %r14, -48(%rbp)
	 # Store(Address(0,Temp(tperc13)),Temp(tperc14))
	 movq -48(%rbp), %r14
	 movq -40(%rbp), %r13
	 movq %r14, 0(%r13)
	 # Bin(tperc17,MUL(),Lit(8),Lit(0))
	 movq $8, %r14
	 movq %r14, -56(%rbp)
	 movq $0, %r14
	 movq -56(%rbp), %r15
	 imulq %r14, %r15
	 movq %r15, -56(%rbp)
	 # Bin(tperc15,ADD(),Temp(tdrip6),Temp(tperc17))
	 movq -32(%rbp), %r14
	 movq %r14, -64(%rbp)
	 movq -56(%rbp), %r14
	 movq -64(%rbp), %r15
	 addq %r14, %r15
	 movq %r15, -64(%rbp)
	 # Move(tperc16,Lit(72))
	 movq $72, %r14
	 movq %r14, -72(%rbp)
	 # Store(Address(8,Temp(tperc15)),Temp(tperc16))
	 movq -72(%rbp), %r14
	 movq -64(%rbp), %r13
	 movq %r14, 8(%r13)
	 # Bin(tperc20,MUL(),Lit(8),Lit(1))
	 movq $8, %r14
	 movq %r14, -80(%rbp)
	 movq $1, %r14
	 movq -80(%rbp), %r15
	 imulq %r14, %r15
	 movq %r15, -80(%rbp)
	 # Bin(tperc18,ADD(),Temp(tdrip6),Temp(tperc20))
	 movq -32(%rbp), %r14
	 movq %r14, -88(%rbp)
	 movq -80(%rbp), %r14
	 movq -88(%rbp), %r15
	 addq %r14, %r15
	 movq %r15, -88(%rbp)
	 # Move(tperc19,Lit(111))
	 movq $111, %r14
	 movq %r14, -96(%rbp)
	 # Store(Address(8,Temp(tperc18)),Temp(tperc19))
	 movq -96(%rbp), %r14
	 movq -88(%rbp), %r13
	 movq %r14, 8(%r13)
	 # Bin(tperc23,MUL(),Lit(8),Lit(2))
	 movq $8, %r14
	 movq %r14, -104(%rbp)
	 movq $2, %r14
	 movq -104(%rbp), %r15
	 imulq %r14, %r15
	 movq %r15, -104(%rbp)
	 # Bin(tperc21,ADD(),Temp(tdrip6),Temp(tperc23))
	 movq -32(%rbp), %r14
	 movq %r14, -112(%rbp)
	 movq -104(%rbp), %r14
	 movq -112(%rbp), %r15
	 addq %r14, %r15
	 movq %r15, -112(%rbp)
	 # Move(tperc22,Lit(108))
	 movq $108, %r14
	 movq %r14, -120(%rbp)
	 # Store(Address(8,Temp(tperc21)),Temp(tperc22))
	 movq -120(%rbp), %r14
	 movq -112(%rbp), %r13
	 movq %r14, 8(%r13)
	 # Bin(tperc26,MUL(),Lit(8),Lit(3))
	 movq $8, %r14
	 movq %r14, -128(%rbp)
	 movq $3, %r14
	 movq -128(%rbp), %r15
	 imulq %r14, %r15
	 movq %r15, -128(%rbp)
	 # Bin(tperc24,ADD(),Temp(tdrip6),Temp(tperc26))
	 movq -32(%rbp), %r14
	 movq %r14, -136(%rbp)
	 movq -128(%rbp), %r14
	 movq -136(%rbp), %r15
	 addq %r14, %r15
	 movq %r15, -136(%rbp)
	 # Move(tperc25,Lit(97))
	 movq $97, %r14
	 movq %r14, -144(%rbp)
	 # Store(Address(8,Temp(tperc24)),Temp(tperc25))
	 movq -144(%rbp), %r14
	 movq -136(%rbp), %r13
	 movq %r14, 8(%r13)
	 # Bin(tperc29,MUL(),Lit(8),Lit(4))
	 movq $8, %r14
	 movq %r14, -152(%rbp)
	 movq $4, %r14
	 movq -152(%rbp), %r15
	 imulq %r14, %r15
	 movq %r15, -152(%rbp)
	 # Bin(tperc27,ADD(),Temp(tdrip6),Temp(tperc29))
	 movq -32(%rbp), %r14
	 movq %r14, -160(%rbp)
	 movq -152(%rbp), %r14
	 movq -160(%rbp), %r15
	 addq %r14, %r15
	 movq %r15, -160(%rbp)
	 # Move(tperc28,Lit(32))
	 movq $32, %r14
	 movq %r14, -168(%rbp)
	 # Store(Address(8,Temp(tperc27)),Temp(tperc28))
	 movq -168(%rbp), %r14
	 movq -160(%rbp), %r13
	 movq %r14, 8(%r13)
	 # Bin(tperc32,MUL(),Lit(8),Lit(5))
	 movq $8, %r14
	 movq %r14, -176(%rbp)
	 movq $5, %r14
	 movq -176(%rbp), %r15
	 imulq %r14, %r15
	 movq %r15, -176(%rbp)
	 # Bin(tperc30,ADD(),Temp(tdrip6),Temp(tperc32))
	 movq -32(%rbp), %r14
	 movq %r14, -184(%rbp)
	 movq -176(%rbp), %r14
	 movq -184(%rbp), %r15
	 addq %r14, %r15
	 movq %r15, -184(%rbp)
	 # Move(tperc31,Lit(113))
	 movq $113, %r14
	 movq %r14, -192(%rbp)
	 # Store(Address(8,Temp(tperc30)),Temp(tperc31))
	 movq -192(%rbp), %r14
	 movq -184(%rbp), %r13
	 movq %r14, 8(%r13)
	 # Bin(tperc35,MUL(),Lit(8),Lit(6))
	 movq $8, %r14
	 movq %r14, -200(%rbp)
	 movq $6, %r14
	 movq -200(%rbp), %r15
	 imulq %r14, %r15
	 movq %r15, -200(%rbp)
	 # Bin(tperc33,ADD(),Temp(tdrip6),Temp(tperc35))
	 movq -32(%rbp), %r14
	 movq %r14, -208(%rbp)
	 movq -200(%rbp), %r14
	 movq -208(%rbp), %r15
	 addq %r14, %r15
	 movq %r15, -208(%rbp)
	 # Move(tperc34,Lit(117))
	 movq $117, %r14
	 movq %r14, -216(%rbp)
	 # Store(Address(8,Temp(tperc33)),Temp(tperc34))
	 movq -216(%rbp), %r14
	 movq -208(%rbp), %r13
	 movq %r14, 8(%r13)
	 # Bin(tperc38,MUL(),Lit(8),Lit(7))
	 movq $8, %r14
	 movq %r14, -224(%rbp)
	 movq $7, %r14
	 movq -224(%rbp), %r15
	 imulq %r14, %r15
	 movq %r15, -224(%rbp)
	 # Bin(tperc36,ADD(),Temp(tdrip6),Temp(tperc38))
	 movq -32(%rbp), %r14
	 movq %r14, -232(%rbp)
	 movq -224(%rbp), %r14
	 movq -232(%rbp), %r15
	 addq %r14, %r15
	 movq %r15, -232(%rbp)
	 # Move(tperc37,Lit(101))
	 movq $101, %r14
	 movq %r14, -240(%rbp)
	 # Store(Address(8,Temp(tperc36)),Temp(tperc37))
	 movq -240(%rbp), %r14
	 movq -232(%rbp), %r13
	 movq %r14, 8(%r13)
	 # Bin(tperc41,MUL(),Lit(8),Lit(8))
	 movq $8, %r14
	 movq %r14, -248(%rbp)
	 movq $8, %r14
	 movq -248(%rbp), %r15
	 imulq %r14, %r15
	 movq %r15, -248(%rbp)
	 # Bin(tperc39,ADD(),Temp(tdrip6),Temp(tperc41))
	 movq -32(%rbp), %r14
	 movq %r14, -256(%rbp)
	 movq -248(%rbp), %r14
	 movq -256(%rbp), %r15
	 addq %r14, %r15
	 movq %r15, -256(%rbp)
	 # Move(tperc40,Lit(32))
	 movq $32, %r14
	 movq %r14, -264(%rbp)
	 # Store(Address(8,Temp(tperc39)),Temp(tperc40))
	 movq -264(%rbp), %r14
	 movq -256(%rbp), %r13
	 movq %r14, 8(%r13)
	 # Bin(tperc44,MUL(),Lit(8),Lit(9))
	 movq $8, %r14
	 movq %r14, -272(%rbp)
	 movq $9, %r14
	 movq -272(%rbp), %r15
	 imulq %r14, %r15
	 movq %r15, -272(%rbp)
	 # Bin(tperc42,ADD(),Temp(tdrip6),Temp(tperc44))
	 movq -32(%rbp), %r14
	 movq %r14, -280(%rbp)
	 movq -272(%rbp), %r14
	 movq -280(%rbp), %r15
	 addq %r14, %r15
	 movq %r15, -280(%rbp)
	 # Move(tperc43,Lit(116))
	 movq $116, %r14
	 movq %r14, -288(%rbp)
	 # Store(Address(8,Temp(tperc42)),Temp(tperc43))
	 movq -288(%rbp), %r14
	 movq -280(%rbp), %r13
	 movq %r14, 8(%r13)
	 # Bin(tperc47,MUL(),Lit(8),Lit(10))
	 movq $8, %r14
	 movq %r14, -296(%rbp)
	 movq $10, %r14
	 movq -296(%rbp), %r15
	 imulq %r14, %r15
	 movq %r15, -296(%rbp)
	 # Bin(tperc45,ADD(),Temp(tdrip6),Temp(tperc47))
	 movq -32(%rbp), %r14
	 movq %r14, -304(%rbp)
	 movq -296(%rbp), %r14
	 movq -304(%rbp), %r15
	 addq %r14, %r15
	 movq %r15, -304(%rbp)
	 # Move(tperc46,Lit(97))
	 movq $97, %r14
	 movq %r14, -312(%rbp)
	 # Store(Address(8,Temp(tperc45)),Temp(tperc46))
	 movq -312(%rbp), %r14
	 movq -304(%rbp), %r13
	 movq %r14, 8(%r13)
	 # Bin(tperc50,MUL(),Lit(8),Lit(11))
	 movq $8, %r14
	 movq %r14, -320(%rbp)
	 movq $11, %r14
	 movq -320(%rbp), %r15
	 imulq %r14, %r15
	 movq %r15, -320(%rbp)
	 # Bin(tperc48,ADD(),Temp(tdrip6),Temp(tperc50))
	 movq -32(%rbp), %r14
	 movq %r14, -328(%rbp)
	 movq -320(%rbp), %r14
	 movq -328(%rbp), %r15
	 addq %r14, %r15
	 movq %r15, -328(%rbp)
	 # Move(tperc49,Lit(108))
	 movq $108, %r14
	 movq %r14, -336(%rbp)
	 # Store(Address(8,Temp(tperc48)),Temp(tperc49))
	 movq -336(%rbp), %r14
	 movq -328(%rbp), %r13
	 movq %r14, 8(%r13)
	 # Move(tperc9,Temp(tdrip6))
	 movq -32(%rbp), %r14
	 movq %r14, -344(%rbp)
	 # SetArg(0,Temp(tperc9))
	 movq -344(%rbp), %r14
	 movq %r14, %rdi
	 # Call(tdrip5,Global(println))
	 callq _println
	 movq %rax, %r14
	 movq %r14, -352(%rbp)
	 # Jmp(Ldrip2)
	 jmp Ldrip2
	 # Jmp(Ldrip2)
	 jmp Ldrip2
	 # LabelStm(Ldrip3)
Ldrip3:
	 # ErrorStm(0)
	 movq $0, %r14
	 movq %r14, %rdi
	 callq _ristretto_die
	 # LabelStm(Ldrip4)
Ldrip4:
	 # ErrorStm(1)
	 movq $1, %r14
	 movq %r14, %rdi
	 callq _ristretto_die
	 # LabelStm(Ldrip2)
Ldrip2:
	 # Move(tperc8,Temp(tdrip1))
	 movq -360(%rbp), %r14
	 movq %r14, -368(%rbp)
	 # Ret(Temp(tperc8))
	 movq -368(%rbp), %r14
	 movq %r14, %rax
	 addq $368, %rsp
	 popq %rbp
	 retq 

