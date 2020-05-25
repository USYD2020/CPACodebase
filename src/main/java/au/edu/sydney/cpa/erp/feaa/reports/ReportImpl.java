package au.edu.sydney.cpa.erp.feaa.reports;

import au.edu.sydney.cpa.erp.ordering.Report;

import java.util.Arrays;
import java.util.Objects;

/** The type Report. */
public class ReportImpl implements Report {

  private String name;
  private double commissionPerEmployee;
  private double[] legalData;
  private double[] cashFlowData;
  private double[] mergesData;
  private double[] tallyingData;
  private double[] deductionsData;
  private static final FlyweightFactory flyweightFactory = new FlyweightFactory();

  /**
   * Instantiates a new Report.
   *
   * @param name the name
   * @param commissionPerEmployee the commission per employee
   * @param legalData the legal data
   * @param cashFlowData the cash flow data
   * @param mergesData the merges data
   * @param tallyingData the tallying data
   * @param deductionsData the deductions data
   */
  public ReportImpl(
      String name,
      double commissionPerEmployee,
      double[] legalData,
      double[] cashFlowData,
      double[] mergesData,
      double[] tallyingData,
      double[] deductionsData) {
    Report reportFound =
        flyweightFactory.getReport(
            name,
            commissionPerEmployee,
            legalData,
            cashFlowData,
            mergesData,
            tallyingData,
            deductionsData);
    if (reportFound != null) {
      reduceRamUseByUsingSameAddressForExistingDuplicateReportImpl(reportFound);
      return;
    }
      this.name = name;
      this.commissionPerEmployee = commissionPerEmployee;
      this.legalData = legalData;
      this.cashFlowData = cashFlowData;
      this.mergesData = mergesData;
      this.tallyingData = tallyingData;
      this.deductionsData = deductionsData;
      flyweightFactory.addReport(this);
  }

  /**
   * Reduce ram use by using same address of the attributes for existing duplicate report.
   *
   * @param flyweight the flyweight report
   */
  public void reduceRamUseByUsingSameAddressForExistingDuplicateReportImpl(Report flyweight) {
    if (flyweight != null) {
      this.name = flyweight.getReportName();
      this.commissionPerEmployee = flyweight.getCommission();
      this.legalData = flyweight.getLegalData();
      this.cashFlowData = flyweight.getCashFlowData();
      this.mergesData = flyweight.getMergesData();
      this.tallyingData = flyweight.getTallyingData();
      this.deductionsData = flyweight.getDeductionsData();
    }
  }

  @Override
  public String getReportName() {
    return name;
  }

  @Override
  public double getCommission() {
    return commissionPerEmployee;
  }

  @Override
  public double[] getLegalData() {
    return legalData;
  }

  @Override
  public double[] getCashFlowData() {
    return cashFlowData;
  }

  @Override
  public double[] getMergesData() {
    return mergesData;
  }

  @Override
  public double[] getTallyingData() {
    return tallyingData;
  }

  @Override
  public double[] getDeductionsData() {
    return deductionsData;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof Report)) return false;

    Report o = (ReportImpl) obj;
    return name.equals(o.getReportName())
        && Double.compare(commissionPerEmployee, o.getCommission()) == 0
        && Arrays.equals(legalData, o.getLegalData())
        && Arrays.equals(cashFlowData, o.getCashFlowData())
        && Arrays.equals(mergesData, o.getMergesData())
        && Arrays.equals(tallyingData, o.getTallyingData())
        && Arrays.equals(deductionsData, o.getDeductionsData());
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        name,
        commissionPerEmployee,
        legalData,
        cashFlowData,
        mergesData,
        tallyingData,
        deductionsData);
  }

  @Override
  public String toString() {

    return String.format("%s", name);
  }
}
