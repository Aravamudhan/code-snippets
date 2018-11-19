package concurrency.jcip.fundamental;

/**
 * To ensure thread safety without synchronization, one must not share data. It is called
 * confinement. ThreadLocal object aides in this process.
 * 
 * @author amudhan
 *
 */
public class ConfinementByThreadLocal {

  private static ThreadLocal<Integer> threadLocalCounter = new ThreadLocal<Integer>() {
    public Integer initialValue() {
      return new Integer(0);
    }
  };

  public static void main(String[] args) {
    CounterThread ct1 = new CounterThread("CT1");
    ct1.run();
    CounterThread ct2 = new CounterThread("CT2");
    ct2.run();
  }

  static class CounterThread implements Runnable {
    String name;

    CounterThread(String name) {
      this.name = name;
    }

    @Override
    public void run() {
      for (int i = 0; i < 10; i++) {
        Integer current = threadLocalCounter.get();
        System.out.println(name + ":" + current);
        current = current.intValue() + 1;
        try {
          Thread.yield();
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        threadLocalCounter.set(current);
      }
    }

  }

}

