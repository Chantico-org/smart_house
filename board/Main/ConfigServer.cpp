#include "ConfigServer.h"

// step 0
// 273,581
// 34,916

// step 1
// 273,805
// 34,708

// final
// 273,797
// 34,436

const String error = "{\"res\": \"Something went wrog\"}";
const String contentType = "application/json";

smart::ConfigServer::ConfigServer() {
	#ifdef DEBUG_CONFIG_SERVER
	Serial.println(F("Creating ConfigServer"));
	Serial.print(F("Setting soft-AP ... "));
	#endif//debug output

	bool APsucess = WiFi.softAP("ESP_01");

	#ifdef DEBUG_CONFIG_SERVER
  Serial.println(APsucess ? F("Ready") : F("Failed!"));
	Serial.print(F("Soft-AP IP address = "));
  Serial.println(WiFi.softAPIP());
	#endif//debug output

	IPAddress ip(WiFi.softAPIP());
	server = new ESP8266WebServer(ip, 8080);

	server->on("/config", HTTP_GET, [this]() {
		this->handleConfig();
	});
	server->begin();

	#ifdef DEBUG_CONFIG_SERVER
	Serial.println(F("ConfigServer started"));
	#endif//debug output
}

void smart::ConfigServer::handleConfig() {
	using smart::deviceState;

	String ssid = server->arg("ssid");
	String pass = server->arg("pass");

	#ifdef DEBUG_CONFIG_SERVER
	Serial.println(F("Config Handle"));
	Serial.print(F("Number of arguments: "));
	Serial.println(server->args());
	Serial.print(F("String SSID: "));
	Serial.println(ssid);
	Serial.print(F("String Password: "));
	Serial.println(pass);
	#endif//debug output

	if (!smart::isValidAP(ssid)) {
		server->send(400, contentType, error);
		return;
	}

	delete deviceState.ssid;
	deviceState.ssid = smart::allocCharP(ssid);

	delete deviceState.password;
	deviceState.password = smart::allocCharP(pass);


	if (!smart::connectToAP()) {
		server->send(400, contentType, error);
		return;
	}
	smart::saveDeviceState();
	server->send(200, contentType, "{\"res\": 0}");
}

smart::ConfigServer::~ConfigServer() {
	#ifdef DEBUG_CONFIG_SERVER
	Serial.println(F("Destroying ConfigServer"));
	#endif//debug output

	delete server;
}

void smart::cleanConfigServer(ConfigServer*& configServer){
	using smart::deviceState;
	if (configServer != NULL) {
		#ifdef DEBUG_CONFIG_SERVER
		Serial.print(F("Stored ssid: "));
		Serial.println(deviceState.ssid);
		Serial.print(F("Stored pass: "));
		Serial.println(deviceState.password);
		#endif
		delete configServer;
		configServer = NULL;
	}
}

// http://192.168.4.1:8080/config?ssid=TP-LINK_40393C&pass=64700240393c
