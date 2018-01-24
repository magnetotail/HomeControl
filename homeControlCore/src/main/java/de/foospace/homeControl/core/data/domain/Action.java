package de.foospace.homeControl.core.data.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Action implements Serializable{

	private static final long serialVersionUID = 5580930169015427484L;

	@Id
	@GeneratedValue
	private Integer id;
	
	@Column(nullable=false)
	private RaspberryPi raspberryPi;
	
	@Column(nullable=false)
	private String description;
	
	
	
}
