package kontomatik;


import org.w3c.dom.Document;
import tools.HttpRequest;
import tools.PollingTask;
import tools.ResourcesBean;
import tools.XmlParser;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.concurrent.*;

/**
 * Created by eduarddedu on 03/07/16.
 */

/**
 * A managed bean class representing an authenticated Kontomatik session.
 * The apiKey String may be initialised from direct user input in the frontend layer.
 */
@Named
@SessionScoped
public class Session implements Serializable {
    /**
     * Represents a session with the Kontomatik API server
     */

    private String signature;
    private String apiKey;
    private String ownerId;
    private final URLs urls = new URLs();

    public void setSignature(String sessionId, String sessionIdSignature, String apiKey) {
        this.apiKey = apiKey;
        this.signature = "apiKey=" + apiKey + "&sessionId=" + sessionId + "&sessionIdSignature=" + sessionIdSignature;
    }
    public void setOwnerId(String id) {
        this.ownerId = id;
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
            System.out.format("Polling task has timed out. Gave up after %s millis. %n", timeout);
            task.cancel();
            return null;
        }
    }


    public InputStream requestCatalog() throws IOException {
        String params = "apiKey=" + apiKey + "&country=all";
        return new HttpRequest("GET", urls.GET_CATALOG, params).getInputStream(); // probably should be urls.newGET_CATALOG(params)
    }

    @Inject
    private ResourcesBean resourcesBean;
    public Document getAggregatesResponse(String periodMonths) throws IOException {
        String params = "periodMonths=" + periodMonths +
                "&apiKey=" + apiKey + "&ownerExternalId=" + ownerId; //should be pulled from data base or something
        System.out.format("ownerExternalId == %s%n", ownerId);
        HttpRequest request = new HttpRequest("GET", urls.newGET_AGGREGATES(params), null);
        return new XmlParser(request.getInputStream()).getDocument();
    }

    // Convenience methods:
    public Document getDocument(URL POST_URL, String params, int timeout) throws IOException {
        return getDocument(POST_URL, params, true, timeout);
    }
    public Document getDocument(URL POST_URL) throws IOException {
        return getDocument(POST_URL, null, false, 0);
    }

    private Document getDocument(URL POST_URL, String params, boolean polling, int timeout) throws IOException {
        String PARAMS = params == null ? signature : signature + params;
        HttpRequest request = new HttpRequest("POST", POST_URL, PARAMS);
        int code = request.getResponseCode();
        System.out.format("POST %s%n", POST_URL.toString());
        System.out.format("Response Code :: %s%n", code);
        if (code >= 400 || !polling)
           return new XmlParser(request.getInputStream()).getDocument();

        String id = new XmlParser(request.getInputStream()).getCommandId();
        URL GET_URL = urls.newGET_STATUS(id, signature);

        PollingTask task = new PollingTask(GET_URL);
        Document xml = poll(task, timeout);
        if (xml == null)
            throw new IOException("An unknown error has occurred while polling: ");
        else
            return xml;
    }

}




