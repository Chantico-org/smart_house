#include "ResourceHandler.h"

ResourceHandler::ResourceHandler(string destination, int period, TResourceHandler handler){
  this->destination = destination;
  this->handler = handler;
  this->period = period;
  activated = false;
  prevMilis = 0;
}

void ResourceHandler::activate(JsonObject& request) {
  const char* requestDestination = request["destination"];
  if (destination == requestDestination) {
    activated = true;
    prevMilis = millis();
  }
}

bool ResourceHandler::loop(uint8_t* dataBuffer, SimpMessage* outBoundMessage) {
  if (!activated) return false;
  unsigned long curMilis = millis();
  if ((curMilis - prevMilis) < period) return false;
  prevMilis = curMilis;
  outBoundMessage->type = SimpMessageType::MESSAGE;
  StaticJsonBuffer<1000> jsonBuffer;
  JsonObject& json = jsonBuffer.createObject();
  json["topic"] = destination.c_str();
  json["body"] = handler();
  json.printTo((char*)dataBuffer, 1000);
  outBoundMessage->data = dataBuffer;
  outBoundMessage->dataLength = strlen((char*)outBoundMessage->data);
  return true;
}

void ResourceHandler::deactivate(JsonObject& request) {
  const char* requestDestination = request["destination"];
  if (destination == requestDestination) {
    activated = false;
  }
}

ResourceHandler::~ResourceHandler() {
  PRINTLN("deactivate resource");
}
