package au.edu.sydney.cpa.erp.feaa.contacts;

import au.edu.sydney.cpa.erp.auth.AuthToken;
import au.edu.sydney.cpa.erp.contact.Email;
import au.edu.sydney.cpa.erp.ordering.Client;

public class EmailHandler implements ReportChainLink {
    private ReportChainLink next;

    public EmailHandler(ReportChainLink next) {
        this.next = next;
    }

    /**
     * Handle a request by comparing the request with the Contact method (also a chainLink), proceed to handle if matched.
     *
     * @param request the requested contact method
     * @param token the token
     * @param client the client
     * @param data the data to be communicated
     * @return the boolean whether this request is handled by EmailHandler and handled successfully
     */
    @Override
    public boolean handleRequest(ContactMethod request, AuthToken token, Client client, String data) {

        // return true if this request is handled by EmailHandler and handled successfully
        if (ContactMethod.EMAIL.equals(request)) {
            String email = client.getEmailAddress();
            if (null != email) {
                Email.sendInvoice(token, client.getFName(), client.getLName(), data, email);
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
