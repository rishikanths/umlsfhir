package edu.ilstu.umls.fhir.model;

import org.hl7.fhir.exceptions.FHIRException;

public enum UMLSConceptMapEquivalence {
	/**
	 * The concepts are related to each other, and have at least some overlap in
	 * meaning, but the exact relationship is not known
	 */
	ALLOWED_QUALIFIER,
	/**
	 * The definitions of the concepts mean the same thing (including when
	 * structural implications of meaning are considered) (i.e. extensionaly
	 * identical).
	 */
	OTHER_RELATIONSHIP;
	
	public static UMLSConceptMapEquivalence fromCode(String codeString) throws FHIRException {
		if (codeString == null || "".equals(codeString))
			return null;
		if ("allowed_qualifier".equals(codeString))
			return ALLOWED_QUALIFIER;
		if ("other".equals(codeString))
			return OTHER_RELATIONSHIP;
		throw new FHIRException("Unknown UMLSConceptMapEquivalence code '" + codeString + "'");
	}

	public String toCode() {
		switch (this) {
		case ALLOWED_QUALIFIER:
			return "allowed_qualifier";
		case OTHER_RELATIONSHIP:
			return "other";
		default:
			return "?";
		}
	}

	public String getSystem() {
		switch (this) {
		case ALLOWED_QUALIFIER:
			return "http://umls.it.ilstu.edu/umls-concept-map-equivalence";
		case OTHER_RELATIONSHIP:
			return "http://umls.it.ilstu.edu/umls-concept-map-equivalence";
		default:
			return "?";
		}
	}

	public String getDefinition() {
		switch (this) {
		case ALLOWED_QUALIFIER:
			return "The concepts are related to each other, and have at least some overlap in meaning, but the exact relationship is not known";
		case OTHER_RELATIONSHIP:
			return "The definitions of the concepts mean the same thing (including when structural implications of meaning are considered) (i.e. extensionally identical).";
		default:
			return "?";
		}
	}

	public String getDisplay() {
		switch (this) {
		case ALLOWED_QUALIFIER:
			return "allowed qualifier";
		case OTHER_RELATIONSHIP:
			return "other relationships";
		default:
			return "?";
		}
	}
}