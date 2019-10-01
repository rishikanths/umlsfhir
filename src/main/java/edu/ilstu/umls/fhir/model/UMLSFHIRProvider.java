package edu.ilstu.umls.fhir.model;

import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.server.IResourceProvider;
import edu.ilstu.umls.fhir.db.UMLSQuery;
/**
 * @author rsaripa
 *
 * The ConceptMap resource provider for UMLS knowledge
 */
public class UMLSFHIRProvider implements IResourceProvider{

	@Override
	public Class<? extends IBaseResource> getResourceType() {
		return UMLSFHIRModel.class;
	}
	
	/**
     * The "@Read" annotation returns a single ConceptMap resource
     * of a requested CUI. 
     * 
     * @param cui
     *    The read operation takes one parameter, which must be of type
     *    StringType.
     * @return 
     *    Returns a ConceptMap resource matching this CUI, or null if none exists.
     */
    @Read()
    public UMLSFHIRModel getResourceById(@IdParam StringType cui) {
    	UMLSQuery q = new UMLSQuery();
        return null;//q.queryCUIMapping(cui.asStringValue());
    }

}