package de.foospace.homeControl.server;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.foospace.homeControl.core.data.domain.Action;
import de.foospace.homeControl.core.data.domain.RaspberryPi;

public class PiManager {
	
	private static PiManager INSTANCE;
	
	static {
		INSTANCE = new PiManager();
	}

	private IpBroadcaster ipBroadcaster;
	private PiAcceptor piAcceptor;
	private Logger logger;
	
	private Map<RaspberryPi, List<Action>> actions;
	
	private PiManager() {
		init();
	}

	public static PiManager getInstance() {
		return INSTANCE;
	}

	private void init() {
		logger = Logger.getLogger(PiManager.class.getName());
		try {
			ipBroadcaster = new IpBroadcaster();
		} catch (SocketException | UnknownHostException e) {
			logger.log(Level.SEVERE, "Error while initiating broadcaster", e);
			return;
		}
		actions = new HashMap<>();
		PiAcceptor.getInstance().start();
		logger.log(Level.INFO, "Listening for Raspberry Pis");
		logger.log(Level.INFO, "Broadcaster started");
		ipBroadcaster.start();
	}
	
	public synchronized void addRaspberryPi(RaspberryPi pi) {
		actions.put(pi, new ArrayList<>());
		logger.log(Level.INFO, "New Raspberry Pi registered: " + pi);
	}
	
	public synchronized void removeRaspberryPi(RaspberryPi pi) {
		actions.remove(pi);
		logger.log(Level.INFO, "Removed Raspberry Pi: " + pi);
	}
	

}
