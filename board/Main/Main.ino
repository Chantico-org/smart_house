#include <ESP8266WiFi.h>
#include "Constants.h"
#include "Commons.h"
#include "ConfigServer.h"
#include "ConfigFile.h"
#include "FS.h"
#include "Client.h"
#include <string>
#include "SimpClient.h"

simp::SimpClient<smart::Client>* client;

smart::ConfigServer *configServer = NULL;

void restartClick() {
  Serial.println("Restart click");
  smart::eraseConfigFile();
}

void clientModeState() {
  Serial.println(F("Switching to lient mode state"));
  WiFi.mode(WIFI_STA);
}

char* response = "OK";
char* wrong = "NO";

uint8_t* changeLight(uint8_t* request) {
  size_t length = strlen((char*) request);
  if (length != 1) {
    return (uint8_t*) wrong;
  }
  int state = request[0] - 48;
  digitalWrite(LIGHT_PIN, state);
  return (uint8_t*)response;
}

void setupClient() {
  using smart::deviceState;
  client = new simp::SimpClient<smart::Client>(new smart::Client(deviceState.serverHost));
  client->onRequest("/control/1", changeLight);
}

#ifdef TEST
void setup() {
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
        setupClient();
      }
      break;
    case CONNECTION_STAGE_SERVER:
      smart::useConfigServer(configServer);
      if (deviceState.connectionStage == CONNECTION_STAGE_CONNECTED) {
        smart::cleanConfigServer(configServer);
        digitalWrite(NORMAL_STATE_PIN, HIGH);
        clientModeState();
        setupClient();
      }
      break;
    case CONNECTION_STAGE_CONNECTED:
//    TODO reconnect to AP
      Serial.println("CONNECTED");
      client->loop();
      digitalWrite(NORMAL_STATE_PIN, HIGH);
      delay(1000);
      digitalWrite(NORMAL_STATE_PIN, LOW);
      delay(1000);
      break;
  }
}
#endif
