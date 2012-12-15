package org.stormchaser.server;

import static com.google.gwt.dev.util.Preconditions.checkArgument;
import static com.google.protobuf.CodedOutputStream.newInstance;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mortbay.jetty.HttpStatus.ORDINAL_400_Bad_Request;
import static org.mortbay.jetty.HttpStatus.ORDINAL_412_Precondition_Failed;
import static org.stormchaser.server.Outpost.Measurements.newBuilder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.annotation.Nonnull;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.stormchaser.server.Outpost.Measurements;

import com.google.protobuf.CodedOutputStream;

public class TestOutpostServlet {

	private static final byte[] TRASH = {1,2,3,4,5};
	private OutpostServlet servlet;
	private HttpServletRequest req;
	private HttpServletResponse resp;

	@Before
	public void setUp() throws Exception {
		servlet = new OutpostServlet();
		req = mock(HttpServletRequest.class);
		resp = mock(HttpServletResponse.class);

	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Checks if a Error-Response is send to the client, if the Content-Type is
	 * not "application/octet-stream". The HTTP-Status have to be 412
	 * "Precondition Failed" in this case.
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

	/**
	 * Checks if an Error-Response is send to the Client, if the Content is not
	 * parseable by the ProtocolBuffer.
	 * 
	 * @throws Exception
	 */
	@Test
	public void doPost_faultyEncoded() throws Exception {
		when(req.getContentType()).thenReturn("application/octet-stream");
		when(req.getInputStream()).then(returnInputStreamOf(TRASH));
		
		servlet.doPost(req, resp);
		
		verify(resp).sendError(eq(ORDINAL_400_Bad_Request), anyString());
	}

	/**
	 * Checks if a correct encoded {@link Measurements}-Message can be decoded.
	 * 
	 * @throws Exception
	 */
	@Test
	public void doPost_decodeMeasurements() throws Exception {
		when(req.getContentType()).thenReturn("application/octet-stream");
		Measurements measurements = newBuilder().setMeasurementIntervallMs(2000).setMeasurementStarttimeUTC(1234567890).

		build();
		when(req.getInputStream()).then(returnInputStreamOf(measurements));

		servlet.doPost(req, resp);
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////
	private Answer<ServletInputStream> returnInputStreamOf(@Nonnull Measurements m) throws IOException {

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		CodedOutputStream output = newInstance(os);
		m.writeTo(output);
		output.flush();

		return returnInputStreamOf(os.toByteArray());
	}

 
	private Answer<ServletInputStream> returnInputStreamOf(int ...bytes) {
		byte[] c = toByteArray(bytes);
		return returnInputStreamOf(c);
	}
	
	private Answer<ServletInputStream> returnInputStreamOf(byte ...bytes) {
		
		final ByteArrayInputStream delegate = new ByteArrayInputStream(bytes);

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

	private byte[] toByteArray(int... bytes) {
		byte[] c = new byte[bytes.length];
		for (int i = 0; i < c.length; i++) {
			int b = c[i];
			checkArgument((b&0xff)==b);
			c[i]=(byte) (0xff&b);
				
		}
		return c;
	}

}
