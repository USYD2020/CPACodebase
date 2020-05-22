package au.edu.sydney.cpa.erp.feaa.contacts;

import au.edu.sydney.cpa.erp.auth.AuthToken;
import au.edu.sydney.cpa.erp.contact.SMS;
import au.edu.sydney.cpa.erp.ordering.Client;

public class SMSHandler implements ReportChainLink {
  private ReportChainLink next;

  public SMSHandler(ReportChainLink next) {
    this.next = next;
  }

  @Override
  public boolean handleRequest(ContactMethod request, AuthToken token, Client client, String data) {

    if (ContactMethod.SMS.equals(request)) {
      String smsPhone = client.getPhoneNumber();
      if (null != smsPhone) {
        SMS.sendInvoice(token, client.getFName(), client.getLName(), data, smsPhone);
        return true;
      }
    }

    if (null == next) {
      return false;
    }

    return next.handleRequest(request, token, client, data);
  }
}
