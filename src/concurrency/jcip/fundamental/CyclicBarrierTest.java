package concurrency.jcip.fundamental;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Show an example of using the CyclicBarrier
 * 
 * @author amudhan
 *
 */
public class CyclicBarrierTest {

  public static void main(String[] args) {
    // Create an object for the outer class
    CyclicBarrierTest cbTest = new CyclicBarrierTest();
    // Use the outer class object to create an object for the inner class and
    CyclicBarrierContainer cbContainer = cbTest.new CyclicBarrierContainer();
    cbContainer.runSimulation(5, 3);
  }

  private class CyclicBarrierContainer {

    private CyclicBarrier cyclicBarrier;
    private List<List<Integer>> partialResults = Collections.synchronizedList(new ArrayList<>());
    private Random random = new Random();
    private int NUM_PARTIAL_RESULTS;
    private int NUM_WORKERS;

    public void runSimulation(int numWorkers, int numberOfPartialResults) {
      NUM_PARTIAL_RESULTS = numberOfPartialResults;
      NUM_WORKERS = numWorkers;
      cyclicBarrier = new CyclicBarrier(NUM_WORKERS, new AggregatorThread());

      System.out.println("Spawning " + NUM_WORKERS + " worker threads to compute "
          + NUM_PARTIAL_RESULTS + " partial results each");

      // Creating worker threads
      for (int i = 0; i < NUM_WORKERS; i++) {
        Thread worker = new Thread(new NumberCruncherThread());
        worker.setName("Thread " + i);
        worker.start();
      }
    }

    /**
     * This is the worker thread
     * 
     * @author amudhan
     *
     */
    private class NumberCruncherThread implements Runnable {
      @Override
      public void run() {
        String thisThreadName = Thread.currentThread().getName();
        List<Integer> partialResult = new ArrayList<>();

        // Crunch some numbers and store the partial result
        for (int i = 0; i < NUM_PARTIAL_RESULTS; i++) {
          Integer num = random.nextInt(10);
          System.out.println(thisThreadName + ": Crunching some numbers! Final result - " + num);
          partialResult.add(num);
        }
        partialResults.add(partialResult);

        try {
          System.out.println(thisThreadName + " waiting for others to reach barrier.");
          cyclicBarrier.await();
        } catch (InterruptedException e) {
          e.printStackTrace();
        } catch (BrokenBarrierException e) {
          e.printStackTrace();
        }
      }
    }

    /**
     * This Runnable gets executed after all the threads have reached the barrier
     * 
     * @author amudhan
     *
     */
    private class AggregatorThread implements Runnable {
      @Override
      public void run() {
        // Any of the worker threads might execute this
        String thisThreadName = Thread.currentThread().getName();

        System.out.println(thisThreadName + ": Computing sum of " + NUM_WORKERS
            + " workers, having " + NUM_PARTIAL_RESULTS + " results each.");
        int sum = 0;
        // Aggregate all the results
        for (List<Integer> threadResult : partialResults) {
          System.out.print("Adding ");
          for (Integer partialResult : threadResult) {
            System.out.print(partialResult + " ");
            sum += partialResult;
          }
          System.out.println();
        }
        System.out.println(thisThreadName + ": Final result = " + sum);
      }
    }

  }

}
