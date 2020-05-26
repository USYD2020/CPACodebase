package au.edu.sydney.cpa.erp.feaa.ordering;

import au.edu.sydney.cpa.erp.ordering.Report;
import au.edu.sydney.cpa.erp.ordering.ScheduledOrder;

import java.util.Map;

public interface TypeOfOrder {

  /**
   * Redefine part of the long description for an order according to its type
   *
   * @param reports the reports associated with an scheduled/nonschedule order
   * @param priority the priority associated with an scheduled/nonschedule order, implying whether
   *     it is critical
   */
  String getLongDescription(Map<Report, Integer> reports, Priority priority);

  /**
   * Redefine the recurring cost for an order according to its type
   *
   * @param reports the reports associated with an scheduled/nonschedule order
   * @param priority the priority associated with an scheduled/nonschedule order, implying whether
   *     it is critical
   */
  double getRecurringCost(Map<Report, Integer> reports, Priority priority);
}
