#include "ConfigServer.h"

IPAddress apIP(192, 168, 4, 1);
IPAddress netMsk(255, 255, 255, 0);
const String error = "{\"res\": \"Something went wrog\"}";
const String contentType = "application/json";

smart::ConfigServer::ConfigServer() {
	#ifdef DEBUG_CONFIG_SERVER
	Serial.println(F("Creating ConfigServer"));
	Serial.print(F("Setting soft-AP ... "));
	#endif//debug output
	WiFi.mode(WIFI_AP);
  WiFi.softAPConfig(apIP, apIP, netMsk);
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
	digitalWrite(HOST_CONNECTION_PIN, HIGH);
	digitalWrite(AP_CONNECTION_PIN, HIGH);
	#ifdef DEBUG_CONFIG_SERVER
	Serial.println(F("ConfigServer started"));
	#endif//debug output
}

void smart::ConfigServer::handleConfig() {
	using smart::deviceState;

	String ssid = server->arg("ssid");
	String pass = server->arg("pass");
	String key = server->arg("key");
	String host = server->arg("host");

	#ifdef DEBUG_CONFIG_SERVER
	Serial.println(F("Config Handle"));
	Serial.print(F("Number of arguments: "));
	Serial.println(server->args());
	Serial.print(F("String SSID: "));
	Serial.println(ssid);
	Serial.print(F("String Password: "));
	Serial.println(pass);
	Serial.print(F("String Key: "));
	Serial.println(key);
	Serial.print(F("String host: "));
	Serial.println(host);
	#endif//debug output

	if (!smart::isValidAP(ssid)) {
		server->send(400, contentType, error);
		return;
	}
	server->send(200, contentType, "{\"res\": \"connecting\"}");

	delete deviceState.ssid;
	deviceState.ssid = smart::allocCharP(ssid);

	delete deviceState.password;
	deviceState.password = smart::allocCharP(pass);

	delete deviceState.key;
	deviceState.key = smart::allocCharP(key);

	delete deviceState.serverHost;
	deviceState.serverHost = smart::allocCharP(host);

	if (!smart::connectToAP()) {
		return;
	}

	smart::saveDeviceState();
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
// deviceId: 1B66D01640E0
// http://192.168.4.1:8080/config?ssid=TP-LINK_40393C&pass=64700240393c&key=f4d749cb-58e0-4cf8-a0d4-45dce15669ad&host=192.168.0.104
