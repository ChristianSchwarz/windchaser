package org.windchaser

import com.google.common.base.Predicate
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.NetworkInterface
import java.util.Enumeration

import static java.lang.Runtime.*
import static java.net.NetworkInterface.*

import static extension com.google.common.collect.Iterators.*

class Sakis3gModem2 implements Modem {

	override connect() {
		try {
			val it = runtime.exec(
				"sudo ./sakis3g connect ---console --pppd APN=internet USBDRIVER=option MODEM=19d2:0016")

			inputStream.print
			errorStream.print
			waitFor
		} catch (Exception e) {
			println("connect" + e)
		}

		networkInterfaces.printAll[up && pointToPoint]
	}

	def static private printAll(Enumeration<NetworkInterface> networkInterfaces, Predicate<NetworkInterface> filter) {
		val iterable = forEnumeration(networkInterfaces)
		iterable.filter(filter).forEach[it.println]
	}

	def static private print(InputStream is) {

		val it = new BufferedReader(new InputStreamReader(is));

		new Thread(printLinesOf(it)) => [
			
			name = "Printer of:"+is
			daemon = true
			start
		];
		

	}

	def static private Runnable printLinesOf(BufferedReader it) {
		[ |
			try
				while (true)
					readLine?.println
			catch (IOException e)
				e.printStackTrace
		]
	}

	def  static private  println(Object string) {
		System.out.println(string)
	}

}
