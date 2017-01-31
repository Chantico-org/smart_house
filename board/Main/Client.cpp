#include "Client.h"

void smart::Client::write(JsonObject& json) {
  json.printTo(data_buffer, MAX_MESSAGE_SIZE);
  Serial.printf("Result %s\n", data_buffer);
  const char data_length = strlen(data_buffer);
  Serial.println(strlen(data_buffer));
  wifi_client->print(data_length);
  wifi_client->write(data_buffer, data_length);
}
