package edu.ilstu.umls.fhir.test;

import static org.junit.Assert.assertNotNull;

import java.util.Properties;

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
import edu.ilstu.umls.fhir.model.UMLSConceptMap;
import edu.ilstu.umls.fhir.model.UMLSRelationTypeCodeValueSetProvider;
import edu.ilstu.umls.fhir.utils.Utils;

public class UMLSConceptTest {

    Properties prop = null;
    FhirContext ctx = null;

    @Before
    public void init() {
        prop = Utils.getHibernateProperties();
        HibernateConfig.buildSessionFactory(prop);
        ctx = FhirContext.forR4();
    }

    @Test
    public void testConceptMap() {

        UMLSQuery q = new UMLSQuery();
        UMLSConceptMap map = UMLSConceptMap.getDefaultUMLSConceptMap();
        q.generateCUIAtomsConcetpMap(map, "C0004238", UMLSQuery.Queries.MAPPING_QUERY.query);
        q.generateCUIRelationsConcetpMap(map, "C0004238", UMLSQuery.Queries.HIERARCHY_QUERY.query, true);
        q.generateCUIRelationsConcetpMap(map, "C0004238", UMLSQuery.Queries.RELATION_QUERY.query, false);
        System.out.println(ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(map));
        assertNotNull(map);

    }

    @Test
    public void testValueSet() {

        UMLSRelationTypeCodeValueSetProvider umlsvs = new UMLSRelationTypeCodeValueSetProvider();
        ValueSet vs = umlsvs.getUMLSRelationTypeCode(new IdType("umls-rel"));
        System.out.println(ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(vs));
        assertNotNull(vs);

    }

    @Test
    public void testStructureDefinition() {

        ExtensionsProvider umlsvs = new ExtensionsProvider();
        StructureDefinition vs = umlsvs.mappingLabel(new IdType("conceptmap-code-semantic-type"));
        System.out.println(ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(vs));
        assertNotNull(vs);

    }

    @After
    public void cleanUp() {
        HibernateConfig.closeSessionFactory();
    }

}