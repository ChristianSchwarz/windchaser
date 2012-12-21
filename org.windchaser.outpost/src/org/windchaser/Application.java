package org.windchaser;

import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;

import java.util.concurrent.ScheduledExecutorService;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;

public class Application {

	public void start(String[] args) {
		
		ScheduledExecutorService executor = newSingleThreadScheduledExecutor();
		
		 // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();

        // provision gpio pin #02 as an input pin with its internal pull down resistor enabled
        final GpioPinDigitalInput digitalInput = gpio.provisionDigitalInputPin(RaspiPin.GPIO_00, PinPullResistance.PULL_DOWN);

		new Collector(digitalInput, executor);
	}

}
