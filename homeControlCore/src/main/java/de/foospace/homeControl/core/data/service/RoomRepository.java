package de.foospace.homeControl.core.data.service;

import org.springframework.data.repository.CrudRepository;

import de.foospace.homeControl.core.data.domain.Room;

public interface RoomRepository extends CrudRepository<Room, Integer> {

}
