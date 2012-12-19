package org.windchaser;

import static com.pi4j.io.gpio.PinState.HIGH;
import static com.pi4j.io.gpio.PinState.LOW;
import static org.mockito.Matchers.isNotNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.google.common.eventbus.Subscribe;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

public class CollectorTest {

	@Mock
	private GpioPinDigitalInput digitalInput;

	@Mock
	private CollectorListener collectorListener;

	@InjectMocks
	private Collector collector;

	@Captor
	private ArgumentCaptor<GpioPinListenerDigital> dipListener;

	@Before
	public void setUp() throws Exception {
		initMocks(this);
		verify(digitalInput).addListener(dipListener.capture());
	}

	/**
	 * Checks if a {@link NullPointerException} is thrown when <code>null</code>
	 * is passed.
	 * 
	 * @throws Exception
	 */
	@Test(expected = NullPointerException.class)
	public void new_nullPin() throws Exception {
		new Collector(null);
	}

	/**
	 * Checks that nothing happens when no listener is registered and an state
	 * change on the digital input is triggered.
	 * 
	 * @throws Exception
	 */
	@Test
	public void noNotificationWhenNoListenerIsRegistered() throws Exception {
		fireGpioPinDigitalStateChanged(HIGH);
		verifyNoMoreInteractions(digitalInput);
	}

	/**
	 * Checks if a Event is fired when the digital input change to high.
	 * @throws Exception
	 */
	@Test
	public void notificationWhenStateChange_toHigh() throws Exception {
		collector.addListener(collectorListener);
		fireGpioPinDigitalStateChanged(HIGH);
		verify(collectorListener).onStateChanged(isNotNull(MeasurementValues.class));
	}

	
	/**
	 *  Checks if a Event is fired when the digital input change to low.
	 * @throws Exception
	 */
	@Test
	public void noNotificationWhenStateChange_toLow() throws Exception {
		collector.addListener(collectorListener);
		fireGpioPinDigitalStateChanged(LOW);
		verify(collectorListener, never()).onStateChanged(isNotNull(MeasurementValues.class));
	}
	
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	static class CollectorListener {
		@Subscribe
		public void onStateChanged(MeasurementValues evt) {
		};
	}
	
	private void fireGpioPinDigitalStateChanged(PinState state) {
		GpioPinDigitalStateChangeEvent evt = new GpioPinDigitalStateChangeEvent(new Object(), digitalInput, state);
		GpioPinListenerDigital pinListener = dipListener.getValue();
		pinListener.handleGpioPinDigitalStateChangeEvent(evt);
	}
}
