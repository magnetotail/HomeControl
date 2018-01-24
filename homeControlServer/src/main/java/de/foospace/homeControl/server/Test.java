package de.foospace.homeControl.server;

import de.foospace.homeControl.core.data.domain.Room;
import de.foospace.homeControl.core.data.service.RoomRepository;

public class Test {
	
	private RoomRepository roomRepo;
	
	public void addRoom(Room room) {
		roomRepo.save(room);
	}

	
	
}
