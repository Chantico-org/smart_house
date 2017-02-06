#include <ESP8266WiFi.h>
#define RELAY_PIN D8
void setup() {
  // put your setup code here, to run once:
  pinMode(RELAY_PIN, OUTPUT);
  digitalWrite(RELAY_PIN , LOW);
}

void loop() {
  digitalWrite(RELAY_PIN , HIGH);
  delay(1000);
  digitalWrite(RELAY_PIN , LOW);
  delay(1000);
}
