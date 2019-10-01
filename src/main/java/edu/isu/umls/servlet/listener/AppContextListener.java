package edu.isu.umls.servlet.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.log4j.Logger;

import edu.ilstu.umls.fhir.db.HibernateConfig;

/**
 * @author Rishi Saripalle
 * 
 */
@WebListener
public class AppContextListener implements ServletContextListener
{
	private static final Logger log = Logger.getLogger(HibernateConfig.class);

	public void contextInitialized(ServletContextEvent contextEvent)
	{
		try{
			edu.ilstu.umls.fhir.log.LoggerUtil.configLogger();
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
