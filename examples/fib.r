extern void println(int[] s);
extern int[] int2str(int n);

int fib(int n) {
  if (n <= 1)
    return 1;
  else
    return fib(n-1) + fib(n-2);
}

void main() {
  println(int2str(fib(10)));
  return;
}
