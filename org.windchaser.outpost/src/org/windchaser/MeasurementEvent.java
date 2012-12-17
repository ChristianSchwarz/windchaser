package org.windchaser;

import java.util.EventObject;

/**
 * A {@link MeasurementEvent} contains a number of recorded ticks (state
 * changes) permeasurement duration. For every half turn of the anemometer, one
 * tick is generated. From the number of ticks per unit of time, the wind speed
 * can be derived.
 * 
 * @author Christian Schwarz
 * 
 */
public class MeasurementEvent extends EventObject {

	/**	 */
	private static final long serialVersionUID = 1L;
	private final int ticksPerInterval;
	private final int durationMs;

	/**
	 * Creates a new event representing one measurement interval.
	 * 
	 * @param source
	 *            the {@link Collector} that fires this event, not <code>null</code>
	 * @param ticksPerInterval
	 *            number of ticks that were detected during the measurement, must be >=0
	 * @param durationMs
	 *            the duration of the measurement must be >=0
	 */
	public MeasurementEvent(Collector source, int ticksPerInterval, int durationMs) {
		super(source);
		this.ticksPerInterval = ticksPerInterval;
		this.durationMs = durationMs;
		
	}

	@Override
	public Collector getSource() {
		return (Collector) super.getSource();
	}

	/**
	 * Returns the number of ticks per  measurement. One tick represents a
	 * half turn of the anemometer. From the number of ticks per time unit,
	 * the wind speed can be derived.
	 * 
	 * @return number of ticks >=0
	 */
	public int getTicksPerInterval() {
		return ticksPerInterval;
	}

	/**
	 * Returns the duration of the measurement, in milliseconds.
	 * @return milliseconds >=0
	 */ 
	public int getDurationMs() {
		return durationMs;
	}
	
	
}