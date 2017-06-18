#include <ESP8266WiFi.h>
#include "Constants.h"
#include "Commons.h"
#include "ConfigServer.h"
#include "ConfigFile.h"
#include "Client.h"
#include "FS.h"
#include <string>
#include<forward_list>

#define TEST

std::forward_list<int> flist1 = {1,2,3};
std::string amt = "Hello world";

smart::ConfigServer *configServer = NULL;
smart::Client *client = NULL;
char *message_buffer = new char[smart::Client::MAX_MESSAGE_SIZE];

void restartClick() {
  Serial.println("Restart click");
  smart::eraseConfigFile();
}

void clientModeState() {
  Serial.println(F("Switching to lient mode state"));
  WiFi.mode(WIFI_STA);
}

#ifdef TEST
void setup() {
  for(int& a: flist1) {
    Serial.println(a);
  }
  delay(5000);
  Serial.begin(115200);
  Serial.println();
  Serial.println();
  Serial.println(F("Setuping..."));
}
void loop() {
  delay(1000);
  Serial.println(F("Lap 1"));
  delay(1000);
  Serial.println(F("Lap 2"));
}
#else
void setup() {
  Serial.begin(115200);
  Serial.println();
  Serial.println();
  Serial.println(F("Setuping..."));
  pinMode(RESET_BTN_PIN, OUTPUT);
  digitalWrite(RESET_BTN_PIN, LOW);
  pinMode(RESET_BTN_PIN, INPUT);
  attachInterrupt(digitalPinToInterrupt(RESET_BTN_PIN), restartClick, CHANGE);
  pinMode(NORMAL_STATE_PIN, OUTPUT);
  pinMode(AP_CONNECTION_PIN, OUTPUT);
  pinMode(HOST_CONNECTION_PIN, OUTPUT);
  pinMode(LIGHT_PIN, OUTPUT);
  digitalWrite(NORMAL_STATE_PIN, LOW);
  digitalWrite(AP_CONNECTION_PIN, LOW);
  digitalWrite(HOST_CONNECTION_PIN, LOW);
  digitalWrite(LIGHT_PIN, LOW);
  smart::initId();
}

void loop() {
  using smart::deviceState;
  switch(deviceState.connectionStage) {
    case CONNECTION_STAGE_FILE:
      smart::useConfigFile();
      if (deviceState.connectionStage == CONNECTION_STAGE_CONNECTED) {
        digitalWrite(NORMAL_STATE_PIN, HIGH);
        clientModeState();
      }
      break;
    case CONNECTION_STAGE_SERVER:
      smart::useConfigServer(configServer);
      if (deviceState.connectionStage == CONNECTION_STAGE_CONNECTED) {
        smart::cleanConfigServer(configServer);
        digitalWrite(NORMAL_STATE_PIN, HIGH);
        clientModeState();
      }
      break;
    case CONNECTION_STAGE_CONNECTED:
      if (client == NULL) {
        client = new smart::Client(deviceState.serverHost, SERVER_PORT);
        bool connected = client->connect();
        while(!connected) {
          delay(1000);
          connected = client->connect();
        }
        StaticJsonBuffer<smart::Client::MAX_MESSAGE_SIZE> buffer;
        JsonObject& root = buffer.createObject();
        root["deviceId"] = deviceState.deviceId;
        root["deviceKey"] = deviceState.key;
        root["firmwareVersion"] = FIRMWARE_VERSION;
        root.createNestedArray("sensors");
        root.createNestedArray("controls");
        client->write(root);
        bool received = client->read(message_buffer);
        while(!received) {
          delay(1000);
          Serial.println("Waiting for message");
          received = client->read(message_buffer);
        }
        Serial.printf("Received: %s\n", message_buffer);
      }
      if (client->read(message_buffer)) {
        Serial.println(F("Received a message: "));
        Serial.println(message_buffer);
        StaticJsonBuffer<smart::Client::MAX_MESSAGE_SIZE> buffer;
        JsonObject& root = buffer.parseObject(message_buffer);
        if (root["command"] == 1) {
          digitalWrite(LIGHT_PIN, HIGH);
          client->write("Ok, let's turn on light");
        }
        if (root["command"] == 2) {
          digitalWrite(LIGHT_PIN, LOW);
          client->write("Ok, let's turn off light");
        }
      }
      digitalWrite(NORMAL_STATE_PIN, HIGH);
      delay(1000);
      digitalWrite(NORMAL_STATE_PIN, LOW);
      delay(1000);
      break;
  }
}
#endif
