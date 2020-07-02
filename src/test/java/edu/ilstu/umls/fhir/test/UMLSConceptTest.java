package edu.ilstu.umls.fhir.test;

import static org.junit.Assert.assertNotNull;

import java.util.Properties;

import org.hibernate.cfg.Environment;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import edu.ilstu.umls.fhir.db.HibernateConfig;
import edu.ilstu.umls.fhir.db.UMLSQuery;
import edu.ilstu.umls.fhir.model.ExtensionsProvider;
import edu.ilstu.umls.fhir.model.ExtensionsProvider;
import edu.ilstu.umls.fhir.model.UMLSConceptMap;
import edu.ilstu.umls.fhir.model.UMLSRelationTypeCodeValueSetProvider;

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
        q.generateCUIAtomsConcetpMap(map, "C0004238", UMLSQuery.Queries.MAPPING_QUERY.query);
        q.generateCUIRelationsConcetpMap(map, "C0004238", UMLSQuery.Queries.HIERARCHY_QUERY.query, true);
        q.generateCUIRelationsConcetpMap(map, "C0004238", UMLSQuery.Queries.RELATION_QUERY.query, false);
        FhirContext ctx = FhirContext.forR4();
        System.out.println(ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(map));

        assertNotNull(map);

    }

    @Test
    public void testValueSet() {

        UMLSRelationTypeCodeValueSetProvider umlsvs = new UMLSRelationTypeCodeValueSetProvider();
        ValueSet vs = umlsvs.getUMLSRelationTypeCode(new IdType("umls-rel"));
        FhirContext ctx = FhirContext.forR4();
        System.out.println(ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(vs));

        assertNotNull(vs);

    }

    @Test
    public void testSD() {

        ExtensionsProvider umlsvs = new ExtensionsProvider();
        StructureDefinition vs = umlsvs.mappingLabel(new IdType("conceptmap-code-semantic-type"));
        FhirContext ctx = FhirContext.forR4();
        System.out.println(ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(vs));

        assertNotNull(vs);

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