#ifndef SIMP_CLIENT
#define SIMP_CLIENT

#include <string.h>
#include <forward_list>
#include <ArduinoJson.h>

namespace simp {
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

  template<class T>
  class SimpClient {
  private:
    T* client;
    uint8_t* data_buffer;
    uint8_t* length_buffer;
    size_t pending_bytes = 0;
    void writeLength(size_t data_length);
    void write(const uint8_t *buf, size_t size);
    void write(uint8_t byte);
    static const int MESSAGE_LENGTH = 4;
  public:
    static const int MAX_MESSAGE_SIZE = 1024;
    SimpClient(T* client);
    void writeMessage(SimpMessage& messsage);
    void loop();
    ~SimpClient();
  };
} /* simp */

#include "SimpClient.cpp"

#endif
