#ifndef SIMP_REQUEST_HANDLER
#define SIMP_REQUEST_HANDLER

#include "SimpCommons.h"
#include <string.h>
#include <functional>
#include <ArduinoJson.h>
#include <ESP8266WiFi.h>

using namespace std;
using namespace simp;

namespace simp {
  class RequestHandler {
  private:
    string destination;
    TRequestHandler handler;
  public:
    RequestHandler(string, TRequestHandler);
    bool canHandle(JsonObject&);
    void handle(JsonObject&);
    ~RequestHandler();
  };
}

#endif /* !SIMP_REQUEST_HANDLER */
