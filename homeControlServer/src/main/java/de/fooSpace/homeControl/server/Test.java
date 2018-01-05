package de.fooSpace.homeControl.server;

import de.fooSpace.homeControl.core.data.domain.Room;
import de.fooSpace.homeControl.core.data.service.RoomRepository;

public class Test {
	
	private RoomRepository roomRepo;
	
	public void addRoom(Room room) {
		roomRepo.save(room);
	}

	
	
}
