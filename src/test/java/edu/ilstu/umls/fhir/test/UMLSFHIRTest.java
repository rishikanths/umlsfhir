package edu.ilstu.umls.fhir.test;

import static org.junit.Assert.assertNotEquals;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;

public class UMLSFHIRTest {

	private URL url = null;
	private HttpURLConnection conn = null;

	@Test
	public void getConceptMap() {
		try {
			url = new URL("http://localhost:8080/umls-fhir/mapcui?cui=C1710490");
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			String output;
			while ((output = br.readLine()) != null) {
				assertNotEquals(0, output.length());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
