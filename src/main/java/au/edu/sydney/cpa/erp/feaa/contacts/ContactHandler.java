package au.edu.sydney.cpa.erp.feaa.contacts;

import au.edu.sydney.cpa.erp.auth.AuthToken;
import au.edu.sydney.cpa.erp.ordering.Client;

import java.util.Arrays;
import java.util.List;

/** The type Contact handler. */
public class ContactHandler {
  // Define report chain to handle quests in order, this chain order is assumed to be so as given by previous coder
  private static final ReportChainLink reportChain =
      new SMSHandler(
          new MailHandler(
              new EmailHandler(
                  new PhoneCallHandler(
                      new InternalAccountingHandler(new CarrierPigeonHandler(null))))));

  /**
   * Send invoice boolean.
   *
   * @param token the token
   * @param client the client
   * @param priorityRequests the priority requests
   * @param data the data
   * @return the boolean return it any of the priorityRequests is handled, by using the report chain to handle quests in order
   */
  public static boolean sendInvoice(
      AuthToken token, Client client, List<ContactMethod> priorityRequests, String data) {
    for (ContactMethod request : priorityRequests) {
      if (reportChain.handleRequest(request, token, client, data)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Gets known methods.
   *
   * @return the known methods
   */
  public static List<String> getKnownMethods() {
    return Arrays.asList(
        "Carrier Pigeon", "Email", "Mail", "Internal Accounting", "Phone call", "SMS");
  }
}
