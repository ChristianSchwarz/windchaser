package org.windchaser;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.primitives.Ints.toArray;
import static com.pi4j.io.gpio.PinState.HIGH;
import static java.lang.System.currentTimeMillis;
import static java.lang.System.nanoTime;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

/**
 * 
 * @author Christian Schwarz
 * 
 */
public class Collector {
	private static final int SECONDS_2 = 2000;

	/**
	 * Dispatches {@link MeasurementValues} to registered listeners
	 * 
	 * @see #addListener(Object)
	 */
	private final EventBus eventBus;

	/**
	 * Handles changes to state {@link PinState#HIGH}
	 */
	private final GpioPinListenerDigital gpioPinListenerDigital = new GpioPinListenerDigital() {
		private long t ;
		private boolean logDiff=false;
		@Override
		public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent evt) {
			if (evt.getState()==HIGH)
				logDiff=true;
			
			long currentTime = nanoTime()/10000;
			int delta = (int)(currentTime - t);
			
			if (t==0)
				delta=0;
			
			System.out.println(evt.getState() + " " + delta);

			t = currentTime;
			
			if(logDiff)
				changes.add(delta);
		}
	};

	private ScheduledExecutorService executor;
	/**
	 * The interval where ticks are counted, in milliseconds<br>
	 * Default: 2seconds
	 * */
	private long measurmentIntervall = SECONDS_2;


	/** */
	private List<Integer> changes = new ArrayList<>(400);

	private long measurementStart;

	/**
	 * Creates a new collector that listens on the given GPIO-Pin.
	 * 
	 * @param digitalInput
	 *            the DigitalInput to listen to
	 * @param executor
	 *            used to time the {@link MeasurementValues} generation
	 */
	public Collector(GpioPinDigitalInput digitalInput, ScheduledExecutorService executor) {
		checkNotNull(digitalInput, "Parameter >digitalInput< must be null!");
		checkNotNull(executor, "Parameter >executor< must be null!");
		eventBus = new AsyncEventBus("Collector:" + digitalInput.getName(), executor);
		this.executor = executor;

		digitalInput.addListener(gpioPinListenerDigital);
		startCollecting();
	}

	private void startCollecting() {
		measurementStart = currentTimeMillis();
		Runnable command = new Runnable() {

			@Override
			public void run() {
				postMeasurementValues();
			}
		};
		executor.scheduleAtFixedRate(command, measurmentIntervall, measurmentIntervall, MILLISECONDS);

	}

	protected void postMeasurementValues() {
		final List<Integer> changes= this.changes;
		this.changes = new ArrayList<>(500);
		
		
		long currentTimeMillis = currentTimeMillis();
		long durationMillis = currentTimeMillis - measurementStart;
		

		System.out.println(durationMillis + "ms ->" + changes.size());

		eventBus.post(new MeasurementValues(measurementStart, toArray(changes)));
		measurementStart = currentTimeMillis;
		
	}

	/**
	 * Registers the given listener, to receive event notifications when a
	 * change from LOW to HIGH is detected on the digital input. The
	 * <code>listener</code>-Object must have a method with one argument of the
	 * type {@link MeasurementValues} and the annotation {@code @Subscribe},
	 * e.g.:
	 * 
	 * <pre>
	 * {@code @Subscribe}
	 * public void stateChanged(MeasurementValues values){
	 *   ...
	 * }
	 * </pre>
	 * 
	 * 
	 * @param listener
	 */
	public void addListener(Object listener) {
		checkNotNull(listener);
		eventBus.register(listener);
	}

	/**
	 * Set the interval {@link MeasurementValues} should be created and send to
	 * listeners.
	 * <p/>
	 * Note: No {@link MeasurementValues} will be created if no tick was
	 * recognized! during the interval.
	 * 
	 * @param millis
	 *            the interval to set in milliseconds
	 */
	public void setCollectorInterval(int millis) {
		checkArgument(millis >= 0, "Parameter >millis< must be positive! Got:" + millis);

	}
}