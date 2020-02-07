package edu.ilstu.umls.fhir.server;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.HardcodedServerAddressStrategy;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.server.interceptor.ResponseHighlighterInterceptor;
import edu.ilstu.umls.fhir.model.CodeSystemProvider;
import edu.ilstu.umls.fhir.model.ConceptMapProvider;

@WebServlet(value = "/fhir/*")
public class FHIRServer extends RestfulServer {

    private static final Logger log = LoggerFactory.getLogger("FHIRServer");

    private static final long serialVersionUID = 1L;

    public final String BASE_URL = "http://umls.it.ilstu.edu/umlsfhir/";

    public final static FhirContext appFHIRContext = FhirContext.forR4();

    public FHIRServer() {
        log.debug("Set the FHIR Context to R4");
        setFhirContext(appFHIRContext);
        setServerAddressStrategy(new HardcodedServerAddressStrategy(BASE_URL));
    }

    @Override
    protected void initialize() throws ServletException {
        try {
            registerProvider(new ConceptMapProvider());
            registerProvider(new CodeSystemProvider());
            LoggingInterceptor loginIntercepter = new LoggingInterceptor();
            loginIntercepter.setLogger(log);
            loginIntercepter.setMessageFormat(
                    "Source[${remoteAddr}] Operation[${operationType} ${idOrResourceName}] UA[${requestHeader.user-agent}] Params[${requestParameters}]");
            registerInterceptor(loginIntercepter);

            registerInterceptor(new ResponseHighlighterInterceptor());
            setDefaultPrettyPrint(true);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }
}