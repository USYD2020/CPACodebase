package au.edu.sydney.cpa.erp.feaa.contacts;

import au.edu.sydney.cpa.erp.auth.AuthToken;
import au.edu.sydney.cpa.erp.ordering.Client;

import java.util.Arrays;
import java.util.List;

public class ContactHandler {
  private static final ReportChainLink reportChain =
      new SMSHandler(
          new MailHandler(
              new EmailHandler(
                  new PhoneCallHandler(
                      new InternalAccountingHandler(new CarrierPigeonHandler(null))))));

  public static boolean sendInvoice(
      AuthToken token, Client client, List<ContactMethod> priorityRequests, String data) {
    for (ContactMethod request : priorityRequests) {
      if (reportChain.handleRequest(request, token, client, data)) {
        return true;
      }
    }
    return false;
  }

  public static List<String> getKnownMethods() {
    return Arrays.asList(
        "Carrier Pigeon", "Email", "Mail", "Internal Accounting", "Phone call", "SMS");
  }
}
