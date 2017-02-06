#include "Client.h"

const int BIT_MASK = 255;

void smart::Client::write(JsonObject& json) {
  json.printTo(data_buffer, Client::MAX_MESSAGE_SIZE);
  Serial.print(F("Result "));
  Serial.println(data_buffer);
  int data_length = strlen(data_buffer);
  Serial.print(F("Data Length: "));
  Serial.println(data_length);
  for(int i=Client::MESSAGE_LENGTH - 1; i != -1; i--) {
    uint8_t byte = data_length & 255;
    Serial.printf("Byte: %d\n", byte);
    length_field[i] = byte;
    data_length = data_length >> 8;
  }
  const uint8_t* temp = length_field;
  for (int i = 0; i < Client::MESSAGE_LENGTH; i++){
    Serial.printf("In Result: %d\n", temp[i]);
  }
  // TODO: check that network write correct amount of bytes
  wifi_client->write(temp, Client::MESSAGE_LENGTH);
  wifi_client->print(data_buffer);
}

bool smart::Client::connect(int timeout) {
  Serial.printf("Conneting to %s:%d\n", host, port);
  bool connected = wifi_client->connect(host, port);
  unsigned long begin_millis = millis();
  Serial.printf("connected %d\n", connected);
  while(!connected) {
    digitalWrite(D6, HIGH);
    if (millis() - begin_millis > timeout) {
      Serial.println(F("Connection timeout"));
      return false;
    }

    Serial.println(F("Cannot connect to host"));
    delay(500);
    connected = wifi_client->connect(host, port);
  }
  digitalWrite(D6, LOW);
  return true;
}
