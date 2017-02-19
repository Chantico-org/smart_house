#include "Client.h"

const int BIT_MASK = 255;
uint32_t convertFromBytes(const uint8_t* bytes) {
  uint32_t result = 0;
  for (int i = 0; i < 3; i++){
    result |= bytes[i];
    result = result << 8;
  }
  result |= bytes[3];
  return result;
}
bool smart::Client::read(char* buffer){
  if (wifi_client->available() < 1) {
    return false;
  }
  uint32_t length = 0;
  uint8_t* bytes = new uint8_t[Client::MESSAGE_LENGTH];
  wifi_client->readBytes(bytes, Client::MESSAGE_LENGTH);
  for (int i=0; i < (Client::MESSAGE_LENGTH - 1); i++) {
    length |= bytes[i];
    length = length << 8;
  }
  length |= bytes[Client::MESSAGE_LENGTH - 1];
  Serial.print(F("Bytes in message: "));
  Serial.println(length);
  wifi_client->readBytes(buffer, length);
  buffer[length] = '\0';
  return true;
}
void smart::Client::writeClientLength(int data_length) {
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
}
void smart::Client::write(char* data) {
  writeClientLength(strlen(data));
  wifi_client->print(data);
  Serial.println(data);
}
void smart::Client::write(JsonObject& json) {
  json.printTo(data_buffer, Client::MAX_MESSAGE_SIZE);
  Serial.print(F("Result "));
  Serial.println(data_buffer);
  int data_length = strlen(data_buffer);
  writeClientLength(data_length);
  wifi_client->print(data_buffer);
}

bool smart::Client::connect(int timeout) {
  Serial.printf("Conneting to %s:[%d]\n", host, port);
  int state = LOW;
  digitalWrite(HOST_CONNECTION_PIN, state);
  bool connected = wifi_client->connect(host, port);
  state = !state;
  digitalWrite(HOST_CONNECTION_PIN, state);
  unsigned long begin_millis = millis();
  while(!connected) {
    if (millis() - begin_millis > timeout) {
      Serial.println(F("Connection timeout"));
      digitalWrite(HOST_CONNECTION_PIN, HIGH);
      return false;
    }
    Serial.println(F("Cannot connect to host"));
    delay(500);
    connected = wifi_client->connect(host, port);
    state = !state;
    digitalWrite(HOST_CONNECTION_PIN, state);
  }
  digitalWrite(HOST_CONNECTION_PIN, LOW);
  return true;
}
