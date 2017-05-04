extern void println(int[] s);
extern int[] int2str(int n);

int fact(int n) {
  if (n == 0)
    return 1;
  else
    return n * fact(n-1);
}

void main() {
  println(int2str(fact(10)));
  return;
}
