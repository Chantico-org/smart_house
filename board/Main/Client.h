#ifndef CLIENT_H
#define CLIENT_H

#include <ESP8266WiFi.h>
#include <ArduinoJson.h>
#include "Constants.h"
#include "Commons.h"

// 5 minutes
const int DEFAULT_TIMEOUT = 1000 * 60;

namespace smart {
  class Client
  {
  private:
    WiFiClient* wifi_client;
    uint8_t* length_field;
    char* data_buffer;
    char* host;
    int port;
    void writeClientLength(int data_length);
    static const int MESSAGE_LENGTH = 4;
  public:
    static const int MAX_MESSAGE_SIZE = 1024;
    Client(const char*, int);
    bool read(char*);
    void write(JsonObject&);
    void write(char *);
    bool connect(int timeout = DEFAULT_TIMEOUT);
    bool connected();
    ~Client();
  };
} // smart

inline smart::Client::Client(const char* host, int port) {
  this->port = port;
  this->host = smart::allocCharP(host);
  wifi_client = new WiFiClient;
  Serial.println(Client::MESSAGE_LENGTH);
  Serial.println(Client::MAX_MESSAGE_SIZE);
  length_field = new uint8_t[Client::MESSAGE_LENGTH];
  data_buffer = new char[Client::MAX_MESSAGE_SIZE];
}

inline bool smart::Client::connected() {
  return wifi_client->connected();
}

inline smart::Client::~Client() {
  delete wifi_client;
  delete data_buffer;
  delete host;
  delete length_field;
}

#endif // CLIENT_H
