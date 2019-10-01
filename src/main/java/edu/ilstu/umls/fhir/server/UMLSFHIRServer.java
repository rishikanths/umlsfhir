package edu.ilstu.umls.fhir.server;

import org.eclipse.jetty.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import edu.ilstu.umls.fhir.db.UMLSQuery;

public class UMLSFHIRServer {

	private Integer ourPort;
	private Server ourServer;
	private FhirContext ourCtx;
	private IGenericClient ourClient;

	private static final Logger log = LoggerFactory.getLogger("systemLogger"); 
	
	public static void main(String args[]) {
		
		try {
			new UMLSFHIRServer().startServer();
		}catch(Exception e) {
			log.error(e.getMessage(), e);
		}
		
	}

	public void startServer() throws Exception {
		
		//UMLSQuery.searchByString("mala");
		UMLSQuery q = new UMLSQuery();
		q.queryCUIMapping("C0004238");
		//q.queryCUIHierarchy("C0004238");
		//q.queryCUIRelationships("C0004238", false);
		/*ourPort = RandomServerPortProvider.findFreePort();
		ourServer = new Server(ourPort);
		String base = "http://localhost:" + ourPort;
		System.out.println(base);
		
		WebAppContext root = new WebAppContext();
		root.setResourceBase("src/main/webapp/");
        root.setExtraClasspath("target/umlsfhir/WEB-INF/classes");
		//root.setWar(new File(path+"umlsfhir.war").toURI().toString());
		root.setContextPath("/umlsfhir");
		ourServer.setHandler(root);

		ourServer.start();
		ourServer.join();
		log.debug("UMLS-FHIR Server started successfully");
		ourCtx = FhirContext.forDstu3();
		ourClient = ourCtx.newRestfulGenericClient(base);
	
*/	}

}
