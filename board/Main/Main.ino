#include <ESP8266WiFi.h>
#include <Ticker.h>
#include "Commons.h"
#include "ConfigServer.h"
#include "ConfigFile.h"
#include "FS.h"

smart::ConfigServer *configServer = NULL;
Ticker d5_timer;

#define TEST

#ifdef TEST
void setup() {
  Serial.begin(115200);
  Serial.println();
  Serial.println();
  pinMode(D5, OUTPUT);
  pinMode(D6, OUTPUT);
  pinMode(D7, OUTPUT);
  // d5_timer.attach(2, D5_blink);
  // pinMode(D7, INPUT_PULLUP);
  // attachInterrupt(digitalPinToInterrupt(D7), btn_push, CHANGE);
}
#else
void setup() {
  Serial.begin(115200);
  Serial.println();
  Serial.println();
  pinMode(D5, OUTPUT);
}
#endif

#ifdef TEST
void btn_push() {
  Serial.println("Button push");
  d5_timer.detach();
}
#endif

void D5_blink() {
  int state = digitalRead(D5);
  digitalWrite(D5, !state);
}

#ifdef TEST
void loop() {
  delay(1000);
  digitalWrite(D5, HIGH);
  delay(1000);
  digitalWrite(D6, HIGH);
  delay(1000);
  digitalWrite(D7, HIGH);
  delay(1000);
  digitalWrite(D7, LOW);
  delay(1000);
  digitalWrite(D6, LOW);
  delay(1000);
  digitalWrite(D5, LOW);
}
#else
void loop() {
	using smart::deviceState;
  switch(deviceState.connectionStage) {
    case CONNECTION_STAGE_FILE:
      smart::useConfigFile();
      if (deviceState.connectionStage == CONNECTION_STAGE_CONNECTED) {
        d5_timer.attach(2, D5_blink);
      }
      break;
    case CONNECTION_STAGE_SERVER:
      smart::useConfigServer(configServer);
      if (deviceState.connectionStage == CONNECTION_STAGE_CONNECTED) {
        smart::cleanConfigServer(configServer);
        d5_timer.attach(2, D5_blink);
      }
      break;
    case CONNECTION_STAGE_CONNECTED:
      // digitalWrite(D5, HIGH);
      // delay(2000);
      // digitalWrite(D5, LOW);
      // delay(2000);
      break;
  }
}
#endif