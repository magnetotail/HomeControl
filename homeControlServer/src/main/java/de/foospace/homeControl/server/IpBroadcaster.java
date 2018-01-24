package de.foospace.homeControl.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

class IpBroadcaster extends Thread {
	
	Logger logger;

	private DatagramSocket socket;
	private DatagramPacket packet;
	private String message;

	public IpBroadcaster() throws SocketException, UnknownHostException {
		message = "Home Control Server Broadcast";
		packet = new DatagramPacket(message.getBytes(), message.getBytes().length, getBroadcastAddress(), 6543);
		socket = new DatagramSocket();
		logger = Logger.getLogger(IpBroadcaster.class.getName());
		logger.log(Level.INFO, "Broadcaster started");
		socket.setBroadcast(true);
	}

	private InetAddress getBroadcastAddress() throws UnknownHostException {
		byte[] localAddress = InetAddress.getLocalHost().getAddress();
		//TODO: generate address with netmask in case of smaller net
		localAddress[3] = (byte) 255;
		return InetAddress.getByAddress(localAddress);
	}

	@Override
	public void run() {
		while (true) {
			try {
				socket.send(packet);
				logger.log(Level.INFO, "Broadcast sent to Address: " + packet.getSocketAddress());
				Thread.sleep(5000);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}