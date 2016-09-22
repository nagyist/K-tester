package tools;

import org.w3c.dom.Document;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.*;

/**
 * Author: Eduard Dedu
 * eduardcdedu@gmail.com
 * Created on: 18/09/16
 */
public class KontomatikServiceExecutor {


    public Document callImportOwners(String sessionSignature) throws IOException {
        URL POST_URL = KontomatikServiceURL.POST_IMPORT_OWNERS;
        return runPOSTCommand(POST_URL, sessionSignature, null, true);
    }

    public Document callImportAccounts(String sessionSignature) throws IOException {
        URL POST_URL = KontomatikServiceURL.POST_IMPORT_ACCOUNTS;
        return runPOSTCommand(POST_URL, sessionSignature, null, true);
    }

    public Document callImportAccountTransactions(String sessionSignature, String data) throws IOException {
        URL POST_URL = KontomatikServiceURL.POST_IMPORT_ACCOUNT_TRANSACTIONS;
        return runPOSTCommand(POST_URL, sessionSignature, data, true);
    }

    public Document callDefaultImport(String sessionSignature, String data) throws IOException {
        URL POST_URL = KontomatikServiceURL.POST_DEFAULT_IMPORT;
        return runPOSTCommand(POST_URL, sessionSignature, data, true);

    }

    public Document callSignOut(String sessionSignature) throws IOException {
        URL POST_URL = KontomatikServiceURL.POST_SIGN_OUT;
        return runPOSTCommand(POST_URL, sessionSignature, null, false);
    }



    public Document callAggregatedValues(String params) throws IOException {
        URL GET_URL = KontomatikServiceURL.newGET_AGGREGATES(params);
        HttpRequest request = new HttpRequest("GET",
                GET_URL);
        System.out.format("GET %s%n", GET_URL.toString());
        System.out.format("Response Code :: %s%n", request.getResponseCode());

        return new XmlParser(request.getInputStream()).getDocument();

    }



    private Document runPOSTCommand(URL POST_URL, String sessionSignature, String data, boolean polling)
            throws IOException {

        HttpRequest request = new HttpRequest("POST", POST_URL, data == null ? sessionSignature : data); // Some commands require additional params (data).
        System.out.format("POST %s%n", request.getUrlObject().toString());
        int code = request.getResponseCode();
        System.out.format("Response Code :: %s%n", code);

        if (code != 200 && code != 202) { // Some error occurred, let's still hope we have an XML response from the service
            return new XmlParser(request.getInputStream()).getDocument();
        }
        else if (!polling) {
            return new XmlParser(request.getInputStream()).getDocument(); // Sign-out command doesn't need polling.
        }
        else {
            String id = new XmlParser(request.getInputStream()).getCommandId();
            URL GET_URL = KontomatikServiceURL.newGET_STATUS(id, sessionSignature);
            return poll(GET_URL);
        }
    }


    private ExecutorService executor = Executors.newSingleThreadExecutor(
            new ThreadFactory() {
                @Override
                public Thread newThread(Runnable runnable) {
                    return new Thread(runnable, "polling-task");
                }
            }
    );

    private Document poll(URL GET_URL) throws IOException {
        PollingTask task = new PollingTask(GET_URL);
        Future<Document> future = executor.submit(task);
        int timeout = 180;
        try {
            return future.get(timeout, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            System.out.format("Polling task has timed out. Gave up after %s minutes. %n", timeout/60);
            task.cancel();
            throw new IOException(ex);
        }
    }
}
