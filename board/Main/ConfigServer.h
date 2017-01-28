#ifndef CONFIG_SERVER_H
#define CONFIG_SERVER_H

#include <ESP8266WiFi.h>
#include <WiFiClient.h>
#include <ESP8266WebServer.h>
#include "Commons.h"

#define DEBUG_CONFIG_SERVER

namespace smart {
	class ConfigServer
	{
		private:
			ESP8266WebServer *server;
			void handleConfig();
		public:
			ConfigServer();
			void handleClient();
			~ConfigServer();
	};
}

#endif // CONFIG_SERVER_H
