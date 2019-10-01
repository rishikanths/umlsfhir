package edu.ilstu.umls.fhir.db;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hl7.fhir.r4.model.ConceptMap.ConceptMapGroupComponent;
import org.hl7.fhir.r4.model.ConceptMap.TargetElementComponent;
import org.hl7.fhir.r4.model.Enumerations.ConceptMapEquivalence;
import org.hl7.fhir.r4.model.StringType;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import edu.ilstu.umls.fhir.model.UMLSFHIRModel;
import edu.ilstu.umls.fhir.model.UMLSFHIRModel.UMLSSourceElementComponent;
import edu.ilstu.umls.fhir.model.UMLSFHIRModel.UMLSTargetElementComponent;

@SuppressWarnings({ "unchecked", "deprecation", "rawtypes" })
public class UMLSQuery {

	public enum Queries {

		CUI_QUERY("SELECT c.CUI, STR, STY from umls2018.mrconso as c, umls2018.mrsty as s "
				+ "WHERE TS = 'P' AND STT='PF' AND ISPREF='Y' AND LAT='ENG' AND c.CUI = s.CUI AND STR LIKE ? LIMIT 200"),
		MTH_QUERY(
				"SELECT c.CUI, c.STR, s.STY FROM umls2018.mrconso as c, umls2018.mrsty as s WHERE c.CUI= ? AND TS = 'P' AND STT='PF' AND ISPREF = 'Y' AND c.cui = s.cui AND c.SAB='MTH'"),
		SAB_QUERY(
				"SELECT c.CUI, c.STR, s.STY,c.SAB, sab.SVER FROM umls2018.mrconso as c, umls2018.mrsty as s, umls2018.mrsab as sab "
						+ "WHERE c.CUI= ? AND c.cui = s.cui AND c.TS='P' and c.ISPREF = 'Y' and c.TS = 'P' and c.SUPPRESS = 'N' "
						+ "AND c.SAB = sab.RSAB " + "order by sab.SVER DESC"),
		MAPPING_QUERY(
				"SELECT c.CUI, c.CODE, c.STR, s.STY, sab.RSAB,sab.SON, sab.SVER FROM umls2018.mrconso as c, umls2018.mrsty as s, umls2018.mrsab as sab "
						+ "WHERE c.CUI= ? AND c.cui = s.cui AND TS='P' AND c.SAB<> 'MTH' AND c.SAB=sab.RSAB and sab.VCUI IS NOT NULL "
						+ "GROUP BY c.str, sab.SON, sab.SVER ORDER BY sab.RSAB"),
		RELATION_QUERY(
				"SELECT CUI1, STR, REL, RELA, sab.RSAB, sab.SVER, sab.SON, RG FROM umls2018.MRREL as rel, umls2018.mrsab as sab, umls2018.MRCONSO c "
						+ "WHERE CUI2 = ? AND REL IN('RO','RU','SY','RL') AND SL = sab.RSAB AND sab.VCUI IS NOT NULL AND c.CUI = CUI1 "
						+ "AND c.TS='P' AND c.ISPREF = 'Y' AND STT = 'PF' GROUP BY CUI1, REL, sab.RSAB ORDER BY sab.RSAB"),
		HIERARCHY_QUERY(
				"SELECT CUI1, c.STR, REL, sab.SON,sab.RSAB, sab.SVER, RG FROM umls2018.MRREL as rel, umls2018.mrsab as sab, umls2018.MRCONSO c "
						+ "WHERE CUI2 = ? AND REL IN('PAR','CHD') AND SL = sab.RSAB AND sab.VCUI IS NOT NULL AND c.CUI = CUI1 "
						+ "AND c.TS='P' AND c.ISPREF = 'Y' AND STT = 'PF' AND c.SAB = 'MTH' ORDER BY CUI1"),
		SEMANTIC_TYPE_QUERY("SELECT STY FROM umls2018.MRSTY WHERE CUI = ?");

		private String query;

		Queries(String query) {
			this.query = query;
		}

	}

	private static final Logger log = Logger.getLogger(UMLSQuery.class);
	private Session session = null;
	private final IParser fhirJson = FhirContext.forR4().newJsonParser();
	private UMLSFHIRModel model = UMLSFHIRModel.getDefaultFHIRModel();

	public UMLSFHIRModel getModel() {
		return model;
	}

	public Map<String, String[]> searchByString(String term) {
		Map<String, String[]> map = new LinkedHashMap<>();
		try {
			log.info("Hibernate session opened");
			session = HibernateConfig.getSession();
			List<Object[]> results = session.createNativeQuery(Queries.CUI_QUERY.query).setParameter(1, term + "%")
					.list();
			for (Object[] o : results) {
				map.put(o[0].toString(), new String[] { o[0].toString(), o[1].toString() });
			}
			HibernateConfig.closeSession(session);
			log.info("Hibernate session closed");
		} catch (Exception e) {
			e.printStackTrace(System.out);
			session.getTransaction().rollback();
			log.error(e.getMessage(), e);
		}
		return map;
	}

	public String queryCUIMapping(String cui) {
		try {
			session = HibernateConfig.getSession();
			model.setUrl(model.getUrl()+cui);
			List<Object[]> results = session.createNativeQuery(Queries.MAPPING_QUERY.query).setParameter(1, cui).list();
			boolean first = true;
			ConceptMapGroupComponent g = model.getGroup().get(0);
			UMLSSourceElementComponent source = (UMLSSourceElementComponent) g.getElement().get(0);
			List<TargetElementComponent> targets = new ArrayList();
			source.setTarget(targets);
			for (Object[] o : results) {
				if (first) {
					List<Object[]> temp = session.createNativeQuery(Queries.MTH_QUERY.query).setParameter(1, cui)
							.list();
					if (temp.size() == 0) {
						temp = session.createNativeQuery(Queries.SAB_QUERY.query).setParameter(1, cui).list();
						g.setSource("http://nlm.nih.gov/research/umls/" + temp.get(0)[3].toString());
					} else {
						g.setSource("http://nlm.nih.gov/research/umls");
						g.setSourceVersion("2018AA");
					}
					source.setDisplay(temp.get(0)[1].toString()).setCode(temp.get(0)[0].toString());
					source.setSemanticType(new StringType(temp.get(0)[2].toString()));
					first = false;
				}

				UMLSTargetElementComponent target = new UMLSTargetElementComponent();
				target.setCode(o[1].toString()).setDisplay(o[2].toString())
						.setEquivalence(ConceptMapEquivalence.EQUIVALENT);
				target.setAssertedBy(o[5].toString());
				targets.add(target);
			}
			//queryCUIRelationships(cui);
			//queryCUIHierarchy(cui);
			System.out.println(fhirJson.encodeResourceToString(model));
			HibernateConfig.closeSession(session);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			session.getTransaction().rollback();
			log.error(e.getMessage(), e);
		}
		return fhirJson.encodeResourceToString(model);

	}

	public void queryCUIRelationships(String cui) {
		try {
			List<Object[]> results = session.createNativeQuery(Queries.RELATION_QUERY.query).setParameter(1, cui)
					.list();
			List<ConceptMapGroupComponent> groups = model.getGroup();
			String tempCUI = "";
			String tempRELA = "!";
			UMLSTargetElementComponent target = null;
			for (Object[] o : results) {
				if(o[3] == null)
					o[3] = "N/A";
				if (!tempCUI.equals(o[0].toString())
						|| (tempCUI.equals(o[0].toString()) && !tempRELA.equals(o[3].toString()))) {

					ConceptMapGroupComponent g = groups.get(0);
					UMLSSourceElementComponent source = (UMLSSourceElementComponent) g.getElement().get(0);
					List<TargetElementComponent> targets = source.getTarget();

					tempCUI = o[0].toString();
					target = new UMLSTargetElementComponent();
					target.setCode(tempCUI);
					target.setDisplay(o[1].toString());
					target.setEquivalence(ConceptMapEquivalence.RELATEDTO);
					if (o[3] != null) {
						tempRELA = o[3].toString();
						target.setRelationshipLabel(tempRELA);
					} else {
						target.setRelationshipLabel("");
					}
					List<String> tempST = session.createNativeQuery(Queries.SEMANTIC_TYPE_QUERY.query)
							.setParameter(1, o[0].toString()).list();
					for (String st : tempST)
						target.setSemanticType(st);
					target.setAssertedBy(o[4].toString());
					targets.add(target);
				} else {
					target.setAssertedBy(o[4].toString());
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	public void queryCUIHierarchy(String cui) {
		try {
			List<Object[]> results = session.createNativeQuery(Queries.HIERARCHY_QUERY.query).setParameter(1, cui)
					.list();
			List<ConceptMapGroupComponent> groups = model.getGroup();
			String tempCUI = "";
			String tempREL = "!";
			UMLSTargetElementComponent target = null;
			for (Object[] o : results) {
				if (!tempCUI.equals(o[0].toString())
						|| (tempCUI.equals(o[0].toString()) && !tempREL.equals(o[2].toString()))) {

					ConceptMapGroupComponent g = groups.get(0);
					UMLSSourceElementComponent source = (UMLSSourceElementComponent) g.getElement().get(0);
					List<TargetElementComponent> targets = source.getTarget();

					tempCUI = o[0].toString();
					target = new UMLSTargetElementComponent();
					target.setCode(tempCUI);
					target.setDisplay(o[1].toString());
					tempREL = o[2].toString();
					if (tempREL.equals("CHD"))
						target.setEquivalence(ConceptMapEquivalence.SPECIALIZES);
					else
						target.setEquivalence(ConceptMapEquivalence.SUBSUMES);
					List<String> tempST = session.createNativeQuery(Queries.SEMANTIC_TYPE_QUERY.query)
							.setParameter(1, o[0].toString()).list();
					for (String st : tempST)
						target.setSemanticType(st);
					target.setAssertedBy(o[3].toString());
					targets.add(target);
				} else {
					target.setAssertedBy(o[3].toString());
				}
			}
		} catch (Exception e) {
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

}
