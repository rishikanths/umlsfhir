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
import edu.ilstu.umls.fhir.model.UMLSConceptMap.UMLSSourceElementComponent;
import edu.ilstu.umls.fhir.model.UMLSConceptMap.UMLSTargetElementComponent;

@SuppressWarnings({ "unchecked", "deprecation" })
public class UMLSQuery {

	public enum Queries {

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

		private String query;

		Queries(String query) {
			this.query = query;
		}

	}

	private static final Logger log = LoggerFactory.getLogger(UMLSQuery.class);
	private Session session = null;
	private UMLSConceptMap model = UMLSConceptMap.getDefaultUMLSConceptMap();
	private UMLSConceptMap.UMLSSourceElementComponent sourceElement = new UMLSSourceElementComponent();

	public CodeSystem searchByString(String term) {
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setId(term);
		codeSystem.setUrl(UMLSConceptMap.UMLS_URL + "CodeSystem?search=" + term);
		codeSystem.setTitle("List of UMLS concepts with name starting with - " + term);
		codeSystem.setStatus(PublicationStatus.DRAFT);
		codeSystem.setExperimental(true);
		codeSystem.setContact(UMLSConceptMap.getContactDetails());
		codeSystem.setContent(CodeSystemContentMode.FRAGMENT);
		codeSystem.setHierarchyMeaning(CodeSystemHierarchyMeaning.ISA);
		codeSystem.setDescriptionElement(new MarkdownType("### UMLS with FHIR"
				+ ">The CodeSystem instance captures all the concepts (CodeSystem.cocnept) whose label contains the search string."));
		try {
			session = HibernateConfig.getSession();
			List<Object[]> results = session.createNativeQuery(Queries.SEARCH_QUERY.query).setParameter(1, term + "%")
					.list();
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

	public ConceptMap generateConceptMap(String cui, char type) {
		session = HibernateConfig.getSession();
		model.setUrl(model.getUrl() + "ConceptMap/" + cui);
		model.setId(cui);
		getCUIInformation(cui);
		if (type == 'a') {
			queryCUI(cui, UMLSQuery.Queries.MAPPING_QUERY.query);
			queryCUIRelations(cui, UMLSQuery.Queries.HIERARCHY_QUERY);
			queryCUIRelations(cui, UMLSQuery.Queries.RELATION_QUERY);
		} else if (type == 'c') {
			queryCUI(cui, UMLSQuery.Queries.MAPPING_QUERY.query);
		} else if (type == 'h') {
			queryCUIRelations(cui, UMLSQuery.Queries.HIERARCHY_QUERY);
		} else if (type == 'r') {
			queryCUIRelations(cui, UMLSQuery.Queries.RELATION_QUERY);
		}
		HibernateConfig.closeSession(session);
		return model;
	}

	private void queryCUI(String cui, String query) {
		try {
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
					group.setSource(UMLSConceptMap.UMLS_URL);
					group.setSourceVersion(UMLSConceptMap.UMLS_VER);
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

	private void queryCUIRelations(String cui, UMLSQuery.Queries query) {
		try {
			List<Object[]> results = session.createNativeQuery(query.query).setParameter(1, cui).list();
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
					group.setSource(UMLSConceptMap.UMLS_URL);
					group.setSourceVersion(UMLSConceptMap.UMLS_VER);
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
				target.setEquivalence(getRelationMapping(o[2].toString()));
				target.setSemanticType(getCUiSemanticType(o[0].toString()));
				if (o[3] != null && query != UMLSQuery.Queries.HIERARCHY_QUERY) {
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

	public ConceptMapEquivalence getRelationMapping(String RELA) {

		switch (RELA) {
		case "RU":
			return ConceptMapEquivalence.RELATEDTO;
		case "RO":
			return ConceptMapEquivalence.RELATEDTO;
		case "RB":
			return ConceptMapEquivalence.WIDER;
		case "RL":
			return ConceptMapEquivalence.EQUIVALENT;
		case "RQ":
			return ConceptMapEquivalence.EQUIVALENT;
		case "SY":
			return ConceptMapEquivalence.EQUIVALENT;
		case "PAR":
			return ConceptMapEquivalence.SUBSUMES;
		case "RN":
			return ConceptMapEquivalence.NARROWER;
		case "SIB":
			return ConceptMapEquivalence.SPECIALIZES;
		case "CHD":
			return ConceptMapEquivalence.SPECIALIZES;
		case "XR":
			return ConceptMapEquivalence.UNMATCHED;
		default:
			return ConceptMapEquivalence.UNMATCHED;
		}
	}

	private void getCUIInformation(String cui) {
		try {
			List<Object[]> results = session.createNativeQuery(Queries.MTH_QUERY.query).setParameter(1, cui).list();
			if (results.size() == 0)
				results = session.createNativeQuery(Queries.PREF_CUI_QUERY.query).setParameter(1, cui).list();
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
				semanticType.setSystem(UMLSConceptMap.UMLS_URL);
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
