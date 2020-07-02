# UMLS FHIR Terminological Service
## Goal
The objective of this work is to understand how FHIR can be leveraged to represent UMLS knowledge in an interoperable format to overcome UMLS challenges and leverage FHIR’s features to implement a terminological service that allows standardized access to rich UMLS knowledge. UMLS FHIR API serves mappings between the terms from multiple sources to a concept, similar to NLM UMLS API, and also expresses hierarchy and associations between the concepts. UMLS FHIR API is a light-weight solution based HAPI library, an open-source implementation of FHIR standard that can be integrated into any biomedical and healthcare applications. 

## Database Connection
- To install the service locally, please modify Hibernate configuration in **Utils** Java file in **edu.ilstu.umls.fhir.utilsr** package. 
- Also, in the same file, change the BASE_URL value.

## Accessing UMLS Knowledge
The FHIR UMLS API terminology service base URL is http://umls.it.ilstu.edu/umlsfhir/fhir, while the interactive web application deployed on the HAPI Restful Server is available at http://umls.it.ilstu.edu/umlsfhir. The UMLS-FHIR terminology service has two endpoints:
- /CodingSystem?search={searchString} –Returns an instance of CodeSystem with all the concepts whose name matches the search string. Example - http://umls.it.ilstu.edu/umlsfhir/fhir/CodeSystem?search=mala – returns all the concepts in UMLS with prefix “mala” as an instance of CodeSystem.
- /ConceptMap/{concept_cui} – Returns an instance of extended ConceptMap (Figure 6) with the UMLS knowledge associated with the CUI.Example - http://umls.it.ilstu.edu/umlsfhir/fhir/ConceptMap/C0004238

