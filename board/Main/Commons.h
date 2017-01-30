#ifndef COMMONS_H
#define COMMONS_H

#define DEBUG_COMMONS

#include <ESP8266WiFi.h>


#define DEFAULT_TIME_OUT 30000

#define CONNECTION_STAGE_FILE 0
#define CONNECTION_STAGE_SERVER 1
#define CONNECTION_STAGE_CONNECTED 2

namespace smart {
	struct DeviceState
	{
		char connectionStage = CONNECTION_STAGE_FILE;
		char* ssid = NULL;
		char* password = NULL;
	};
	extern DeviceState deviceState;

	bool isValidAP(const char*);
	bool isValidAP(String);

	bool connectToAP(int timeout = DEFAULT_TIME_OUT);

	char* allocCharP(String&);
	char* allocCharP(const char*);
}

#endif
