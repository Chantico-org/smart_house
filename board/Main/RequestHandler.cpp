#include "RequestHandler.h"

using namespace simp;
using namespace std;

RequestHandler::RequestHandler(string destination, TRequestHandler handler){
  this->destination = destination;
  this->handler = handler;
}

bool RequestHandler::canHandle(JsonObject& request) {
//  return (destination == request["destination"]);
  return true;
}

void RequestHandler::handle(JsonObject& request) {
  const char *body = request["body"];
  request["body"] = handler((uint8_t*) body);
}

RequestHandler::~RequestHandler() {
  PRINTLN("Handler destroy");
}
