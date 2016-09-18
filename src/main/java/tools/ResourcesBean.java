package tools;

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
    private String apiKey = "6e907d69e7acbbdf8411b62010070c0a6309bebbe5ca549136ca6e067e2543a4";
    private String ownerExternalId;
    private final String appName = "K-tester";


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

    public String getOwnerExternalId() { return ownerExternalId; }

    public void setOwnerExternalId(String id) { ownerExternalId = id; }

    public String getAppName() { return appName; }


}