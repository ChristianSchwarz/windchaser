package org.windchaser;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

public class CollectorTest {

	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Checks if a {@link NullPointerException} is thrown when <code>null</code>
	 * is passed.
	 * 
	 * @throws Exception
	 */
	@Test(expected=NullPointerException.class)
	public void new_nullPin() throws Exception {
		new Collector(null);
	}
	
	@Test
	public void new_withPin() throws Exception {
		GpioPinDigitalInput digitalInput = mock(GpioPinDigitalInput.class);
		new Collector(digitalInput);
		verify(digitalInput).addListener(any(GpioPinListenerDigital.class));
	}
}

