package edu.ilstu.umls.fhir.db;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

public class HibernateConfig {

	private static final Logger log = Logger.getLogger(HibernateConfig.class);

	private  static SessionFactory sessionFactory = null;

	private static ServiceRegistry reg = null;

	public static void buildSessionFactory() {
		if(sessionFactory ==null) {
			log.info("Creating the session factory");
			Configuration cfg = null;
			try {
				cfg = new Configuration();
				cfg.setProperties(getHibernateProperties());
				reg = new StandardServiceRegistryBuilder().applySettings(cfg.getProperties()).build();
				sessionFactory = cfg.buildSessionFactory(reg);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
	}

	public static Session getSession() {
		Session session = null;
		try {
			session = sessionFactory.openSession();
		}catch (Exception e) {
			log.error(e.getMessage(),e);
		}catch (Throwable t) {
			log.error(t.getMessage(),t);
		}
		if (session == null) {
			log.error("Session is not instantiated");
		}
		return session;
	}
	
	public static void closeSession(Session session) {
		try {
			session.close();
		}catch (Exception e) {
			log.error(e.getMessage(),e);
		}catch (Throwable t) {
			log.error(t.getMessage(),t);
		}
	}
	
	public static void closeSessionFactory() {
		try {
			sessionFactory.close();
			log.info("Successfully closed the session factory");
		}catch (Exception e) {
			log.error(e.getMessage(),e);
		}catch (Throwable t) {
			log.error(t.getMessage(),t);
		}
	}
	
	private static Properties getHibernateProperties() {
		Properties p = new Properties();
		p.put(Environment.DRIVER, "com.mysql.jdbc.Driver");
		p.put(Environment.URL, "jdbc:mysql://localhost:3306/umls2018");
		p.put(Environment.USER, "root");
		p.put(Environment.PASS, "rishi");
		p.put(Environment.DIALECT, "org.hibernate.dialect.MySQL5Dialect");
		p.put(Environment.SHOW_SQL, false);
		p.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
		p.put(Environment.GENERATE_STATISTICS, false);
		
		p.put(Environment.C3P0_ACQUIRE_INCREMENT, 1);
		p.put(Environment.C3P0_MIN_SIZE, 5);
		p.put(Environment.C3P0_MAX_SIZE, 10);
		p.put(Environment.C3P0_IDLE_TEST_PERIOD, 3000);
		
		return p;
	}

}
