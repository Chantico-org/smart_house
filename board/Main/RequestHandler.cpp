#include "RequestHandler.h"

#ifdef DESKTOP

#include <iostream>
#include <stdio.h>
#include <unistd.h>

#define PRINTLN(line) cout << line << endl

#else
#define PRINTLN(line)
#endif

using namespace simp;
using namespace std;

RequestHandler::RequestHandler(string destination, TRequestHandler handler){
  this->destination = destination;
  this->handler = handler;
}

bool RequestHandler::canHandle(JsonObject& request) {
  return destination == request["destination"];
}

void RequestHandler::handle(JsonObject& request) {
  const char *body = request["body"];
  request["body"] = handler((uint8_t*) body);
}

RequestHandler::~RequestHandler() {
  PRINTLN("Handler destroy");
}
