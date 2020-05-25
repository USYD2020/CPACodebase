package au.edu.sydney.cpa.erp.feaa.ordering;

import java.time.LocalDateTime;

public class TypeAccounting extends NewOrderImpl implements TypeOfOrder{
    public TypeAccounting(int id, int client, LocalDateTime date) {
        super(id, client, date);
    }
}
