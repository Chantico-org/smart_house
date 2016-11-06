var net = require('net');

function dataHandler(client) {
	return (data) => {
		console.log(data.toString());
	};
}

function handleClient(client) {
  console.log('client connected');
  client.on('data', dataHandler(client));
  client.on('end', () => {
  	console.log('client disconected');
  });
}

const server = net.createServer();

server.on('connection', handleClient);

server.on('error', (err) => {
  throw err;
});

server.listen(8080, (err) => {
	if (err) {
		console.err(err);
	}
	console.log('Listening on port 8080');
});