package au.edu.sydney.cpa.erp.feaa;

import au.edu.sydney.cpa.erp.auth.AuthToken;
import au.edu.sydney.cpa.erp.ordering.Order;

import java.util.List;

public interface UOW {
  void saveOrder(AuthToken token, Order order);

  List<Integer> getClientIDs(AuthToken token);

  String getClientField(AuthToken token, int id, String fieldName);

  Order getOrder(AuthToken token, int id);

  boolean removeOrder(AuthToken token, int id);

  List<Order> getOrders(AuthToken token);

  int getNextOrderID();

  default void simulateFastDataBase(int seconds, String message) {
    /*
    * No, the real database doesn't sleep. Yes, the test database takes 10 seconds to save a record etc.
    * No, the fast UOW database removes that 10 seconds lag.
    * Note that this method is used to simulate the CLI interactions.

    * But the same interactions (print messages like "Saving..........Done")
    * would appear again when commit occurs at the TestDataBase.
     */

    System.out.print(message);
    for (int i = 0; i < seconds; i++) {
      System.out.print(".");
    }
    System.out.print("done!\n");
  }

  void commit(AuthToken token);
}
