extern int[] int2str(int x);
extern void println(int[] s);

void main() {
  int[] s = int2str(123456789);
  println(s);
  return;
}
