package kontomatik;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Created by eduarddedu on 13/06/16.
 */
@Named
@SessionScoped
public class ResourcesBean implements Serializable {

    private String clientName = "smartloans-test";
    private String apiKey; // api key should never been exposed on this branch !
    private String ownerExternalId = "100780";
    private final String contextRoot = "K-tester";


    public String getClientName() {
        return clientName;
    }

    public void setClientName(String name) {
        clientName = name;
    }

    public String getApiKey() { return apiKey; }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getOwnerExternalId() {
        return ownerExternalId;
    }

    public String getContextRoot() { return contextRoot; }

}