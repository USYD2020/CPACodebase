package au.edu.sydney.cpa.erp.feaa.contacts;

import au.edu.sydney.cpa.erp.auth.AuthToken;
import au.edu.sydney.cpa.erp.contact.CarrierPigeon;
import au.edu.sydney.cpa.erp.feaa.contacts.ContactMethod;
import au.edu.sydney.cpa.erp.feaa.contacts.ReportChainLink;
import au.edu.sydney.cpa.erp.ordering.Client;

public class CarrierPigeonHandler implements ReportChainLink {
    private ReportChainLink next;
    public CarrierPigeonHandler(ReportChainLink next) {
        this.next = next;
    }

    @Override
    public boolean handleRequest(ContactMethod request, AuthToken token, Client client, String data) {

        if (ContactMethod.CARRIER_PIGEON.equals(request)) {
            String pigeonCoopID = client.getPigeonCoopID();
            if (null != pigeonCoopID) {
                CarrierPigeon.sendInvoice(token, client.getFName(), client.getLName(), data, pigeonCoopID);
                return true;
            }
        }

        if (null == next) {
            return false;
        }

        return next.handleRequest(request, token, client, data);
    }
}
