#include "Client.h"

void smart::Client::connect() {
  Serial.printf("Conneting to %s:[%d]\n", host, port);
  int state = LOW;
  digitalWrite(HOST_CONNECTION_PIN, state);
  bool connected = wifi_client->connect(host, port);
  state = !state;
  digitalWrite(HOST_CONNECTION_PIN, state);
  unsigned long begin_millis = millis();
  while(!connected) {
    Serial.println(F("Cannot connect to host"));
    delay(500);
    connected = wifi_client->connect(host, port);
    state = !state;
    digitalWrite(HOST_CONNECTION_PIN, state);
  }
  digitalWrite(HOST_CONNECTION_PIN, LOW);
}
