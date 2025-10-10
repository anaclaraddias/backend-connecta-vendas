package br.unibh.sdm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@OpenAPIDefinition(info = @Info(title = "Connecta Vendas", version = "1.0", description = "API de vendas da Connecta Vendas", 
	license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0"),
	contact = @Contact(name = "Suporte da Empresa Connecta Vendas", email = "suporte@connectavendas.com"), 
	termsOfService = "http://connectavendas.com/termos_uso_api"))
@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}