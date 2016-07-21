package kontomatik;

import org.w3c.dom.Document;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Created by eduarddedu on 02/07/16.
 */
class PollingTask implements Callable<Document> {
    private final String url;
    private HttpUtil httpUtil;
    private volatile boolean stopped = false;

    public PollingTask(String url, HttpUtil h) {
        this.url = url;
        httpUtil = h;

    }

    protected void cancel() {
        stopped = true;
    }



    public Document call() {
        int freq = 1000;
        try {
            while (!stopped) {
                InputStream in = httpUtil.doGetRequest(url).getConnectionInputStream();
                XmlParser parser = new XmlParser(in);
                String state = parser.getState();
                System.out.format("state=%s%n", state);
                if (state.equals("successful") || state.equals("error") || state.equals("fatal")) {
                    return parser.getDocument();
                } else {
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
