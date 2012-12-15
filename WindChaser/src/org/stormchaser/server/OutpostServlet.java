package org.stormchaser.server;

import static org.mortbay.jetty.HttpStatus.ORDINAL_400_Bad_Request;
import static org.mortbay.jetty.HttpStatus.ORDINAL_412_Precondition_Failed;
import static org.stormchaser.server.Outpost.Measurements.parseFrom;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mortbay.jetty.HttpStatus;
import org.stormchaser.server.Outpost.Measurements;

/**
 * Beantwortet Anfragen der Wetterstation
 * @author Christian Schwarz
 *
 */
@SuppressWarnings("serial")
public class OutpostServlet extends HttpServlet {
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		if (!isContentTypeOctetStream(req)){
			resp.sendError(ORDINAL_412_Precondition_Failed, "Unsupported content type!");
			return;
		}
		
		Measurements m;
		try {
			m = parseFrom(req.getInputStream());
		} catch (IOException e) {
			resp.sendError(ORDINAL_400_Bad_Request, e.getMessage());
			return;
		}
		int measurementIntervallMs = m.getMeasurementIntervallMs();
		long starttimeUTC = m.getMeasurementStarttimeUTC();
		m.getTicksList();
	}

	
	private boolean isContentTypeOctetStream(HttpServletRequest req) {
		return "application/octet-stream".equalsIgnoreCase(req.getContentType());
	}
}