package au.edu.sydney.cpa.erp.feaa.ordering;

import au.edu.sydney.cpa.erp.ordering.Order;
import au.edu.sydney.cpa.erp.ordering.Report;
import au.edu.sydney.cpa.erp.ordering.ScheduledOrder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class OrderImpl implements Order, Priority, ScheduledOrder,TypeOfOrder {
    private Map<Report, Integer> reports = new HashMap<>();
    private  int id;
    private LocalDateTime date;
    private int client;
    private boolean finalised = false;

    private boolean isCritical = false;
    private double criticalLoading;
    private int clientID;
    private int maxCountedEmployees;

    private boolean isScheduled = false;
    private int numQuarters;

    public OrderImpl(int id, int clientID, LocalDateTime date, double criticalLoading, int maxCountedEmployees,  int numQuarters) {
        this.id = id;
        this.date = date;
        this.criticalLoading = criticalLoading;
        this.clientID = clientID;
        this.maxCountedEmployees = maxCountedEmployees;
        this.numQuarters = numQuarters;
    }

    @Override
    public double getCriticalLoading() {
        return criticalLoading;
    }

    @Override
    public double getRecurringCost() {
        double cost = 0.0;
        for (Report report : reports.keySet()) {
            cost += reports.get(report) * report.getCommission();
        }
        return cost;
    }

    @Override
    public int getNumberOfQuarters() {
        return numQuarters;
    }

    @Override
    public int getOrderID() {
        return id;
    }

    @Override
    public double getTotalCommission() {
        return getRecurringCost() * numQuarters;
    }

    @Override
    public LocalDateTime getOrderDate() {
        return date;
    }

    @Override
    public void setReport(Report report, int employeeCount) {
        if (finalised) throw new IllegalStateException("Order was already finalised.");

        // We can't rely on equal reports having the same object identity since they get
        // rebuilt over the network, so we have to check for presence and same values

        for (Report contained: reports.keySet()) {
            if (contained.getCommission() == report.getCommission() &&
                    contained.getReportName().equals(report.getReportName()) &&
                    Arrays.equals(contained.getLegalData(), report.getLegalData()) &&
                    Arrays.equals(contained.getCashFlowData(), report.getCashFlowData()) &&
                    Arrays.equals(contained.getMergesData(), report.getMergesData()) &&
                    Arrays.equals(contained.getTallyingData(), report.getTallyingData()) &&
                    Arrays.equals(contained.getDeductionsData(), report.getDeductionsData())) {
                report = contained;
                break;
            }
        }

        reports.put(report, employeeCount);
    }

    @Override
    public Set<Report> getAllReports() {
        return reports.keySet();
    }

    @Override
    public int getReportEmployeeCount(Report report) {
        // We can't rely on equal reports having the same object identity since they get
        // rebuilt over the network, so we have to check for presence and same values

        for (Report contained: reports.keySet()) {
            if (contained.getCommission() == report.getCommission() &&
                    contained.getReportName().equals(report.getReportName()) &&
                    Arrays.equals(contained.getLegalData(), report.getLegalData()) &&
                    Arrays.equals(contained.getCashFlowData(), report.getCashFlowData()) &&
                    Arrays.equals(contained.getMergesData(), report.getMergesData()) &&
                    Arrays.equals(contained.getTallyingData(), report.getTallyingData()) &&
                    Arrays.equals(contained.getDeductionsData(), report.getDeductionsData())) {
                report = contained;
                break;
            }
        }
        Integer result = reports.get(report);
        return null == result ? 0 : result;
    }

    @Override
    public String generateInvoiceData() {
        return null;
    }

    @Override
    public int getClient() {
        return client;
    }

    @Override
    public void finalise() {
        this.finalised = true;
    }

    @Override
    public Order copy() {
        Order copy = new OrderImpl(id, client, date,criticalLoading,maxCountedEmployees,numQuarters);
        for (Report report : reports.keySet()) {
            copy.setReport(report, reports.get(report));
        }
        return copy;
    }

    @Override
    public String shortDesc() {
        return null;
    }

    @Override
    public String longDesc() {
        return null;
    }

    protected Map<Report, Integer> getReports() {
        return reports;
    }

    protected boolean isFinalised() {
        return finalised;
    }

}
