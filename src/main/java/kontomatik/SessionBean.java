package kontomatik;


import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by eduarddedu on 03/07/16.
 */

@Named
@SessionScoped
public class SessionBean implements Serializable {
    /**
     * Represents a session with the Kontomatik API server
     */

    private String SIGNATURE;
    private String apiKey;

    public void setSignature(String sessionId, String sessionIdSignature, String apiKey) {
        this.apiKey = apiKey;
        this.SIGNATURE = "apiKey=" + apiKey + "&sessionId=" + sessionId + "&sessionIdSignature=" + sessionIdSignature;
    }

    protected static final String API_ENDPOINT = "https://test.api.kontomatik.com/";


    private String findCommandId(String text) {
        String id;
        // Use a capturing group in regex:
        String pattern = "command id=\"([0-9]+)\"";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(text);
        if (m.find()) {
            id = m.group(1);
        } else {
            throw new IllegalArgumentException(
                    String.format("Pattern %s not found in String %s%n", pattern, text));
        }
        return id;
    }

    private String createGetUrl(String response) {
        String id = findCommandId(response);
        return String.format("%s%s.xml?%s",
                Urls.POLL_STATUS.value, id, SIGNATURE);
    }

    private ExecutorService executor = Executors.newSingleThreadExecutor();


    private boolean poll(PollingTask task, int timeout) {
        Future<Boolean> future = executor.submit(task);
        boolean success = false;
        try {
            success = future.get(timeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ignore) {
            // Task couldn't complete in given time but there should still be a response.
            System.out.format("Polling task timed out after %s millis.", timeout);
        }
        return success;
    }


    public InputStream requestCatalog() throws IOException {
        String params = "apiKey=" + apiKey + "&country=all";
        String GET_URL = Urls.CATALOG.value + "?" + params;
        return new HttpUtil().getConnectionInputStream(GET_URL);
    }

    @Inject
    private ResourcesBean resourcesBean;

    public String getAggregatesResponse(String periodMonths) throws IOException {
        String params = "periodMonths=" + periodMonths +
                "&apiKey=" + apiKey + "&ownerExternalId=" + resourcesBean.getOwnerExternalId();
        String GET_URL = Urls.AGGREGATED_VALUES.value + "?" + params;
        HttpUtil h = new HttpUtil().doGetRequest(GET_URL);
        return h.getResponse();
    }

    // Convenience methods:

    public String getCommandResponse(Urls url, String params, int timeout) throws IOException {
        return getCommandResponse(url, params, true, timeout);
    }
    public String getCommandResponse(Urls url) throws IOException {
        return getCommandResponse(url, null, false, 0);
    }

    private String getCommandResponse(Urls url, String params, boolean poll, int timeout) throws IOException {
        String POST_URL = url.value;
        String PARAMS = params == null ? SIGNATURE : SIGNATURE + params;
        HttpUtil h = new HttpUtil().doPostRequest(POST_URL, PARAMS);
        int responseCode = h.getResponseCode();
        System.out.format("%s command called :: Response Code == %s%n", url, responseCode);
        if (responseCode > HttpURLConnection.HTTP_ACCEPTED)
            throw new IOException("Call failed");
        if (!poll)
            return h.getResponse();
        String GET_URL = createGetUrl(h.getResponse());
        PollingTask task = new PollingTask(GET_URL, h);
        if ( !poll(task, timeout)) {
            throw new IOException("A critical error has occurred while polling.");
        } else {
            return h.getResponse();
        }
    }

    private String formStyle;
    public String getFormStyle() { return formStyle; }
    public void setFormStyle(String s) {
        formStyle = s;
    }

}




