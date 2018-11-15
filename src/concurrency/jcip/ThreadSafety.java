package concurrency.jcip;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.NotThreadSafe;
import net.jcip.annotations.ThreadSafe;

public class ThreadSafety {


}


@NotThreadSafe
class MutableInteger {

  private int value;

  // The getter and setter methods are not thread safe
  // One thread can call getValue and another can call setValue
  public int getValue() {
    return value;
  }

  public void setValue(int value) {
    this.value = value;
  }

}


@ThreadSafe
class SynchronizedInteger {

  @GuardedBy("this")
  private int value;

  public synchronized int getValue() {
    return value;
  }

  public synchronized void setValue(int value) {
    this.value = value;
  }

}
