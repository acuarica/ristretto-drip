extern void println(int[] s);
extern void print(int[] s);
extern int[] int2str(int n);
extern boolean strequal(int[] a, int[] b);

void sort(int[] a) {
  println("sort");
  println(a);
  qsort(a, 0, a.length-1);
  return;
}

void qsort(int[] a, int lo, int hi) {
  int i = lo;
  int j = hi;
  int pivot = a[lo + (hi-lo) / 2];

  // partition around the pivot
  while (i <= j) {
    while (a[i] < pivot) {
      i = i + 1;
    }
    while (a[j] > pivot) {
      j = j - 1;
    }
    if (i <= j) {
      int t = a[i];
      a[i] = a[j];
      a[j] = t;
      i = i + 1;
      j = j - 1;
    }
  }

  if (lo < j)
    qsort(a, lo, j);
  if (i < hi)
    qsort(a, i, hi);

  return;
}

void assert(boolean b) {
  if (! b) {
    println("test failed");
  }
  else {
    println("test passed");
  }
  return;
}

void main() {
  int[] s = "thequickbrownfoxjumpedoverthelazydogs";
  print("input ");
  println(s);
  sort(s);
  print("output ");
  println(s);
  assert(strequal(s, "abcddeeeefghhijklmnoooopqrrsttuuvwxyz"));
  return;
}
