package org.windchaser;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.eventbus.EventBus;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import java.time.Duration;
import java.util.concurrent.ScheduledExecutorService;

@SuppressWarnings("all")
public class Collector2 {
  private EventBus eventBus;
  
  private Duration interval = Collector2.DEFAULT_INTERVAL;
  
  private final static Duration DEFAULT_INTERVAL = Duration.ofSeconds(5);
  
  /**
   * Handles changes to state {@link PinState#HIGH}
   */
  private final GpioPinListenerDigital gpioPinListenerDigital = new GpioPinListenerDigital() {
    public void handleGpioPinDigitalStateChangeEvent(final GpioPinDigitalStateChangeEvent evt) {
      PinState _state = evt.getState();
      boolean _equals = Objects.equal(_state, PinState.LOW);
      if (_equals) {
        return;
      }
    }
  };
  
  private final ScheduledExecutorService executor;
  
  /**
   * Creates a new collector that listens on the given GPIO-Pin.
   * 
   * @param digitalInput
   *            the DigitalInput to listen to
   * @param executor
   *            used to time the {@link Measurement} generation
   */
  public Collector2(final GpioPinDigitalInput digitalInput, final ScheduledExecutorService executor) {
    throw new Error("Unresolved compilation problems:"
      + "\nAmbiguous binary operation.\nThe operator declarations\n\toperator_plus(Object, String) in ObjectExtensions and\n\toperator_plus(String, Object) in StringExtensions\nboth match.");
  }
  
  /**
   * Registers the given listener, to receive event notifications when a
   * change from LOW to HIGH is detected on the digital input. The
   * <code>listener</code>-Object must have a method with one argument of the
   * type {@link Measurement} and the annotation {@code @Subscribe},
   * e.g.:
   * 
   * <pre>
   * {@code @Subscribe}
   * public void stateChanged(MeasurementValues values){
   *   ...
   * }
   * </pre>
   * 
   * 
   * @param listener
   */
  public void addListener(final Object listener) {
    Preconditions.<Object>checkNotNull(listener);
    this.eventBus.register(listener);
  }
  
  /**
   * Set the interval {@link Measurement} should be created and send to
   * listeners.
   * <p/>
   * Note: No {@link Measurement} will be created if no tick was
   * recognized! during the interval.
   * 
   * @param interval
   *            the interval to set in milliseconds
   */
  public void setCollectorInterval(final Duration interval) {
    boolean _isNegative = interval.isNegative();
    boolean _not = (!_isNegative);
    String _plus = ("Parameter >interval< must be a positive duration! Got:" + interval);
    Preconditions.checkArgument(_not, _plus);
    Duration _elvis = null;
    if (interval != null) {
      _elvis = interval;
    } else {
      _elvis = Collector2.DEFAULT_INTERVAL;
    }
    this.interval = _elvis;
  }
}
