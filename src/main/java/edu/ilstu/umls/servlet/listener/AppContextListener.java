package edu.ilstu.umls.servlet.listener;

import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.hibernate.cfg.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.ilstu.umls.fhir.db.HibernateConfig;

/**
 * @author Rishi Saripalle
 * 
 */
@WebListener
public class AppContextListener implements ServletContextListener {
	private static final Logger log = LoggerFactory.getLogger(HibernateConfig.class);

	public void contextInitialized(ServletContextEvent contextEvent) {
		try {
			HibernateConfig.buildSessionFactory(getHibernateProperties());
			log.info("Context initialized sucessfully");
		} catch (Exception e) {
			log.error("Context could not open sucessfully", e);
		}
	}

	public void contextDestroyed(ServletContextEvent contextEvent) {
		try {
			HibernateConfig.closeSessionFactory();
			log.info("Context closed sucessfully");
		} catch (Exception e) {
			log.error("Context could not close sucessfully", e);
		}
	}

	private Properties getHibernateProperties() {
		Properties p = new Properties();
		p.put(Environment.DRIVER, "com.mysql.jdbc.Driver");
		p.put(Environment.URL, "jdbc:mysql://10.110.10.130:3306/umls");
		p.put(Environment.USER, "root");
		p.put(Environment.PASS, "umls");
		p.put(Environment.DIALECT, "org.hibernate.dialect.MySQL5Dialect");
		p.put(Environment.SHOW_SQL, false);
		p.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
		p.put(Environment.GENERATE_STATISTICS, false);

		p.put(Environment.C3P0_ACQUIRE_INCREMENT, 1);
		p.put(Environment.C3P0_MIN_SIZE, 5);
		p.put(Environment.C3P0_MAX_SIZE, 10);
		p.put(Environment.C3P0_IDLE_TEST_PERIOD, 3000);
		p.put(Environment.C3P0_TIMEOUT, 0);
		return p;
	}

}
