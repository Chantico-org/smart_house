#include "ConfigServer.h"

#ifdef DEBUG_CONFIG_SERVER
#endif//debug output

const String error = "{\"res\": \"Something went wrog\"}";
const String contentType = "application/json";

smart::ConfigServer::ConfigServer() {
	#ifdef DEBUG_CONFIG_SERVER
	Serial.println("Creating ConfigServer");
	#endif//debug output

	IPAddress ip(WiFi.softAPIP());
	server = new ESP8266WebServer(ip, 8080);

	server->on("/config", HTTP_GET, [this]() {
		this->handleConfig();
	});
	server->begin();

	#ifdef DEBUG_CONFIG_SERVER
	Serial.println("ConfigServer started");
	#endif//debug output
}

void smart::ConfigServer::handleConfig() {
	using smart::deviceState;

	String ssid = server->arg("ssid");
	String pass = server->arg("pass");

	#ifdef DEBUG_CONFIG_SERVER
	Serial.println("Config Handle");
	Serial.printf("Number of arguments: %d \n", server->args());
	Serial.println("String SSID: " + ssid);
	Serial.println("String Password: " + pass);
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
	server->send(200, contentType, "{\"res\": \"Connected\"}");
}

void smart::ConfigServer::handleClient() {
	server->handleClient();
}

smart::ConfigServer::~ConfigServer() {
	#ifdef DEBUG_CONFIG_SERVER
	Serial.println("Destroying ConfigServer");
	#endif//debug output

	delete server;
}
// http://192.168.4.1:8080/config?ssid=TP-LINK_40393C&pass=64700240393c
