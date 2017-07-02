#ifndef CLIENT_H
#define CLIENT_H

#include <ESP8266WiFi.h>
#include <ArduinoJson.h>
#include "Constants.h"
#include "Commons.h"

namespace smart {
  class Client
  {
  private:
    WiFiClient* wifi_client;
    char* host;
    int port;
  public:
    Client(const char*, int);
    void connect();
    inline uint8_t connected(){
      return wifi_client->connected();
    };
    inline size_t read(uint8_t *buf, size_t size) {
      return wifi_client->read(buf, size);
    }
    inline int available() {
      return wifi_client->available();
    }
    inline size_t write(const uint8_t *buf, size_t size) {
      return wifi_client->write(buf, size);
    }
    inline size_t write(uint8_t byte) {
      return wifi_client->write(byte);
    }
    ~Client();
  };
} // smart

inline smart::Client::Client(const char* host, int port = SERVER_PORT) {
  this->port = port;
  this->host = smart::allocCharP(host);
  wifi_client = new WiFiClient;
}

inline smart::Client::~Client() {
  delete wifi_client;
  delete host;
}

#endif // CLIENT_H
