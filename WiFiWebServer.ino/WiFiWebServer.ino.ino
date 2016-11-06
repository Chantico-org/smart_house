#include <ESP8266WiFi.h>


char ssid[] = "CAMPUS";      // your network SSID (name)
char pass[] = "CAMPUS2016";   // your network password

const char host[] = "46.182.83.195";
const int port = 8080;

int status = WL_IDLE_STATUS;
//WiFiClient client;
void setup() {
  //Initialize serial and wait for port to open:
  Serial.begin(115200);
  delay(10);
  while (!Serial) {
    ; // wait for serial port to connect. Needed for native USB port only
  }
  Serial.println();
  Serial.println();
  Serial.print("Connecting to ");
  Serial.println(ssid);
  WiFi.begin(ssid, pass);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("");
  Serial.println("WiFi connected");  
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());
}

void printMessage() {
  Serial.print("Connecting to ");
  Serial.print(host);
  Serial.print(":");
  Serial.println(port);
}

void loop() { 
  printMessage();
  WiFiClient client;
  if (!client.connect(host, port)) {
    Serial.println("connection failed");
    delay(10000);
    return;
  }
  client.print("Hello world");
  client.stop();
  delay(5000);
}

