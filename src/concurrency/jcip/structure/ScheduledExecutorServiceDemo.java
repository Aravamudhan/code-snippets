package concurrency.jcip.structure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * A ScheduledExecutorService can schedule commands to run after a given delay or to execute
 * periodically.
 * 
 * @author amudhan
 *
 */
public class ScheduledExecutorServiceDemo {

  public static void main(String[] args) {

    List<Integer> numberList = Collections.synchronizedList(new ArrayList<>());

    // A task that calculates the average of all the numbers in the list and then clears the list
    // after that
    Runnable numberAverageTask = () -> {
      System.out.println("Current list :" + numberList);
      double average = numberList.stream().mapToDouble(val -> val).average().orElse(0.0);
      numberList.clear();
      System.out.println(
          Thread.currentThread().getName() + ": Average for the last 5 seconds : " + average);
    };

    /**
     * The corePoolSize is 0, meaning that the minimum number of threads to keep in the pool if they
     * are idle. In this case, we do not want to keep any threads if they are idle.
     */
    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(0);

    /**
     * Schedule the scheduledTask
     */
    ScheduledFuture<?> numberAverageFuture =
        scheduler.scheduleAtFixedRate(numberAverageTask, 0, 5, TimeUnit.SECONDS);

    Runnable produceRunnable = () -> {
      // Until the numberAverageFuture task is not cancelled, this task should keep running
      while (!numberAverageFuture.isCancelled()) {
        System.out.println(Thread.currentThread().getName() + " adding a value to the list.....");
        numberList.add((int) (Math.random() * (1000 - 1)) + 1);
        try {
          Thread.sleep(300);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    };

    Runnable numberAveragerCanceller = () -> numberAverageFuture.cancel(true);

    // Scheduling a thread that cancels numberAverageFuture after 15 seconds
    scheduler.schedule(numberAveragerCanceller, 15, TimeUnit.SECONDS);

    System.out.println("Starting produce value thread................");

    new Thread(produceRunnable, "ProduceValue1").start();
    new Thread(produceRunnable, "ProduceValue2").start();
    new Thread(produceRunnable, "ProduceValue3").start();

  }

}
