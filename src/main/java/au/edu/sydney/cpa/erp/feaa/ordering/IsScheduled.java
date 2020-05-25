package au.edu.sydney.cpa.erp.feaa.ordering;

import au.edu.sydney.cpa.erp.ordering.ScheduledOrder;

import java.time.LocalDateTime;

public class IsScheduled extends NewOrderImpl implements ScheduledOrder {
    public IsScheduled(int id, int client, LocalDateTime date) {
        super(id, client, date);
    }

    @Override
    public double getRecurringCost() {
        return 0;
    }

    @Override
    public int getNumberOfQuarters() {
        return 0;
    }
}
