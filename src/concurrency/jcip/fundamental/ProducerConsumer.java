package concurrency.jcip.fundamental;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

public class ProducerConsumer {

  public static void main(String[] args) {
    blockingQueueSample();
    synchronousQueueSample();
  }

  private static void blockingQueueSample() {
    System.out.println("Starting blockingQueueSample method..................");
    List<String> names = Arrays.asList("Jon", "Tyrion", "Dany", "Bran", "Arya", "Ned", "Tywin",
        "Jamie", "Cat", "Jory");
    BlockingQueue<String> bq = new LinkedBlockingQueue<>(3);
    Runnable consumerRunnable = () -> {
      for (int i = 0; i < 10; i++) {
        try {
          // The take method will block until an element becomes available in the queue
          System.out.println("Consuming : " + bq.take());
          Thread.sleep(2000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    };
    Runnable producerRunnable = () -> {
      for (int i = 0; i < 10; i++) {
        try {
          String name = names.get(i);
          System.out.println("Producing :" + name);
          bq.put(name);
          Thread.sleep(500);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    };
    Thread consumer = new Thread(consumerRunnable, "Consumer");
    Thread producer = new Thread(producerRunnable, "Producer");
    consumer.start();
    producer.start();
    try {
      consumer.join();
      producer.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  private static void synchronousQueueSample() {

    System.out.println("Starting synchronousQueueSample method..................");

    BlockingQueue<String> sq = new SynchronousQueue<>();

    List<String> names = new LinkedList<>(Arrays.asList("Jon", "Tyrion", "Dany", "Bran", "Arya",
        "Ned", "Tywin", "Jamie", "Cat", "Jory", "Wyman", "Lyanna", "Syrio"));

    Runnable producerRunnable = () -> {
      try {
        while (names.size() > 0) {
          String name = names.remove(0);
          System.out.println("Producing :" + name);
          sq.put(name);
          Thread.sleep(500);
        }
        System.out.println("Producer exiting...........");
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    };
    Thread producer = new Thread(producerRunnable, "Producer");
    producer.start();

    Runnable consumerRunnable = () -> {
      try {
        // Until the producer is running we continue with consumer
        String currentThreadName = Thread.currentThread().getName();
        while (producer.isAlive()) {
          System.out
              .println(currentThreadName + " is checking for producer :" + producer.isAlive());
          System.out.println(
              currentThreadName + " : Consuming : " + sq.poll(1500, TimeUnit.MILLISECONDS));
          System.out.println(currentThreadName + " is waiting for producer.....");
          Thread.sleep(3000);
        }
        System.out.println(Thread.currentThread().getName() + " is exiting.....");
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    };


    Thread consumer1 = new Thread(consumerRunnable, "Consumer1");
    Thread consumer2 = new Thread(consumerRunnable, "Consumer2");
    Thread consumer3 = new Thread(consumerRunnable, "Consumer3");

    consumer1.start();
    consumer2.start();
    consumer3.start();

  }

}
