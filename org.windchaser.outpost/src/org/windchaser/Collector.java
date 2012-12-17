package org.windchaser;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.pi4j.io.gpio.PinState.HIGH;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

/**
 * 
 * @author Christian Schwarz
 * 
 */
public class Collector {

	private final EventBus eventBus = new EventBus(this.getClass().getName());

	/**
	 * Creates a new collector that listens on the given GPIO-Pin.
	 * 
	 * @param digitalInput
	 *            the DigitalInput to listen to
	 */
	public Collector(GpioPinDigitalInput digitalInput) {
		checkNotNull(digitalInput);
		digitalInput.addListener(new GpioPinListenerDigital() {

			@Override
			public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent evt) {
				if (evt.getState() == HIGH)
					postStateChanged();
			}
		});
	}

	/**
	 * Will be invoked when the state changed from LOW to HIGH
	 */
	private void postStateChanged() {
		eventBus.post(new MeasurementEvent(this, 0, 0));
	}

	/**
	 * Registers the given listener, to receive event notifications when a
	 * change from LOW to HIGH is detected on the digital input. The
	 * <code>listener</code>-Object must have a method with one argument of the
	 * type {@link MeasurementEvent} and the annotation {@code @Subscribe},
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
}