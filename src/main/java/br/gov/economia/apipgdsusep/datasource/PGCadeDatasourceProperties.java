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
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "pgdEntityManagerFactory",
basePackages = {"br.gov.economia.apipgdsusep.repository"})
public class PGCadeDatasourceProperties {
	
	@Autowired Environment env;

	@Bean(name = "dataSourcePGD")
	@ConfigurationProperties(prefix = "pgd.spring.datasource")
	public DataSource dataSource() {
		
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
	    dataSource.setUrl(env.getProperty("pgd.spring.datasource.url"));
	    dataSource.setUsername(env.getProperty("pgd.spring.datasource.username"));
	    dataSource.setPassword(env.getProperty("pgd.spring.datasource.password"));

	    return dataSource;
	}

	@Bean(name = "pgdEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(
			EntityManagerFactoryBuilder builder, @Qualifier("dataSourcePGD") DataSource dataSource) {
		Map<String, Object> properties = new HashMap<String, Object>();
	    properties.put("hibernate.hbm2ddl.auto", "none");
		return builder
				.dataSource(dataSource)
				.packages("br.gov.economia.apipgdsusep.entity")
				.persistenceUnit("pgd")
				.properties(properties)
				.build();
	}

	@Bean(name = "pgdTransactionManager")
	public PlatformTransactionManager transactionManager(
			@Qualifier("pgdEntityManagerFactory") EntityManagerFactory pgdEntityManagerFactory) {
		return new JpaTransactionManager(pgdEntityManagerFactory);
	}
	
}