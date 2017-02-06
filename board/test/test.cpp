#include <iostream>
#include <stdio.h>

using namespace std;

unsigned char* convertInt(int length) {
	unsigned char *result = new unsigned char[4];
	for(int i=3; i != -1; i--) {
		char byte = length & 255;
		result[i] = byte;
		length = length >> 8;
	}
	return result;
}

unsigned int convertFromBytes(const unsigned char* bytes) {
	unsigned int result = 0;
	for (int i = 0; i < 3; i++){
		result |= bytes[i];
		result = result << 8;
	}
	result |= bytes[3];
	return result;
}

int main() {
	unsigned char * bytes = convertInt(0xf);
	for (int i = 0; i < 4; i++) {
		printf("%d\n", bytes[i]);
	}	
	unsigned int result = convertFromBytes(bytes);
    cout << "Hello, world!" << endl;
    return 0; 
}
