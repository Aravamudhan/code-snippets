package concurrency.jcip.fundamental.cacheImplementation;

import java.util.HashMap;
import java.util.Map;

import net.jcip.annotations.GuardedBy;

/**
 * This demonstrates a naive cache implementation using the memoization technique<br/>
 * 
 * @author amudhan
 *
 */
public class Memoizer1_Naive<A, V> implements Computable<A, V> {

  /**
   * The cache is not thread safe
   */
  @GuardedBy("this")
  private final Map<A, V> cache = new HashMap<A, V>();

  private final Computable<A, V> c;

  public Memoizer1_Naive(Computable<A, V> c) {
    this.c = c;
  }

  /**
   * At the same time two threads can do Map#get on the cache and they both may receive null as the
   * result. To avoid threads accessing the cache at the same time, we synchronize on the compute
   * method. The problem here is, other threads will have to block when a certain thread is
   * performing a long running computation creating a bottleneck.<br/>
   */
  public synchronized V compute(A arg) throws InterruptedException {
    V result = cache.get(arg);
    if (result == null) {
      result = c.compute(arg);
      cache.put(arg, result);
    }
    return result;
  }
}
