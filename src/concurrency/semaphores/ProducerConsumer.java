package concurrency.semaphores;

import java.util.Scanner;
import java.util.concurrent.Semaphore;

/* How it works
 * 1. Producer creates a resource, Consumer consumes the resource.
 * 2. Consumer can not consume when there are nothing is produced.
 */
/*
 * 'acquire' or Wait - decrementing the value of a semaphore.
 *   If the value becomes negative the thread that decrements waits
 *   until it becomes non negative again.
 * 'release()'or Signal - increasing the value of a semaphore.
 *   One and only way to increment a semaphore value so that the blocked
 *   threads(i.e.)threads that wait for this value to become non negative,
 *   get activated.
 */
public class ProducerConsumer {

	/*
	 * This is the value(resource) that gets updated in the Producer thread and
	 * read in the Consumer thread.
	 */
	static String event = "";
	/*
	 * Only by acquiring the lock variable Producer or Consumer can access the
	 * event variable.
	 */
	/*
	 * This variable is equivalent of a mutex.
	 */
	static Semaphore lock = new Semaphore(1);
	/*
	 * The purpose of this variable to let the Consumer know that the event
	 * variable has a new value. When this value is accessed for the first time,
	 * it is incremented by the Producer. Hence it is initialized with zero.
	 */
	static Semaphore available = new Semaphore(0);

	public static void main(String[] args) {
		/**
		 * Starting producer and consumer threads.
		 */
		new Producer().start();
		new Consumer().start();
	}

}

class Producer extends Thread {
	public void run() {
		System.out.println("Inside producer");
		Scanner in = new Scanner(System.in);
		String input = null;
		do {
			System.out.println("Enter event name. Type no to stop.   ");
			input = in.next();
			try {
				/*
				 * Acquiring a semaphore variable named lock. acquire method
				 * decreases the value of a semaphore. In this case that becomes
				 * 0.
				 */
				ProducerConsumer.lock.acquire();
				{
					ProducerConsumer.event = input;
					System.out.println("Produced " + ProducerConsumer.event);
					/*
					 * Once the operation is completed 'lock' is released so
					 * that consumer can acquire it.
					 */
				}
				ProducerConsumer.lock.release();
				/*
				 * Only if the semaphore variable 'available' is released,
				 * (i.e.) becomes non negative, can the consumer access the
				 * event variable.
				 */
				ProducerConsumer.available.release();
				/*
				 * Putting the current thread to sleep is mandatory. Otherwise
				 * this thread will keep updating the event variable, but the
				 * consumer thread wont get a chance to access the values.
				 */
				Producer.sleep(1);
			} catch (InterruptedException e) {
				System.out.println("Exception in the Producer "
						+ e.getMessage());
				e.printStackTrace();
			}
		} while (!input.equalsIgnoreCase("no"));
		in.close();
	}
}

class Consumer extends Thread {
	public void run() {
		System.out.println("Inside consumer");
		while (!ProducerConsumer.event.equalsIgnoreCase("no")) {
			try {
				ProducerConsumer.available.acquire();
				ProducerConsumer.lock.acquire();
				{
					System.out.println("Consumed " + ProducerConsumer.event);
				}
				ProducerConsumer.lock.release();
			} catch (InterruptedException e) {
				System.out.println("Exception in the Consumer "
						+ e.getMessage());
				e.printStackTrace();
			}
		}
	}
}
