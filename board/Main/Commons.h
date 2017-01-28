#ifndef COMMONS_H
#define COMMONS_H

#define DEBUG_COMMONS

#include <ESP8266WiFi.h>

#define DEFAULT_TIME_OUT 10000

namespace smart {
	struct DeviceState
	{
		bool isConnected = false;
		char* ssid = NULL;
		char* password = NULL;
	};
	extern DeviceState deviceState;

	bool isValidAP(const char*);
	bool isValidAP(String);

	bool connectToAP(int timeout = DEFAULT_TIME_OUT);

	char* allocCharP(String&);
}

#endif
