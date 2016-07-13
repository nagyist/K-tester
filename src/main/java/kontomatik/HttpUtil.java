package kontomatik;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by eduarddedu on 02/07/16.
 */
class HttpUtil {
    /**
     * Http utility class based on java.net library.
     * Fires up an Http GET or POST request and loads the server response into a String
     * The String can be retrieved using getResponse() and the response code can be obtained
     * with getResponseCode()
     */
    private final String USER_AGENT = "Mozilla/5.0";
    private URL obj;
    private HttpURLConnection con = null;

    private void checkConnectionStatus() {
        if (con == null)
            throw new IllegalStateException("Http request was never submitted or it failed");
    }

    public String getResponse() throws IOException {
        checkConnectionStatus();
        String text;
        if (con.getResponseCode() >= 400) {  // HttpURLConnection.getInputStream() would throw an IOException in this case
            text = con.getResponseMessage();
        } else {
            InputStream in = new BufferedInputStream(con.getInputStream());
            int x;
            StringBuilder sb = new StringBuilder();
            while ((x = in.read()) != -1)
                sb.append((char) x);
            in.close();
            text = sb.toString();
        }
        return text;
    }

    public int getResponseCode() throws IOException {
        checkConnectionStatus();
        return con.getResponseCode();
    }

    protected HttpUtil doGetRequest(String GET_URL) throws IOException {
        obj = new URL(GET_URL);
        con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);
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
        return this;
    }
}
