package com.shopping.electronic.store;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
@OpenAPIDefinition(info = @Info(
        title = "Electronic Store",
        description = "Demo Service created using SpringBoot to buy different electronic gadgets online.",
        contact = @Contact(
                url = "https://www.linkedin.com/in/vansh-garg-5b316a179/",
                name = "Vansh Garg",
                email = "ag06vansh@gmail.com"
        )))
public class ElectronicStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElectronicStoreApplication.class, args);
    }

}
