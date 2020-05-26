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

    /**
     * Handle a request by comparing the request with the Contact method (also a chainLink), proceed to handle if matched.
     *
     * @param request the requested contact method
     * @param token the token
     * @param client the client
     * @param data the data to be communicated
     * @return the boolean whether this request is handled by CarrierPigeonHandler and handled successfully
     */
    @Override
    public boolean handleRequest(ContactMethod request, AuthToken token, Client client, String data) {

        // return true if this request is handled by CarrierPigeonHandler and handled successfully
        if (ContactMethod.CARRIER_PIGEON.equals(request)) {
            String pigeonCoopID = client.getPigeonCoopID();
            if (null != pigeonCoopID) {
                CarrierPigeon.sendInvoice(token, client.getFName(), client.getLName(), data, pigeonCoopID);
                return true;
            }
            // return false if this request is handled by CarrierPigeonHandler and handled with failure
            return false;
        }

        // proceed to next handler if this request is not handled and the handler has a next chain
        if (null == next) {
            return false;
        }

        return next.handleRequest(request, token, client, data);
    }
}
