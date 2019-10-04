package edu.ilstu.umls.fhir.server;

import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

import edu.ilstu.umls.fhir.db.UMLSQuery;

/**
 * @author Rishi Saripalle
 * 
 * Service point to search the UMLS database for a given user string. 
 * For example, if the user enter "Mala", the database will be searched
 * for all concepts starting with the given word.
 */

@WebServlet(description = "Search by String input", displayName = "Search", value = "/search")
public class SearchByString extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final Logger log = LogManager.getLogger("SearchString");


	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		log.info("Searching for - "+request.getParameter("term"));
		try {

			Map<String,String[]> concepts = new UMLSQuery().searchByString(request.getParameter("term"));
			response.setContentType("application/text");
			response.getWriter().write(new Gson().toJson(concepts));
			concepts.clear();

		} catch (Exception e) {
			log.error(e.getMessage(), e.getCause());
		}
	}

}
