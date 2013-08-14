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

	private final int[] diffs;

	/**
	 * Creates a new event representing one measurement interval.
	 * 
	
	 */
	public MeasurementValues(int[] diffs) {
		checkNotNull(diffs);
		this.diffs = diffs.clone();
	}

	public int[] getDiffs() {
		return diffs;
	}

}