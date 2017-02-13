package concurrency.semaphores;

import java.util.concurrent.Semaphore;

import org.apache.commons.lang3.RandomStringUtils;

/*
 * How it works:
 * 1. In the solution of ReaderWriter there is a possibility of starvation.
 * 2. This solution avoids that.  
 */
public class NoStarveReadersWriters {

	public static Semaphore mutex = new Semaphore(1);
	public static Semaphore criticalSectionEmpty = new Semaphore(1);
	/*
	 * This prevents starvation.
	 */
	public static Semaphore noStarvePreventor = new Semaphore(1);
	public static int readers = 0;
	public static String resource = "";

	public static void main(String[] args) {
		new NoStarveWriter("WriterOne").run();
		new NoStarveReader("ReaderOne").run();
		new NoStarveReader("ReaderTwo").run();
		new NoStarveWriter("WriterTwo").run();
		new NoStarveWriter("WriterThree").run();
		new NoStarveReader("ReaderThree").run();
		new NoStarveReader("ReaderFour").run();
	}

}

class NoStarveWriter implements Runnable {

	private String name;

	public NoStarveWriter(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public void run() {
		try {
			/*
			 * This semaphore makes sure that the Readers arriving after Writers
			 * block until the Writer exits. 
			 */
			NoStarveReadersWriters.noStarvePreventor.acquire();
			{
				NoStarveReadersWriters.criticalSectionEmpty.acquire();
				{
					String input = RandomStringUtils.randomPrint(3, 5);
					System.out.println(this.name + " has produced: " + input);
					NoStarveReadersWriters.resource = input;
				}
			}
			
			NoStarveReadersWriters.noStarvePreventor.release();
			NoStarveReadersWriters.criticalSectionEmpty.release();
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

class NoStarveReader implements Runnable {

	private String name;

	public NoStarveReader(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public void run() {
		try {
			/*
			 * This prevents starvation of Writers. If there is a Writer already
			 * waiting then that Writer would hold this semaphore causing the
			 * Readers to block. This acts like a turnstile gate that is present
			 * in the subways where only one person can enter and exit at a time. 
			 */
			NoStarveReadersWriters.noStarvePreventor.acquire();
			NoStarveReadersWriters.noStarvePreventor.release();

			NoStarveReadersWriters.mutex.acquire();
			{

				if (NoStarveReadersWriters.readers == 0) {
					NoStarveReadersWriters.criticalSectionEmpty.acquire();
				}
				NoStarveReadersWriters.readers++;
			}
			NoStarveReadersWriters.mutex.release();

			System.out.println(this.name + " has consumed: "
					+ NoStarveReadersWriters.resource);

			NoStarveReadersWriters.mutex.acquire();
			{
				NoStarveReadersWriters.readers--;
				if (NoStarveReadersWriters.readers == 0) {
					NoStarveReadersWriters.criticalSectionEmpty.release();
				}
			}
			NoStarveReadersWriters.mutex.release();

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
