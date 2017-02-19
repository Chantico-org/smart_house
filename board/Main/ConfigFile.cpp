#include "ConfigFile.h"

#define JSON_BUF_SIZE

#ifdef CONFIG_STUB
bool useStub() {
  using smart::deviceState;
  deviceState.ssid = "TP-LINK_40393C";
  deviceState.password = "64700240393c";
  if (!smart::connectToAP()) {
    return false;
  }
  return true;
}
#endif

void smart::eraseConfigFile() {
  #ifdef DEBUG_CONFIG_FILE
  Serial.println(F("Mounting FS..."));
  #endif

  if (!SPIFFS.begin()) {
    #ifdef DEBUG_CONFIG_FILE
    Serial.println(F("Failed to mount file system"));
    #endif
    return;
  }
  if (SPIFFS.exists("/config.json")) {
    #ifdef DEBUG_CONFIG_FILE
    Serial.println(F("File exists"));
    #endif
    SPIFFS.remove("/config.json");
  }
}

void smart::useConfigFile() {
  using smart::deviceState;

  #ifdef DEBUG_CONFIG_FILE
  Serial.println(F("Mounting FS..."));
  #endif

  if (!SPIFFS.begin()) {
    #ifdef DEBUG_CONFIG_FILE
    Serial.println(F("Failed to mount file system"));
    #endif
    return;
  }

  #ifdef DEBUG_CONFIG_FILE
  Serial.println(F("Reading config file..."));
  #endif

  #ifdef CONFIG_STUB
  Serial.println(F("Using stub file"));
  if (useStub()) {
    return;
  }
  #endif

  File configFile = SPIFFS.open("/config.json", "r");
  if (!configFile) {
    #ifdef DEBUG_CONFIG_FILE
    Serial.println(F("File not exists."));
    #endif
    deviceState.connectionStage = CONNECTION_STAGE_SERVER;
    return;
  }

  size_t size = configFile.size();
  std::unique_ptr<char[]> buf(new char[size]);
  configFile.readBytes(buf.get(), size);
  configFile.close();
  StaticJsonBuffer<200> jsonBuffer;
  JsonObject& json = jsonBuffer.parseObject(buf.get());

  if (!json.success()) {

    #ifdef DEBUG_CONFIG_FILE
    Serial.println(F("Failed to parse config file"));
    #endif

    deviceState.connectionStage = CONNECTION_STAGE_SERVER;
    return;
  }

  delete deviceState.ssid;
  deviceState.ssid = smart::allocCharP(json["ssid"]);

  delete deviceState.password;
  deviceState.password = smart::allocCharP(json["pass"]);

  delete deviceState.key;
  deviceState.key = smart::allocCharP(json["key"]);

  delete deviceState.serverHost;
  deviceState.serverHost = smart::allocCharP(json["host"]);

  if (!smart::connectToAP()) {
    deviceState.connectionStage = CONNECTION_STAGE_SERVER;
    return;
  }
  deviceState.connectionStage = CONNECTION_STAGE_CONNECTED;
  SPIFFS.end();
}

bool smart::saveDeviceState() {
  using smart::deviceState;
  StaticJsonBuffer<200> jsonBuffer;
  JsonObject& json = jsonBuffer.createObject();
  json["ssid"] = deviceState.ssid;
  json["pass"] = deviceState.password;
  json["key"] = deviceState.key;
  json["host"] = deviceState.serverHost;
  File configFile = SPIFFS.open("/config.json", "w");
  if (!configFile) {
    #ifdef DEBUG_CONFIG_FILE
    Serial.println(F("Failed to open config file for writing"));
    #endif
    return false;
  }
  json.printTo(configFile);
  configFile.close();
  return true;
}
