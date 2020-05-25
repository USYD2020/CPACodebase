package au.edu.sydney.cpa.erp.feaa.ordering;

import au.edu.sydney.cpa.erp.ordering.Order;

public interface TypeOfOrder {

    double getTotalCommission();

    String generateInvoiceData();

    Order copy();

    String shortDesc();

    String longDesc();
}
