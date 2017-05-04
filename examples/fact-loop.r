extern void println(int[] s);
extern int[] int2str(int n);

int fact(int n) {
  int r = 1;
  while (n > 0) {
    r = r * n;
    n = n - 1;
  }
  return r;
}

void main() {
  println(int2str(fact(10)));
  return;
}
