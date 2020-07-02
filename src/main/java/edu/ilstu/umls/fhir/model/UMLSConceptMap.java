/**
	* @author rsaripa
	*
	*/
package edu.ilstu.umls.fhir.model;

import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.Configuration;
import org.hl7.fhir.r4.model.ContactDetail;
import org.hl7.fhir.r4.model.ContactPoint;
import org.hl7.fhir.r4.model.Enumeration;
import org.hl7.fhir.r4.model.ContactPoint.ContactPointSystem;
import org.hl7.fhir.r4.model.ContactPoint.ContactPointUse;
import org.hl7.fhir.r4.model.Enumerations.PublicationStatus;
import org.hl7.fhir.r4.model.StringType;

import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import edu.ilstu.umls.fhir.model.UMLSRelationCodeEnumeration.UMLSRelationTypeCodeEnumFactory;
import edu.ilstu.umls.fhir.model.UMLSRelationCodeEnumeration.UMLSRelationCode;

@ResourceDef(name = "ConceptMap", profile = "http://umls.it.ilstu.edu/umlsfhi/fhir/StructureDefinition/UMLSConceptMap")
public class UMLSConceptMap extends ConceptMap {

	private static final long serialVersionUID = 1;

	public static final String UMLS_URL = "https://www.nlm.nih.gov/research/umls/";

	public static final String UMLS_VER = "2019AB";

	@Block()
	public static class UMLSSourceElementComponent extends ConceptMap.SourceElementComponent {

		private static final long serialVersionUID = 1;

		@Child(name = "semanticType", type = {
				Coding.class }, min = 1, max = Child.MAX_UNLIMITED, modifier = false, summary = false)
		@Extension(url = "http://umls.it.ilstu.edu/umlsfhir/fhir/StructureDefinition/conceptmap-code-semantic-type", definedLocally = true, isModifier = false)
		@Description(shortDefinition = "Semantic type of the code", formalDefinition = "Semantic type of an atom or concept assigned by NLM from UMLS Semantic Network.")
		protected List<Coding> semanticType;

		public List<Coding> getSemanticType() {
			return semanticType;
		}

		public UMLSSourceElementComponent setSemanticType(Coding type) {
			if (semanticType == null)
				semanticType = new ArrayList<Coding>();
			semanticType.add(type);

			return this;
		}

		public UMLSSourceElementComponent setSemanticType(List<Coding> type) {
			if (semanticType == null)
				semanticType = new ArrayList<Coding>();
			semanticType.addAll(type);

			return this;
		}

	}

	@Block()
	public static class UMLSTargetElementComponent extends TargetElementComponent {

		private static final long serialVersionUID = 1;

		@Child(name = "semanticType", type = { Coding.class }, min = 1, max = Child.MAX_UNLIMITED)
		@Extension(url = "http://umls.it.ilstu.edu/umlsfhir/fhir/StructureDefinition/conceptmap-code-semantic-type", definedLocally = true, isModifier = false)
		@Description(shortDefinition = "Semantic type of the code", formalDefinition = "Semantic type of an atom/concept assigned by NLM from UMLS Semantic Network.")
		protected List<Coding> semanticType;

		@Child(name = "mappingLabel", type = { StringType.class }, min = 0, max = 1)
		@Extension(url = "http://umls.it.ilstu.edu/umlsfhir/fhir/StructureDefinition/conceptmap-target-mapping-label", definedLocally = true, isModifier = false)
		@Description(shortDefinition = "Additional information on the nature of relationship between source and target", formalDefinition = "The relationship label between the source and target concepts. The relationship is read from target to source (e.g. the target 'has manifestation' source).")
		protected StringType mappingLabel;

		@Child(name = "mappingType", type = { CodeType.class }, min = 1, max = 1)
		@Extension(url = "http://umls.it.ilstu.edu/umlsfhir/fhir/StructureDefinition/conceptmap-target-mapping-type", definedLocally = true, isModifier = false)
		@Description(shortDefinition = "The type of relationship between source and target", formalDefinition = "The relationship type (REL) between the source and target concepts.")
		protected Enumeration<UMLSRelationCode> mappingType;

		public List<Coding> getSemanticType() {
			return semanticType;
		}

		public UMLSTargetElementComponent setSemanticType(Coding type) {
			if (semanticType == null)
				semanticType = new ArrayList<Coding>();
			semanticType.add(type);

			return this;
		}

		public UMLSTargetElementComponent setSemanticType(List<Coding> type) {
			if (semanticType == null)
				semanticType = new ArrayList<Coding>();
			semanticType.addAll(type);

			return this;
		}

		public StringType getMappingLabel() {
			return mappingLabel;
		}

		public UMLSTargetElementComponent setMappingLabel(String type) {
			if (mappingLabel == null)
				mappingLabel = new StringType();
			mappingLabel.setValueAsString(type);
			return this;
		}

		public Enumeration<UMLSRelationCode> getMappingType() {
			if (this.mappingType == null)
				if (Configuration.errorOnAutoCreate())
					throw new Error("Attempt to auto-create TargetElementComponent.equivalence");
				else if (Configuration.doAutoCreate())
					this.mappingType = new Enumeration<UMLSRelationCode>(new UMLSRelationTypeCodeEnumFactory()); // bb
			return this.mappingType;
		}

		public UMLSTargetElementComponent setMappingType(UMLSRelationCode type) {
			if (this.mappingType == null)
				this.mappingType = new Enumeration<UMLSRelationCode>(new UMLSRelationTypeCodeEnumFactory());
			this.mappingType.setValue(type);
			return this;
		}

	}

	public static UMLSConceptMap getDefaultUMLSConceptMap() {

		UMLSConceptMap model = new UMLSConceptMap();

		model.setUrl("http://umls.it.ilstu.edu/umlsfhir/");
		model.setVersion("1.0");

		model.setStatus(PublicationStatus.DRAFT);
		model.setExperimental(true);
		model.setPublisher("Rishi Saripalle - Illinois State University");

		model.setContact(getContactDetails());

		List<ConceptMapGroupComponent> groups = new ArrayList<ConceptMapGroupComponent>();
		model.setGroup(groups);

		return model;

	}

	public static List<ContactDetail> getContactDetails() {
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

		return contactDetails;

	}
}
