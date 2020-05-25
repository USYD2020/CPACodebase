package au.edu.sydney.cpa.erp.feaa.ordering;

import java.time.LocalDateTime;

public class IsCriticalOrder extends NewOrderImpl implements Priority {
    public IsCriticalOrder(int id, int client, LocalDateTime date) {
        super(id, client, date);
    }

    @Override
    public double getCriticalLoading() {
        return 0;
    }
}
