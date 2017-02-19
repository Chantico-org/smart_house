#include "Commons.h"

smart::DeviceState smart::deviceState;
const char* smart::idFormat = "%X%X";
bool smart::isValidAP(const char* ssid) {
  int networksFound = WiFi.scanNetworks();
  #ifdef DEBUG_COMMONS
  Serial.printf("Looking for %s\n", ssid);
  #endif
  for (int i = 0; i < networksFound; i++) {
    String network = WiFi.SSID(i);
    #ifdef DEBUG_COMMONS
    Serial.printf("Comparing to %s result: %d\n", network.c_str(), strcmp(network.c_str(), ssid));
    #endif
    if (strcmp(network.c_str(), ssid) == 0) {
      return true;
    }
  }
  return false;
}

bool smart::isValidAP(String ssid) {
  return smart::isValidAP(ssid.c_str());
}

char* smart::allocCharP(String &source) {
  // to include null terminator
  const int sourceLength = source.length() + 1;
  char *result  = new char[sourceLength];
  source.toCharArray(result, sourceLength);
  return result;
}

char* smart::allocCharP(const char* source) {
  const int sourceLength = strlen(source) + 1;
  char *result  = new char[sourceLength];
  strcpy(result, source);
  return result;
}

bool smart::connectToAP(int timeout) {
  using smart::deviceState;
  const char* ssid = deviceState.ssid;
  const char* password = deviceState.password;
  WiFi.begin(ssid, password);
  unsigned long begin_millis = millis();
  #ifdef DEBUG_COMMONS
  Serial.print(F("Connecting to "));
  Serial.printf("%s [%s]\n", ssid, password);
  #endif
  int state = LOW;
  digitalWrite(AP_CONNECTION_PIN, state);
  while (WiFi.status() != WL_CONNECTED)
  {
    if(millis() - begin_millis > timeout) {
      #ifdef DEBUG_COMMONS
      Serial.println();
      Serial.println(F("Connection timeout"));
      #endif
      digitalWrite(AP_CONNECTION_PIN, HIGH);
      deviceState.connectionStage = CONNECTION_STAGE_SERVER;
      return false;
    }
    state = !state;
    digitalWrite(AP_CONNECTION_PIN, state);
    delay(500);
    #ifdef DEBUG_COMMONS
    Serial.print(F("."));
    #endif
  }
  #ifdef DEBUG_COMMONS
  Serial.println(F(" connected"));
  Serial.print(F("IP "));
  Serial.printf("[%s]\n", WiFi.localIP().toString().c_str());
  #endif
  digitalWrite(AP_CONNECTION_PIN, LOW);
  deviceState.connectionStage = CONNECTION_STAGE_CONNECTED;
  return true;
}
