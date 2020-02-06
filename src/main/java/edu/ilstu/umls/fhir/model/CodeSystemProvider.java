/*
 * @author rsaripa
 *
 */
package edu.ilstu.umls.fhir.model;

import org.hl7.fhir.r4.model.CodeSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import edu.ilstu.umls.fhir.db.UMLSQuery;

public class CodeSystemProvider implements IResourceProvider {

    private static final Logger log = LoggerFactory.getLogger("CodeProvder");

    @Override
    public Class<CodeSystem> getResourceType() {
        return CodeSystem.class;
    }

    @Search
    public CodeSystem searchCUI(@RequiredParam(name = "search") StringParam string) {
        String searchString = string.getValue();
        log.info("Preparing CodeSystem  for search string - " + searchString);
        try {
            return new UMLSQuery().searchByString(searchString, UMLSQuery.Queries.SEARCH_QUERY.query);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
        }
        return null;
    }

}