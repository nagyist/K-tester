package kontomatik;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by eduarddedu on 02/07/16.
 */
class HttpUtil {
    private final String USER_AGENT = "Mozilla/5.0";
    private String text = null;
    private URL obj;
    private HttpURLConnection con = null;

    public String getResponse() {
        if (text == null)
            throw new IllegalStateException("Http request was not submitted or it failed");
        return text;
    }

    public int getResponseCode() throws IOException {
        if (con == null)
            throw new IllegalStateException("Http request was not submitted or it failed");
        return con.getResponseCode();
    }

    private void setResponse() throws IOException {
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        StringBuilder response = new StringBuilder();
        String s;
        while ((s = in.readLine()) != null) {
            response.append(s);
        }
        in.close();
        text = response.toString();
    }

    protected HttpUtil doGetRequest(String GET_URL) throws IOException {
        obj = new URL(GET_URL);
        con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);
        setResponse();
        return this;
    }


    protected HttpUtil doPostRequest(String POST_URL, String POST_PARAMS) throws IOException {
        obj = new URL(POST_URL);
        con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setDoOutput(true);

        OutputStream os = con.getOutputStream();
        os.write(POST_PARAMS.getBytes());
        os.flush();
        os.close();
        setResponse();
        return this;
    }
}
