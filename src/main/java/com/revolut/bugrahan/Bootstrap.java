package com.revolut.bugrahan;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@OpenAPIDefinition(
        info = @Info(
                title = "Swagger Server",
                version = "1.0.0",
                description = "This is a sample server to transfer money between accounts for `Revolut Backend Test.`",
                termsOfService = "http://swagger.io/terms/",
                contact = @Contact(email = "bugrahanmemis@gmail.com"),
                license = @License(
                        name = "Apache 2.0",
                        url = "http://www.apache.org/licenses/LICENSE-2.0.html"
                )
        )
)
public class Bootstrap {
}
