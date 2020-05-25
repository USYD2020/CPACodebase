package au.edu.sydney.cpa.erp.feaa.ordering;

import java.time.LocalDateTime;

public class IsNotCriticalOrder extends NewOrderImpl implements Priority {
    public IsNotCriticalOrder(int id, int client, LocalDateTime date) {
        super(id, client, date);
    }

    @Override
    public double getCriticalLoading() {
        return 0;
    }
}
