package de.foospace.homeControl.server;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import de.foospace.homeControl.core.data.domain.Room;
import de.foospace.homeControl.core.data.service.RoomRepository;

@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = RoomRepository.class)
@EntityScan(basePackageClasses = Room.class)
public class HomeControlApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomeControlApplication.class, args);
		PiManager.getInstance().getClass();
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
}
