package au.edu.sydney.cpa.erp.feaa.reports;

import au.edu.sydney.cpa.erp.ordering.Report;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/** The type Flyweight factory. */
public class FlyweightFactory {
  private List<Report> flyweights = new LinkedList<>();

  /**
   * Gets report if found one with duplicate attributes in the flyweights list.
   *
   * @param name the name
   * @param commissionPerEmployee the commission per employee
   * @param legalData the legal data
   * @param cashFlowData the cash flow data
   * @param mergesData the merges data
   * @param tallyingData the tallying data
   * @param deductionsData the deductions data
   * @return the report
   */
  public Report getReport(
      String name,
      double commissionPerEmployee,
      double[] legalData,
      double[] cashFlowData,
      double[] mergesData,
      double[] tallyingData,
      double[] deductionsData) {
    for (int i = 0; i < flyweights.size(); i++) {
      Report o = flyweights.get(i);
      if (name.equals(o.getReportName())
          && Double.compare(commissionPerEmployee, o.getCommission()) == 0
          && Arrays.equals(legalData, o.getLegalData())
          && Arrays.equals(cashFlowData, o.getCashFlowData())
          && Arrays.equals(mergesData, o.getMergesData())
          && Arrays.equals(tallyingData, o.getTallyingData())
          && Arrays.equals(deductionsData, o.getDeductionsData())) {
        return o;
      }
    }
    return null;
  }

  /**
   * Add a new report into flyweights list.
   *
   * @param report the report
   */
  public void addReport(Report report) {
    flyweights.add(report);
  }
}
