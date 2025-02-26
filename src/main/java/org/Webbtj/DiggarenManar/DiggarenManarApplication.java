package org.Webbtj.DiggarenManar;

import org.Webbtj.DiggarenManar.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DiggarenManarApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiggarenManarApplication.class, args);
    }


    /**
     * Endast test för att se att det fungerar att lägga till en user i databasen.
     * @param userService
     * @return
     */
    @Bean
    CommandLineRunner run(UserService userService) {
        return args -> {
            userService.createUser("Testsson", "Testsson", "Testsson@email.com");
            System.out.println("✅ User added!");
        };
    }
}
