package edu.ilstu.umls.fhir.db;

import java.util.Properties;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateConfig {

	private static final Logger log = LoggerFactory.getLogger(HibernateConfig.class);

	private static SessionFactory sessionFactory = null;

	private static ServiceRegistry reg = null;

	public static void buildSessionFactory(Properties dbProperties) {
		if (sessionFactory == null) {
			log.info("Creating the session factory");
			Configuration cfg = null;
			try {
				cfg = new Configuration();
				cfg.setProperties(dbProperties);
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
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} catch (Throwable t) {
			log.error(t.getMessage(), t);
		}
		if (session == null) {
			log.error("Session is not instantiated");
		}
		return session;
	}

	public static void closeSession(Session session) {
		try {
			session.close();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} catch (Throwable t) {
			log.error(t.getMessage(), t);
		}
	}

	public static void closeSessionFactory() {
		try {
			sessionFactory.close();
			log.info("Successfully closed the session factory");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} catch (Throwable t) {
			log.error(t.getMessage(), t);
		}
	}

}
