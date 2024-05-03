package com.example.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().addSecurityItem(new SecurityRequirement().
                        addList("Bearer Authentication"))
                .components(new Components().addSecuritySchemes
                        ("Bearer Authentication", createAPIKeyScheme()))
                .info(new Info().title("USER MANAGEMENT SERVICE REST API")
                        .description("""
                                Java practical test assignment
                                                                
                                The task has two parts:
                                1. Using the resources listed below learn what is RESTful API and what are the best practices to implement it\s
                                2. According to the requirements implement the RESTful API based on the web Spring Boot application: controller, responsible for the resource named Users.\s
                                                                                            
                                Requirements:
                                
                                1. It has the following fields:
                                    1.1. Email (required). Add validation against email pattern
                                    1.2. First name (required)
                                    1.3. Last name (required)
                                    1.4. Birth date (required). Value must be earlier than current date
                                    1.5. Address (optional)
                                    1.6. Phone number (optional)
                                2. It has the following functionality:
                                    2.1. Create user. It allows to register users who are more than [18] years old. The value [18] should be taken from properties file.
                                    2.2. Update one/some user fields
                                    2.3. Update all user fields
                                    2.4. Delete user
                                    2.5. Search for users by birth date range. Add the validation which checks that “From” is less than “To”.  Should return a list of objects
                                3. Code is covered by unit tests using Spring
                                4. Code has error handling for REST
                                5. API responses are in JSON format
                                6. Use of database is not necessary. The data persistence layer is not required.
                                7. Any version of Spring Boot. Java version of your choice
                                8. You can use Spring Initializer utility to create the project: Spring Initializr                     
                                """)
                        .version("1.0"))
                ;
    }
}

