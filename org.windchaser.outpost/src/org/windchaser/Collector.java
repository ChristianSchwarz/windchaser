package org.windchaser;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.pi4j.io.gpio.PinState.HIGH;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
	private static final int SECONDS_10 = 10000;

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

		@Override
		public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent evt) {
			if (evt.getState() == HIGH)
				postStateChanged();
		}
	};

	private ScheduledExecutorService executor;
	/**
	 * The interval where ticks are counted, in milliseconds<br>
	 * Default: 10seconds
	 * */
	private long measurmentIntervall=SECONDS_10;

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
		eventBus = new AsyncEventBus("Collector:"+digitalInput.getName(), executor);
		this.executor = executor;

		digitalInput.addListener(gpioPinListenerDigital);
		startCollecting();
	}

	private void startCollecting() {
		Runnable command = new Runnable() {
			
			@Override
			public void run() {
				postMeasurementValues();
			}
		};
		executor.scheduleAtFixedRate(command, measurmentIntervall, measurmentIntervall, MILLISECONDS);

	}

	protected void postMeasurementValues() {
		eventBus.post(new MeasurementValues(0, 0));
	}

	/**
	 * Will be invoked when the state changed from LOW to HIGH
	 */
	private void postStateChanged() {

		// 
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
	 * public void stateChanged(PinStateChangedEvent evt){
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