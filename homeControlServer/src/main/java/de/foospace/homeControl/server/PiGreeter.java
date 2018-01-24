package de.foospace.homeControl.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

import de.foospace.homeControl.core.data.domain.RaspberryPi;
import de.foospace.homeControl.core.data.domain.Room;

public class PiGreeter {

	private ServerSocket socket;
	private List<RaspberryPi> uninitializedPis;
	

	public PiGreeter() throws UnknownHostException, IOException {
		init();
	}

	private void init() throws UnknownHostException, IOException {
		socket = new ServerSocket(6544, 10, InetAddress.getLocalHost());
	}
	
	

}
