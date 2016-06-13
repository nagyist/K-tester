package beans;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;

/**
 * Created by eduarddedu on 13/06/16.
 */
@Named
@ApplicationScoped
public class ClientBean {

    private String name;
    private String apiKey;

    Properties props = new Properties();
    FileInputStream in;

    {
        try {
            in  = new FileInputStream("resources/credentials");
            name = props.getProperty("name");
        } catch(FileNotFoundException e) {
            System.out.println("Can't read or write to client-credentials file.");
        }
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        // Persist name:
        props.setProperty("name", name);
    }


    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
        // Persist API key:
        props.setProperty("apiKey", apiKey);
    }
}