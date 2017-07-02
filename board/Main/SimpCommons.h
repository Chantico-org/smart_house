#ifndef SIMP_COMMONS
#define SIMP_COMMONS

#include <functional>

#ifdef DESKTOP

#include <iostream>
#include <stdio.h>
#include <unistd.h>
#include <sys/time.h>

#define PRINTLN(line) cout << line << endl

void delay(int period);
unsigned long millis();

#else

#include <Arduino.h>

#define PRINTLN(line)

#endif

namespace simp {
  typedef std::function<uint8_t*(uint8_t*)> TRequestHandler;
  typedef std::function<uint8_t*()> TResourceHandler;

  enum SimpMessageType {
    SUBSCRIBE,
    MESSAGE,
    UN_SUBSCRIBE,
    REQUEST,
    RESPONSE
  };

  struct SimpMessage {
    SimpMessageType type;
    uint32_t dataLength;
    uint8_t* data;
  };
}

#endif /* !SIMP_COMMONS */
