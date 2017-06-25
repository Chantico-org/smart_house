#ifndef SIMP_CLIENT
#define SIMP_CLIENT

#include <string.h>
#include <functional>
#include <forward_list>
#include <ArduinoJson.h>
#include "SimpCommons.h"
#include "RequestHandler.h"

using namespace std;

namespace simp {
  SimpMessageType infereFromByte(uint8_t byte);
  uint8_t convertToByte(SimpMessageType type);

  template<class T>
  class SimpClient {
  private:
    T* client;
    uint8_t* dataBuffer;
    uint8_t* lengthBuffer;
    size_t pendingBytes = 0;
    forward_list<RequestHandler*>* requestHandlers;
    SimpMessage *inBoundMessage, *outBoundMessage;
    void writeLength(uint32_t dataLength);
    void write(const uint8_t *buf, size_t size);
    void write(uint8_t byte);
    static const int MESSAGE_LENGTH = 4;
  public:
    void onRequest(string, TRequestHandler);
    static const int MAX_MESSAGE_SIZE = 1024;
    SimpClient(T* client);
    void writeMessage(SimpMessage*);
    bool readMessage(SimpMessage*);
    void loop();
    ~SimpClient();
  };
} /* simp */

#include "SimpClient.cpp"

#endif
