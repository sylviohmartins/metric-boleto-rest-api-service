package io.sylviohmartins.metric.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfiguration {

    @Bean
    OpenAPI openAPI() {
        Contact contact = new Contact() //
                .name("Demo") //
                .email("sylviohmartins@gmail.com") //
                .url("https://github.com/sylviohmartins");

        Info info = new Info() //
                .title("Demo") //
                .version("1.0.0") //
                .contact(contact) //
                .description("Essa é uma descrição.");

        return new OpenAPI().info(info);
    }

}
