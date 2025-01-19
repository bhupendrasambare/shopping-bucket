/**
 * author @bhupendrasambare
 * Date   :20/01/25
 * Time   :1:32 am
 * Project:shopping-bucket
 **/
package com.example.application.utils;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI myOpenAPI() {
        Contact contact = new Contact();
        contact.setEmail("bhupendrasam1404@gmail.com");
        contact.setName("Bhupendra sambare");
        contact.setUrl("https://bhupendrasambare.github.io/profile/");

        Info info = new Info()
                .title("Shopping Sales API")
                .version("1.0")
                .contact(contact)
                .description("This API exposes endpoints to manage shopping sales.");

        return new OpenAPI().info(info);
    }
}
