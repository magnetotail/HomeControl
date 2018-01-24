package de.foospace.homecontrol.controlberry;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.foospace.homeControl.core.data.Message;
import de.foospace.homeControl.core.data.domain.RaspberryPi;
import de.foospace.homeControl.core.remoteInterfaces.IConfigurator;
import de.foospace.homecontrol.controlberry.remoteClasses.Configurator;

public class App {


	private RaspberryPi configuration;
	private Configurator remoteConfigurator;

	public static void main(String[] args) {
		new App().start();
	}

	private void start() {
		System.out.println("Hello World!");
		ServerResolver resolver = ServerResolver.getInstance();
		Socket connector = null;
		try {
			resolver.join();
			Registry registry = LocateRegistry.createRegistry(1099);
			remoteConfigurator = new Configurator(this);
			IConfigurator stub = (IConfigurator) UnicastRemoteObject.exportObject(remoteConfigurator, 1099);
			registry.rebind(IConfigurator.CONFIGURATOR_IDENTIFIER, stub);
			// registry.rebind("Foo", stub);
			System.out.println("Configurator bound");

			if(resolver.getFoundInetAddress().isPresent()) {
				System.out.println("Connecting to: " + resolver.getFoundInetAddress().get());
				connector = new Socket(resolver.getFoundInetAddress().get(), 6544);
				PrintWriter writer = new PrintWriter(connector.getOutputStream(),true);
				writer.println(Message.CONNECT.message());
				Thread.sleep(50);
				System.out.println("Connected to Server");
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				connector.close();
				System.out.println("Socket closed");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public RaspberryPi getConfiguration() {
		return configuration;
	}

	public void setConfiguration(RaspberryPi configuration) {
		this.configuration = configuration;
		System.out.println("Configuration set");
	}
}
