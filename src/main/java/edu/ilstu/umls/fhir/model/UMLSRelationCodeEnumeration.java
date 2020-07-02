package edu.ilstu.umls.fhir.model;

import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.r4.model.Base;
import org.hl7.fhir.r4.model.EnumFactory;
import org.hl7.fhir.r4.model.Enumeration;
import org.hl7.fhir.r4.model.PrimitiveType;

public class UMLSRelationCodeEnumeration {

    public enum UMLSRelationCode {

        /**
         * Allowed qualifier
         */
        AQ,
        /**
         * has child relationship in a Metathesaurus source vocabulary
         */
        CHD,
        /**
         * Deleted concept
         */
        DEL,
        /**
         * has parent relationship in a Metathesaurus source vocabulary
         */
        PAR,
        /**
         * can be qualified by.
         */
        QB,
        /**
         * has a broader relationship
         */
        RB,
        /**
         * the relationship is similar or "alike". the two concepts are similar or
         * "alike". In the current edition of the Metathesaurus, most relationships with
         * this attribute are mappings provided by a source, named in SAB and SL; hence
         * concepts linked by this relationship may be synonymous, i.e.
         * self-referential: CUI1 = CUI2. In previous releases, some MeSH Supplementary
         * Concept relationships were represented in this way.
         */
        RL,
        /**
         * has a RL relationship
         */
        RN,
        /**
         * has relationship other than synonymous, RL, or broader
         */
        RO,
        /**
         * related and possibly synonymous.
         */
        RQ,
        /**
         * Related, unspecified
         */
        RU,
        /**
         * has sibling relationship in a Metathesaurus source vocabulary.
         */
        SIB,
        /**
         * source asserted synonymy.
         */
        SY,
        /**
         * Not related, no mapping
         */
        XR,
        /**
         * Empty relationship
         */
        EMPTY;

        public static UMLSRelationCode fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
            if ("AQ".equals(codeString))
                return AQ;
            if ("CHD".equals(codeString))
                return CHD;
            if ("DEL".equals(codeString))
                return DEL;
            if ("PAR".equals(codeString))
                return PAR;
            if ("QB".equals(codeString))
                return QB;
            if ("RB".equals(codeString))
                return RB;
            if ("RL".equals(codeString))
                return RL;
            if ("RN".equals(codeString))
                return RN;
            if ("RO".equals(codeString))
                return RO;
            if ("RQ".equals(codeString))
                return RQ;
            if ("RU".equals(codeString))
                return RU;
            if ("SIB".equals(codeString))
                return SIB;
            if ("SY".equals(codeString))
                return SY;
            if ("XR".equals(codeString))
                return XR;
            if ("EMPTY".equals(codeString))
                return EMPTY;
            throw new FHIRException("Unknown UMLSRelationCode code '" + codeString + "'");
        }

        public String toCode() {
            switch (this) {
            case AQ:
                return "AQ";
            case CHD:
                return "CHD";
            case DEL:
                return "DEL";
            case PAR:
                return "PAR";
            case QB:
                return "QB";
            case RB:
                return "RB";
            case RL:
                return "RL";
            case RN:
                return "RN";
            case RO:
                return "RO";
            case RQ:
                return "RQ";
            case RU:
                return "RU";
            case SIB:
                return "SIB";
            case SY:
                return "SY";
            case XR:
                return "XR";
            case EMPTY:
                return " ";
            default:
                return "?";
            }
        }

        public String getSystem() {
            switch (this) {
            case AQ:
                return "http://umls.it.ilstu.edu/fhir/umls-relation-code";
            case CHD:
                return "http://umls.it.ilstu.edu/fhir/umls-relation-code";
            case DEL:
                return "http://umls.it.ilstu.edu/fhir/umls-relation-code";
            case PAR:
                return "http://umls.it.ilstu.edu/fhir/umls-relation-code";
            case QB:
                return "http://umls.it.ilstu.edu/fhir/umls-relation-code";
            case RB:
                return "http://umls.it.ilstu.edu/fhir/umls-relation-code";
            case RL:
                return "http://umls.it.ilstu.edu/fhir/umls-relation-code";
            case RN:
                return "http://umls.it.ilstu.edu/fhir/umls-relation-code";
            case RO:
                return "http://umls.it.ilstu.edu/fhir/umls-relation-code";
            case RQ:
                return "http://umls.it.ilstu.edu/fhir/umls-relation-code";
            case RU:
                return "http://umls.it.ilstu.edu/fhir/umls-relation-code";
            case SIB:
                return "http://umls.it.ilstu.edu/fhir/umls-relation-code";
            case SY:
                return "http://umls.it.ilstu.edu/fhir/umls-relation-code";
            case XR:
                return "http://umls.it.ilstu.edu/fhir/umls-relation-code";
            default:
                return "?";
            }
        }

        public String getDefinition() {
            switch (this) {
            case AQ:
                return "Allowed qualifier";
            case CHD:
                return "has child relationship in a Metathesaurus source vocabulary";
            case DEL:
                return "Deleted Concept";
            case PAR:
                return "has parent relationship in a Metathesaurus source vocabulary ";
            case QB:
                return "can be qualified by";
            case RB:
                return "has a broader relationship";
            case RL:
                return "the relationship is similar or \"alike\". the two concepts are similar or \"alike\". In the current edition of the Metathesaurus, most relationships with this attribute are mappings provided by a source, named in SAB and SL; hence concepts linked by this relationship may be synonymous, i.e. self-referential: CUI1 = CUI2. In previous releases, some MeSH Supplementary Concept relationships were represented in this way.";
            case RN:
                return "has a RL relationship";
            case RO:
                return "has relationship other than synonymous, RL, or broader";
            case RQ:
                return "related and possibly synonymous.";
            case RU:
                return "Related, unspecified";
            case SIB:
                return "has sibling relationship in a Metathesaurus source vocabulary.";
            case SY:
                return "source asserted synonymy.";
            case XR:
                return "Not related, no mapping";
            case EMPTY:
                return "Empty relationship";
            default:
                return "?";
            }
        }

        public String getDisplay() {
            switch (this) {
            case AQ:
                return "AQ";
            case CHD:
                return "CHD";
            case DEL:
                return "DEL";
            case PAR:
                return "PAR";
            case QB:
                return "QB";
            case RB:
                return "RB";
            case RL:
                return "RL";
            case RN:
                return "RN";
            case RO:
                return "RO";
            case RQ:
                return "RQ";
            case RU:
                return "RU";
            case SIB:
                return "SIB";
            case SY:
                return "SY";
            case XR:
                return "XR";
            case EMPTY:
                return "EMPTY";
            default:
                return "?";
            }
        }
    }

    public static class UMLSRelationTypeCodeEnumFactory implements EnumFactory<UMLSRelationCode> {
        public UMLSRelationCode fromCode(String codeString) throws IllegalArgumentException {
            if (codeString == null || "".equals(codeString))
                if (codeString == null || "".equals(codeString))
                    return null;
            if ("AQ".equals(codeString))
                return UMLSRelationCode.AQ;
            if ("CHD".equals(codeString))
                return UMLSRelationCode.CHD;
            if ("PAR".equals(codeString))
                return UMLSRelationCode.PAR;
            if ("QB".equals(codeString))
                return UMLSRelationCode.QB;
            if ("RB".equals(codeString))
                return UMLSRelationCode.RB;
            if ("RL".equals(codeString))
                return UMLSRelationCode.RL;
            if ("RN".equals(codeString))
                return UMLSRelationCode.RN;
            if ("RO".equals(codeString))
                return UMLSRelationCode.RO;
            if ("RQ".equals(codeString))
                return UMLSRelationCode.RQ;
            if ("RU".equals(codeString))
                return UMLSRelationCode.RU;
            if ("SIB".equals(codeString))
                return UMLSRelationCode.SIB;
            if ("SY".equals(codeString))
                return UMLSRelationCode.SY;
            if ("XR".equals(codeString))
                return UMLSRelationCode.XR;
            if ("EMPTY".equals(codeString))
                return UMLSRelationCode.EMPTY;
            throw new IllegalArgumentException("Unknown UMLSRelationCode code '" + codeString + "'");
        }

        public Enumeration<UMLSRelationCode> fromType(Base code) throws FHIRException {
            if (code == null)
                return null;
            if (code.isEmpty())
                return new Enumeration<UMLSRelationCode>(this);
            String codeString = ((PrimitiveType) code).asStringValue();
            if (codeString == null || "".equals(codeString))
                return null;
            if ("AQ".equals(codeString))
                return new Enumeration<UMLSRelationCode>(this, UMLSRelationCode.AQ);
            if ("CHD".equals(codeString))
                return new Enumeration<UMLSRelationCode>(this, UMLSRelationCode.CHD);
            if ("PAR".equals(codeString))
                return new Enumeration<UMLSRelationCode>(this, UMLSRelationCode.PAR);
            if ("QB".equals(codeString))
                return new Enumeration<UMLSRelationCode>(this, UMLSRelationCode.QB);
            if ("RB".equals(codeString))
                return new Enumeration<UMLSRelationCode>(this, UMLSRelationCode.RB);
            if ("RL".equals(codeString))
                return new Enumeration<UMLSRelationCode>(this, UMLSRelationCode.RL);
            if ("RN".equals(codeString))
                return new Enumeration<UMLSRelationCode>(this, UMLSRelationCode.RN);
            if ("DEL".equals(codeString))
                return new Enumeration<UMLSRelationCode>(this, UMLSRelationCode.DEL);
            if ("RO".equals(codeString))
                return new Enumeration<UMLSRelationCode>(this, UMLSRelationCode.RO);
            if ("RQ".equals(codeString))
                return new Enumeration<UMLSRelationCode>(this, UMLSRelationCode.RQ);
            if ("RU".equals(codeString))
                return new Enumeration<UMLSRelationCode>(this, UMLSRelationCode.RU);
            if ("SIB".equals(codeString))
                return new Enumeration<UMLSRelationCode>(this, UMLSRelationCode.SIB);
            if ("SY".equals(codeString))
                return new Enumeration<UMLSRelationCode>(this, UMLSRelationCode.SY);
            if ("XR".equals(codeString))
                return new Enumeration<UMLSRelationCode>(this, UMLSRelationCode.XR);
            if ("EMPTY".equals(codeString))
                return new Enumeration<UMLSRelationCode>(this, UMLSRelationCode.EMPTY);
            throw new FHIRException("Unknown UMLSRelationCode code '" + codeString + "'");
        }

        public String toCode(UMLSRelationCode code) {
            if (code == UMLSRelationCode.AQ)
                return "AQ";
            if (code == UMLSRelationCode.CHD)
                return "CHD";
            if (code == UMLSRelationCode.PAR)
                return "PAR";
            if (code == UMLSRelationCode.QB)
                return "QB";
            if (code == UMLSRelationCode.RB)
                return "RB";
            if (code == UMLSRelationCode.RL)
                return "RL";
            if (code == UMLSRelationCode.RN)
                return "RN";
            if (code == UMLSRelationCode.DEL)
                return "DEL";
            if (code == UMLSRelationCode.RO)
                return "RO";
            if (code == UMLSRelationCode.RQ)
                return "RQ";
            if (code == UMLSRelationCode.RU)
                return "RU";
            if (code == UMLSRelationCode.SIB)
                return "SIB";
            if (code == UMLSRelationCode.SY)
                return "SY";
            if (code == UMLSRelationCode.XR)
                return "XR";
            if (code == UMLSRelationCode.EMPTY)
                return "";
            return "?";
        }

        public String toSystem(UMLSRelationCode code) {
            return code.getSystem();
        }
    }
}