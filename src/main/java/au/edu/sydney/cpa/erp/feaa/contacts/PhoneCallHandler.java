package au.edu.sydney.cpa.erp.feaa.contacts;

import au.edu.sydney.cpa.erp.auth.AuthToken;
import au.edu.sydney.cpa.erp.contact.PhoneCall;
import au.edu.sydney.cpa.erp.ordering.Client;

public class PhoneCallHandler implements ReportChainLink {
    private ReportChainLink next;

    public PhoneCallHandler(ReportChainLink next) {
        this.next = next;
    }

    @Override
    public boolean handleRequest(ContactMethod request, AuthToken token, Client client, String data) {

        if (ContactMethod.PHONECALL.equals(request)) {
            String phone = client.getPhoneNumber();
            if (null != phone) {
                PhoneCall.sendInvoice(token, client.getFName(), client.getLName(), data, phone);
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
