package kr.co.fns.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
@ConfigurationPropertiesScan("kr.co.fns.app")
public class FantooApplication {

	public static void main(String[] args) {
		SpringApplication.run(FantooApplication.class, args);
	}

}
