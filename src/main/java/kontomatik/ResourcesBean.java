package kontomatik;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.*;
import java.util.Properties;

/**
 * Created by eduarddedu on 13/06/16.
 */
@Named
@SessionScoped
public class ResourcesBean implements Serializable {

    private String clientName;
    private String apiKey;
    private String ownerExternalId = "100776";
    private Properties props = new Properties();
    private FileInputStream in;

    {
       init();
    }

    private void init() {
        try {
            in  = new FileInputStream("src/main/java/kontomatik/credentials.txt");
            props.load(in);
            clientName = props.getProperty("clientName");
            apiKey = props.getProperty("apiKey");
        } catch(FileNotFoundException e) {
            System.err.println("Credentials file not found:" + e.getMessage());
        } catch(IOException e1) {
            System.err.println("Cannot read from credentials file:" + e1.getMessage());
        }

    }


    public String getClientName() {
        return clientName;
    }


    public void setClientName(String name) {
        clientName = name;
        props.setProperty("clientName", clientName);
    }

    public String getApiKey() { return apiKey; }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
        props.setProperty("apiKey", apiKey);
    }

    public String getOwnerExternalId() {
        return ownerExternalId;
    }

}