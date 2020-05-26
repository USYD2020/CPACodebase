package au.edu.sydney.cpa.erp.feaa.contacts;

import au.edu.sydney.cpa.erp.auth.AuthToken;
import au.edu.sydney.cpa.erp.contact.SMS;
import au.edu.sydney.cpa.erp.ordering.Client;

public class SMSHandler implements ReportChainLink {
  private ReportChainLink next;

  public SMSHandler(ReportChainLink next) {
    this.next = next;
  }

  /**
   * Handle a request by comparing the request with the Contact method (also a chainLink), proceed
   * to handle if matched.
   *
   * @param request the requested contact method
   * @param token the token
   * @param client the client
   * @param data the data to be communicated
   * @return the boolean whether this request is handled by SMSHandler and handled successfully
   */
  @Override
  public boolean handleRequest(ContactMethod request, AuthToken token, Client client, String data) {

    // return true if this request is handled by this handler and handled successfully
    if (ContactMethod.SMS.equals(request)) {
      String smsPhone = client.getPhoneNumber();
      if (null != smsPhone) {
        SMS.sendInvoice(token, client.getFName(), client.getLName(), data, smsPhone);
        return true;
      }
      // return false if this request is handled by this handler and handled with failure
      return false;
    }

    // proceed to next handler if this request is not handled and the handler has a next chain
    if (null == next) {
      return false;
    }

    return next.handleRequest(request, token, client, data);
  }
}
