#include "Commons.h"

smart::DeviceState smart::deviceState;

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
  const int sourceLength = source.length();
  char *result  = new char[sourceLength + 1];
  source.toCharArray(result, sourceLength + 1);
  return result;
}

bool smart::connectToAP(int timeout) {
  using smart::deviceState;
  const char* ssid = deviceState.ssid;
  const char* password = deviceState.password;
  WiFi.begin(ssid, password);
  unsigned long begin_millis = millis();
  #ifdef DEBUG_COMMONS
  Serial.printf("Connecting to %s [%s]\n", ssid, password);
  #endif
  while (WiFi.status() != WL_CONNECTED)
  {
    if(millis() - begin_millis > timeout) {
      #ifdef DEBUG_COMMONS
      Serial.println();
      Serial.println("Connection timeout");
      #endif
      deviceState.isConnected = false;
      return false;
    }
    delay(500);
    #ifdef DEBUG_COMMONS
    Serial.print(".");
    #endif
  }
  #ifdef DEBUG_COMMONS
  Serial.println(" connected");
  Serial.printf("IP [%s]\n", WiFi.localIP().toString().c_str());
  #endif
  deviceState.isConnected = true;
  return true;
}
