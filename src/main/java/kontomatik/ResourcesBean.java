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

    private String clientName = "ededu-test";
    private String apiKey = "54689db20f69a84961672d65b1e76a24894d070daaee618078eef7c30d6e9d73";
    private String ownerExternalId = "100780";
    private final String contextRoot = "kontomatik-app";


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