package concurrency.jcip.fundamental.cacheImplementation;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This implementation uses a ConcurrentHashMap which is inherently thread-safe<br/>
 * Because of that we do not have to make the entire compute method synchronized<br/>
 * Now multiple threads can execute the compute method at the same time<br/>
 * 
 * @author amudhan
 *
 * @param <A>
 * @param <V>
 */
public class Memoizer2_Concurrent<A, V> implements Computable<A, V> {

  private final Map<A, V> cache = new ConcurrentHashMap<A, V>();

  private final Computable<A, V> c;

  public Memoizer2_Concurrent(Computable<A, V> c) {
    this.c = c;
  }

  /**
   * Even though the cache is thread-safe there is a small window of unsafe operation that can
   * happen. Two threads calling the compute at the time, might end up computing the same value<br/>
   * This is the check-and-then-act pattern. One thread might have checked and decided to compute,
   * and before it computes and puts the result in the cache another thread might also perform a
   * check and decide to compute. Now this is inefficient.
   * 
   */
  public V compute(A arg) throws InterruptedException {
    V result = cache.get(arg);
    if (result == null) {
      result = c.compute(arg);
      cache.put(arg, result);
    }
    return result;
  }
}
