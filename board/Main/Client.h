#ifndef CLIENT_H
#define CLIENT_H

#include <ESP8266WiFi.h>
#include <ArduinoJson.h>

#define MAX_MESSAGE_SIZE 255

namespace smart {
  class Client
  {
  private:
    WiFiClient* wifi_client;
    char* data_buffer;
  public:
    Client(const char*, int);
    JsonObject read(StaticJsonBuffer&);
    void write(JsonObject&);
    ~Client();
  };
} // smart

inline smart::Client::Client() {
  wifi_client = new WiFiClient;
  data_buffer = new char[MAX_MESSAGE_SIZE];
}

inline smart::Client::~Client() {
  delete wifi_client;
  delete data_buffer;
}

#endif // CLIENT_H
