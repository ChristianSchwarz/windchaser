package org.windchaser;

import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.zip.GZIPOutputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;

public class Application {

	private Object listener = new Object() {
		@Subscribe
		public void stateChanged(MeasurementValues evt) {
			int[] diffs = evt.getDiffs();
			if (diffs.length == 0)
				return;

			DefaultHttpClient httpclient = new DefaultHttpClient();

			HttpPost httpPost = new HttpPost("http://192.168.0.199:8888/windchaser/outpost");

			try {

				byte[] bytes = toByteArray(diffs);
				System.out.println(diffs.length*4+"/"+ bytes.length);
				ByteArrayEntity byteArrayEntity = new ByteArrayEntity(bytes);

				httpPost.setEntity(byteArrayEntity);
				HttpResponse response;

				response = httpclient.execute(httpPost);

				System.out.println(response.getStatusLine());
				HttpEntity entity = response.getEntity();
				// do something useful with the response body
				// and ensure it is fully consumed
				EntityUtils.consume(entity);
			} catch (IOException e) {

				e.printStackTrace();
			} finally {
				httpPost.releaseConnection();
			}
		}

		private byte[] toByteArray(int[] diffs) throws IOException {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			GZIPOutputStream gzip = new GZIPOutputStream(bos);
		
			DataOutputStream dos = new DataOutputStream(gzip);

			for (int diff : diffs)
				dos.writeInt(diff);
			
			dos.flush();
			gzip.finish();
			return bos.toByteArray();

		}

	};

	public void start(String[] args) {

		ScheduledExecutorService executor = newSingleThreadScheduledExecutor(new ThreadFactoryBuilder().setPriority(Thread.MAX_PRIORITY).build());

		// create gpio controller
		final GpioController gpio = GpioFactory.getInstance();

		// provision gpio pin #02 as an input pin with its internal pull down
		// resistor enabled
		final GpioPinDigitalInput digitalInput = gpio.provisionDigitalInputPin(RaspiPin.GPIO_00, PinPullResistance.PULL_DOWN);

		Collector collector = new Collector(digitalInput, executor);
		collector.addListener(listener);
	}

}
