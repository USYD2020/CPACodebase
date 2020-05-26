package au.edu.sydney.cpa.erp.feaa.ordering;

import au.edu.sydney.cpa.erp.ordering.ScheduledOrder;

public class IsNotPriority implements Priority{

    /**
     * Return 0 for a non-critical priority
     */
    @Override
    public double getCriticalLoading() {
        return 0;
    }

    /**
     * Redefine the long description for this type, used as part of the long description of an order
     *
     * @param order a scheduled order with numNumberOfQuarters = 1 or more than 1
     */
    @Override
    public String generateInvoiceData(ScheduledOrder order) {
        String isScheduled = "";
        if (order.getNumberOfQuarters() > 1) {
            isScheduled =
                    String.format(
                            "$%,.2f each quarter, with a total overall cost of: ",
                            order.getRecurringCost());}

        StringBuilder sb = new StringBuilder();

        sb.append("Thank you for your Crimson Permanent Assurance accounting order!\n");
        sb.append("The cost to provide these services: ");
        sb.append(isScheduled);
        sb.append(String.format("$%,.2f", order.getTotalCommission()));
        sb.append("\nPlease see below for details:\n");

        return sb.toString();
    }
}
