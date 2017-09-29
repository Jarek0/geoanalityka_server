package pl.gisexpert.util;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;

@Startup
@Singleton
@TransactionManagement(TransactionManagementType.BEAN)
public class DBMigrator {

	@Inject
	private Logger log;

	@Resource(lookup = "java:/ankietaDS")
	private DataSource dataSource;

	@PostConstruct
	private void init() {
		if (dataSource == null) {
			log.error("No datasource found to execute the db migrations!");
			throw new EJBException("No datasource found to execute the db migrations!");
		}

		log.debug("Migrating database schema.");

		Flyway flyway = new Flyway();
		flyway.setDataSource(dataSource);

		// Flyway was introduced to the project when production was at v0.4
		flyway.setBaselineVersionAsString("0.4");
		flyway.setBaselineOnMigrate(true);
		//flyway.setValidateOnMigrate(false);
		//flyway.repair();
		flyway.migrate();
	}
}
