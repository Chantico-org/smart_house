#ifndef COMMONS_H
#define COMMONS_H

#define DEBUG_COMMONS

#include <ESP8266WiFi.h>
#include "Constants.h"

#define DEFAULT_TIME_OUT 30000

#define CONNECTION_STAGE_FILE 0
#define CONNECTION_STAGE_SERVER 1
#define CONNECTION_STAGE_CONNECTED 2

namespace smart {
  extern const char* idFormat;

	struct DeviceState
	{
		char* deviceId = new char[16];
		char connectionStage = CONNECTION_STAGE_FILE;
		char* ssid = NULL;
		char* password = NULL;
		char* key = NULL;
		char* serverHost = NULL;
	};

	extern DeviceState deviceState;
	inline void initId() {
  	sprintf(deviceState.deviceId, idFormat, ESP.getChipId(), ESP.getFlashChipId());
	}
	bool isValidAP(const char*);
	bool isValidAP(String);

	bool connectToAP(int timeout = DEFAULT_TIME_OUT);

	char* allocCharP(String&);
	char* allocCharP(const char*);
}

#endif
