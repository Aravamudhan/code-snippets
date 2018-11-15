package concurrency.jcip;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author amudhan
 *
 */
public class CountDownLatchTest {

  public static void main(String[] args) {
    long elapsedTime = getTotalRunningTime(5000);
    System.out.println(
        "Total time for the threads to complete : " + TimeUnit.NANOSECONDS.toSeconds(elapsedTime));

  }

  /**
   * This methods creates the given number of threads and returns the time it takes for all the
   * threads to complete the task
   * 
   * @param nThreads
   * @return
   */
  private static long getTotalRunningTime(int nThreads) {
    final CountDownLatch startGate = new CountDownLatch(1);
    final CountDownLatch endGate = new CountDownLatch(nThreads);

    Runnable r = () -> {
      try {
        // The run method starts
        // As soon as it starts it wait on the startGate variable
        // Once the startGate becomes 0, it continues the execution of the method
        startGate.await();
        System.out.println(Thread.currentThread().getName() + " is running............");
        long count = 0;
        for (long j = 0; j < 1000000; j++) {
          count = count + j;
        }
        // Once the thread completes every thing else, it simply reduces the endGate
        endGate.countDown();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    };
    for (int i = 0; i < nThreads; i++) {
      Thread t = new Thread(r);
      t.start();
    }
    long start = System.nanoTime();
    // Once the threads are created and started startGate is counted down.
    // Now the threads that are looking at this variable to reach 0, will be notified of this change
    // and they continue executing the instructions that come after the CounDownLatch#await method
    // on the startGate variable
    startGate.countDown();
    try {
      // Now the main thread awaits on the endGate
      // This variable is reduced by the threads that are created
      endGate.await();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    long end = System.nanoTime();
    return end - start;
  }


}
