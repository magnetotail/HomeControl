package de.fooSpace.homeControl.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

class IpBroadcaster extends Thread {

	DatagramSocket socket;
	DatagramPacket packet;
	String message;

	public IpBroadcaster() throws SocketException, UnknownHostException {
		message = "Home Control Server Broadcast";
		packet = new DatagramPacket(message.getBytes(), message.getBytes().length, getBroadcastAddress(), 6543);
		socket = new DatagramSocket();
		System.out.println(InetAddress.getLocalHost());
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
				System.out.println("Broadcast sent to Address: " + packet.getSocketAddress());
				Thread.sleep(5000);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}