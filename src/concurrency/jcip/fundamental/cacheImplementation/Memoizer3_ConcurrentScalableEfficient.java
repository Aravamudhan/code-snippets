package concurrency.jcip.fundamental.cacheImplementation;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * This implementation is concurrent, scalable and efficient<br/>
 * 
 * @author amudhan
 *
 */
public class Memoizer3_ConcurrentScalableEfficient<A, V> implements Computable<A, V> {

  private final ConcurrentMap<A, Future<V>> cache = new ConcurrentHashMap<A, Future<V>>();

  private final Computable<A, V> c;

  public Memoizer3_ConcurrentScalableEfficient(Computable<A, V> c) {
    this.c = c;
  }

  public V compute(final A arg) throws InterruptedException {
    while (true) {
      Future<V> f = cache.get(arg);
      if (f == null) {
        Callable<V> eval = new Callable<V>() {
          public V call() throws InterruptedException {
            return c.compute(arg);
          }
        };
        FutureTask<V> ft = new FutureTask<V>(eval);
        // putIfAbsent is atomic even though it follows the check-then-act pattern. If there is no
        // value for the given key, then the method puts the key-value pair and returns null. If
        // there is already a key-value pair for the given key, then this method will return the
        // value and won't try to put the new value. Since the cache is thread-safe, there is no
        // possibility of two threads putting the same value
        f = cache.putIfAbsent(arg, ft);
        // The f will be null if there is no value present for the given arg. That means it is 1st
        // thread that is trying to put a certain arg inside the cache. Any other attempts for the same arg will return a non-null Future value
        if (f == null) {
          f = ft;
          ft.run();
        }
      }
      try {
        return f.get();
      } catch (CancellationException e) {
        cache.remove(arg, f);
      } catch (ExecutionException e) {
        Throwable t = e.getCause();
        if (t instanceof RuntimeException)
          throw (RuntimeException) t;
        else if (t instanceof Error)
          throw (Error) t;
        else
          throw new IllegalStateException("Not unchecked", t);
      }
    }
  }
}
