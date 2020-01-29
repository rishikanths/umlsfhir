package edu.ilstu.umls.fhir.server;

import edu.ilstu.umls.fhir.db.HibernateConfig;
import edu.ilstu.umls.fhir.db.UMLSQuery;

public class MainTest {

    public static void main(String args[]) {

        HibernateConfig.buildSessionFactory();
        UMLSQuery q = new UMLSQuery();

        q.generateConceptMap("C0024530", 'a');
        HibernateConfig.closeSessionFactory();

    }

}