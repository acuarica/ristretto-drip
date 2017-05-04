#ifndef _ristretto_h
#define _ristretto_h

#include <sys/types.h>
#ifdef __linux__
#include <stdint.h>
#endif

typedef uint64_t rint;
typedef uint64_t rbool;

#define RFALSE ((rbool) 0)
#define RTRUE  ((rbool) 1)

typedef struct {
  rint length;
  rint data[1];
} rintarray;

typedef struct {
  rint length;
  char data[0];
} rarray;

void ristretto_die(rint error_code);
void *ristretto_alloc(rint bytes);

#endif
