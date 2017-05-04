extern void print(int[] s);
extern void println(int[] s);
extern int[] int2str(int n);

int gcd(int a, int b) {
  while (b != 0) {
    /*
    print("a ");
    print(int2str(a));
    print(" b ");
    print(int2str(b));
    println("");
    */

    int t = b;
    b = a % b;
    a = t;
  }
  return a;
}

void main() {
  println(int2str(gcd(12, 10)));
  return;
}
