package de.foospace.homeControl.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.foospace.homeControl.core.data.Message;


public class PiAcceptor extends Thread {

	private static PiAcceptor INSTANCE;
	static {
		INSTANCE = new PiAcceptor();
	}

//	private List<PiInitializer> piInitializers;
	private Map<InetAddress, PiInitializer> piInitializers;
	
	private Logger logger;

	private boolean shouldRun;

	private PiAcceptor() {
		super("PiAcceptor Thread");
		logger = Logger.getLogger(PiAcceptor.class.getName());
		piInitializers = new HashMap<>();
	}
	
	public static PiAcceptor getInstance() {
		return INSTANCE;
	}

	@Override
	public void run() {
		shouldRun = true;
		try(ServerSocket acceptingSocket = new ServerSocket(6544)) {
			logger.log(Level.INFO, "Listening for Raspberry Pis on " + acceptingSocket.getLocalSocketAddress());
			while (shouldRun) {
				Socket requestingPiSocket = acceptingSocket.accept();
				logger.log(Level.INFO, "Connection from " + requestingPiSocket.getInetAddress());
				Thread.sleep(100);
				if(requestingPiSocket.getInputStream().available() > 0) {
					BufferedReader reader = new BufferedReader(new InputStreamReader(requestingPiSocket.getInputStream()));
					String message = reader.readLine();
					logger.log(Level.INFO, "Got Message: " + message + " from " + requestingPiSocket.getInetAddress());
					if(message.equals(Message.DISCONNECT.message())) {
						piInitializers.get(requestingPiSocket.getInetAddress()).dispose();
						PrintWriter writer = new PrintWriter(requestingPiSocket.getOutputStream(), true);
						piInitializers.remove(requestingPiSocket.getInetAddress());
						writer.println(Message.OK.message());
					}else if(message.equals(Message.CONNECT.message())) {
						logger.log(Level.INFO, requestingPiSocket.getInetAddress().getHostAddress() + " is requesting access");
						PiInitializer initializer = new PiInitializer(requestingPiSocket);
						piInitializers.put(requestingPiSocket.getInetAddress(), initializer);
						initializer.start();
					}
					else {
						logger.log(Level.SEVERE, "Unknown Option: " + message);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
