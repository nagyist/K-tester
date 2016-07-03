package kontomatik;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Created by eduarddedu on 02/07/16.
 */
class PollStatusTask implements Callable<Boolean> {
    int repeat = 1000; // milliseconds
    String url;
    HttpUtil httpUtil;

    public PollStatusTask(String GET_URL, HttpUtil h) {
        url = GET_URL;
        this.httpUtil = h;

    }

    public Boolean call() {
        String response;
        while (true) {
            try {
                // Is it necessary to submit a new request?
                response = httpUtil.doGetRequest(url).getResponse();
                if (response.contains("state=\"successful\"")) {
                    return true;
                } else {
                    TimeUnit.MILLISECONDS.sleep(repeat);
                }
            } catch (IOException e) {
                System.out.println("PollStatusTask error :: " + e.toString());
                return false;

            } catch (InterruptedException ex) {
                System.out.println ("PollStatusTask interrupted...");
                return false;
            }
        }

    }

}
