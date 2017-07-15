CC=g++
ARDUINO_JSON_PATH=/root/Arduino/libraries/ArduinoJson
MAIN_PATH = $(PWD)/../Main
C_FLAGS = -I $(ARDUINO_JSON_PATH) -I $(MAIN_PATH) -D DESKTOP

W_FLAGS = -Wno-write-strings
# $(CC) $(C_FLAGS) $(W_FLAGS) test.cpp $(MAIN_PATH)/RequestHandler.cpp $(MAIN_PATH)/ResourceHandler.cpp $(MAIN_PATH)/SimpCommons.cpp -o test.bin
run:
  $(CC) $(W_FLAGS) test.cpp -o test.bin;
	./test.bin
