#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include "ristretto.h"

#define RFALSE ((rbool) 0)
#define RTRUE ((rbool) 1)

#define ARRAY_BOUNDS_ERROR 0
#define DIV_BY_0_ERROR 1

void *
ristretto_alloc(rint bytes)
{
    char *a = (char *) calloc(bytes, 1);
    if (a == NULL) {
      printf("allocation of %lld bytes failed\n", bytes);
      exit(1);
    }
    // printf("allocated %lld bytes\n", bytes);
    return a;
}

void
ristretto_die(rint error_code)
{
    switch (error_code) {
      case ARRAY_BOUNDS_ERROR:
        printf("Bounds error. Aborting.");
        break;
      case DIV_BY_0_ERROR:
        printf("Divide by 0. Aborting.");
        break;
    }
    exit(1);
}
