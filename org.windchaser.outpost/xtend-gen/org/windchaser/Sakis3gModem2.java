package org.windchaser;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.UnmodifiableIterator;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.NetworkInterface;
import java.util.Enumeration;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.windchaser.Modem;

@SuppressWarnings("all")
public class Sakis3gModem2 implements Modem {
  public void connect() {
    try {
      try {
        Runtime _runtime = Runtime.getRuntime();
        final Process it = _runtime.exec(
          "sudo ./sakis3g connect ---console --pppd APN=internet USBDRIVER=option MODEM=19d2:0016");
        InputStream _inputStream = it.getInputStream();
        Sakis3gModem2.print(_inputStream);
        InputStream _errorStream = it.getErrorStream();
        Sakis3gModem2.print(_errorStream);
        it.waitFor();
      } catch (final Throwable _t) {
        if (_t instanceof Exception) {
          final Exception e = (Exception)_t;
          String _plus = ("connect" + e);
          Sakis3gModem2.println(_plus);
        } else {
          throw Exceptions.sneakyThrow(_t);
        }
      }
      Enumeration<NetworkInterface> _networkInterfaces = NetworkInterface.getNetworkInterfaces();
      final Predicate<NetworkInterface> _function = new Predicate<NetworkInterface>() {
        public boolean apply(final NetworkInterface it) {
          try {
            boolean _and = false;
            boolean _isUp = it.isUp();
            if (!_isUp) {
              _and = false;
            } else {
              boolean _isPointToPoint = it.isPointToPoint();
              _and = _isPointToPoint;
            }
            return _and;
          } catch (Throwable _e) {
            throw Exceptions.sneakyThrow(_e);
          }
        }
      };
      Sakis3gModem2.printAll(_networkInterfaces, _function);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  private static void printAll(final Enumeration<NetworkInterface> networkInterfaces, final Predicate<NetworkInterface> filter) {
    final UnmodifiableIterator<NetworkInterface> iterable = Iterators.<NetworkInterface>forEnumeration(networkInterfaces);
    UnmodifiableIterator<NetworkInterface> _filter = Iterators.<NetworkInterface>filter(iterable, filter);
    final Procedure1<NetworkInterface> _function = new Procedure1<NetworkInterface>() {
      public void apply(final NetworkInterface it) {
        Sakis3gModem2.println(it);
      }
    };
    IteratorExtensions.<NetworkInterface>forEach(_filter, _function);
  }
  
  private static Thread print(final InputStream is) {
    Thread _xblockexpression = null;
    {
      InputStreamReader _inputStreamReader = new InputStreamReader(is);
      final BufferedReader it = new BufferedReader(_inputStreamReader);
      Runnable _printLinesOf = Sakis3gModem2.printLinesOf(it);
      Thread _thread = new Thread(_printLinesOf);
      final Procedure1<Thread> _function = new Procedure1<Thread>() {
        public void apply(final Thread it) {
          String _plus = ("Printer of:" + is);
          it.setName(_plus);
          it.setDaemon(true);
          it.start();
        }
      };
      _xblockexpression = ObjectExtensions.<Thread>operator_doubleArrow(_thread, _function);
    }
    return _xblockexpression;
  }
  
  private static Runnable printLinesOf(final BufferedReader it) {
    final Runnable _function = new Runnable() {
      public void run() {
        try {
          boolean _while = true;
          while (_while) {
            String _readLine = it.readLine();
            if (_readLine!=null) {
              Sakis3gModem2.println(_readLine);
            }
            _while = true;
          }
        } catch (final Throwable _t) {
          if (_t instanceof IOException) {
            final IOException e = (IOException)_t;
            e.printStackTrace();
          } else {
            throw Exceptions.sneakyThrow(_t);
          }
        }
      }
    };
    return _function;
  }
  
  private static void println(final Object string) {
    System.out.println(string);
  }
}
