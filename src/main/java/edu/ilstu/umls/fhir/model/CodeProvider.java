/*
 * @author rsaripa
 *
 */
package edu.ilstu.umls.fhir.model;

import java.util.List;

import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.IdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import edu.ilstu.umls.fhir.db.UMLSQuery;

public class CodeProvider implements IResourceProvider {

    private static final Logger log = LoggerFactory.getLogger("CodeProvder");

    @Override
    public Class<CodeSystem> getResourceType() {
        return CodeSystem.class;
    }

    @Search
    public CodeSystem searchCUI(@RequiredParam(name = "search") StringParam string) {
        String searchString = string.getValue();
        log.info("Preparing Coding list for search string - " + searchString);
        try {
            return new UMLSQuery().searchByString(searchString);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
        }
        return null;
    }

}