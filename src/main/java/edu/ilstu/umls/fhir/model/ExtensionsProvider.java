/*
 * @author rsaripa
 *
 */
package edu.ilstu.umls.fhir.model;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.ElementDefinition;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.model.Type;
import org.hl7.fhir.r4.model.UriType;
import org.hl7.fhir.r4.model.UsageContext;
import org.hl7.fhir.r4.model.ElementDefinition.ElementDefinitionBindingComponent;
import org.hl7.fhir.r4.model.ElementDefinition.TypeRefComponent;
import org.hl7.fhir.r4.model.Enumerations.BindingStrength;
import org.hl7.fhir.r4.model.StructureDefinition.ExtensionContextType;
import org.hl7.fhir.r4.model.StructureDefinition.StructureDefinitionContextComponent;
import org.hl7.fhir.r4.model.StructureDefinition.StructureDefinitionDifferentialComponent;
import org.hl7.fhir.r4.model.codesystems.ExtensionContextTypeEnumFactory;
import org.jboss.jandex.TypeTarget.Usage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.server.IResourceProvider;

public class ExtensionsProvider implements IResourceProvider {

    private static final Logger log = LoggerFactory.getLogger("ExtensionsProvider");

    @Override
    public Class<StructureDefinition> getResourceType() {
        return StructureDefinition.class;
    }

    @Read
    public StructureDefinition mappingLabel(@IdParam IdType id) {
        String name = id.getIdPart();
        StructureDefinition sd = new StructureDefinition();
        sd.setType("Extension");
        sd.setContact(UMLSConceptMap.getContactDetails());
        StructureDefinitionContextComponent context = new StructureDefinitionContextComponent();
        context.setType(ExtensionContextType.FHIRPATH);
        sd.addContext(context);
        if (name.equals("conceptmap-target-mapping-label")) {
            context.setExpression("ConceptMap.group.element.target");
            sd.setId("concetpmap-target-mapping-label");
            sd.setUrl("http://umls.it.ilstu.edu/umlsfhir/fhir/StructureDefinition/conceptmap-target-mapping-label");
            sd.setName("concetpmap-target-mapping-label");
            sd.setTitle("Target Mapping Label");
            sd.setDescription("Allows the ConceptMap to capture the type of relation");
            sd.setBaseDefinition("http://hl7.org/fhir/StructureDefinition/Extension");
            StructureDefinitionDifferentialComponent sddiff = new StructureDefinitionDifferentialComponent();
            sddiff.setDisallowExtensions(true);
            List<ElementDefinition> elements = new ArrayList<>();
            sddiff.setElement(elements);
            sd.setDifferential(sddiff);
            ElementDefinition ed = new ElementDefinition();
            ed.setId("Extension");
            ed.setPath("Extension");
            ed.setShort("Target Mapping Label");
            ed.setMin(1);
            ed.setMax("1");
            elements.add(ed);

            ElementDefinition edURI = new ElementDefinition();
            edURI.setId("Extension.url");
            edURI.setPath("Extension.url");
            edURI.setMin(1);
            edURI.setMax("1");
            edURI.setFixed(new UriType(
                    "http://umls.it.ilstu.edu/umlsfhir/fhir/StructureDefinition/conceptmap-target-mapping-label"));
            elements.add(edURI);

            ElementDefinition edExt = new ElementDefinition();
            edExt.setId("Extension.extension");
            edExt.setPath("Extension.extension");
            edExt.setMin(0);
            elements.add(edExt);

            ElementDefinition edValue = new ElementDefinition();
            edValue.setId("Extension.value[x]:valueString");
            edValue.setPath("Extension.valueString");
            edValue.setSliceName("valueString");
            edValue.setMin(1);
            edValue.setMax("1");
            TypeRefComponent comp = new TypeRefComponent();
            comp.setCode("string");
            List<TypeRefComponent> comps = new ArrayList<>();
            comps.add(comp);
            edValue.setType(comps);
            elements.add(edValue);
        } else if (name.equals("conceptmap-target-mapping-type")) {
            context.setExpression("ConceptMap.group.element.target");
            sd.setId("concetpmap-target-mapping-type");
            sd.setUrl("http://umls.it.ilstu.edu/umlsfhir/fhir/StructureDefinition/conceptmap-target-mapping-type");
            sd.setName("concetpmap-target-mapping-type");
            sd.setTitle("Target Mapping type");
            sd.setDescription("Allows the ConceptMap to capture the type of relation");
            sd.setBaseDefinition("http://hl7.org/fhir/StructureDefinition/Extension");
            StructureDefinitionDifferentialComponent sddiff = new StructureDefinitionDifferentialComponent();
            sddiff.setDisallowExtensions(true);
            List<ElementDefinition> elements = new ArrayList<>();
            sddiff.setElement(elements);
            sd.setDifferential(sddiff);
            ElementDefinition ed = new ElementDefinition();
            ed.setId("Extension");
            ed.setPath("Extension");
            ed.setShort("Target Mapping Type");
            ed.setMin(1);
            ed.setMax("1");
            elements.add(ed);

            ElementDefinition edURI = new ElementDefinition();
            edURI.setId("Extension.url");
            edURI.setPath("Extension.url");
            edURI.setFixed(new UriType(
                    "http://umls.it.ilstu.edu/umlsfhir/fhir/StructureDefinition/conceptmap-target-mapping-type"));
            edURI.setMin(1);
            edURI.setMax("1");
            elements.add(edURI);

            ElementDefinition edExt = new ElementDefinition();
            edExt.setId("Extension.extension");
            edExt.setPath("Extension.extension");
            edExt.setMin(0);
            elements.add(edExt);

            ElementDefinition edValue = new ElementDefinition();
            edValue.setId("Extension.value[x]:valueCode");
            edValue.setPath("Extension.valueCode");
            edValue.setSliceName("valueCode");
            edValue.setMin(1);
            edValue.setMax("1");
            ElementDefinitionBindingComponent bind = new ElementDefinitionBindingComponent();
            bind.setValueSet("http://umls.it.ilstu.edu/umlsfhir/fhir/ValueSet/umls-rel");
            bind.setStrength(BindingStrength.REQUIRED);
            edValue.setBinding(bind);
            TypeRefComponent comp = new TypeRefComponent();
            comp.setCode("Code");
            List<TypeRefComponent> comps = new ArrayList<>();
            comps.add(comp);
            edValue.setType(comps);
            elements.add(edValue);
        } else if (name.equals("conceptmap-code-semantic-type")) {
            context.setExpression("ConceptMap.group.element");
            sd.setId("conceptmap-code-semantic-type");
            sd.setUrl("http://umls.it.ilstu.edu/umlsfhir/fhir/StructureDefinition/conceptmap-code-semantic-type");
            sd.setName("conceptmap-code-semantic-type");
            sd.setTitle("Semantic Type of the codes in Element and Target");
            sd.setDescription("Allows the ConceptMap to capture the type of relation");
            StructureDefinitionContextComponent context2 = new StructureDefinitionContextComponent();
            context2.setType(ExtensionContextType.FHIRPATH);
            context2.setExpression("ConceptMap.group.element.target");
            sd.addContext(context2);
            sd.setBaseDefinition("http://hl7.org/fhir/StructureDefinition/Extension");
            StructureDefinitionDifferentialComponent sddiff = new StructureDefinitionDifferentialComponent();
            sddiff.setDisallowExtensions(true);
            List<ElementDefinition> elements = new ArrayList<>();
            sddiff.setElement(elements);
            sd.setDifferential(sddiff);
            ElementDefinition ed = new ElementDefinition();
            ed.setId("Extension");
            ed.setPath("Extension");
            ed.setShort("Source or Target code's Semantic Types");
            ed.setMin(0);
            ed.setMax("1");
            elements.add(ed);

            ElementDefinition edURI = new ElementDefinition();
            edURI.setId("Extension.url");
            edURI.setPath("Extension.url");
            edURI.setMin(1);
            edURI.setMax("1");
            edURI.setFixed(new UriType(
                    "http://umls.it.ilstu.edu/umlsfhir/fhir/StructureDefinition/conceptmap-code-semantic-type"));
            elements.add(edURI);

            ElementDefinition edExt = new ElementDefinition();
            edExt.setId("Extension.extension");
            edExt.setPath("Extension.extension");
            edExt.setMin(0);
            elements.add(edExt);

            ElementDefinition edValue = new ElementDefinition();
            edValue.setId("Extension.value[x]:valueCoding");
            edValue.setPath("Extension.valueCoding");
            edValue.setSliceName("valueCoding");
            edValue.setMin(1);
            edValue.setMax("1");
            TypeRefComponent comp = new TypeRefComponent();
            comp.setCode("Coding");
            List<TypeRefComponent> comps = new ArrayList<>();
            comps.add(comp);
            edValue.setType(comps);
            elements.add(edValue);
        }

        return sd;
    }

}