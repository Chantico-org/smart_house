var net = require('net');
const client = net.connect(8080, () => {
  //'connect' listener
  console.log('connected to server!');
  client.write('Hello world!'), () => {
  	console.log('callback');
  	client.write('next\n');
  };
});