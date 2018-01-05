package de.fooSpace.homeControl.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

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
			packet = new DatagramPacket(message.getBytes(), message.getBytes().length, getBroadcastAddress(), 6543);
			socket = new DatagramSocket();
			System.out.println(InetAddress.getLocalHost());
			socket.setBroadcast(true);
		}

		private InetAddress getBroadcastAddress() throws UnknownHostException {
			byte[] localAddress = InetAddress.getLocalHost().getAddress();
			//TODO: generate address with netmask in case of smaller net
			localAddress[3] = (byte) 255;
			return InetAddress.getByAddress(localAddress);
		}

		@Override
		public void run() {
			while (true) {
				try {
					socket.send(packet);
					System.out.println("Broadcast sent to Address: " + packet.getSocketAddress());
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
