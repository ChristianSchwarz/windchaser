package org.stormchaser.server;

import org.stormchaser.client.GreetingService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements GreetingService {

	public int[] greetServer() throws IllegalArgumentException {
			return (int[]) getServletContext().getAttribute("diff");
	}

	
}
