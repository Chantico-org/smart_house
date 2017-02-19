#ifndef CONFIG_SERVER_H
#define CONFIG_SERVER_H

#include <ESP8266WiFi.h>
#include <WiFiClient.h>
#include <ESP8266WebServer.h>
#include "Constants.h"
#include "Commons.h"
#include "ConfigFile.h"

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

	void useConfigServer(ConfigServer*&);

	void cleanConfigServer(ConfigServer*&);
}

inline void smart::useConfigServer(ConfigServer*& configServer) {
	if (configServer == NULL) {
		configServer = new smart::ConfigServer();
	}
	configServer->handleClient();
}

inline void smart::ConfigServer::handleClient() {
	server->handleClient();
}

#endif // CONFIG_SERVER_H
