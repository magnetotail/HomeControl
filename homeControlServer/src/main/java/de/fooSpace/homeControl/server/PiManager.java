package de.fooSpace.homeControl.server;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PiManager {

	private IpBroadcaster ipBroadcaster;
	private Logger logger;
	
	public PiManager() {
		init();
	}


	private void init() {
		logger = Logger.getLogger(PiManager.class.getName());
		try {
			ipBroadcaster = new IpBroadcaster();
		} catch (SocketException | UnknownHostException e) {
			logger.log(Level.SEVERE, "Error while initiating broadcaster", e);
		}
		ipBroadcaster.start();
		logger.log(Level.INFO, "Broadcaster started");
	}
	
	
	

}
