package org.stormchaser.server;

import static org.mortbay.jetty.HttpStatus.ORDINAL_400_Bad_Request;

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

import com.google.appengine.labs.repackaged.com.google.common.primitives.Ints;

/**
 * Beantwortet Anfragen der Wetterstation
 * 
 * @author Christian Schwarz
 * 
 */
@SuppressWarnings("serial")
public class OutpostServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			int[] content = decodeContent(req.getInputStream());

			getServletContext().setAttribute("diff", content);
		} catch (IOException e) {
			System.err.println(e);
			resp.sendError(ORDINAL_400_Bad_Request, e.getMessage());
			return;
		}

	}

	private int[] decodeContent(ServletInputStream inputStream) throws IOException {
		GZIPInputStream gzip = new GZIPInputStream(inputStream);
		DataInputStream dis = new DataInputStream(gzip);
		List<Integer> diffs = new ArrayList<Integer>();
		try {
			
			while (true)
				diffs.add(dis.readInt());
		} catch (IOException e) {

		}

		System.out.println(diffs);
		return Ints.toArray(diffs);
	}

}