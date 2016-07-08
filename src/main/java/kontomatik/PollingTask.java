package kontomatik;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Created by eduarddedu on 02/07/16.
 */
class PollingTask implements Callable<String> {
    final int repeat = 1000; // milliseconds
    final String url;
    HttpUtil httpUtil;

    public PollingTask(String url, HttpUtil h) {
        this.url = url;
        httpUtil = h;

    }

    public String call() {
        String response;
        while (true) {
            try {
                response = httpUtil.doGetRequest(url).getResponse();
                if (response.contains("state=\"successful\"") || response.contains("state=\"error\"")) {
                    return response;
                } else {
                    TimeUnit.MILLISECONDS.sleep(repeat);
                }
            } catch (InterruptedException | IOException ex) {
                return null;
            }
        }

    }

}
