package concurrency.semaphores;

import java.util.concurrent.Semaphore;

import org.apache.commons.lang3.RandomStringUtils;

/*
 * How it works
 * 1. When a thread is writing to a resource(file system or database), that resource must be prevented
 * from being read, or being modified by some other thread until the write operation is completed.
 * 2. There can be any number of concurrent readers accessing the resource but there can only be
 * one writer at a time accessing the resource. 
 * 3. This is called categorical mutual exclusion.
 * 4. One risk of this listing is starvation. A bunch of readers might keep on reading, or
 * writers keep on writing, never leaving the critical section emptry for other category
 * to enter.
 */
public class ReadersWriters {

	/*
	 * Mutual exclusion semaphore.
	 */
	public static Semaphore mutex = new Semaphore(1);
	/*
	 * This semaphore tracks whether the critical section is currently accessed
	 * by some thread or is empty.
	 */
	public static Semaphore criticalSectionEmpty = new Semaphore(1);

	/*
	 * To track the number of readers in the critical section.
	 */
	public static int readers = 0;

	public static String resource = "";

	public static void main(String[] args) {
		new Writer("WriterOne").run();
		new Reader("ReaderOne").run();
		new Reader("ReaderTwo").run();
		new Writer("WriterTwo").run();
		new Writer("WriterThree").run();
		new Reader("ReaderThree").run();
		new Reader("ReaderFour").run();
	}

}

class Writer implements Runnable {

	private String name;

	public Writer(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public void run() {
		try {
			/*
			 * Only if the criticalSectionEmpty semaphore is available, can the
			 * writer enter into the critical section.
			 */
			ReadersWriters.criticalSectionEmpty.acquire();
			{
				String input = RandomStringUtils.randomPrint(3, 5);
				System.out.println(this.name + " has produced: " + input);
				ReadersWriters.resource = input;
			}
			ReadersWriters.criticalSectionEmpty.release();
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}

class Reader implements Runnable {

	private String name;

	public Reader(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public void run() {
		try {
			ReadersWriters.mutex.acquire();
			{
				if (ReadersWriters.readers == 0) {
					/*
					 * First reader locks the critical section.
					 * Failing to acquire this semaphore would mean
					 * that a writer may end up writing at the same
					 * time a reader might be reading a resource.
					 * This pattern is called light-switch pattern.
					 */
					ReadersWriters.criticalSectionEmpty.acquire();
				}
				ReadersWriters.readers++;
			}
			ReadersWriters.mutex.release();

			System.out.println(this.name + " has consumed: "
					+ ReadersWriters.resource);

			ReadersWriters.mutex.acquire();
			{
				ReadersWriters.readers--;
				if (ReadersWriters.readers == 0) {
					/*
					 * Last reader unlocks the critical section.
					 */
					ReadersWriters.criticalSectionEmpty.release();
				}
			}
			ReadersWriters.mutex.release();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
