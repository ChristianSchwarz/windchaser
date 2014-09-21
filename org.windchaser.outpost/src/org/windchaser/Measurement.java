package org.windchaser;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 
 * 
 * @author Christian Schwarz
 * 
 */
public class Measurement {

	private final float airSpeedInKnots;
	

	/**
	 * Creates a new event representing one measurement interval.
	 * 
	
	 */
	public Measurement(float airSpeedInKnots) {
		this.airSpeedInKnots = airSpeedInKnots;
		
	}

	public float getAirSpeedInKnots() {
		return airSpeedInKnots;
	}

}