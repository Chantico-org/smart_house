#include <ESP8266WiFi.h>
#include <Ticker.h>
#include "Commons.h"
#include "ConfigServer.h"
#include "ConfigFile.h"
#include "Client.h"
#include "FS.h"

smart::ConfigServer *configServer = NULL;
smart::Client *client = NULL;

#define FIRMWARE_VERSION 0x000001

#ifdef TEST
void setup() {}
#else
void setup() {
  Serial.begin(115200);
  Serial.println();
  Serial.println();
  pinMode(D5, OUTPUT);
  pinMode(D6, OUTPUT);
  digitalWrite(D5, LOW);
  digitalWrite(D6, LOW);
  smart::initId();
}
#endif
#ifdef TEST
void loop() {}
#else
void loop() {
	using smart::deviceState;
  switch(deviceState.connectionStage) {
    case CONNECTION_STAGE_FILE:
      smart::useConfigFile();
      if (deviceState.connectionStage == CONNECTION_STAGE_CONNECTED) {
        digitalWrite(D5, HIGH);
      }
      break;
    case CONNECTION_STAGE_SERVER:
      smart::useConfigServer(configServer);
      if (deviceState.connectionStage == CONNECTION_STAGE_CONNECTED) {
        smart::cleanConfigServer(configServer);
        digitalWrite(D5, HIGH);
      }
      break;
    case CONNECTION_STAGE_CONNECTED:
      if (client == NULL) {
        client = new smart::Client("192.168.0.102", 8080);
        client->connect();
        StaticJsonBuffer<smart::Client::MAX_MESSAGE_SIZE> buffer;
        JsonObject& root = buffer.createObject();
        root["deviceId"] = deviceState.deviceId;
        root["firmwareVersion"] = FIRMWARE_VERSION;
        client->write(root);
      }
      while(!client->connected()) {
          Serial.println("Cannot connet here");
          delay(1000);
          client->connect();
      }
      digitalWrite(D5, HIGH);
      delay(2000);
      digitalWrite(D5, LOW);
      delay(2000);
      break;
  }
}
#endif
