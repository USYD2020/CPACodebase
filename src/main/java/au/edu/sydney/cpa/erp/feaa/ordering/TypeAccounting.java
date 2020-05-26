package au.edu.sydney.cpa.erp.feaa.ordering;

import au.edu.sydney.cpa.erp.ordering.Report;
import au.edu.sydney.cpa.erp.ordering.ScheduledOrder;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class TypeAccounting implements TypeOfOrder {
  private int maxCountedEmployees;

  public TypeAccounting(int maxCountedEmployees) {
    this.maxCountedEmployees = maxCountedEmployees;
  }

  /**
   * Redefine part of the long description for an order according to its type, use
   * maxCountedEmployees to compare with the algorithm in reports
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
      double subtotal = report.getCommission() * Math.min(maxCountedEmployees, reports.get(report));
      sb.append(
          String.format(
              "\tReport name: %s\tEmployee Count: %d\tCommission per employee: $%,.2f\tSubtotal: $%,.2f",
              report.getReportName(), reports.get(report), report.getCommission(), subtotal));

      if (reports.get(report) > maxCountedEmployees) {
        sb.append(" *CAPPED*\n");
      } else {
        sb.append("\n");
      }
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
      cost += report.getCommission() * Math.min(maxCountedEmployees, reports.get(report));
    }
    cost += cost * priority.getCriticalLoading();
    return cost;
  }
}
