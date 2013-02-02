package org.windchaser;

import static java.lang.Runtime.getRuntime;
import static java.net.NetworkInterface.getNetworkInterfaces;
import static java.util.Collections.list;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;

public class Modem {

	public void connect() {
		try {
			Process process = getRuntime().exec("sudo ./sakis3g connect ---console --pppd APN=internet USBDRIVER=option MODEM=19d2:0016");

			InputStream is = process.getInputStream();
			InputStream err = process.getErrorStream();
			print(is);
			print(err);

			process.waitFor();
			for (NetworkInterface ni:list(getNetworkInterfaces())){
				if (!ni.isPointToPoint())
					continue;
				if(!ni.isUp())
					continue;
				
				for (InetAddress addr:list(ni.getInetAddresses())){
					System.out.println(addr.getHostAddress());
				}
			}
				
			
		} catch (IOException | InterruptedException e) {
			System.err.println("connect" + e);
		}
		
	}

	private void print(InputStream is) {
		final BufferedReader reader = new BufferedReader(new InputStreamReader(is));

		Thread t = new Thread() {
			@Override
			public void run() {
				String readLine = null;
				while(true) 
					try {
						readLine = reader.readLine();
						if (readLine==null)
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
