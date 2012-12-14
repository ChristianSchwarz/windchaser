package org.stormchaser.server;

import static com.google.gwt.thirdparty.guava.common.base.Preconditions.checkArgument;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.*;
import static org.mortbay.jetty.HttpStatus.ORDINAL_412_Precondition_Failed;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.google.gwt.thirdparty.guava.common.base.Preconditions;

public class TestOutpostServlet {

	private OutpostServlet servlet;
	private HttpServletRequest req;
	private HttpServletResponse resp;
	private ServletInputStream servletInputStream;

	@Before
	public void setUp() throws Exception {
		servlet = new OutpostServlet();
		req = mock(HttpServletRequest.class);
		resp = mock(HttpServletResponse.class);
		servletInputStream = mock(ServletInputStream.class);
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Prüft ob ein Error Antwort an den Client gesendet wird, wenn der
	 * Content-Type nicht "application/octet-stream" ist. Der zurückgegebene
	 * HTTP-Status muss in diesem Fall 412 "Precondition Failed" sein.
	 * 
	 * @throws ServletException
	 * @throws IOException
	 */
	@Test
	public void doPost_unsupportedContentType() throws ServletException, IOException {
		when(req.getContentType()).thenReturn("unknown");
		servlet.doPost(req, resp);
		verify(resp).sendError(ORDINAL_412_Precondition_Failed, "Unsupported content type!");
		
	}

	@Test
	public void doPost_decodeMeasurements() throws Exception {
		when(req.getContentType()).thenReturn("application/octet-stream");
		when(req.getInputStream()).then(returnInputStreamOf(0x00,0x00,0x00));
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////
	private Answer<ServletInputStream> returnInputStreamOf(int... bytes) {
		byte[] content = new byte[bytes.length];
		for (int i = 0; i < bytes.length; i++) {
			int b = bytes[i];
			checkArgument((b & 0xFF) != b, "Invalid value:" + b);
			content[i] = (byte) (b & 0xFF);
		}
		final ByteArrayInputStream delegate = new ByteArrayInputStream(content);

		return new Answer<ServletInputStream>() {

			@Override
			public ServletInputStream answer(InvocationOnMock invocation) throws Throwable {
				return new ServletInputStream() {

					@Override
					public int read() throws IOException {
						return delegate.read();
					}

					@Override
					public int available() throws IOException {
						return delegate.available();
					}

				};
			}
		};
	}

}
