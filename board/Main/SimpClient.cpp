#include <SimpClient.h>

#ifdef DESKTOP

#include <iostream>
#include <stdio.h>
#include <unistd.h>

#define PRINTLN(line) cout << line << endl

#else
#define PRINTLN(line)
#endif
using namespace std;
using namespace simp;

SimpMessageType simp::infereFromByte(uint8_t byte) {
  return (SimpMessageType) byte;
}

uint32_t convertFromBytes(const uint8_t* bytes, size_t messageLength) {
  uint32_t result = 0;
  uint32_t index = messageLength - 1;
  for (int i = 0; i < index; i++){
    result |= bytes[i];
    result = result << 8;
  }
  result |= bytes[index];
  return result;
}

uint8_t simp::convertToByte(SimpMessageType type) {
  return (uint8_t) type;
}


template <class T>
SimpClient<T>::SimpClient(T* client) {
  this->client = client;
  dataBuffer = new uint8_t[MAX_MESSAGE_SIZE];
  lengthBuffer = new uint8_t[MESSAGE_LENGTH];
  inBoundMessage = new SimpMessage;
  outBoundMessage = new SimpMessage;
  requestHandlers = new forward_list<RequestHandler*>;
}

template<class T>
void SimpClient<T>::write(const uint8_t* buf, size_t size) {
  pendingBytes = size;
  while(pendingBytes != 0) {
    size_t writed = client->write(buf, size);
    pendingBytes -= writed;
  }
}

template <class T>
void SimpClient<T>::write(uint8_t byte) {
  pendingBytes = 1;
  while(pendingBytes != 0) {
    size_t writed = client->write(byte);
    pendingBytes -= writed;
  }
}

template <class T>
void SimpClient<T>::writeLength(uint32_t dataLength) {
  if (dataLength > MAX_MESSAGE_SIZE) throw 234;
  for(int i = MESSAGE_LENGTH - 1; i != -1; i--) {
    uint8_t byte = dataLength & 255;
    lengthBuffer[i] = byte;
    dataLength = dataLength >> 8;
  }
  write(lengthBuffer, MESSAGE_LENGTH);
}

template <class T>
void SimpClient<T>::writeMessage(SimpMessage* message) {
  uint32_t length = message->dataLength;
  writeLength(length + 1);
  write(convertToByte(message->type));
  write(message->data, length);
}

template <class T>
bool SimpClient<T>::readMessage(SimpMessage* message) {
  if (!client->available()) return false;
  PRINTLN("readMessage");
  size_t readed = client->read(lengthBuffer, MESSAGE_LENGTH);
  if (readed == 0) return false;
  uint32_t size = convertFromBytes(lengthBuffer, MESSAGE_LENGTH);
  uint32_t offset = 0;
  while(offset!=size) {
    readed = client->read(dataBuffer + offset, size - offset);
    offset += readed;
  }
  message->type = infereFromByte(dataBuffer[0]);
  message->dataLength = size - 1;
  dataBuffer[size] = 0;
  message->data = dataBuffer + 1;
  return true;
}

template <class T>
void SimpClient<T>::loop() {
  StaticJsonBuffer<1000> jsonBuffer;
  PRINTLN("LOOP");
  if (!client->connected()) {
    outBoundMessage->type = SimpMessageType::REQUEST;
    JsonObject& root = jsonBuffer.createObject();
    root["deviceId"] = "fewf";
    root["deviceKey"] = "secret";
    root["firmwareVersion"] = 0x0000d1;
    root.createNestedArray("sensors");
    root.createNestedArray("controls");
    root.printTo((char*)dataBuffer, MAX_MESSAGE_SIZE);
    outBoundMessage->data = dataBuffer;
    outBoundMessage->dataLength = strlen((char*)outBoundMessage->data);
    client->connect();
    writeMessage(outBoundMessage);
    while(!readMessage(inBoundMessage)) {
      sleep(5);
    }
    PRINTLN(inBoundMessage->data);
    return;
  }
  if (readMessage(inBoundMessage)) {
    switch(inBoundMessage->type) {
      case REQUEST: {
        PRINTLN("Request");
        PRINTLN(inBoundMessage->data);
        JsonObject& json = jsonBuffer.parseObject(inBoundMessage->data);
        forward_list<RequestHandler*>::iterator iterator;
        for (iterator = requestHandlers->begin(); iterator != requestHandlers->end(); iterator++) {
          RequestHandler *currentHandler = *iterator;
          if (currentHandler->canHandle(json)) {
            StaticJsonBuffer<1000> jsonBuffer1;
            currentHandler->handle(json);
            PRINTLN(json["body"]);
            JsonObject& json1 = jsonBuffer1.createObject();
            json1["id"] = json["id"];
            json1["body"] = json["body"];
            json1.printTo((char*)dataBuffer, MAX_MESSAGE_SIZE);
            outBoundMessage->type = RESPONSE;
            outBoundMessage->data = dataBuffer;
            outBoundMessage->dataLength = strlen((char*)outBoundMessage->data);
            writeMessage(outBoundMessage);
          }
        }
        PRINTLN(json["destination"]);
        break;
      }
      default:
        PRINTLN("UNKNOWN");
        break;
    }
  }
}

template <class T>
void SimpClient<T>::onRequest(string destination, TRequestHandler handler) {
  requestHandlers->push_front(new RequestHandler(destination, handler));
}

template <class T>
SimpClient<T>::~SimpClient() {
  delete client;
  delete[] dataBuffer;
  delete[] lengthBuffer;
  delete inBoundMessage;
  delete outBoundMessage;
  forward_list<RequestHandler*>::iterator iterator;
  for (iterator = requestHandlers->begin(); iterator != requestHandlers->end(); iterator++) {
    delete *iterator;
  }
  delete requestHandlers;
}
