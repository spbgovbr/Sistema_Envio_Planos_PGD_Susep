package br.gov.economia.apipgdsusep.datasource;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "entityManagerFactory",
basePackages = {"br.gov.economia.apipgdsusep.repository"})
public class ApiDatasourceProperties {
	
	@Autowired Environment env;

	@Primary
	@Bean(name = "dataSourceApi")
	@ConfigurationProperties(prefix = "api.spring.datasource")
	public DataSource dataSource() {
		
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
	    dataSource.setUrl(env.getProperty("api.spring.datasource.url"));
	    dataSource.setUsername(env.getProperty("api.spring.datasource.username"));
	    dataSource.setPassword(env.getProperty("api.spring.datasource.password"));

	    return dataSource;
	}

	@Primary
	@Bean(name = "entityManagerFactory")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(
			EntityManagerFactoryBuilder builder, @Qualifier("dataSourceApi") DataSource dataSource) {
		Map<String, Object> properties = new HashMap<String, Object>();
	    properties.put("hibernate.hbm2ddl.auto", "none");
		return builder
				.dataSource(dataSource)
				.packages("br.gov.economia.apipgdsusep.entity")
				.persistenceUnit("apiDS")
				.properties(properties)
				.build();
	}

	@Primary
	@Bean(name = "transactionManager")
	public PlatformTransactionManager transactionManager(
			@Qualifier("entityManagerFactory") EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);
	}
	
}