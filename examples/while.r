extern int[] int2str(int x);
extern void println(int[] s);

void main() {
  int i = 0;
  while (i < 10) {
    int[] s = int2str(i);
    println(s);
    i = i + 1;
  }
  return;
}
