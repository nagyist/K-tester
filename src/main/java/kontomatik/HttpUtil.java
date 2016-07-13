package kontomatik;

import java.io.*;
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

    private void checkConnectionStatus() {
        if (con == null)
            throw new IllegalStateException("Http request was never submitted or it failed");
    }

    public String getResponse() {
        checkConnectionStatus();
        return text;
    }

    public int getResponseCode() throws IOException {
        checkConnectionStatus();
        return con.getResponseCode();
    }

    private void setText(int responseCode) throws IOException {
        checkConnectionStatus();
        if (responseCode >= 400) {  // HttpURLConnection.getInputStream() would throw an IOException in this case
            text = con.getResponseMessage();
            return;
        }
        InputStream in = new BufferedInputStream(con.getInputStream());
        int x;
        StringBuilder sb = new StringBuilder();
        while ( (x = in.read()) != -1)
            sb.append( (char) x);
        in.close();
        text = sb.toString();
    }

    protected HttpUtil doGetRequest(String GET_URL) throws IOException {
        obj = new URL(GET_URL);
        con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);
        setText(con.getResponseCode());
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
        setText(con.getResponseCode());
        return this;
    }
}
