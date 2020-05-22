package au.edu.sydney.cpa.erp.feaa.contacts;

import au.edu.sydney.cpa.erp.auth.AuthToken;
import au.edu.sydney.cpa.erp.feaa.contacts.ContactMethod;
import au.edu.sydney.cpa.erp.ordering.Client;

public interface ReportChainLink {
    boolean handleRequest(ContactMethod request, AuthToken token, Client client, String data);

}
