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
import edu.ilstu.umls.fhir.model.CodeProvider;
import edu.ilstu.umls.fhir.model.ConceptMapProvider;

@WebServlet(value = "/umlsfhir/*")
public class FHIRServer extends RestfulServer {

    private static final Logger log = LoggerFactory.getLogger("FHIRServer");

    private static final long serialVersionUID = 1L;

    private final String BASE_URL = "http://umls.it.ilstu.edu/umlsfhir/";

    public FHIRServer() {
        log.info("Calling the FHIRServer constructor");
        setServerAddressStrategy(new HardcodedServerAddressStrategy(BASE_URL));
    }

    @Override
    protected void initialize() throws ServletException {
        setFhirContext(FhirContext.forR4());
        log.debug("Set the FHIR Context to R4");
        registerProvider(new ConceptMapProvider());
        registerProvider(new CodeProvider());
        LoggingInterceptor loginIntercepter = new LoggingInterceptor();
        loginIntercepter.setLogger(log);
        loginIntercepter.setMessageFormat(
                "Source[${remoteAddr}] Operation[${operationType} ${idOrResourceName}] UA[${requestHeader.user-agent}] Params[${requestParameters}]");
        registerInterceptor(loginIntercepter);

        registerInterceptor(new ResponseHighlighterInterceptor());

    }
}