package au.edu.sydney.cpa.erp.feaa;

import au.edu.sydney.cpa.erp.auth.AuthToken;
import au.edu.sydney.cpa.erp.database.TestDatabase;
import au.edu.sydney.cpa.erp.ordering.Order;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/** The type Save order runnable. */
public class SaveOrderRunnableForUOW implements Runnable {
  /** The Orders. */
  List<Order> orders;

  /** The Token. */
  AuthToken token;

  private Lock lock = new ReentrantLock();

  /**
   * Instantiates a new Save order runnable.
   *
   * @param token the token
   * @param orders the orders to be saved
   */
  public SaveOrderRunnableForUOW(AuthToken token, List<Order> orders) {
    this.orders = orders;
    this.token = token;
  }

  /** Save an order into the TestDataBase. */
  @Override
  public void run() {
    if (orders.size() != 0) {
      try {
        Thread.sleep(100);
        lock.lock();
        TestDatabase.getInstance().saveOrder(this.token, this.orders.get(0));
        this.orders.remove(0);
        lock.unlock();
      } catch (InterruptedException ignored) {
      }
    }
  }
}
