package de.foospace.homeControl.core.data.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class Device implements Serializable{

	private static final long serialVersionUID = -7923059508106471172L;

	@Id 
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	private Manufacturer manufacturer;
	
	private Room room;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Manufacturer getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(Manufacturer manufacturer) {
		this.manufacturer = manufacturer;
	}

	public Room getRoom() {
		return room;
	}
	
	public void setRoom(Room room) {
		this.room = room;
	}
	
}
