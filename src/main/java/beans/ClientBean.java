package beans;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
       init();
    }

    private void init() {
        try {
            in  = new FileInputStream("src/main/resources/credentials.txt");
            props.load(in);
            name = props.getProperty("name");
            apiKey = props.getProperty("apiKey");
        } catch(FileNotFoundException e) {
            System.err.println("Credentials file not found:" + e.getMessage());
        } catch(IOException e1) {
            System.err.println("Cannot read from credentials file:" + e1.getMessage());
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

    public String getApiKey() { return apiKey; }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
        // Persist API key:
        props.setProperty("apiKey", apiKey);
    }
}