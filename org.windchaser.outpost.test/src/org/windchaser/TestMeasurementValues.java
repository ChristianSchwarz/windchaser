package org.windchaser;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests the class {@link MeasurementValues}
 * 
 * @author Christian Schwarz
 * 
 */
public class TestMeasurementValues {

	private MeasurementValues measurementValues;

	@Before
	public void setUp() throws Exception {

	}

	/**
	 * Checks if a {@link IllegalArgumentException} is thrown when a negative
	 * "tick" value was passed.
	 * 
	 * @throws Exception
	 */
	@Test(expected = IllegalArgumentException.class)
	public void new_negativeTicks() throws Exception {
		new MeasurementValues(-1, 50);
	}

	/**
	 * Checks if a {@link IllegalArgumentException} is thrown when a negative
	 * "duration" value was passed.
	 * 
	 * @throws Exception
	 */
	@Test(expected = IllegalArgumentException.class)
	public void new_negativeDuration() throws Exception {
		new MeasurementValues(-1, 50);
	}

	/**
	 * Checks if 0 is accepted as smallest tick count. In this case no exception
	 * should raise.
	 * 
	 * @throws Exception
	 */
	@Test
	public void new_withSmallestDuration() throws Exception {
		new MeasurementValues(0, 50);
	}

	/**
	 * Checks if 1 is accepted as smallest duration.
	 * 
	 * @throws Exception
	 */
	@Test
	public void new_withSmallestTickNumber() throws Exception {
		new MeasurementValues(50,1);
	}
}
