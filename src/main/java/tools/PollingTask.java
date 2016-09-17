package tools;

import org.w3c.dom.Document;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Created by eduarddedu on 02/07/16.
 */
public class PollingTask implements Callable<Document> {
    private final URL GET_URL;
    private volatile boolean stopped = false;

    public PollingTask(URL GET_URL) {
        this.GET_URL = GET_URL;
        System.out.format("GET %s%n", GET_URL.toString());
    }

    public void cancel() {
        stopped = true;
    }



    public Document call() {
        int freq = 1000;
        try {
            while (!stopped) {
                InputStream in = new HttpRequest("GET", GET_URL, null).getInputStream();
                XmlParser parser = new XmlParser(in);
                String state = parser.getState();
                System.out.format("state == \"%s\"%n", state);
                if (state.equals("successful") || state.equals("error") || state.equals("fatal")) {
                    return parser.getDocument();
                } else {
                    System.out.format("progress == %s%n", parser.getProgress());
                    TimeUnit.MILLISECONDS.sleep(freq);
                }
            }
        } catch (InterruptedException ignore) {
            System.out.println("Interrupted");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
