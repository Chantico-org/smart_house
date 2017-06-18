#include <iostream>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>
#include <ArduinoJson.h>
// #include "sample.h"
#include <SimpClient.h>

using namespace std;

void error(char *msg) {
	cout << "Error here" << endl;
	perror(msg);
  exit(0);
}

class LinuxTcpClient: public simp::Client {
private:
	int sockfd;
	int portno = 7080;
	struct sockaddr_in serveraddr;
	struct hostent *server;
	char *hostname = "localhost";
	uint8_t __connected = 0;
public:
	void connect() {
		sockfd = socket(AF_INET, SOCK_STREAM, 0);
		if (sockfd < 0)
      error("ERROR opening socket");

		server = gethostbyname(hostname);
		if (server == NULL) {
			fprintf(stderr,"ERROR, no such host as %s\n", hostname);
			exit(0);
		}

		/* build the server's Internet address */
		bzero((char *) &serveraddr, sizeof(serveraddr));
		serveraddr.sin_family = AF_INET;
		bcopy((char *)server->h_addr,
			(char *)&serveraddr.sin_addr.s_addr, server->h_length);
		serveraddr.sin_port = htons(portno);

		if (::connect(sockfd, (struct sockaddr *)&serveraddr, sizeof(serveraddr)) < 0)
			error("ERROR connecting");

		__connected = 1;
	}
 	uint8_t connected() {return __connected;}
  uint8_t read() {return 0;}
  size_t read(uint8_t *buf, size_t size) {return 0;}

	size_t write(const uint8_t *buf, size_t size) {
		return ::write(sockfd, buf, size);
	}

  size_t write(uint8_t byte) {
		uint8_t* buffer = new uint8_t[1];
		buffer[0] = byte;
		write(buffer, 1);
		delete[] buffer;
	}

	~LinuxTcpClient() {
		cout << "Closing.." << endl;
		close(sockfd);
	}
};

int main() {
	uint8_t byte = 0;
	simp::SimpMessageType type = simp::infereFromByte(byte);
	std::cout << (simp::SimpMessageType::SUBSCRIBE == type) << '\n';
	std::cout << (simp::convertToByte(simp::SimpMessageType::SUBSCRIBE)) << '\n';
	simp::SimpClient* client = new simp::SimpClient(new LinuxTcpClient());
	for (size_t i = 0; i < 10; i++) {
		client->loop();
	}
	delete client;
	return 0;
}
