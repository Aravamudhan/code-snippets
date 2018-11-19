package concurrency.jcip.fundamental;

/**
 * Demoing monitor pattern using an intrinsic lock
 * 
 * @author amudhan
 *
 */
public class MonitorPattern {

  private static CounterMonitorPattern counter = new CounterMonitorPattern();

  public static void main(String[] args) throws InterruptedException {
    testMonitorPattern();
  }

  private static void testMonitorPattern() throws InterruptedException {
    RunnableMonitorPattern r = new RunnableMonitorPattern();
    // Create an array of 10 threads
    Thread threads[] = new Thread[10];
    for (int i = 0; i < 10; i++) {
      Thread t = new Thread(r, "T" + (i + 1));
      // Start the threads
      t.start();
      threads[i] = t;
    }
    // Calling the join method from the main thread
    // The main thread will wait until a thread on which join method was called
    for (int i = 0; i < 10; i++) {
      threads[i].join();
    }
    // Unless join method is called, this print statement would be executed even before all threads
    // complete their execution
    System.out.println("Finally :" + counter.getCount());
  }

  private static class RunnableMonitorPattern implements Runnable {

    @Override
    public void run() {
      for (int i = 0; i < 3; i++) {
        counter.incr();
        System.out.println(
            "[" + Thread.currentThread().getName() + " Thread] Count:[" + counter.getCount() + "]");
        try {
          Thread.sleep(1);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
  }

  private static class CounterMonitorPattern {

    private int count = 0;

    public void incr() {
      // Using synchronized on the this. This is same as mentioning the synchronized outside the
      // method like in the getCount method
      synchronized (this) {
        ++count;
      }
    }

    // The incr method and getCount method both follow the same pattern
    // This type of synchronization is same as using synchronized on the 'this'
    public synchronized int getCount() {
      return count;
    }
  }

}


