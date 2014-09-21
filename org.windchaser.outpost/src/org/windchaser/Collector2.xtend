package org.windchaser

import com.google.common.eventbus.AsyncEventBus
import com.google.common.eventbus.EventBus
import com.pi4j.io.gpio.GpioPinDigitalInput
import com.pi4j.io.gpio.PinState
import com.pi4j.io.gpio.event.GpioPinListenerDigital
import java.time.Duration
import java.util.concurrent.ScheduledExecutorService

import static com.google.common.base.Preconditions.*
import static java.time.Duration.*

class Collector2 {
	
	EventBus eventBus
	
	Duration interval = DEFAULT_INTERVAL
	
	final static Duration DEFAULT_INTERVAL = ofSeconds(5)
	/**
	 * Handles changes to state {@link PinState#HIGH}
	 */
	 final GpioPinListenerDigital gpioPinListenerDigital = [evt|
			if (evt.state==PinState.LOW)
				return
	]
	
	final ScheduledExecutorService executor
	
	
	/**
	 * Creates a new collector that listens on the given GPIO-Pin.
	 * 
	 * @param digitalInput
	 *            the DigitalInput to listen to
	 * @param executor
	 *            used to time the {@link Measurement} generation
	 */
	  new(GpioPinDigitalInput digitalInput, ScheduledExecutorService executor) {
		checkNotNull(digitalInput, "Parameter >digitalInput< must be null!");
		checkNotNull(executor, "Parameter >executor< must be null!");
		eventBus = new AsyncEventBus("Collector:" + digitalInput.name, executor);
		this.executor = executor;


		digitalInput.addListener(gpioPinListenerDigital);
	
	}
	
	/**
	 * Registers the given listener, to receive event notifications when a
	 * change from LOW to HIGH is detected on the digital input. The
	 * <code>listener</code>-Object must have a method with one argument of the
	 * type {@link Measurement} and the annotation {@code @Subscribe},
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
	def void addListener(Object listener) {
		checkNotNull(listener);
		eventBus.register(listener);
	}

	/**
	 * Set the interval {@link Measurement} should be created and send to
	 * listeners.
	 * <p/>
	 * Note: No {@link Measurement} will be created if no tick was
	 * recognized! during the interval.
	 * 
	 * @param interval
	 *            the interval to set in milliseconds
	 */
	def void setCollectorInterval(Duration interval) {
		checkArgument(!interval.negative, "Parameter >interval< must be a positive duration! Got:" + interval);
		this.interval=interval?:DEFAULT_INTERVAL
	}}