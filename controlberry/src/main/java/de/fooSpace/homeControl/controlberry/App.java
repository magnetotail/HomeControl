package de.fooSpace.homeControl.controlberry;

import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import de.fooSpace.homeControl.core.remoteInterfaces.IrControllable;
import de.fooSpace.remoteClasses.IrSender;

public class App {

	private static String hostname = "localhost";
//	private static String hostname = "192.168.0.17";
	private static IrSender sender;

	public static void main(String[] args) throws IOException {
		System.out.println("Hello World!");
//		Registry registry = LocateRegistry.createRegistry(0);
		Registry registry = LocateRegistry.getRegistry(null);
		sender = new IrSender();
		IrControllable stub = (IrControllable) UnicastRemoteObject.exportObject(sender, 1099);
//		registry.rebind("Foo", stub);
		System.out.println("Engine bound");
		ServerResolver resolver = new ServerResolver();
		System.out.println("Message from " + resolver.listenForPacketAndGetAddress());
	}
}
