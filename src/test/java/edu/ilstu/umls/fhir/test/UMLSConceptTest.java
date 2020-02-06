package edu.ilstu.umls.fhir.test;

import static org.junit.Assert.assertNotNull;

import java.util.Properties;

import org.hibernate.cfg.Environment;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.ilstu.umls.fhir.db.HibernateConfig;
import edu.ilstu.umls.fhir.db.UMLSQuery;
import edu.ilstu.umls.fhir.model.UMLSConceptMap;

public class UMLSConceptTest {

    Properties prop = null;

    @Before
    public void init() {
        prop = getHibernateProperties();
        HibernateConfig.buildSessionFactory(prop);
    }

    @Test
    public void testConceptMap() {

        UMLSQuery q = new UMLSQuery();
        UMLSConceptMap map = UMLSConceptMap.getDefaultUMLSConceptMap();
        q.generateCUIAtomsConcetpMap(map, "C0024530", UMLSQuery.Queries.MAPPING_QUERY.query);

        assertNotNull(map);

    }

    @After
    public void cleanUp() {
        HibernateConfig.closeSessionFactory();
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