/**
 * 
 *//*
*//**
 * @author rsaripa
 *
 */
package edu.ilstu.umls.fhir.model;

import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.ContactDetail;
import org.hl7.fhir.r4.model.ContactPoint;
import org.hl7.fhir.r4.model.ContactPoint.ContactPointSystem;
import org.hl7.fhir.r4.model.ContactPoint.ContactPointUse;
import org.hl7.fhir.r4.model.Enumerations.PublicationStatus;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.UriType;

import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;

@ResourceDef(name = "UMLSConceptMap", profile = "http://umls.it.ilstu.edu/fhir/StructureDefinition/UMLSConceptMap")
public class UMLSFHIRModel extends ConceptMap {

	private static final long serialVersionUID = 1;

	@Block()
	public static class UMLSSourceElementComponent extends ConceptMap.SourceElementComponent {

		private static final long serialVersionUID = 1;

		@Child(name = "semanticType", type = {CodeType.class }, min = 0, max = 6, modifier=false, summary=false)
		@Extension(url="http://hl7.org/fhir/StructureDefinition/conceptmap-code-semantictype", definedLocally=false, isModifier=false)
		@Description(shortDefinition = "Identifies semantic type of the element", formalDefinition = "Identity the semantic type of the element/item being mapped.")
		protected List<StringType> semanticType;
		
		public List<StringType> getSemanticType(StringType type) {
			return semanticType;
		}
		
		public UMLSSourceElementComponent setSemanticType(StringType type) {
			if(semanticType == null)
				semanticType = new ArrayList();
			semanticType.add(type);
			
			return this;
		}
		
	}
	@Block()
	public static class UMLSTargetElementComponent extends TargetElementComponent {
		
		private static final long serialVersionUID = 1;

		@Child(name = "assertedBy", type = {StringType.class}, min = 0, max = 1)
		@Extension(url="http://hl7.org/fhir/StructureDefinition/conceptmap-mapping-assertedby", definedLocally=false, isModifier=false)
		@Description(shortDefinition = "Provides context to the mappings", formalDefinition = "The target value set provides context to the mappings. Note that the mapping is made between concepts, not between value sets, but the value set provides important context about how the concept mapping choices are made.")
		protected List<StringType> assertedBy;
		
		@Child(name = "uri", type = {UriType.class}, min = 0, max = 1)
		@Extension(url="http://hl7.org/fhir/StructureDefinition/target-uri", definedLocally=false, isModifier=false)
		@Description(shortDefinition = "Target URI", formalDefinition = "The target source URI.")
		protected UriType uri;
		
		@Child(name = "version", type = {StringType.class}, min = 0, max = 1)
		@Extension(url="http://hl7.org/fhir/StructureDefinition/target-version", definedLocally=false, isModifier=false)
		@Description(shortDefinition = "Target version", formalDefinition = "The version of the target terminology.")
		protected StringType version;
		
		@Child(name = "semanticType", type = {StringType.class }, min = 0, max = 6)
		@Extension(url="http://hl7.org/fhir/StructureDefinition/conceptmap-code-semantictype", definedLocally=false, isModifier=false)
		@Description(shortDefinition = "Identifies semantic type of the element", formalDefinition = "Identity the semantic type of the element/item being mapped.")
		protected List<StringType> semanticType;

		@Child(name = "relationshipLabel", type = {StringType.class}, min = 0, max = 1)
		@Extension(url="http://hl7.org/fhir/StructureDefinition/conceptmap-mapping-relation", definedLocally=false, isModifier=false)
		@Description(shortDefinition = "", formalDefinition = "The relationship label between the source and target concepts. The relationship is read from target to source (e.g. the target 'has manifestation' source).")
		protected StringType relationshipLabel;
		
		public List<StringType> getSemanticType(StringType type) {
			return semanticType;
		}
		
		public UMLSTargetElementComponent setSemanticType(String type) {
			if(semanticType == null)
				semanticType = new ArrayList();
			semanticType.add(new StringType(type));
			
			return this;
		}
		
		public UMLSTargetElementComponent setTargetURI(String u) {
			uri = new UriType(u);
			return this;
		}
		
		public UMLSTargetElementComponent setTargetURI(UriType u) {
			uri = u.copy();
			return this;
		}
		
		public UMLSTargetElementComponent setTargetVersion(String version) {
			this.version = new StringType(version);
			return this;
		}
		
		public List<StringType> getAssertedBy() {
			return assertedBy;
		}
		
		public UMLSTargetElementComponent setAssertedBy(String name) {
			if(assertedBy == null)
				assertedBy = new ArrayList<StringType>();
			assertedBy.add(new StringType(name));
			return this;
		}
		
		public StringType getRelationshipLabel(StringType type) {
			return relationshipLabel;
		}
		
		public UMLSTargetElementComponent setRelationshipLabel(String type) {
			if(relationshipLabel == null)
				relationshipLabel = new StringType();
			relationshipLabel.setValueAsString(type);
			return this;
		}
	}
	
	public static UMLSFHIRModel getDefaultFHIRModel() {
		
		UMLSFHIRModel model = new UMLSFHIRModel();
		
		model.setUrl("http://umls.it.ilstu.edu/umls-fhir/mapcui/");
		model.setVersion("1.0");
		model.setStatus(PublicationStatus.DRAFT);
		model.setExperimental(true);
		model.setPublisher("Rishi Saripalle - Illinois State University");
		
		ContactPoint cp = new ContactPoint();
		cp.setSystem(ContactPointSystem.EMAIL);
		cp.setValue("rishi.saripalle@ilstu.edu");
		cp.setUse(ContactPointUse.WORK);
		
		ContactDetail cd = new ContactDetail();
		cd.setName("Rishi Saripalle");
		List<ContactPoint> contacts = new ArrayList<ContactPoint>();
		contacts.add(cp);
		cd.setTelecom(contacts);
		
		List<ContactDetail> contactDetails = new ArrayList<ContactDetail>();
		contactDetails.add(cd);
		model.setContact(contactDetails);
		
		model.setSource(new UriType().setValue("http://nlm.nih.gov/research/umls"));
		
		ConceptMapGroupComponent group = new ConceptMapGroupComponent();
		group.setSource("http://nlm.nih.gov/research/umls");
		group.setSourceVersion("2018AA");
		SourceElementComponent source = new UMLSSourceElementComponent();
		group.addElement(source);
		List<ConceptMapGroupComponent> groups = new ArrayList();
		groups.add(group);
		model.setGroup(groups);

		return model;
		
	}
	
	public static ConceptMap getConceptMap() {
		
		ConceptMap map = new ConceptMap();
		
		map.setUrl("http://umls.it.ilstu.edu/fhir/cui/");
		map.setVersion("1.0");
		map.setStatus(PublicationStatus.DRAFT);
		map.setExperimental(true);
		map.setPublisher("Rishi Saripalle - Illinois State University");
		
		ContactPoint cp = new ContactPoint();
		cp.setSystem(ContactPointSystem.EMAIL);
		cp.setValue("rishi.saripalle@ilstu.edu");
		cp.setUse(ContactPointUse.WORK);
		
		ContactDetail cd = new ContactDetail();
		cd.setName("Rishi Saripalle");
		List<ContactPoint> contacts = new ArrayList<ContactPoint>();
		contacts.add(cp);
		cd.setTelecom(contacts);
		
		List<ContactDetail> contactDetails = new ArrayList<ContactDetail>();
		contactDetails.add(cd);
		map.setContact(contactDetails);
		
		return map;
	}
}
