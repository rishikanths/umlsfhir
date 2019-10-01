package edu.ilstu.umls.fhir.log;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.LogManager;

public class LoggerUtil {

	public static void configLogger() {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		URL url = cl.getResource("log4j.properties");
		if (url != null) {
			try (InputStream in = url.openStream()) {
				LogManager.getLogManager().readConfiguration(in);
			} catch (IOException e) {
				e.printStackTrace(System.err);
			}
		}
	}

}
