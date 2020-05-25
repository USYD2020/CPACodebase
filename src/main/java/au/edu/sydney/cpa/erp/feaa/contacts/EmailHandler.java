package au.edu.sydney.cpa.erp.feaa.contacts;

import au.edu.sydney.cpa.erp.auth.AuthToken;
import au.edu.sydney.cpa.erp.contact.Email;
import au.edu.sydney.cpa.erp.ordering.Client;

public class EmailHandler implements ReportChainLink {
    private ReportChainLink next;

    public EmailHandler(ReportChainLink next) {
        this.next = next;
    }

    @Override
    public boolean handleRequest(ContactMethod request, AuthToken token, Client client, String data) {

        if (ContactMethod.EMAIL.equals(request)) {
            String email = client.getEmailAddress();
            if (null != email) {
                Email.sendInvoice(token, client.getFName(), client.getLName(), data, email);
                return true;
            }
            return false;
        }

        if (null == next) {
            return false;
        }

        return next.handleRequest(request, token, client, data);
    }
}
