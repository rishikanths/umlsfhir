/*
 * @author rsaripa
 *
 */
package edu.ilstu.umls.fhir.model;

import java.util.Calendar;

import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.ValueSet;
import org.hl7.fhir.r4.model.Enumerations.PublicationStatus;
import org.hl7.fhir.r4.model.ValueSet.ConceptReferenceComponent;
import org.hl7.fhir.r4.model.ValueSet.ConceptSetComponent;
import org.hl7.fhir.r4.model.ValueSet.ValueSetComposeComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.server.IResourceProvider;

public class UMLSRelationTypeCodeValueSetProvider implements IResourceProvider {

    private static final Logger log = LoggerFactory.getLogger("UMLSRelationTypeCodeValueSetProvider");

    @Override
    public Class<ValueSet> getResourceType() {
        return ValueSet.class;
    }

    @Read
    public ValueSet getUMLSRelationTypeCode(@IdParam IdType id) {
        String vsName = id.getIdPart();
        try {
            ValueSet set = new ValueSet();
            Meta metaData = new Meta();
            metaData.setLastUpdated(Calendar.getInstance().getTime());
            set.setMeta(metaData);
            set.setUrl("http://umls.it.ilstu.edu/umlsfhir/fhir/ValueSet/umls-rel");
            set.setVersion("1.0");
            set.setName("UMLSRelationTypeCode");
            set.setTitle("UMLS REL Set");
            set.setStatus(PublicationStatus.DRAFT);
            set.setExperimental(true);
            set.setContact(UMLSConceptMap.getContactDetails());
            set.setDescription("The set of values REL column in MRREL table/RRF file can accept in UMLS");

            ValueSetComposeComponent compose = new ValueSetComposeComponent();
            ConceptSetComponent concepts = new ConceptSetComponent();
            for (UMLSRelationCodeEnumeration.UMLSRelationCode code : UMLSRelationCodeEnumeration.UMLSRelationCode
                    .values()) {
                ConceptReferenceComponent ref = new ConceptReferenceComponent();
                ref.setCode(code.toCode());
                ref.setDisplay(code.getDefinition());
                concepts.addConcept(ref);
            }
            compose.addInclude(concepts);
            set.setCompose(compose);

            return set;
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
        }
        return null;
    }

}