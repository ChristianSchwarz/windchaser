package org.windchaser;

import static com.pi4j.io.gpio.PinState.HIGH;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.concurrent.ScheduledExecutorService;

import org.hamcrest.Matcher;
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

	@Mock
	private ScheduledExecutorService executor;

	@InjectMocks
	private Collector collector;

	@Captor
	private ArgumentCaptor<GpioPinListenerDigital> dipListener;

	@Captor
	private ArgumentCaptor<Runnable> dispatchTask;
	@Before
	public void setUp() throws Exception {
		initMocks(this);
		verify(digitalInput).addListener(dipListener.capture());
	}

	/**
	 * Verifies that a {@link NullPointerException} is thrown when <code>null</code>
	 * is passed.
	 * 
	 * @throws Exception
	 */
	@Test(expected = NullPointerException.class)
	public void new_nullPin() throws Exception {
		new Collector(null, executor);
	}

	/**
	 * Verifies that {@link NullPointerException} is thrown when <code>null</code>
	 * is passed.
	 * 
	 * @throws Exception
	 */
	@Test(expected = NullPointerException.class)
	public void new_nullExecutor() throws Exception {
		new Collector(digitalInput, null);
	}

	/**
	 * Verifies that a {@link IllegalArgumentException} is thrown when a negative 
	 * interval is set.
	 * 
	 * @throws Exception
	 */
	@Test(expected = IllegalArgumentException.class)
	public void setMeasurementIntervall_negative() throws Exception {
		collector.setCollectorInterval(-1);
	}

	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void dispatch_noListenerRegistered() throws Exception {
		verify(executor).scheduleAtFixedRate(dispatchTask.capture(), eq(10000L),eq(10000L), eq(MILLISECONDS));
		
		fireGpioPinDigitalStateChanged(HIGH);
		
		dispatchTask.getValue().run();
		
		verify(collectorListener,never()).onStateChanged(any(MeasurementValues.class));
		
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void dispatch_toListenerRegistered() throws Exception {
		verify(executor).scheduleAtFixedRate(dispatchTask.capture(), eq(10000L),eq(10000L), eq(MILLISECONDS));
		
		collector.addListener(collectorListener);
		fireGpioPinDigitalStateChanged(HIGH);
		
		dispatchTask.getValue().run();
		
		verify(collectorListener).onStateChanged(any(MeasurementValues.class));
		
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
	
//	private Matcher<MeasurementValues> countedTicks(int ticks){
//		return new CustomMatcher<MeasurementValues>(){};
//	}
}
