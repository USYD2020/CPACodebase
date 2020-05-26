package au.edu.sydney.cpa.erp.feaa.ordering;

import au.edu.sydney.cpa.erp.ordering.ScheduledOrder;

/** The type is priority. */
public class IsPriority implements Priority {
  private double criticalLoading;

  /**
   * Instantiates a new IsCritical priority.
   *
   * @param criticalLoading the critical loading
   */
  public IsPriority(double criticalLoading) {
    this.criticalLoading = criticalLoading;
  }

    /**
     * Return the criticalLoading of a priority
     */
  @Override
  public double getCriticalLoading() {
    return criticalLoading;
  }

    /**
     * Redefine the long description for this type, used as part of the long description of an order
     *
     * @param order a scheduled order with numNumberOfQuarters = 1 or more than 1
     */
  @Override
  public String generateInvoiceData(ScheduledOrder order) {
    String isScheduled = "Your priority business account has been charged: ";
    if (order.getNumberOfQuarters() > 1) {
      isScheduled =
          String.format(
              "Your priority business account will be charged: $%,.2f each quarter for %d quarters, with a total overall cost of: ",
              order.getRecurringCost(), order.getNumberOfQuarters());
    }
    return String.format(
        "%s$%,.2f" + "\nPlease see your internal accounting department for itemised details.",
        isScheduled, order.getTotalCommission());
  }
}
