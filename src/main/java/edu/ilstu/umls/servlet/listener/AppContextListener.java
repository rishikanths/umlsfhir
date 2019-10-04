package edu.ilstu.umls.servlet.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.ilstu.umls.fhir.db.HibernateConfig;

/**
 * @author Rishi Saripalle
 * 
 */
@WebListener
public class AppContextListener implements ServletContextListener
{
	private static final Logger log = LogManager.getLogger(HibernateConfig.class);

	public void contextInitialized(ServletContextEvent contextEvent)
	{
		try{
			HibernateConfig.buildSessionFactory();
			log.info("Context initialized sucessfully");
		}catch(Exception e){
			log.error("Context could not open sucessfully",e);
		}
	}

	public void contextDestroyed(ServletContextEvent contextEvent)
	{
		try{
			HibernateConfig.closeSessionFactory();
			log.info("Context closed sucessfully");
		}catch(Exception e){
			log.error("Context could not close sucessfully",e);
		}
	}

}
