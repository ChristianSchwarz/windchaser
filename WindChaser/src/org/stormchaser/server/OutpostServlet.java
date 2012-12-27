package org.stormchaser.server;

import static org.mortbay.jetty.HttpStatus.ORDINAL_400_Bad_Request;
import static org.mortbay.jetty.HttpStatus.ORDINAL_412_Precondition_Failed;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Beantwortet Anfragen der Wetterstation
 * @author Christian Schwarz
 *
 */
@SuppressWarnings("serial")
public class OutpostServlet extends HttpServlet {
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
//		if (!isContentTypeOctetStream(req)){
//			resp.sendError(ORDINAL_412_Precondition_Failed, "Unsupported content type!"+ req.getContentType());
//			return;
//		}
		
		
		try {
			decodeContent(req.getInputStream());
		} catch (IOException e) {
			System.err.println(e);
			resp.sendError(ORDINAL_400_Bad_Request, e.getMessage());
			return;
		}
		
	}

	
	private void decodeContent(ServletInputStream inputStream) throws IOException  {
		GZIPInputStream gzip= new GZIPInputStream(inputStream);
		DataInputStream dis = new DataInputStream(gzip);
		List<Integer> diffs = new ArrayList<Integer>();
		try{
		while(true)
			diffs.add(dis.readInt());
		}
		catch (IOException e) {
			
		}
		
		System.out.println(diffs);
		
	}


	private boolean isContentTypeOctetStream(HttpServletRequest req) {
		return "application/octet-stream".equalsIgnoreCase(req.getContentType());
	}
}