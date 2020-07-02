package edu.ilstu.umls.fhir.utils;

import java.util.Properties;
import org.hibernate.cfg.Environment;

public class Utils {

    public static final String BASE_URL = "http://umls.it.ilstu.edu";

    public static final String UMLS_URL = "https://www.nlm.nih.gov/research/umls/";

    public static final String UMLS_VER = "2019AB";

    public static Properties getHibernateProperties() {
        Properties p = new Properties();
        p.put(Environment.DRIVER, "com.mysql.jdbc.Driver");
        p.put(Environment.URL, "jdbc:mysql://10.110.10.130:3306/umls?serverTimezone=America/Chicago");
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