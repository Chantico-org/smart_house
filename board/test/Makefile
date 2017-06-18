CC=g++
ARDUINO_JSON_PATH=/root/Arduino/libraries/ArduinoJson
MAIN_PATH = $(PWD)/../Main
SRC = test.cpp #$(PWD)/../Main/SimpClient.cpp test.cpp

run:
	$(CC) -I $(ARDUINO_JSON_PATH) -I $(MAIN_PATH) $(SRC) -o test
	./test
