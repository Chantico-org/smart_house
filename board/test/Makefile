CC=g++
ARDUINO_JSON_PATH=/root/Arduino/libraries/ArduinoJson
MAIN_PATH = $(PWD)/../Main
C_FLAGS = -I $(ARDUINO_JSON_PATH) -I $(MAIN_PATH) -D DESKTOP

W_FLAGS = -Wno-write-strings

run:
	$(CC) $(C_FLAGS) $(W_FLAGS) test.cpp ../Main/RequestHandler.cpp -o test.bin
	./test.bin
