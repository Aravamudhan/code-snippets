/**
 * This package contains code snippets implementing concurrency using semaphores.
 * 
 * Semaphores are objects that are used to enforce safe concurrent
 * access of shared variables or the critical section.
 * When multiple threads are accessing a resource concurrently with one set 
 * writing and other set reading there is a high probability of reading/writing stale values.
 * Using the pattern of wait and signal, semaphores enable communication among multiple threads
 * for a safe read/write of resources.
 */
/**
 * @author amudhan
 *
 */
package concurrency.semaphores;