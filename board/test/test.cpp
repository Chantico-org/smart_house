#include <iostream>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>
#include <SimpClient.h>

using namespace std;

void error(char *msg) {
	cout << "Error here" << endl;
	perror(msg);
  exit(0);
}

class LinuxTcpClient{
private:
	int sockfd = -1;
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
		return write(&byte, 1);
	}

	~LinuxTcpClient() {
		cout << "Closing.." << endl;
		close(sockfd);
	}
};

typedef simp::SimpClient<LinuxTcpClient> SimpClient;

int main() {
	SimpClient* client = new SimpClient(new LinuxTcpClient());
	for (size_t i = 0; i < 10; i++) {
		client->loop();
	}
	delete client;
	return 0;
}
