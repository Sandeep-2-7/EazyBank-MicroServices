package com.eazybytes.accounts;

import com.eazybytes.accounts.dto.AccountsContactInfoDto;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@OpenAPIDefinition(info = @Info(title = "Accounts MicroService REST API documentation",
                                description = "EazyBank Accounts MicroService REST API Documentation",
                                version = "v1",
                                contact = @Contact(
                                        name = "Sandeep",
                                        email = "sandeep27102010@gmail.com"
                                ),
                                license = @License(name = "Apache 2.0",  url = "www.google.com")
                                ),
        externalDocs = @ExternalDocumentation(
                description =  "EazyBank Accounts microservice REST API Documentation",
                url = "https://www.eazybytes.com/swagger-ui.html"
        )
                )
@EnableConfigurationProperties(value = {AccountsContactInfoDto.class})
public class    AccountsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountsApplication.class, args);
	}

}
