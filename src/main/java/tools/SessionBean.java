package tools;


import org.w3c.dom.Document;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
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
        System.out.format("commandId == %s%n", id);
        return String.format("%s%s.xml?%s",
                KontomatikUrl.POLL_STATUS.value, id, SIGNATURE);
    }

    private ExecutorService executor = Executors.newSingleThreadExecutor(
            new ThreadFactory() {
                @Override
                public Thread newThread(Runnable runnable) {
                    return new Thread(runnable, "polling-task");
                }
            }
    );
    private Document poll(PollingTask task, int timeout) {
        Future<Document> future = executor.submit(task);
        try {
            return future.get(timeout, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ignore) {
            System.out.format("Polling task has timed out after %s millis. %n", timeout);
            task.cancel();
            return null;
        }
    }


    public InputStream requestCatalog() throws IOException {
        String params = "apiKey=" + apiKey + "&country=all";
        String GET_URL = KontomatikUrl.CATALOG.value + "?" + params;
        return new HttpUtil().doGetRequest(GET_URL).getConnectionInputStream();
    }

    @Inject
    private ResourcesBean resourcesBean;
    public Document getAggregatesResponse(String periodMonths) throws IOException {
        String params = "periodMonths=" + periodMonths +
                "&apiKey=" + apiKey + "&ownerExternalId=" + resourcesBean.getOwnerExternalId();
        String GET_URL = KontomatikUrl.AGGREGATED_VALUES.value + "?" + params;
        HttpUtil h = new HttpUtil().doGetRequest(GET_URL);
        return new XmlParser(h.getConnectionInputStream()).getDocument();
    }

    // Convenience methods:
    public Document getCommandResponse(KontomatikUrl url, String params, int timeout) throws IOException {
        return getCommandResponse(url, params, true, timeout);
    }
    public Document getCommandResponse(KontomatikUrl url) throws IOException {
        return getCommandResponse(url, null, false, 0);
    }

    private Document getCommandResponse(KontomatikUrl url, String params, boolean polling, int timeout) throws IOException {
        String POST_URL = url.value;
        String PARAMS = params == null ? SIGNATURE : SIGNATURE + params;
        HttpUtil h = new HttpUtil().doPostRequest(POST_URL, PARAMS);
        int responseCode = h.getResponseCode();
        System.out.format("%s called :: response code == %s%n", url, responseCode);
        if (responseCode >= 400 || !polling)
           return new XmlParser(h.getConnectionInputStream()).getDocument();
        String GET_URL = createGetUrl(h.getConnectionInputStream());
        PollingTask task = new PollingTask(GET_URL, h);
        Document xml = poll(task, timeout);
        if (xml == null)
            throw new IOException("An unknown error has occurred while polling.");
        else
            return xml;
    }

}




