package de.fooSpace.homeControl.controlberry;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class ServerResolver {
	
	private DatagramSocket listenSocket;
	private DatagramPacket receivePacket;
	
	public ServerResolver () throws SocketException, UnknownHostException {
		init();
	}

	private void init() throws SocketException, UnknownHostException {
		byte[] data = new byte[29];
		listenSocket = new DatagramSocket(6543);
		System.out.println("listening on " + listenSocket.getLocalSocketAddress());
		receivePacket = new DatagramPacket(data, data.length);
	}

	public InetAddress listenForPacketAndGetAddress() throws IOException {
		listenSocket.receive(receivePacket);
		return receivePacket.getAddress();
	}
}
