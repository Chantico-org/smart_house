#include <ESP8266WiFi.h>
// #include <OneWire.h>
// #include <DallasTemperature.h>
// #define ONE_WIRE_BUS D8// DS18B20 pin
// OneWire *oneWire;
// DallasTemperature *DS18B20;
#include "Commons.h"
#include "ConfigServer.h"

// const char* ssid = "TP-LINK_40393C";
// const char* password = "64700240393c";
// IPAddress local_IP(192,168,4,22);
// IPAddress gateway(192,168,4,9);
// IPAddress subnet(255,255,255,0);
smart::ConfigServer *configServer = NULL;

void setup() {
  Serial.begin(115200);
  Serial.println();
  Serial.println();
  // pinMode(D7, OUTPUT);
  // oneWire = new OneWire(ONE_WIRE_BUS);
  // DS18B20 = new DallasTemperature(oneWire);
  // pinMode(D8, INPUT);
  // Serial.print("Setting soft-AP configuration ... ");
  // Serial.println(WiFi.softAPConfig(local_IP, gateway, subnet) ? "Ready" : "Failed!");

  Serial.print("Setting soft-AP ... ");
  Serial.println(WiFi.softAP("ESP_01") ? "Ready" : "Failed!");

  Serial.print("Soft-AP IP address = ");
  Serial.println(WiFi.softAPIP());
}

void loop() {
	// Serial.println("High");
	// digitalWrite(D7, HIGH);
	// delay(1000);
	// Serial.println("LOW");
	// digitalWrite(D7, LOW);
	// delay(1000);
	// float temp;
	// DS18B20->requestTemperatures();
 //  temp = DS18B20->getTempCByIndex(0);
 //  Serial.print("Temperature: ");
 //  Serial.println(temp);

	using smart::deviceState;
	if (!deviceState.isConnected) {
		if (configServer == NULL) {
			configServer = new smart::ConfigServer();
		}
		configServer->handleClient();
	} else {
		if (configServer != NULL) {
			Serial.printf("Stored ssid %s\n", deviceState.ssid);
			Serial.printf("Stored pass %s\n", deviceState.password);
			delete configServer;
			configServer = NULL;
		}
	}
}
