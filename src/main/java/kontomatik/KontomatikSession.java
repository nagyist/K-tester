package kontomatik;


import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by eduarddedu on 03/07/16.
 */
@ApplicationScoped
public class KontomatikSession {

    private String SIGNATURE;


    public void setSignature(String sessionId, String sessionIdSignature, String apiKey) {
        System.out.println("Inside setSignature()");
        this.SIGNATURE = "apiKey=" + apiKey + "&sessionId=" + sessionId + "&sessionIdSignature=" + sessionIdSignature;
    }


    private Map<String, String> map = new HashMap<>();

    {
        map.put("default-import", "https://test.api.kontomatik.com/v1/command/default-import.xml");
        map.put("import-owners", "https://test.api.kontomatik.com/v1/command/import-owners.xml");
        map.put("import-accounts", "https://test.api.kontomatik.com/v1/command/import-accounts.xml");
        map.put("import-transactions", "https://test.api.kontomatik.com/v1/command/import-account-transactions.xml");
        map.put("poll-command-status", "https://test.api.kontomatik.com/v1/command/");
    }


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
                map.get("poll-command-status"), id, SIGNATURE);
    }

    private ExecutorService executor = Executors.newSingleThreadExecutor();

    private String pollForCommandStatus(HttpUtil h) {
        String GET_URL = createGetUrl(h.getResponse());
        PollStatusTask task = new PollStatusTask(GET_URL, h);
        Future<Boolean> future = executor.submit(task);
        try {
            Boolean success = future.get(60, TimeUnit.SECONDS);
            future.cancel(true);
            if (success) {
                return h.getResponse();
            } else {
                return "PollStatusTask has timed out. Final response: " + h.getResponse();
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

    }


    public String executeImportOwners() throws IOException {

        String POST_URL = map.get("import-owners");
        HttpUtil h = new HttpUtil().doPostRequest(POST_URL, SIGNATURE);
        int responseCode = h.getResponseCode();
        System.out.println("POST Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_ACCEPTED) { //success
            return pollForCommandStatus(h);
        } else {
            return "Error: " + h.getResponse();
        }
    }
}




