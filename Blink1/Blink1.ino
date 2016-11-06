/*
 ESP8266 Blink by Simon Peter
 Blink the blue LED on the ESP-01 module
 This example code is in the public domain
 
 The blue LED on the ESP-01 module is connected to GPIO1 
 (which is also the TXD pin; so we cannot use Serial.print() at the same time)
 
 Note that this sketch uses LED_BUILTIN to find the pin with the internal LED
*/

void setup() {
  Serial.begin(115200);
  Serial.println();
  pinMode(D7, OUTPUT);     // Initialize the LED_BUILTIN pin as an output
}

// the loop function runs over and over again forever
void loop() {
  delay(3000);
  digitalWrite(D7, HIGH);   // Turn the LED on (Note that LOW is the voltage level
                                    // but actually the LED is on; this is because 
                                    // it is acive low on the ESP-01)
  delay(5000);                      // Wait for a second
  digitalWrite(D7, LOW);  // Turn the LED off by making the voltage HIGH
//  delay(2 000);                      // Wait for two seconds (to demonstrate the active low LED)
}
