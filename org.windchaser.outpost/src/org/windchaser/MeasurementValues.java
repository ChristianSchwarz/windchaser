package org.windchaser;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 
 * 
 * @author Christian Schwarz
 * 
 */
public class MeasurementValues {

	private final long durationMs;
	private final int[] diffs;

	/**
	 * Creates a new event representing one measurement interval.
	 * 
	 * 
	 * @param durationMs
	 *            the duration of the measurement must be >=0
	 */
	public MeasurementValues(long durationMs, int[] diffs) {
		checkNotNull(diffs);
		checkArgument(durationMs > 0, "Parameter >durationMs< must greater than 0! Got:" + durationMs);
		this.diffs = diffs.clone();
		this.durationMs = durationMs;

	}

	public int[] getDiffs() {
		return diffs;
	}

}