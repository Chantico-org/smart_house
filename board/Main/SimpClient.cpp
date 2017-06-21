#include <SimpClient.h>

#ifdef DESKTOP

#include <iostream>
#include <stdio.h>

#define PRINTLN(line) std::cout<<line<<'\n'

#endif

simp::SimpMessageType simp::infereFromByte(uint8_t byte) {
  return (simp::SimpMessageType) byte;
}

uint8_t simp::convertToByte(simp::SimpMessageType type) {
  return (uint8_t) type;
}


template <class T>
simp::SimpClient<T>::SimpClient(T* client) {
  this->client = client;
  data_buffer = new uint8_t[MAX_MESSAGE_SIZE];
  length_buffer = new uint8_t[MESSAGE_LENGTH];
}

template<class T>
void simp::SimpClient<T>::write(const uint8_t* buf, size_t size) {
  pending_bytes = size;
  while(pending_bytes != 0) {
    size_t writed = client->write(buf, size);
    pending_bytes -= writed;
  }
}

template <class T>
void simp::SimpClient<T>::write(uint8_t byte) {
  pending_bytes = 1;
  while(pending_bytes != 0) {
    size_t writed = client->write(byte);
    pending_bytes -= writed;
  }
}

template <class T>
void simp::SimpClient<T>::writeLength(size_t data_length) {
  if (data_length > MAX_MESSAGE_SIZE) throw 234;
  for(int i = MESSAGE_LENGTH - 1; i != -1; i--) {
    uint8_t byte = data_length & 255;
    length_buffer[i] = byte;
    data_length = data_length >> 8;
  }
  write(length_buffer, MESSAGE_LENGTH);
}

template <class T>
void simp::SimpClient<T>::writeMessage(simp::SimpMessage& message) {
  size_t length = strlen((char*)message.data);
  writeLength(length + 1);
  write(convertToByte(message.type));
  write(message.data, length);
}

template <class T>
void simp::SimpClient<T>::loop() {
  if (!client->connected()) {
    simp::SimpMessage message;
    message.type = simp::SimpMessageType::REQUEST;
    StaticJsonBuffer<200> jsonBuffer;
    JsonObject& root = jsonBuffer.createObject();
    root["sensor"] = "gps";
    root["time"] = 1351824120;
    root.printTo((char*)data_buffer, SimpClient::MAX_MESSAGE_SIZE);
    message.data = data_buffer;
    client->connect();
    writeMessage(message);
  }
}

template <class T>
simp::SimpClient<T>::~SimpClient() {
  delete client;
  delete[] data_buffer;
  delete[] length_buffer;
}
