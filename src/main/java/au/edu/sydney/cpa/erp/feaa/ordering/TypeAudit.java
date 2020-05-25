package au.edu.sydney.cpa.erp.feaa.ordering;

import java.time.LocalDateTime;

public class TypeAudit extends NewOrderImpl implements TypeOfOrder {
    public TypeAudit(int id, int client, LocalDateTime date) {
        super(id, client, date);
    }
}
