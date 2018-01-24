package de.foospace.homeControl.core.remoteInterfaces;

import java.io.IOException;
import java.rmi.Remote;

public interface IrControllable extends Remote, ActionInterface {
	
	public String sendIRCode(String code) throws IOException;
}
