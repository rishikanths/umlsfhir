package edu.ilstu.umls.fhir.db;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hl7.fhir.r4.model.ConceptMap.ConceptMapGroupComponent;
import org.hl7.fhir.r4.model.ConceptMap.SourceElementComponent;
import org.hl7.fhir.r4.model.ConceptMap.TargetElementComponent;
import org.hl7.fhir.r4.model.Enumerations.ConceptMapEquivalence;
import org.hl7.fhir.r4.model.Enumerations.PublicationStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.MarkdownType;
import org.hl7.fhir.r4.model.CodeSystem.CodeSystemContentMode;
import org.hl7.fhir.r4.model.CodeSystem.CodeSystemHierarchyMeaning;

import edu.ilstu.umls.fhir.model.UMLSConceptMap;
import edu.ilstu.umls.fhir.model.UMLSRelationCodeEnumeration.UMLSRelationCode;
import edu.ilstu.umls.fhir.utils.Utils;
import edu.ilstu.umls.fhir.model.UMLSConceptMap.UMLSSourceElementComponent;
import edu.ilstu.umls.fhir.model.UMLSConceptMap.UMLSTargetElementComponent;

@SuppressWarnings({ "unchecked", "deprecation" })
public class UMLSQuery {

	public static enum Queries {

		SEARCH_QUERY("SELECT c.CUI, STR, STY from umls.MRCONSO as c, umls.MRSTY as s "
				+ "WHERE LAT='ENG' AND c.CUI = s.CUI AND STR LIKE ? GROUP BY c.CUI ORDER BY c.STR ASC"),
		MTH_QUERY(
				"SELECT c.CUI, c.STR, s.TUI, s.STY FROM umls.MRCONSO as c, umls.MRSTY as s WHERE c.CUI= ? AND TS = 'P' AND STT='PF' AND ISPREF = 'Y' AND c.cui = s.cui AND c.SAB='MTH'"),
		PREF_CUI_QUERY(
				"SELECT c.CUI, c.STR, s.TUI, s.STY FROM umls.MRCONSO as c, umls.MRSTY as s WHERE c.CUI= ? AND TS = 'P' AND STT='PF' AND ISPREF = 'Y' AND c.cui = s.cui"),
		SAB_QUERY(
				"SELECT c.CUI, c.STR, s.STY,c.SAB, sab.SVER FROM umls.MRCONSO as c, umls.MRSTY as s, umls.MRSAB as sab WHERE c.CUI= ? AND c.cui = s.cui AND (c.TS='P'OR c.TS='S') AND c.SAB = sab.RSAB AND c.LAT ='ENG' AND c.SAB !='MTH'"
						+ "order by sab.SVER DESC"),
		MAPPING_QUERY(
				"SELECT c.CUI, c.CODE, c.STR, s.TUI, s.STY, sab.RSAB,sab.SON, sab.SVER FROM umls.MRCONSO as c, umls.MRSTY as s, umls.MRSAB as sab WHERE c.CUI= ? AND c.cui = s.cui AND c.SAB<> 'MTH' AND c.SAB=sab.RSAB GROUP BY c.str, sab.SON, sab.SVER ORDER BY sab.RSAB, sab.SVER"),
		RELATION_QUERY(
				"SELECT CUI2, STR, REL, RELA, sab.SON, sab.RSAB, sab.SVER, RG FROM umls.MRREL as rel, umls.MRSAB as sab, umls.MRCONSO c WHERE CUI1 = ? AND REL NOT IN('PAR','CHD') AND SL = sab.RSAB AND c.CUI = CUI2 GROUP BY CUI2, REL ORDER BY sab.RSAB, sab.SVER"),
		HIERARCHY_QUERY(
				"SELECT CUI2, c.STR, REL,RELA, sab.SON,sab.RSAB, sab.SVER, RG FROM umls.MRREL as rel, umls.MRSAB as sab, umls.MRCONSO c WHERE CUI1 = ? AND REL IN('PAR','CHD') AND SL = sab.RSAB AND c.CUI = CUI2 GROUP BY CUI2, REL ORDER BY CUI2"),

		SEMANTIC_TYPE_QUERY("SELECT TUI, STY FROM umls.MRSTY WHERE CUI = ?");

		public String query;

		Queries(String query) {
			this.query = query;
		}

	}

	private static final Logger log = LoggerFactory.getLogger(UMLSQuery.class);
	private Session session = null;
	private UMLSConceptMap.UMLSSourceElementComponent sourceElement = new UMLSSourceElementComponent();

	public UMLSQuery() {
		try {
			session = HibernateConfig.getSession();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			log.error(e.getMessage(), e);
		}
	}

	public CodeSystem searchByString(String term, String query) {
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setId(term);
		codeSystem.setUrl(Utils.BASE_URL + "/umlsfhir/fhir/CodeSystem?search=" + term);
		codeSystem.setTitle("List of UMLS concepts with name starting with - " + term);
		codeSystem.setStatus(PublicationStatus.DRAFT);
		codeSystem.setExperimental(true);
		codeSystem.setContact(UMLSConceptMap.getContactDetails());
		codeSystem.setContent(CodeSystemContentMode.FRAGMENT);
		codeSystem.setHierarchyMeaning(CodeSystemHierarchyMeaning.ISA);
		codeSystem.setDescriptionElement(new MarkdownType("### UMLS with FHIR"
				+ "The CodeSystem instance captures the concepts (CodeSystem.cocnept) whose offical NLM provided name contains the search string."));
		try {
			session = HibernateConfig.getSession();
			List<Object[]> results = session.createNativeQuery(query).setParameter(1, term + "%").list();
			for (Object[] o : results) {
				CodeSystem.ConceptDefinitionComponent cd = new CodeSystem.ConceptDefinitionComponent();
				cd.setCode(o[0].toString());
				cd.setDisplay(o[1].toString());
				codeSystem.addConcept(cd);
			}
			HibernateConfig.closeSession(session);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			session.getTransaction().rollback();
			log.error(e.getMessage(), e);
		}
		return codeSystem;
	}

	public void generateCUIAtomsConcetpMap(ConceptMap model, String cui, String query) {
		try {
			getCUIInformation(cui);
			List<Object[]> results = session.createNativeQuery(query).setParameter(1, cui).list();
			UMLSConceptMap.UMLSSourceElementComponent source = null;
			ConceptMapGroupComponent group = null;
			String sab = "";
			String version = "";
			for (Object[] o : results) {
				String newSab = o[5].toString();
				String newVersion = o[7].toString();
				if (!sab.equals(newSab) && !version.equals(newVersion)) {
					sab = newSab;
					version = newVersion;
					group = new ConceptMapGroupComponent();
					group.setSource(Utils.UMLS_URL);
					group.setSourceVersion(Utils.UMLS_VER);
					group.setTarget("urn:umls:" + o[5].toString() + ":" + o[7].toString());
					group.setTargetVersion(o[7].toString());
					List<SourceElementComponent> sources = new ArrayList<>();

					source = new UMLSConceptMap.UMLSSourceElementComponent();
					source.setCode(sourceElement.getCode());
					source.setDisplay(sourceElement.getDisplay());
					source.setSemanticType(sourceElement.getSemanticType());
					sources.add(source);
					group.setElement(sources);
					model.addGroup(group);
					source.setTarget(new ArrayList<TargetElementComponent>());
				}
				UMLSConceptMap.UMLSTargetElementComponent target = new UMLSTargetElementComponent();
				target.setCode(o[1].toString());
				target.setDisplay(o[2].toString());
				target.setEquivalence(ConceptMapEquivalence.EQUAL);
				target.setSemanticType(source.getSemanticType());

				source.addTarget(target);
			}

		} catch (Exception e) {
			e.printStackTrace(System.out);
			session.getTransaction().rollback();
			log.error(e.getMessage(), e);
		}
	}

	public void generateCUIRelationsConcetpMap(ConceptMap model, String cui, String query, boolean hierarchy) {
		try {
			if (sourceElement == null)
				getCUIInformation(cui);
			List<Object[]> results = session.createNativeQuery(query).setParameter(1, cui).list();
			UMLSConceptMap.UMLSSourceElementComponent source = null;
			ConceptMapGroupComponent group = null;
			String sab = "";
			String version = "";
			for (Object[] o : results) {
				String newSab = o[5].toString();
				String newVersion = "";
				if (o[6] != null)
					newVersion = o[6].toString();
				if (!sab.equals(newSab) && !version.equals(newVersion)) {
					sab = newSab;
					version = newVersion;
					group = new ConceptMapGroupComponent();
					group.setSource(Utils.UMLS_URL);
					group.setSourceVersion(Utils.UMLS_VER);
					group.setTarget("urn:umls:" + o[5].toString() + ":" + newVersion);
					group.setTargetVersion(newVersion);
					List<SourceElementComponent> sources = new ArrayList<>();

					source = new UMLSConceptMap.UMLSSourceElementComponent();
					source.setCode(sourceElement.getCode());
					source.setDisplay(sourceElement.getDisplay());
					source.setSemanticType(sourceElement.getSemanticType());
					sources.add(source);
					group.setElement(sources);
					model.addGroup(group);
					source.setTarget(new ArrayList<TargetElementComponent>());
				}
				UMLSConceptMap.UMLSTargetElementComponent target = new UMLSTargetElementComponent();
				target.setCode(o[0].toString());
				target.setDisplay(o[1].toString());
				// target.setEquivalence(getRelationMapping(o[2].toString()));
				target.setMappingType(getRelationMapping(o[2].toString()));
				target.setSemanticType(getCUiSemanticType(o[0].toString()));
				if (o[3] != null && !hierarchy) {
					target.setMappingLabel(o[3].toString());
				}

				source.addTarget(target);
			}

		} catch (Exception e) {
			e.printStackTrace(System.out);
			session.getTransaction().rollback();
			log.error(e.getMessage(), e);
		}
	}

	private UMLSRelationCode getRelationMapping(String RELA) {

		switch (RELA) {
		case "RU":
			return UMLSRelationCode.RU;
		case "RO":
			return UMLSRelationCode.RO;
		case "RB":
			return UMLSRelationCode.RB;
		case "RL":
			return UMLSRelationCode.RL;
		case "RQ":
			return UMLSRelationCode.RQ;
		case "SY":
			return UMLSRelationCode.SY;
		case "PAR":
			return UMLSRelationCode.PAR;
		case "RN":
			return UMLSRelationCode.RN;
		case "SIB":
			return UMLSRelationCode.SIB;
		case "CHD":
			return UMLSRelationCode.CHD;
		case "XR":
			return UMLSRelationCode.XR;
		case "":
			return UMLSRelationCode.EMPTY;
		default:
			return UMLSRelationCode.XR;
		}
	}

	private void getCUIInformation(String cui) {
		try {
			List<Object[]> results = session.createNativeQuery(Queries.MTH_QUERY.query).setParameter(1, cui).list();
			if (results.size() == 0)
				results = session.createNativeQuery(Queries.PREF_CUI_QUERY.query).setParameter(1, cui).list();
			if (results.size() == 0)
				throw new IllegalArgumentException("Entered CUI - " + cui + " doesnt exist");
			Object[] r = results.get(0);
			sourceElement.setCode(r[0].toString());
			sourceElement.setDisplay(r[1].toString());

			sourceElement.setSemanticType(getCUiSemanticType(cui));

		} catch (Exception e) {
			e.printStackTrace(System.out);
			session.getTransaction().rollback();
			log.error(e.getMessage(), e);
		}
	}

	private List<Coding> getCUiSemanticType(String cui) {
		List<Coding> semanticTypes = new ArrayList<>();
		try {
			List<Object[]> results = session.createNativeQuery(Queries.SEMANTIC_TYPE_QUERY.query).setParameter(1, cui)
					.list();
			for (Object[] o : results) {
				Coding semanticType = new Coding();
				semanticType.setSystem(Utils.UMLS_URL);
				semanticType.setCode(o[0].toString());
				semanticType.setDisplay(o[1].toString());
				semanticTypes.add(semanticType);
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
			session.getTransaction().rollback();
			log.error(e.getMessage(), e);
		}
		return semanticTypes;
	}

}
