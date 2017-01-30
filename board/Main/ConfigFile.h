#ifndef CONFIG_FILE_H
#define CONFIG_FILE_H

#include <ESP8266WiFi.h>
#include <ArduinoJson.h>
#include "Commons.h"
#include "FS.h"


#define DEBUG_CONFIG_FILE

namespace smart {
  void useConfigFile();
  bool saveDeviceState();
} // smart

#endif // CONFIG_FILE_H
