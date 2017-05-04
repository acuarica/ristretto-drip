// Matrix-matrix multiple for n*n matrices.
int[][] mult(int[][] a, int[][] b, int n) {
  int[][] c = new int[n][n];
  int i = 0;
  while (i < n) {
    int j = 0;
    while (j < n) {
      int k = 0;
      c[i][j] = 0;
      while (k < n) {
        c[i][j] = c[i][j] + a[i][k] * b[k][j];
        k = k + 1;
      }
      j = j + 1;
    }
    i = i + 1;
  }
  return c;
}

extern int[] int2str(int a);
extern void print(int[] a);
extern void println(int[] a);

void print_mult(int[][] a, int[][] b, int[][] c, int n) {
  print_mat(a, n);
  println("*");
  print_mat(b, n);
  println("=");
  print_mat(c, n);
  println("");
  return;
}

void print_mat(int[][] a, int n) {
  int i = 0;
  while (i < n) {
    int j = 0;
    while (j < n) {
      if (j != 0) print(" ");
      print(int2str(a[i][j]));
      j = j + 1;
    }
    println("");
    i = i + 1;
  }
  return;
}

void main() {
  int[][] a = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
  int[][] b = { { 1, 0, 0 }, { 0, 1, 0 }, { 0, 0, 1 } };
  print_mult(a, b, mult(a, b, 3), 3);
  print_mult(b, a, mult(b, a, 3), 3);
  print_mult(a, a, mult(a, a, 3), 3);
  print_mult(b, b, mult(b, b, 3), 3);
  return;
}
