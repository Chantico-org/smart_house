#include "SimpCommons.h"

#ifdef DESKTOP

unsigned long millis() {
  struct timeval te;
  gettimeofday(&te, NULL);
  return te.tv_sec*1000L + te.tv_usec/1000;
}

void delay(int period) {
  sleep(period);
}

#endif
