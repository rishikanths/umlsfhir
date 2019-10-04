package edu.ilstu.umls.fhir.server;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.ilstu.umls.fhir.db.UMLSQuery;

/**
 * @author Rishi Saripalle
 * 
 */

@WebServlet(description = "Search with CUI", displayName = "Search with CUI", value = "/mapcui")
public class SearchByCUI extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final Logger log = LogManager.getLogger("SearchCUI");


	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		log.info("Searching for - "+request.getParameter("cui"));
		try {

			String modelasJson = new UMLSQuery().queryCUIMapping(request.getParameter("cui"));
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(modelasJson);
		} catch (Exception e) {
			log.error(e.getMessage(), e.getCause());
		}
	}

}
