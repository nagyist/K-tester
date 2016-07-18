package kontomatik;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Created by eduarddedu on 02/07/16.
 */
class PollingTask implements Callable<Boolean> {
    final int repeat = 1000; // milliseconds
    final String url;
    HttpUtil httpUtil;

    public PollingTask(String url, HttpUtil h) {
        this.url = url;
        httpUtil = h;

    }

    public Boolean call() { // Returns false only when a serious unexpected connection error occurs
        String response;
        while (true) {
            try {
                response = httpUtil.doGetRequest(url).getResponse();
                if (response.contains("state=\"successful\"") || response.contains("state=\"error\"")
                        || response.contains("state=\"fatal\"")) {
                    return true;
                } else {
                    TimeUnit.MILLISECONDS.sleep(repeat);
                }
            } catch (InterruptedException e1) {
                return true;
            } catch (IOException e2) {
                e2.printStackTrace();
                return false;
            }
        }

    }

}
