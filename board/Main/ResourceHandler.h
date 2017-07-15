#ifndef SIMP_RESOURCE_HANDLER
#define SIMP_RESOURCE_HANDLER

#include "SimpCommons.h"
#include <string.h>
#include <functional>
#include <Arduino.h>
#include <ArduinoJson.h>

using namespace std;
using namespace simp;

namespace simp {
  class ResourceHandler {
  private:
    string destination;
    TResourceHandler handler;
    unsigned long prevMilis;
    bool activated;
    int period;
  public:
    ResourceHandler(string, int, TResourceHandler);
    void activate(JsonObject&);
    bool loop(uint8_t*, SimpMessage*);
    void deactivate(JsonObject&);
    ~ResourceHandler();
  };
}

#endif /* !SIMP_RESOURCE_HANDLER */
