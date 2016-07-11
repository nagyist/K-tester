package kontomatik;


import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by eduarddedu on 03/07/16.
 */
@ApplicationScoped
public class KontomatikSession {

    private String SIGNATURE;


    public void setSignature(String sessionId, String sessionIdSignature, String apiKey) {
        this.SIGNATURE = "apiKey=" + apiKey + "&sessionId=" + sessionId + "&sessionIdSignature=" + sessionIdSignature;
    }

    public String getSignature() {
        return SIGNATURE;
    }
    protected static final String API_ENDPOINT = "https://test.api.kontomatik.com/";


    private String parseCommandId(String text) {
        String id;
        // Use a capturing group in regex:
        String pattern = "command id=\"([0-9]+)\"";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(text);
        if (m.find()) {
            id = m.group(1);
        } else {
            throw new IllegalStateException(
                    String.format("Pattern %s not found in response", pattern));
        }
        return id;
    }

    private String createGetUrl(String response) {
        String id = parseCommandId(response);
        return String.format("%s%s.xml?%s",
                Urls.POLL_STATUS.value, id, SIGNATURE);
    }

    private ExecutorService executor = Executors.newSingleThreadExecutor();

    private String pollForCommandStatus(HttpUtil h, int timeout) {
        String GET_URL = createGetUrl(h.getResponse());
        PollingTask task = new PollingTask(GET_URL, h);
        Future<String> future = executor.submit(task);
        try {
            return future.get(timeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            //There will always be an ExecutionException if the task can't complete before timeout.
            System.out.format("Polling task timeout after %s millis.", timeout);
            //Return final response:
            return h.getResponse();
        }

    }


    public String executeCommand(Urls url, String params, int pollingTimeout) throws IOException {
        String POST_URL = url.value;
        String PARAMS = params == null ? SIGNATURE : SIGNATURE + params;
        HttpUtil h = new HttpUtil().doPostRequest(POST_URL, PARAMS);
        int responseCode = h.getResponseCode();
        System.out.format("API %s command called. Response Code :: %s", url, responseCode);
        if (responseCode == HttpURLConnection.HTTP_ACCEPTED) { //success
            return pollForCommandStatus(h, pollingTimeout);
        } else {
            return "Error: " + h.getResponse();
        }


    }

}




