package de.foospace.homeControl.server;

import java.io.IOException;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.foospace.homeControl.core.data.domain.RaspberryPi;
import de.foospace.homeControl.core.remoteInterfaces.IConfigurator;

public class PiInitializer extends Thread {
	
	private Logger logger;
	private RaspberryPi pi;
	private Socket piSocket;

	private Map<String, Remote> remoteInterfaces;
	
	public PiInitializer(Socket piSocket) {
		this.piSocket = piSocket;
		logger = Logger.getLogger(PiInitializer.class.getName());
		pi = new RaspberryPi();
		pi.setAddress(piSocket.getInetAddress());
		remoteInterfaces = new HashMap<>();
	}
	
	@Override
	public void run() {
		try {
			Registry remoteRegistry = LocateRegistry.getRegistry(piSocket.getInetAddress().getHostAddress());
			IConfigurator configurator = (IConfigurator) remoteRegistry.lookup(IConfigurator.CONFIGURATOR_IDENTIFIER);
			remoteInterfaces.put(IConfigurator.CONFIGURATOR_IDENTIFIER, configurator);
			PiManager.getInstance().addRaspberryPi(pi);
			configurator.setSystemConfiguration(pi);
			logger.log(Level.INFO, "Configuration set on " + pi);
			piSocket.close();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error while initiating " + piSocket.getInetAddress().getHostAddress(), e);
		} catch (NotBoundException e) {
			logger.log(Level.SEVERE, "Configurator Object not bound on Address: " + piSocket.getInetAddress().getHostAddress(), e);
		}
		
	}
	
	public void dispose() {
		for(Entry<String,Remote> remoteInterfaceEntry : remoteInterfaces.entrySet()) {
			remoteInterfaceEntry.setValue(null);
		}
		remoteInterfaces.clear();
		PiManager.getInstance().removeRaspberryPi(pi);
	}
	

}
