#ifndef SIMP_CLIENT
#define SIMP_CLIENT

#include <string>
#include <forward_list>

// TEMP
#include <iostream>
#include <stdio.h>
// TEMP

namespace simp {
  class Client {
  public:
    virtual void connect()=0;
    virtual uint8_t connected()=0;
    virtual uint8_t read()=0;
    virtual size_t read(uint8_t *buf, size_t size)=0;
    virtual size_t write(const uint8_t *buf, size_t size)=0;
    virtual size_t write(uint8_t)=0;
    virtual ~Client(){}
  };

  enum SimpMessageType {
    SUBSCRIBE,
    MESSAGE,
    UN_SUBSCRIBE,
    REQUEST,
    RESPONSE
  };

  struct SimpMessage {
    SimpMessageType type;
    uint8_t* data;
  };

  SimpMessageType infereFromByte(uint8_t byte);
  uint8_t convertToByte(SimpMessageType type);

  class SimpClient {
  private:
    simp::Client* client;
    uint8_t* data_buffer;
    uint8_t* length_buffer;
    size_t pending_bytes = 0;
    void writeLength(size_t data_length);
    void write(const uint8_t *buf, size_t size);
    void write(uint8_t byte);
    static const int MESSAGE_LENGTH = 4;
  public:
    static const int MAX_MESSAGE_SIZE = 1024;
    SimpClient(simp::Client* client);
    void writeMessage(SimpMessage& messsage);
    void loop();
    ~SimpClient();
  };
} /* simp */

// TODO move to cpp file

inline simp::SimpMessageType simp::infereFromByte(uint8_t byte) {
  return (simp::SimpMessageType) byte;
}

inline uint8_t simp::convertToByte(simp::SimpMessageType type) {
  return (uint8_t) type;
}

simp::SimpClient::SimpClient(simp::Client* client) {
  this->client = client;
  data_buffer = new uint8_t[MAX_MESSAGE_SIZE];
  length_buffer = new uint8_t[MESSAGE_LENGTH];
}

// TODO implement blocking API
void simp::SimpClient::write(const uint8_t* buf, size_t size) {
  if (pending_bytes == 0) {
    pending_bytes = size;
    client->write(buf, size);
    pending_bytes = 0;
  } else {
    std::cout << "Pending.." << '\n';
  }
}

// TODO implement blocking API
void simp::SimpClient::write(uint8_t byte) {
  if (pending_bytes == 0) {
    pending_bytes = 1;
    client->write(byte);
    pending_bytes = 0;
  } else {
    std::cout << "Pending.." << '\n';
  }
}

void simp::SimpClient::writeLength(size_t data_length) {
  if (data_length > MAX_MESSAGE_SIZE) throw 234;
  for(int i = MESSAGE_LENGTH - 1; i != -1; i--) {
    uint8_t byte = data_length & 255;
    length_buffer[i] = byte;
    data_length = data_length >> 8;
  }
  write(length_buffer, MESSAGE_LENGTH);
}

void simp::SimpClient::writeMessage(simp::SimpMessage& message) {
  size_t length = strlen((char*)message.data);
  writeLength(length + 1);
  write(convertToByte(message.type));
  write(message.data, length);
}
void simp::SimpClient::loop() {
  if (!client->connected()) {
    client->connect();
  }
  simp::SimpMessage message;
  message.type = simp::SimpMessageType::REQUEST;
  char* data = "Hello worlds";
  message.data = (uint8_t*)data;
  writeMessage(message);
  // delete[] data;
}

simp::SimpClient::~SimpClient() {
  delete client;
  delete[] data_buffer;
  delete[] length_buffer;
}

#endif
