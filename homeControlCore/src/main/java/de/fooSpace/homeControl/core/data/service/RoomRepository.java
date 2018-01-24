package de.fooSpace.homeControl.core.data.service;

import org.springframework.data.repository.CrudRepository;

import de.fooSpace.homeControl.core.data.domain.Room;

public interface RoomRepository extends CrudRepository<Room, Integer> {

}
