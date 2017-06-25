#ifndef SIMP_COMMONS
#define SIMP_COMMONS

#include <functional>

namespace simp {
  typedef std::function<uint8_t*(uint8_t*)> TRequestHandler;

  enum SimpMessageType {
    SUBSCRIBE,
    MESSAGE,
    UN_SUBSCRIBE,
    REQUEST,
    RESPONSE
  };

  struct SimpMessage {
    SimpMessageType type;
    uint32_t dataLength;
    uint8_t* data;
  };
}

#endif /* !SIMP_COMMONS */
