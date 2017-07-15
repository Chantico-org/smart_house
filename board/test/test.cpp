#include <iostream>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <fcntl.h>
#include <sys/ioctl.h>
#include <netinet/in.h>
#include <netdb.h>
//#include <SimpClient.h>

//using namespace simp;
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
  size_t read(uint8_t *buf, size_t size) {
    return ::recv(sockfd, buf, size, 0);
  }

  int available() {
    fd_set fd;
    struct timeval tv;
    tv.tv_sec = 0;
    tv.tv_usec = 1;
    FD_ZERO(&fd);
    FD_SET(sockfd, &fd);
    if (select(sockfd + 1, &fd, NULL, NULL, &tv) < 0) {
      error("Socket select");
    }
    return FD_ISSET(sockfd, &fd);
  }

	size_t write(const uint8_t *buf, size_t size) {
		return ::write(sockfd, buf, size);
	}

  size_t write(uint8_t byte) {
		return write(&byte, 1);
	}

	~LinuxTcpClient() {
		cout << "Closing.." << endl;
		if (connected()) {
  		close(sockfd);
		}
	}
};

//typedef SimpClient<LinuxTcpClient> MyClient;

char *response = "OK";

uint8_t* handleCommand(uint8_t* inBoundBuffer) {
  cout << "In handle: " << inBoundBuffer << endl;
  return (uint8_t*) response;
}

uint8_t* sendTemparature() {
	return (uint8_t*) response;
}

int main() {
  char* t = "1";
  uint8_t* temp = (uint8_t*)t;
  if (temp[0] == 1) {
    cout << "ok" << endl;
  }
  cout << ((int)temp[0]) << endl;
//	MyClient* client = new MyClient(new LinuxTcpClient());
//	client->onRequest("/control/1", handleCommand);
//	client->onResourse("/temperature", 10000, sendTemparature);
//	while(true) {
//		client->loop();
//		sleep(1);
//	}
//	delete client;
	return 0;
}
