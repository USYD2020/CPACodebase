package au.edu.sydney.cpa.erp.feaa.contacts;

import au.edu.sydney.cpa.erp.auth.AuthToken;
import au.edu.sydney.cpa.erp.contact.Mail;
import au.edu.sydney.cpa.erp.ordering.Client;

public class MailHandler implements ReportChainLink{
    private ReportChainLink next;

    public MailHandler(ReportChainLink next) {
        this.next = next;
    }

    @Override
    public boolean handleRequest(ContactMethod request, AuthToken token, Client client, String data) {

        if (ContactMethod.MAIL.equals(request)) {
            String address = client.getAddress();
            String suburb = client.getSuburb();
            String state = client.getState();
            String postcode = client.getPostCode();
            if (null != address && null != suburb &&
                    null != state && null != postcode) {
                Mail.sendInvoice(token, client.getFName(), client.getLName(), data, address, suburb, state, postcode);
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
