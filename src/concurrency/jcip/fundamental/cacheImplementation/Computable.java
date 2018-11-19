package concurrency.jcip.fundamental.cacheImplementation;

/**
 * This interface defines a function with the input of type A and result of type V<br/>
 * 
 * @author amudhan
 *
 * @param <A>
 * @param <V>
 */
public interface Computable<A, V> {
  V compute(A arg) throws InterruptedException;
}
