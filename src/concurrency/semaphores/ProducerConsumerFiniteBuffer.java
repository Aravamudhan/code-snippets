package concurrency.semaphores;

import java.util.Scanner;
import java.util.Stack;
import java.util.concurrent.Semaphore;
/*
 * How it works:
 * 1. This works like ProducerConsumer problem except the buffer to
 * store the inputs is limited in size.
 * 2. Producer can add an item to the buffer only if it has an empty space.
 * 3. Consumer can retrieve when at least one item is available.
 * 4. Producer-Consumer behaviour is enforced by the semaphores available
 * and lock. The constraint of limited buffer size is enforced by the spaces
 * semaphore.
 * 5. 'available' is signaled by the Producer when adding an item to the buffer.
 * 6. 'spaces' is signaled by the Consumer when removing an item from the buffer.
 * 7. 'spaces' semaphore is decremented(wait/acquire) by the Producer and 
 * incremented(signal/release) by the Consumer.
 */

public class ProducerConsumerFiniteBuffer {

	private static final int BUFFER_SIZE = 5;
	/*
	 * The buffer.
	 */
	private static Stack<String> events = new Stack<String>();

	/*
	 * The semaphore that manages the size constraint on the events(buffer)
	 */
	public static Semaphore spaces = new Semaphore(BUFFER_SIZE);
	/*
	 * Producer always releases this semaphore, Consumer always acquires this
	 * semaphore.
	 */
	public static Semaphore available = new Semaphore(0);
	/*
	 * The semaphore that controls the access to the events variable(the
	 * critical resource).
	 */
	public static Semaphore lock = new Semaphore(1);

	public static Stack<String> getEvents() {
		return events;
	}

	public static int getBufferSize() {
		return BUFFER_SIZE;
	}

	public static void main(String[] args) {
		new ProducerBuffer().start();
		new ConsumerBuffer().start();
	}
}

class ProducerBuffer extends Thread {
	public void run() {
		Scanner in = new Scanner(System.in);
		String input = null;
		do {
			System.out.println("Enter event name. Type no to stop.   ");
			input = in.next();
			try {
				/*
				 * Only if space is available can the Producer produce an item.
				 */
				ProducerConsumerFiniteBuffer.spaces.acquire();
				{
					ProducerConsumerFiniteBuffer.lock.acquire();
					{
						ProducerConsumerFiniteBuffer.getEvents().push(input);
						System.out
						.println("Remaining space "
								+ (ProducerConsumerFiniteBuffer.getBufferSize() - ProducerConsumerFiniteBuffer
										.getEvents().size()));
					}
					ProducerConsumerFiniteBuffer.lock.release();
				}
				/*
				 * Signals the an item is up for grabs.
				 */
				ProducerConsumerFiniteBuffer.available.release();
			} catch (InterruptedException e) {
				System.out.println("Exception in the Producer "
						+ e.getMessage());
				e.printStackTrace();
			}
		} while (!input.equalsIgnoreCase("no"));
		in.close();
	}
}

class ConsumerBuffer extends Thread {
	public void run() {
		while (true) {
			try {
				/*
				 * Consumer is not as fast as the Producer.
				 */
				ConsumerBuffer.sleep(10000);
				/*
				 * Only if an item is available in the buffer, does the consumer
				 * proceeds.
				 */
				ProducerConsumerFiniteBuffer.available.acquire();
				{
					ProducerConsumerFiniteBuffer.lock.acquire();
					{
						System.out.println("Consumed the "
								+ ProducerConsumerFiniteBuffer.getEvents().pop());
					}
					ProducerConsumerFiniteBuffer.lock.release();
				}
				/*
				 * After consuming a space is freed up in the buffer.
				 */
				ProducerConsumerFiniteBuffer.spaces.release();
			} catch (InterruptedException e) {
				System.out.println("Exception in the Consumer "
						+ e.getMessage());
				e.printStackTrace();
			}
		}
	}
}
