package concurrency.jcip.fundamental;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * To demonstrate how, even when using thread safe state like AtomicInteger, an unsafe thread class
 * may cause inconsistent state. To avoid this, the class should be composed with thread safety in
 * mind
 * 
 * @author amudhan
 *
 */
public class NumberRange {

  private final AtomicInteger lower = new AtomicInteger(0);
  private final AtomicInteger upper = new AtomicInteger(0);

  public void setLower(int i) {
    // Warning -- unsafe check-then-act
    // Two threads may execute this at the same time with different values and then may set their
    // values.
    if (i > upper.get())
      throw new IllegalArgumentException("can't set lower to " + i + " > upper");
    // Simply adding a forced sleep time to demonstrate how this can cause an inconsistent
    // lower-upper bound values
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    lower.set(i);
  }

  // By synchronizing the setLower using the intrinsic lock, we make sure the operations become
  // thread safe
  public synchronized void safeSetLower(int i) {
    setLower(i);
  }

  public void setUpper(int i) {
    // Warning -- unsafe check-then-act
    if (i < lower.get())
      throw new IllegalArgumentException("can't set upper to " + i + " < " + lower);
    upper.set(i);
  }

  public synchronized void safeSetUpper(int i) {
    setUpper(i);
  }

  public boolean isInRange(int i) {
    return (i >= lower.get() && i <= upper.get());
  }

  public AtomicInteger getLower() {
    return lower;
  }

  public AtomicInteger getUpper() {
    return upper;
  }

  @Override
  public String toString() {
    return "UnsafeNumberRange [lower=" + lower + ", upper=" + upper + "]";
  }

  public static void main(String[] args) throws InterruptedException {
    System.out.println("Initial value :" + new NumberRange());
    unsafeMethods();
    safeMethods();
  }

  private static void unsafeMethods() throws InterruptedException {
    System.out.println("Calling the non synchronized methods.....");
    NumberRange range = new NumberRange();
    range.setLower(0);
    range.setUpper(10);

    Runnable r1 = () -> {
      range.setLower(5);
    };
    Runnable r2 = () -> {
      range.setUpper(4);
    };

    Thread t1 = new Thread(r1, "T1");
    Thread t2 = new Thread(r2, "T2");
    t1.start();
    t2.start();
    t1.join();
    t2.join();
    System.out.println(range);

  }

  private static void safeMethods() throws InterruptedException {
    System.out.println("Calling the synchronized methods.....");
    NumberRange range = new NumberRange();
    range.setLower(0);
    range.setUpper(10);
    Thread t3 = new Thread(() -> {
      range.safeSetLower(5);
    }, "T3");

    Thread t4 = new Thread(() -> {
      range.safeSetUpper(4);
    }, "T4");
    t3.start();
    t4.start();
    t3.join();
    t4.join();
    System.out.println(range);
  }

}
