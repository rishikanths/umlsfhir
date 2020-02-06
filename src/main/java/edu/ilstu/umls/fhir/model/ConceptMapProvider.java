/*
 * @author rsaripa
 *
 */
package edu.ilstu.umls.fhir.model;

import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.IdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
/*import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.param.StringParam;*/
import ca.uhn.fhir.rest.server.IResourceProvider;
import edu.ilstu.umls.fhir.db.UMLSQuery;

public class ConceptMapProvider implements IResourceProvider {

    private static final Logger log = LoggerFactory.getLogger("ConceptMapProvider");

    @Override
    public Class<ConceptMap> getResourceType() {
        return ConceptMap.class;
    }

    @Read
    public ConceptMap getCUIMappings(@IdParam IdType id) {
        String code = id.getIdPart();
        log.info("Preparing ConceptMap for CUI - " + code);
        try {
            UMLSConceptMap model = UMLSConceptMap.getDefaultUMLSConceptMap();
            UMLSQuery query = new UMLSQuery();
            query.generateCUIAtomsConcetpMap(model, code, UMLSQuery.Queries.MAPPING_QUERY.query);
            query.generateCUIRelationsConcetpMap(model, code, UMLSQuery.Queries.HIERARCHY_QUERY.query, true);
            query.generateCUIRelationsConcetpMap(model, code, UMLSQuery.Queries.RELATION_QUERY.query, false);

            return model;
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
        }
        return null;
    }

}