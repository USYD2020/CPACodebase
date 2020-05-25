package au.edu.sydney.cpa.erp.feaa.contacts;

import au.edu.sydney.cpa.erp.auth.AuthToken;
import au.edu.sydney.cpa.erp.contact.InternalAccounting;
import au.edu.sydney.cpa.erp.ordering.Client;

public class InternalAccountingHandler implements ReportChainLink {
    private ReportChainLink next;

    public InternalAccountingHandler(ReportChainLink next) {
        this.next = next;
    }

    @Override
    public boolean handleRequest(ContactMethod request, AuthToken token, Client client, String data) {

        if (ContactMethod.INTERNAL_ACCOUNTING.equals(request)) {
            String internalAccounting = client.getInternalAccounting();
            String businessName = client.getBusinessName();
            if (null != internalAccounting && null != businessName) {
                InternalAccounting.sendInvoice(token, client.getFName(), client.getLName(), data, internalAccounting,businessName);
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
