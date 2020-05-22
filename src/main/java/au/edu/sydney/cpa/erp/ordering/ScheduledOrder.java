package au.edu.sydney.cpa.erp.ordering;

import au.edu.sydney.cpa.erp.ordering.Order;

public interface ScheduledOrder extends Order {
    double getRecurringCost();
    int getNumberOfQuarters();
}
