package jcdsee.util;

/** Semaphore used in image loading thread
 */
public class Semaphore {
  
  protected Thread owner = null;
  private int count = 0;

  /** Constuctor
   */
  public Semaphore() {
  }
  
  /** Called by threads that want the lock, i.e wait(). 
   * This will block until lock is acquired
   * @return void
   */
  public synchronized void lock() {
    while (!acquire()) {
      try {
	wait();
      } catch (InterruptedException e) {
	// Do nothing!
      }
    }
  }
  
  /** Called by threads that want the lock, i.e wait().
   * Try to acquire the lock if possible
   * Will NOT block.
   * @return true if lock was acquired
   */
  public synchronized boolean acquire() {
    Thread current = Thread.currentThread();  // Who's calling?
    if (owner == null) {
      owner = current;
      count = 1;
      return true;
    } else if (owner == current) {
      count++;        // Account for nested locks
      return true;
    } else {
      return false;
    }
  }
  
  /** Called by threads that want to unlock the semaphore,
   * i.e signal().
   * @return void
   */
  public synchronized void unlock() {
    Thread current = Thread.currentThread();  // Who's calling?
    if (owner == current) {
      count--;
      if (count==0) {
	owner = null;
	notify();
      }
    }
  }
}
            
