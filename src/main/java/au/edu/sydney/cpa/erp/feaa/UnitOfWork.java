package au.edu.sydney.cpa.erp.feaa;

import au.edu.sydney.cpa.erp.auth.AuthModule;
import au.edu.sydney.cpa.erp.auth.AuthToken;
import au.edu.sydney.cpa.erp.database.TestDatabase;
import au.edu.sydney.cpa.erp.ordering.Order;

import java.util.*;

/** The type Unit of work. */
public class UnitOfWork implements UOW {
  private int nextOrderID = 0;
  private List<Order> orders;
  private final List<Integer> clients;
  private boolean commiting = false;
  private AuthToken token;

  /**
   * Instantiates a new Unit of work to simulate a fast TestDataBase.
   *
   * <p>This is set up to do the same things in the same amount of time as the slow TestDataBase.
   * Because it's not networked and fast until commit to the database.
   */
  public UnitOfWork(AuthToken token) {
    clients = new ArrayList<>();
    orders = new ArrayList<>();
    this.token = token;
    clients.add(0);
    clients.add(1);
    clients.add(2);
    clients.add(3);
  }

  /**
   * Save order using UOW
   *
   * @param token the token
   * @param order the order
   */
  @Override
  public void saveOrder(AuthToken token, Order order) {
    if (!AuthModule.authenticate(token)) {
      throw new SecurityException("Invalid authorisation");
    }
    if (orders.size() != 0) {
      for (Order iter : orders) {
        if (iter.getOrderID() == order.getOrderID()) {
          orders.remove(iter);
          break;
        }
      }
    }
    simulateFastDataBase(10, "Saving order");
    orders.add(order.copy());
  }

  /**
   * Gets client ids using UOW. Only when UOW is not initialised or updated, use UOW as the actual
   * TestDataBase.
   *
   * @param token the token
   * @return the client i ds
   */
  @Override
  public List<Integer> getClientIDs(AuthToken token) {
    if (!AuthModule.authenticate(token)) {
      throw new SecurityException("Invalid authorisation");
    }

    simulateFastDataBase(2, "Getting clients");

    if (orders != null && orders.size() != 0) {
      return new ArrayList<>(clients);
    }
    return TestDatabase.getInstance().getClientIDs(token);
  }

  /**
   * Gets client field using UOW. Only when UOW is not initialised or updated, use UOW as the actual
   * TestDataBase.
   *
   * @param token the token
   * @param id the id
   * @param fieldName the field name
   * @return the client field
   */
  @Override
  public String getClientField(AuthToken token, int id, String fieldName) {
    if (!AuthModule.authenticate(token)) {
      throw new SecurityException("Invalid authorisation");
    }

    simulateFastDataBase(1, "Getting client field");

    switch (fieldName) {
      case "fName":
        switch (id) {
          case 0:
            return "Bob";
          case 1:
            return "Zhi";
          case 2:
            return "Dhanvi";
          case 3:
            return "Felix";
          default:
            return null;
        }
      case "lName":
        switch (id) {
          case 0:
            return "Smith";
          case 1:
            return "Liu";
          case 2:
            return "Kargan";
          case 3:
            return "The Cat";
          default:
            return null;
        }
      case "phoneNumber":
        switch (id) {
          case 0:
            return "12345";
          case 2:
            return "67890";
          case 1:
          case 3:
          default:
            return null;
        }
      case "emailAddress":
        switch (id) {
          case 0:
            return "bob@gmail.com";
          case 1:
            return "zhi@gmail.com";
          case 3:
            return "Felix@yahoo.com";
          case 2:
          default:
            return null;
        }
      case "address":
        switch (id) {
          case 0:
            return "123 Fake St";
          case 2:
            return "17 Blue Rd";
          case 1:
          case 3:
          default:
            return null;
        }
      case "suburb":
        switch (id) {
          case 0:
            return "Darlington";
          case 2:
            return "Camden";
          case 1:
          case 3:
          default:
            return null;
        }
      case "state":
        switch (id) {
          case 0:
          case 2:
            return "NSW";
          case 1:
          case 3:
          default:
            return null;
        }
      case "postCode":
        switch (id) {
          case 0:
            return "2008";
          case 2:
            return "2570";
          case 1:
          case 3:
          default:
            return null;
        }
      case "internal accounting":
        switch (id) {
          case 1:
          case 3:
            return "Ben Affleck";
          case 0:
          case 2:
          default:
            return null;
        }
      case "businessName":
        switch (id) {
          case 1:
            return "Flowers by Zhi";
          case 3:
            return "Felix's Pet Food";
          case 0:
          case 2:
          default:
            return null;
        }
      case "pigeonCoopID":
        switch (id) {
          case 0:
            return "17";
          case 3:
            return "96";
          case 1:
          case 2:
          default:
            return null;
        }
      default:
        throw new IllegalArgumentException("Invalid client field " + fieldName);
    }
  }

  /**
   * Gets order using UOW. Only when UOW is not initialised or updated, use UOW as the actual
   * TestDataBase.
   *
   * @param token the token
   * @param id the id
   * @return the order
   */
  @Override
  public Order getOrder(AuthToken token, int id) {
    if (!AuthModule.authenticate(token)) {
      throw new SecurityException("Invalid authorisation");
    }
    if (orders != null && orders.size() != 0) {
      for (Order iter : orders) {
        if (iter.getOrderID() == id) {
          return iter.copy();
        }
      }
    }
    return TestDatabase.getInstance().getOrder(token, id);
  }

  /**
   * Remove order boolean using UOW. Only when UOW is not initialised or updated, use UOW as the
   * actual TestDataBase.
   *
   * @param token the token
   * @param id the id
   * @return the boolean
   */
  @Override
  public boolean removeOrder(AuthToken token, int id) {
    if (!AuthModule.authenticate(token)) {
      throw new SecurityException("Invalid authorisation");
    }
    if (orders != null && orders.size() != 0) {
      for (Order iter : orders) {
        if (iter.getOrderID() == id) {
          orders.remove(iter);
          return true;
        }
      }
    }
    return TestDatabase.getInstance().removeOrder(token, id);
  }

  /**
   * Gets orders using UOW. Only when UOW is not initialised or updated, use UOW as the actual
   * TestDataBase.
   *
   * @param token the token
   * @return the orders
   */
  @Override
  public List<Order> getOrders(AuthToken token) {
    if (!AuthModule.authenticate(token)) {
      throw new SecurityException("Invalid authorisation");
    }

    if (orders != null && orders.size() != 0) {
      List<Order> result = new ArrayList<>();
      for (Order order : orders) {
        result.add(order.copy());
      }
      return result;
    }
    // use the original database only when this unitOfWok has not been used at all
    return TestDatabase.getInstance().getOrders(token);
  }

  /**
   * Gets next order id using UOW. Start from 0.
   *
   * @return the next order id
   */
  @Override
  public int getNextOrderID() {
    return nextOrderID++;
  }

  /**
   * Commit using UOW to the actual TestDataBase. This happens inside feaa.logout().
   *
   * @param token the token
   */
  @Override
  public void commit(AuthToken token) {
    if (orders != null && orders.size() != 0) {

      /*
       * The following lines are an attempt to solve the SINGLE thread issue in the FEAA module,
       * due to time limit, this issue is not solved.
       * As such, the following lines are commented out to maintain correctness.
       * The relevant classes SaveOrderRunnableForUOW, DriveSaveOrderForUOW
       * are reserved to provide discussions for future work.
       */

      /*
      DriveSaveOrderForUOW.saveOrders(token,orders);
      */

      for (Order o : orders) {
        TestDatabase.getInstance().saveOrder(token, o);
      }
      orders.clear();
    }
  }
}
