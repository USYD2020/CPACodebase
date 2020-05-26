package au.edu.sydney.cpa.erp.feaa.ordering;

import au.edu.sydney.cpa.erp.ordering.Order;
import au.edu.sydney.cpa.erp.ordering.Report;
import au.edu.sydney.cpa.erp.ordering.ScheduledOrder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class OrderImpl implements ScheduledOrder {
  private Map<Report, Integer> reports = new HashMap<>();
  private int id;
  private LocalDateTime date;
  private int client;
  private boolean finalised = false;

  private int clientID;

  private Priority priority;

  private int numQuarters;

  private TypeOfOrder type;

  public OrderImpl(
      int id,
      int clientID,
      LocalDateTime date,
      Priority priority,
      int numQuarters,
      TypeOfOrder type) {
    this.id = id;
    this.clientID = clientID;
    this.date = date;
    this.priority = priority;
    this.numQuarters = numQuarters;
    this.type = type;
  }

  @Override
  public double getRecurringCost() {
    return type.getRecurringCost(reports, priority);
  }

  @Override
  public int getNumberOfQuarters() {
    return numQuarters;
  }

  @Override
  public int getOrderID() {
    return id;
  }

  @Override
  public double getTotalCommission() {
    return getRecurringCost() * numQuarters;
  }

  @Override
  public LocalDateTime getOrderDate() {
    return date;
  }

  @Override
  public void setReport(Report report, int employeeCount) {
    if (finalised) throw new IllegalStateException("Order was already finalised.");

    // We can't rely on equal reports having the same object identity since they get
    // rebuilt over the network, so we have to check for presence and same values

    for (Report contained : reports.keySet()) {
      if (contained.getCommission() == report.getCommission()
          && contained.getReportName().equals(report.getReportName())
          && Arrays.equals(contained.getLegalData(), report.getLegalData())
          && Arrays.equals(contained.getCashFlowData(), report.getCashFlowData())
          && Arrays.equals(contained.getMergesData(), report.getMergesData())
          && Arrays.equals(contained.getTallyingData(), report.getTallyingData())
          && Arrays.equals(contained.getDeductionsData(), report.getDeductionsData())) {
        report = contained;
        break;
      }
    }

    reports.put(report, employeeCount);
  }

  @Override
  public Set<Report> getAllReports() {
    return reports.keySet();
  }

  @Override
  public int getReportEmployeeCount(Report report) {
    // We can't rely on equal reports having the same object identity since they get
    // rebuilt over the network, so we have to check for presence and same values

    for (Report contained : reports.keySet()) {
      if (contained.getCommission() == report.getCommission()
          && contained.getReportName().equals(report.getReportName())
          && Arrays.equals(contained.getLegalData(), report.getLegalData())
          && Arrays.equals(contained.getCashFlowData(), report.getCashFlowData())
          && Arrays.equals(contained.getMergesData(), report.getMergesData())
          && Arrays.equals(contained.getTallyingData(), report.getTallyingData())
          && Arrays.equals(contained.getDeductionsData(), report.getDeductionsData())) {
        report = contained;
        break;
      }
    }
    Integer result = reports.get(report);
    return null == result ? 0 : result;
  }

  @Override
  public String generateInvoiceData() {
    StringBuilder sb = new StringBuilder();

    sb.append(priority.generateInvoiceData(this));

    Map<Report, Integer> reports = this.getReports();
    List<Report> keyList = new ArrayList<>(reports.keySet());
    keyList.sort(Comparator.comparing(Report::getReportName).thenComparing(Report::getCommission));

    if (priority.getCriticalLoading() == 0) {
      for (Report report : keyList) {
        sb.append("\tReport name: ");
        sb.append(report.getReportName());
        sb.append("\tEmployee Count: ");
        sb.append(reports.get(report));
        sb.append("\tCost per employee: ");
        sb.append(String.format("$%,.2f", report.getCommission()));
        sb.append("\tSubtotal: ");
        sb.append(String.format("$%,.2f\n", report.getCommission() * reports.get(report)));
      }
    }

    return sb.toString();
  }

  @Override
  public int getClient() {
    return client;
  }

  @Override
  public void finalise() {
    this.finalised = true;
  }

  @Override
  public Order copy() {
    Order copy = new OrderImpl(id, client, date, priority, numQuarters, type);
    for (Report report : reports.keySet()) {
      copy.setReport(report, reports.get(report));
    }
    return copy;
  }

  @Override
  public String shortDesc() {
    String isScheduledMsg =
        numQuarters > 1 ? String.format(" per quarter, $%,.2f total", getTotalCommission()) : "";
    return String.format("ID:%s $%,.2f", id, getRecurringCost()) + isScheduledMsg;
  }

  @Override
  public String longDesc() {
    String isScheduledQuarter =
        numQuarters > 1 ? String.format("Number of quarters: %d\n", numQuarters) : "";

    String isScheduledCost =
        numQuarters > 1 ? String.format("Recurring cost: $%,.2f\n", this.getRecurringCost()) : "";

    String isCriticalMsg = "";
    if (priority.getCriticalLoading() != 0) {
      double cost = 0.0;
      for (Report report : reports.keySet()) {
        cost += reports.get(report) * report.getCommission();
      }
      isCriticalMsg =
          String.format("Critical Loading: $%,.2f\n", (getRecurringCost() - cost) * numQuarters);
    }
    return String.format(
        isFinalised()
            ? ""
            : "*NOT FINALISED*\n"
                + "Order details (id #%d)\n"
                + "Date: %s\n"
                + isScheduledQuarter
                + "Reports:\n"
                + "%s"
                + isCriticalMsg
                + isScheduledCost
                + "Total cost: $%,.2f\n",
        getOrderID(),
        getOrderDate().format(DateTimeFormatter.ISO_LOCAL_DATE),
        type.getLongDescription(reports, priority), // get the long description from a specific TypeOfOrder
        this.getTotalCommission());
  }

  public Map<Report, Integer> getReports() {
    return reports;
  }

  protected boolean isFinalised() {
    return finalised;
  }
}
