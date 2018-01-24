package de.foospace.homecontrol.controlberry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.foospace.homeControl.core.data.Message;

public class ServerResolver extends Thread {

	private static ServerResolver INSTANCE;

	static {
		INSTANCE = new ServerResolver();
	}

	private static volatile Logger logger;

	private DatagramPacket receivePacket;
	private SocketAddress serverAddress;

	private ServerResolver() {
		super("Server Resolver Thread");
		init();
		this.start();
	}

	public static ServerResolver getInstance() {
		return INSTANCE;
	}

	private void init() {
		logger = Logger.getLogger(ServerResolver.class.getName());
		logger.setLevel(Level.INFO);
		byte[] data = new byte[29];
		receivePacket = new DatagramPacket(data, data.length);
		Runtime.getRuntime().addShutdownHook(new GoodByeServerOnExitHook());
	}

	@Override
	public void run() {
		try(DatagramSocket listenSocket = new DatagramSocket(6543)) {
			
			logger.log(Level.INFO, "Starting listening for Server on " + listenSocket.getLocalSocketAddress());
			listenSocket.receive(receivePacket);
			String message = "";
			if (receivePacket.getData() != null) {
				message = new String(receivePacket.getData());
			}
			serverAddress = receivePacket.getSocketAddress();
			logger.log(Level.INFO, "Received " + message + " from " + receivePacket.getAddress());
		} catch (SocketException e) {
			logger.log(Level.SEVERE, "Error while creating listening socket", e);
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error while listening for Server", e);
		}
	}

	public Optional<SocketAddress> getServerSocketAddressOpt() {
		return Optional.ofNullable(serverAddress);
	}

	public Optional<InetAddress> getFoundInetAddress() {
		return Optional.ofNullable(receivePacket.getAddress());
	}

	private class GoodByeServerOnExitHook extends Thread {

		public GoodByeServerOnExitHook() {
			super("GoodByeServerHook");
		}

		@Override
		public void run() {
			logger.log(Level.INFO, "Sending Disconnect to Server");
			try (Socket socket = new Socket(receivePacket.getAddress(), 6544);
					PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
					BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))){
				writer.println(Message.DISCONNECT.message());
				System.out.println("Sent Exit to Server");
				while(socket.getInputStream().available() <= 0) {
					Thread.sleep(10);
					logger.log(Level.INFO, "Waiting for server response");
				}
				if(!reader.readLine().equals(Message.OK.message()))
					logger.log(Level.SEVERE, "Did not receive OK from server!!");
			} catch (IOException e) {
				logger.log(Level.SEVERE, "Error while trying to disconnect from Server", e);
			} catch (InterruptedException e) {
				logger.log(Level.SEVERE, "Interrupted while waiting for Server Response.");
			}
			logger.log(Level.INFO, "Disconnected from Server");
		}
	}
}
