package concurrency.jcip.fundamental;

/**
 * Here the ready and number variables are shared among the ReaderThread and the main thread This
 * sometimes may causes subtle visibility related bugs. The reader thread might view the ready value
 * and miss the number value
 * 
 * @author amudhan
 *
 */
public class NoVisibility {
  private static boolean ready = false;
  private static int number = 0;

  public static void main(String[] args) throws InterruptedException {
    try {
      new ReaderThread().start();
    } catch (Exception e) {
      e.printStackTrace();
    }
    Thread.sleep(1000);
    // Even in this simple case, the order of assigning a value to the number and ready variable
    // might be reordered by the compiler. This seems like a simple enough program, but with out
    // synchronization there is no guarantee that number and ready variables will be read in this
    // order. The reader thread might view a stale data of the number when it sees it even after
    // the read becomes true. A thread can see upto date value of one variable and stale value
    // of another
    number = 42;
    ready = true;
  }

  private static class ReaderThread extends Thread {
    public void run() {
      while (!ready) {
        System.out.println("The number is not ready. Waiting for it to become ready..........");
        Thread.yield();
      }
      System.out.println(number);
    }
  }

}
