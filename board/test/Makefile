CC=g++
ARDUINO_JSON_PATH=/home/alexei/temp/ArduinoJson
MAIN_PATH = $(PWD)/../Main
C_FLAGS = -I $(ARDUINO_JSON_PATH) -I $(MAIN_PATH) -D DESKTOP

run:
	$(CC) $(C_FLAGS) test.cpp -o test.bin
	./test.bin
