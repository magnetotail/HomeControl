package de.foospace.homeControl.core.remoteInterfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

import de.foospace.homeControl.core.data.domain.Action;
import de.foospace.homeControl.core.data.domain.RaspberryPi;

public interface IConfigurator extends Remote {
	
	public static final String CONFIGURATOR_IDENTIFIER = "configurator";
	
	public void setSystemConfiguration(RaspberryPi pi)throws RemoteException;
	
	public RaspberryPi getSystemConfiguration()throws RemoteException;
	
	public ActionInterface activateAction(Action action)throws RemoteException;
	
	public ActionInterface getRemoteActionInterfaceForAction(Action action)throws RemoteException;

}
