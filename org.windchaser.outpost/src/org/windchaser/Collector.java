package org.windchaser;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.pi4j.io.gpio.PinState.HIGH;

import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

/**
 * 
 * @author Christian Schwarz
 * 
 */
public class Collector {

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
				if (evt.getState()==HIGH)
					stateChanged();
			}
		});
	}

	/**
	 * Will be invoked when the state changed from LOW to HIGH
	 */
	private void stateChanged() {
		
	}
}