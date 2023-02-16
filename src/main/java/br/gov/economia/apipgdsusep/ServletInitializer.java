package br.gov.economia.apipgdsusep;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(basePackages = {"br.gov.economia.apipgdsusep"})
@EnableJpaRepositories("br.gov.economia.apipgdsusep.repository")
@EntityScan("br.gov.economia.apipgdsusep.entity")
@EnableScheduling
public class ServletInitializer extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(ServletInitializer.class, args);
	}

	@Override // for WildFly
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return super.configure(builder);
	}
	
}
