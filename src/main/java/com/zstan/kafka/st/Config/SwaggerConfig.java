package com.zstan.kafka.st.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI api(){
        return new OpenAPI()
                .servers(
                        List.of(
                                new Server()
                                        .url("http://localhost:7070")
                                        .description("Local server")
                        )
                )
                .info(
                        new Info()
                                .title("Our API")
                                .version("1.0")
                                .description("Documentation for Open API yourself")
                                .contact(new Contact()
                                        .url("https://github.com")
                                        .email("shergazy.07.31@gmail.com")
                                        .name("KafkaST")
                                )
                );
    }
}




