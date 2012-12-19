package org.windchaser;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * A {@link MeasurementValues} contains a number of recorded ticks (state
 * changes) permeasurement duration. For every half turn of the anemometer, one
 * tick is generated. By the number of ticks per unit of time, the wind speed
 * can derived.
 * 
 * @author Christian Schwarz
 * 
 */
public class MeasurementValues  {

	private final int ticksPerInterval;
	private final int durationMs;
	

	/**
	 * Creates a new event representing one measurement interval.
	 * 
	 * @param source
	 *            the {@link Collector} that fires this event, not <code>null</code>
	 * @param tickCount
	 *            number of ticks that were detected during the measurement, must be >=0
	 * @param durationMs
	 *            the duration of the measurement must be >=0
	 */
	public MeasurementValues(int tickCount, int durationMs) {
		checkArgument(tickCount>=0,"Parameter >tickCount< must be positiv! Got:"+tickCount);
		checkArgument(durationMs>0,"Parameter >durationMs< must greater than 0! Got:"+durationMs);
		
		this.ticksPerInterval = tickCount;
		this.durationMs = durationMs;
		
	}

	

	/**
	 * Returns the number of ticks detected during the measurement. One tick represents a
	 * half turn of the anemometer. By the number of ticks per time unit,
	 * the wind speed can derived.
	 * 
	 * @return number of ticks >=0
	 */
	public int getTickCount() {
		return ticksPerInterval;
	}

	/**
	 * Returns the duration of the measurement, in milliseconds.
	 * @return milliseconds >=0
	 */ 
	public int getMeasurementDurationMs() {
		return durationMs;
	}
	
	
}