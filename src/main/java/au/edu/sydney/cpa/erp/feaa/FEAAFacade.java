package au.edu.sydney.cpa.erp.feaa;

import au.edu.sydney.cpa.erp.auth.AuthModule;
import au.edu.sydney.cpa.erp.auth.AuthToken;
import au.edu.sydney.cpa.erp.database.TestDatabase;
import au.edu.sydney.cpa.erp.feaa.contacts.ContactHandler;
import au.edu.sydney.cpa.erp.feaa.contacts.ContactMethod;
import au.edu.sydney.cpa.erp.ordering.Client;
import au.edu.sydney.cpa.erp.ordering.Order;
import au.edu.sydney.cpa.erp.ordering.Report;
import au.edu.sydney.cpa.erp.feaa.ordering.*;
import au.edu.sydney.cpa.erp.feaa.reports.ReportDatabase;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** The type Feaa facade. */
@SuppressWarnings("Duplicates")
public class FEAAFacade implements Runnable {
  private AuthToken token;
  private TestDatabase database;
  private UnitOfWork unitOfWork;

  /**
   * Login boolean.
   *
   * @param userName the user name
   * @param password the password
   * @return the boolean
   */
  public boolean login(String userName, String password) {
    this.database = TestDatabase.getInstance();
    token = AuthModule.login(userName, password);
    if(token != null) this.unitOfWork = new UnitOfWork(token);
    return null != token;
  }

  /**
   * Gets all orders.
   *
   * @return the all orders
   */
  public List<Integer> getAllOrders() {
    if (null == token) {
      throw new SecurityException();
    }
    List<Order> orders = unitOfWork.getOrders(token);
    List<Integer> result = new ArrayList<>();
    for (Order order : orders) {
      result.add(order.getOrderID());
    }
    return result;
  }

  /**
   * Create order integer.
   *
   * @param clientID the client id
   * @param date the date
   * @param isCritical the is critical
   * @param isScheduled the is scheduled
   * @param orderType the order type
   * @param criticalLoadingRaw the critical loading raw
   * @param maxCountedEmployees the max counted employees
   * @param numQuarters the num quarters
   * @return the integer
   */
  public Integer createOrder(
      int clientID,
      LocalDateTime date,
      boolean isCritical,
      boolean isScheduled,
      int orderType,
      int criticalLoadingRaw,
      int maxCountedEmployees,
      int numQuarters) {
    if (null == token) {
      throw new SecurityException();
    }

    double criticalLoading = criticalLoadingRaw / 100.0;

    if (!unitOfWork.getClientIDs(token).contains(clientID)) {
      throw new IllegalArgumentException("Invalid client ID");
    }

    int id = unitOfWork.getNextOrderID();
    if (!isScheduled) numQuarters = 1;

    Priority priority;
    if (isCritical) priority = new IsPriority(criticalLoading);
    else priority = new IsNotPriority();

    TypeOfOrder type = null;
    if (orderType == 1) type = new TypeAccounting(maxCountedEmployees);
    else if (orderType == 2) type = new TypeAudit();

    if (type == null) return null;
    Order order = new OrderImpl(id, clientID, date, priority, numQuarters, type);

    unitOfWork.saveOrder(token, order);
    return order.getOrderID();
  }

  /**
   * Gets all client i ds.
   *
   * @return the all client i ds
   */
  public List<Integer> getAllClientIDs() {
    if (null == token) {
      throw new SecurityException();
    }
    return unitOfWork.getClientIDs(token);
  }

  /**
   * Gets client.
   *
   * @param id the id
   * @return the client
   */
  public Client getClient(int id) {
    if (null == token) {
      throw new SecurityException();
    }

    return new ClientImpl(token, id);
  }

  /**
   * Remove order boolean.
   *
   * @param id the id
   * @return the boolean
   */
  public boolean removeOrder(int id) {
    if (null == token) {
      throw new SecurityException();
    }
    return unitOfWork.removeOrder(token, id);
  }

  /**
   * Gets all reports.
   *
   * @return the all reports
   */
  public List<Report> getAllReports() {
    if (null == token) {
      throw new SecurityException();
    }
    return new ArrayList<>(ReportDatabase.getTestReports());
  }

  /**
   * Finalise order boolean.
   *
   * @param orderID the order id
   * @param contactPriority the contact priority
   * @return the boolean
   */
  public boolean finaliseOrder(int orderID, List<String> contactPriority) {
    if (null == token) {
      throw new SecurityException();
    }

    List<ContactMethod> contactPriorityAsMethods = new ArrayList<>();

    if (null != contactPriority) {
      for (String method : contactPriority) {
        switch (method.toLowerCase()) {
          case "internal accounting":
            contactPriorityAsMethods.add(ContactMethod.INTERNAL_ACCOUNTING);
            break;
          case "email":
            contactPriorityAsMethods.add(ContactMethod.EMAIL);
            break;
          case "carrier pigeon":
            contactPriorityAsMethods.add(ContactMethod.CARRIER_PIGEON);
            break;
          case "mail":
            contactPriorityAsMethods.add(ContactMethod.MAIL);
            break;
          case "phone call":
            contactPriorityAsMethods.add(ContactMethod.PHONECALL);
            break;
          case "sms":
            contactPriorityAsMethods.add(ContactMethod.SMS);
            break;
          default:
            break;
        }
      }
    }

    if (contactPriorityAsMethods.size() == 0) { // needs setting to default
      contactPriorityAsMethods =
          Arrays.asList(
              ContactMethod.INTERNAL_ACCOUNTING,
              ContactMethod.EMAIL,
              ContactMethod.CARRIER_PIGEON,
              ContactMethod.MAIL,
              ContactMethod.PHONECALL);
    }

    Order order = unitOfWork.getOrder(token, orderID);

    order.finalise();
    unitOfWork.saveOrder(token, order);
    return ContactHandler.sendInvoice(
        token, getClient(order.getClient()), contactPriorityAsMethods, order.generateInvoiceData());
  }

  /** Logout.
   * Important To commit before logout!!!
   * */
  public void logout() {
    unitOfWork.commit(token);
    AuthModule.logout(token);
    token = null;
  }

  /**
   * Gets order total commission.
   *
   * @param orderID the order id
   * @return the order total commission
   */
  public double getOrderTotalCommission(int orderID) {
    if (null == token) {
      throw new SecurityException();
    }

    Order order = unitOfWork.getOrder(token, orderID);
    if (null == order) {
      return 0.0;
    }

    return order.getTotalCommission();
  }

  /**
   * Order line set.
   *
   * @param orderID the order id
   * @param report the report
   * @param numEmployees the num employees
   */
  public void orderLineSet(int orderID, Report report, int numEmployees) {
    if (null == token) {
      throw new SecurityException();
    }

    Order order = unitOfWork.getOrder(token, orderID);

    if (null == order) {
      System.out.println("got here");
      return;
    }

    order.setReport(report, numEmployees);

   unitOfWork.saveOrder(token, order);
  }

  /**
   * Gets order long desc.
   *
   * @param orderID the order id
   * @return the order long desc
   */
  public String getOrderLongDesc(int orderID) {
    if (null == token) {
      throw new SecurityException();
    }

    Order order = unitOfWork.getOrder(token, orderID);

    if (null == order) {
      return null;
    }

    return order.longDesc();
  }

  /**
   * Gets order short desc.
   *
   * @param orderID the order id
   * @return the order short desc
   */
  public String getOrderShortDesc(int orderID) {
    if (null == token) {
      throw new SecurityException();
    }

    Order order = unitOfWork.getOrder(token, orderID);

    if (null == order) {
      return null;
    }
    return order.shortDesc();
  }

  /**
   * Gets known contact methods.
   *
   * @return the known contact methods
   */
  public List<String> getKnownContactMethods() {
    if (null == token) {
      throw new SecurityException();
    }
    return ContactHandler.getKnownMethods();
  }

  @Override
  public void run() {
    //pass
  }
}
