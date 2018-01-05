package de.fooSpace.homeControl.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import de.fooSpace.homeControl.core.data.domain.Room;
import de.fooSpace.homeControl.core.data.service.RoomRepository;

@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = RoomRepository.class)
@EntityScan(basePackageClasses = Room.class)
public class HomeControlApplication {
	private static Registry registry;

	public static void main(String[] args) {
		SpringApplication.run(HomeControlApplication.class, args);
		try {
			IpBroadcaster broadCaster = new IpBroadcaster();
			broadCaster.start();
		} catch (SocketException e1) {
			e1.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		// System.setProperty("java.security.policy",
		// "file:///home/selphie/dev/workspace/home_control/server.policy");
		try {
			registry = LocateRegistry.createRegistry(0);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	@Bean
	public CommandLineRunner demo(RoomRepository repository) {
		return (args) -> {
			Room entity = new Room();
			entity.setName("Küche");
			repository.save(entity);
			repository.findAll().forEach(System.out::println);
		};
	}

	private static class IpBroadcaster extends Thread {

		DatagramSocket socket;
		DatagramPacket packet;
		String message;

		public IpBroadcaster() throws SocketException, UnknownHostException {
			message = "Home Control Server Broadcast";
			packet = new DatagramPacket(message.getBytes(), message.getBytes().length,
					InetAddress.getByName("192.168.0.255"), 6543);
			socket = new DatagramSocket();
			System.out.println(InetAddress.getLocalHost());
			// socket.bind(new InetSocketAddress(getBroadcastAddrs().get(0), 6543));
			socket.setBroadcast(true);
			// packet.setAddress(InetAddress.getByName("192.168.0.255"));
		}

		public static List<InetAddress> getBroadcastAddrs() throws SocketException {
			Set<InetAddress> resultSet = new LinkedHashSet<>();
			Enumeration<NetworkInterface> nicList = NetworkInterface.getNetworkInterfaces();
			for (; nicList.hasMoreElements();) {
				NetworkInterface nic = nicList.nextElement();
				if (nic.isUp() && !nic.isLoopback()) {
					for (InterfaceAddress ia : nic.getInterfaceAddresses())
						resultSet.add(ia.getBroadcast());
				}
			}
			return Arrays.asList(resultSet.toArray(new InetAddress[0]));

		}

		@Override
		public void run() {
			while (true) {
				try {
					System.out.println("packet Address: " + packet.getSocketAddress() + " Socket Address: "
							+ socket.getLocalSocketAddress() + " Remote Address: " + socket.getRemoteSocketAddress());
					socket.send(packet);
					System.out.println("Broadcast sent, packet is " + message.getBytes().length + " Bytes long.");
					Thread.sleep(5000);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
