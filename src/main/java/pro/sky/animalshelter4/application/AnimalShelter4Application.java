package pro.sky.animalshelter4.application;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@OpenAPIDefinition
public class AnimalShelter4Application {

    public static void main(String[] args) {
        SpringApplication.run(AnimalShelter4Application.class, args);
    }

}
