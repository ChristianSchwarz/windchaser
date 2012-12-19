package org.windchaser;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.suppliers.TestedOn;
import org.mockito.Matchers;

/**
 * Tests the class {@link MeasurementValues}
 * 
 * @author Christian Schwarz
 * 
 */
public class TestMeasurementValues {

	private static final int DURATION = 1000;
	private static final int TICKS = 12;
	private MeasurementValues measurementValues;

	@Before
	public void setUp() throws Exception {
		measurementValues = new MeasurementValues(TICKS, DURATION);
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
		new MeasurementValues(50, 1);
	}

	/**
	 * Checks if the duration passed to the constructor is returned.
	 */
	@Test
	public void getMeasurementDurationMs() {
		assertThat(measurementValues.getMeasurementDurationMs(),is(DURATION));
	}
	
	/**
	 * Checks if the tick count passed to the constructor is returned.
	 */
	@Test
	public void getTicks() {
		assertThat(measurementValues.getTickCount(),is(TICKS));
	}
}
