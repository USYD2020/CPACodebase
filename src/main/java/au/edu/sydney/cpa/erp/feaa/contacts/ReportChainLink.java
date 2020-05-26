package au.edu.sydney.cpa.erp.feaa.contacts;

import au.edu.sydney.cpa.erp.auth.AuthToken;
import au.edu.sydney.cpa.erp.ordering.Client;

/** The interface Report chain link. */
public interface ReportChainLink {
  /**
   * Handle a request by comparing the request with the Contact method (also a chainLink), proceed to handle if matched.
   *
   * @param request the requested contact method
   * @param token the token
   * @param client the client
   * @param data the data to be communicated
   * @return the boolean whether this request is handled and handled successfully
   */
  boolean handleRequest(ContactMethod request, AuthToken token, Client client, String data);
}
