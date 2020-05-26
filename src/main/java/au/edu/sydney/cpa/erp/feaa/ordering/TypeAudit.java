package au.edu.sydney.cpa.erp.feaa.ordering;

import au.edu.sydney.cpa.erp.ordering.Report;
import au.edu.sydney.cpa.erp.ordering.ScheduledOrder;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/** The type is an Audit. */
public class TypeAudit implements TypeOfOrder {

  /**
   * Redefine part of the long description for an order according to its type
   *
   * @param reports the reports associated with an scheduled/nonschedule order
   * @param priority the priority associated with an scheduled/nonschedule order, implying whether
   *     it is critical
   */
  @Override
  public String getLongDescription(Map<Report, Integer> reports, Priority priority) {
    StringBuilder sb = new StringBuilder();

    List<Report> keyList = new ArrayList<>(reports.keySet());
    keyList.sort(Comparator.comparing(Report::getReportName).thenComparing(Report::getCommission));

    for (Report report : keyList) {
      double subtotal = report.getCommission() * reports.get(report);

      sb.append(
          String.format(
              "\tReport name: %s\tEmployee Count: %d\tCommission per employee: $%,.2f\tSubtotal: $%,.2f\n",
              report.getReportName(), reports.get(report), report.getCommission(), subtotal));
    }
    return sb.toString();
  }

  /**
   * Redefine the recurring cost for an order according to its type
   *
   * @param reports the reports associated with an scheduled/nonschedule order
   * @param priority the priority associated with an scheduled/nonschedule order, implying whether
   *     it is critical
   */
  @Override
  public double getRecurringCost(Map<Report, Integer> reports, Priority priority) {
    double cost = 0.0;
    for (Report report : reports.keySet()) {
      cost += reports.get(report) * report.getCommission();
    }
    cost += cost * priority.getCriticalLoading();
    return cost;
  }
}
