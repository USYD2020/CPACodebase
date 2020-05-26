package au.edu.sydney.cpa.erp.feaa.ordering;

import au.edu.sydney.cpa.erp.ordering.ScheduledOrder;

import java.util.Map;

public interface Priority {

    /**
     * Return the criticalLoading of a priority, return 0 for a non-critical priority
     */
    double getCriticalLoading();

    /**
     * Redefine the long description for this type, used as part of the long description of an order
     *
     * @param order a scheduled order with numNumberOfQuarters = 1 or more than 1
     */
    String generateInvoiceData(ScheduledOrder order);

}
