// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.multitenancy;

import com.zaxxer.hikari.HikariDataSource;
import de.bytefish.multitenancy.conf.ApplicationConfiguration;
import de.bytefish.multitenancy.conf.TenantConfiguration;
import de.bytefish.multitenancy.datasource.TenantAwareRoutingSource;
import de.bytefish.multitenancy.model.Tenant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@EnableAsync
@EnableTransactionManagement
@ConfigurationPropertiesScan
public class SampleSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(SampleSpringApplication.class, args);
	}

	@Bean
	public DataSource dataSource(ApplicationConfiguration applicationConfiguration) {

		AbstractRoutingDataSource dataSource = new TenantAwareRoutingSource();

		Map<Object,Object> targetDataSources = new HashMap<>();

		for(var tenantConfiguration : applicationConfiguration.getTenants()) {
			var tenantDataSource = buildDataSource(tenantConfiguration);

			targetDataSources.put(tenantConfiguration.getName(), tenantDataSource);
		}

		dataSource.setTargetDataSources(targetDataSources);

		dataSource.afterPropertiesSet();

		return dataSource;
	}

	public DataSource buildDataSource(TenantConfiguration tenantConfiguration) {
		HikariDataSource dataSource = new HikariDataSource();

		dataSource.setInitializationFailTimeout(0);
		dataSource.setMaximumPoolSize(5);
		dataSource.setDataSourceClassName("org.postgresql.ds.PGSimpleDataSource");
		dataSource.addDataSourceProperty("url", tenantConfiguration.getDbUrl());
		dataSource.addDataSourceProperty("user", tenantConfiguration.getDbUser());
		dataSource.addDataSourceProperty("password", tenantConfiguration.getDbPassword());

		return dataSource;
	}

	public DataSource tenantB() {

		HikariDataSource dataSource = new HikariDataSource();

		dataSource.setInitializationFailTimeout(0);
		dataSource.setMaximumPoolSize(5);
		dataSource.setDataSourceClassName("org.postgresql.ds.PGSimpleDataSource");
		dataSource.addDataSourceProperty("url", "jdbc:postgresql://localhost:5432/sampledb");
		dataSource.addDataSourceProperty("user", "tenant_b");
		dataSource.addDataSourceProperty("password", "tenant_b");

		return dataSource;
	}
}