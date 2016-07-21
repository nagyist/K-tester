package kontomatik;


import org.w3c.dom.Document;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.util.concurrent.*;

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


    private String createGetUrl(InputStream in) throws IOException {
        String id = new XmlParser(in).getCommandId();
        return String.format("%s%s.xml?%s",
                Urls.POLL_STATUS.value, id, SIGNATURE);
    }

    private ExecutorService executor = Executors.newSingleThreadExecutor();


    private Document poll(PollingTask task, int timeout) {
        Future<Document> future = executor.submit(task);
        try {
            return future.get(timeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ignore) {
            System.out.format("Polling task timed out after %s millis. %n", timeout);
            task.cancel();
            return null;
        }
    }


    public InputStream requestCatalog() throws IOException {
        String params = "apiKey=" + apiKey + "&country=all";
        String GET_URL = Urls.CATALOG.value + "?" + params;
        return new HttpUtil().doGetRequest(GET_URL).getConnectionInputStream();
    }

    @Inject
    private ResourcesBean resourcesBean;
    public Document getAggregatesResponse(String periodMonths) throws IOException {
        String params = "periodMonths=" + periodMonths +
                "&apiKey=" + apiKey + "&ownerExternalId=" + resourcesBean.getOwnerExternalId();
        String GET_URL = Urls.AGGREGATED_VALUES.value + "?" + params;
        HttpUtil h = new HttpUtil().doGetRequest(GET_URL);
        return new XmlParser(h.getConnectionInputStream()).getDocument();
    }

    // Convenience methods:
    public Document getCommandResponse(Urls url, String params, int timeout) throws IOException {
        return getCommandResponse(url, params, true, timeout);
    }
    public Document getCommandResponse(Urls url) throws IOException {
        return getCommandResponse(url, null, false, 0);
    }

    private Document getCommandResponse(Urls url, String params, boolean requiresPolling, int timeout) throws IOException {
        String POST_URL = url.value;
        String PARAMS = params == null ? SIGNATURE : SIGNATURE + params;
        HttpUtil h = new HttpUtil().doPostRequest(POST_URL, PARAMS);
        int responseCode = h.getResponseCode();
        System.out.format("%s command called :: Response Code == %s%n", url, responseCode);
        if (responseCode > HttpURLConnection.HTTP_ACCEPTED)
            throw new IOException("Call failed");
        if (!requiresPolling)
        return new XmlParser(h.getConnectionInputStream()).getDocument();
        // else ...
        String GET_URL = createGetUrl(h.getConnectionInputStream());
        PollingTask task = new PollingTask(GET_URL, h);
        Document xml = poll(task, timeout);
        if (xml == null)
            throw new IOException("A critical error has occurred while polling.");
        else
            return xml;
    }

    private String formStyle;

    public String getFormStyle() {
        return formStyle;
    }

    public void setFormStyle(String s) {
        formStyle = s;
    }

}




