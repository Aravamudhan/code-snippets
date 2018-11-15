package concurrency.jcip;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * To create multiple threads each performing separate tasks and finally aggregating the result of
 * all the tasks or waiting for the completion of all the tasks<br/>
 * 1. Create a Callable. A Callable object is just like Runnable except that it can return a
 * value.<br/>
 * 2. Then create an ExecutorService object using the Executors factory.<br/>
 * 3. Now submit all the Callable objects that were created earlier to the ExecutorService object by
 * calling the submit with the Calllable objects. Now the tasks will start in separate thread. For
 * each submit, a new thread will be created.<br/>
 * 4. The submit method will return Future objects with the type being the return type of the
 * submitted Callable objects<br/>
 * 5. The get of the Future objects will wait for the tasks to complete.<br/>
 * 6. Finally aggreate or do whatever necessary with all the results<br/>
 * 7. The combination of ExecutorService, Callable<V> and the Future<V> classes provide a powerful
 * mechanism for background tasks, time consuming calculations, calling remote/web services and
 * working with large amount of data
 * 
 * @author amudhan
 *
 */
public class ExecutorService_Future_Callable_Example {

  public static void main(String[] args) {
    Counter counter = new Counter();
    // Create a callable object. This can be a separate class
    Callable<Integer> task = () -> {
      System.out.println("Calling the increment by :" + Thread.currentThread().getName() + ".....");
      counter.increment();
      return counter.getCount();
    };
    List<Future<Integer>> futureList = new ArrayList<>();
    // Create an ExecutorService
    ExecutorService executor = Executors.newFixedThreadPool(10);
    // Submit callables. In this case 10 separate threads will be created which will execute the
    // Callable object's call method. The submit method returns Future object
    for (int i = 0; i < 10; i++) {
      futureList.add(executor.submit(task));
    }
    System.out.println("Before the execution : " + counter.getCount());
    // Now loop over the future and wait for each of the threads spawned by the ExecutorService
    // object to complete it's task
    futureList.stream().forEach(future -> {
      try {
        System.out
            .println("Completed at :" + LocalDateTime.now() + " with the value : " + future.get());
      } catch (InterruptedException e) {
        e.printStackTrace();
      } catch (ExecutionException e) {
        e.printStackTrace();
      }
    });
    // Shut the executor down and it waits until all the tasks are completed. As soon as the
    // shutdown is called no more tasks are accepted
    executor.shutdown();
    System.out.println("After the execution : " + counter.getCount());
  }

}


class Counter {

  private int count = 0;

  public int getCount() {
    synchronized (this) {
      return count;
    }
  }

  public void increment() {
    synchronized (this) {
      System.out.println("Increment by : " + Thread.currentThread().getName());
      count++;
    }
  }
}
