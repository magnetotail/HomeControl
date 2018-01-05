package de.fooSpace.homeControl.core.remoteInterfaces;

import java.io.IOException;
import java.rmi.Remote;

public interface IrControllable extends Remote {
	
	public String sendIRCode(String code) throws IOException;
}
