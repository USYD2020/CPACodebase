package au.edu.sydney.cpa.erp.feaa.contacts;

import au.edu.sydney.cpa.erp.auth.AuthToken;
import au.edu.sydney.cpa.erp.contact.PhoneCall;
import au.edu.sydney.cpa.erp.ordering.Client;

public class PhoneCallHandler implements ReportChainLink {
  private ReportChainLink next;

  public PhoneCallHandler(ReportChainLink next) {
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
   * @return the boolean whether this request is handled by PhoneHandler and handled successfully
   */
  @Override
  public boolean handleRequest(ContactMethod request, AuthToken token, Client client, String data) {

    // return true if this request is handled by this handler and handled successfully
    if (ContactMethod.PHONECALL.equals(request)) {
      String phone = client.getPhoneNumber();
      if (null != phone) {
        PhoneCall.sendInvoice(token, client.getFName(), client.getLName(), data, phone);
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
