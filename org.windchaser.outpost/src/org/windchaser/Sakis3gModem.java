package org.windchaser;

import static com.google.common.io.Closeables.closeQuietly;
import static java.lang.Runtime.getRuntime;
import static java.net.NetworkInterface.getNetworkInterfaces;
import static java.util.Collections.list;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.net.InetAddress;
import java.net.NetworkInterface;

public class Sakis3gModem implements Modem {

	@Override
	public void connect() throws IOException {

		connectUsbModem("19d2:0016");
		isConnected();

		for (NetworkInterface ni : list(getNetworkInterfaces())) {
			if (!ni.isPointToPoint())
				continue;
			if (!ni.isUp())
				continue;

			for (InetAddress addr : list(ni.getInetAddresses())) {
				System.out.println(addr.getHostAddress());
			}
		}

	}

	private boolean isConnected() throws IOException {

		Process process = getRuntime().exec("sudo ./sakis3g status");

		InputStream is = process.getInputStream();
		InputStream err = process.getErrorStream();
		print(is);
		print(err);

		try {
			return process.waitFor() == 0;
		} catch (InterruptedException e) {
			throw new InterruptedIOException();
		} finally {
			closeQuietly(is);
			closeQuietly(err);
		}
	}

	private void connectUsbModem(String modem) throws IOException {
		Process process = getRuntime().exec(
				"sudo ./sakis3g connect ---console --pppd APN=internet USBDRIVER=option MODEM="
						+ modem);

		InputStream is = process.getInputStream();
		InputStream err = process.getErrorStream();
		print(is);
		print(err);

		try {
			process.waitFor();
		} catch (InterruptedException e) {
			throw new InterruptedIOException();
		} finally {
			closeQuietly(is);
			closeQuietly(err);
		}
	}

	private void print(InputStream is) {
		final BufferedReader reader = new BufferedReader(new InputStreamReader(
				is));

		Thread t = new Thread() {
			@Override
			public void run() {
				String readLine = null;
				while (true)
					try {
						readLine = reader.readLine();
						if (readLine == null)
							return;
						System.out.println(readLine);
					} catch (IOException e) {
						e.printStackTrace();
						return;
					}

			}
		};
		t.setDaemon(true);
		t.start();

	}
}
