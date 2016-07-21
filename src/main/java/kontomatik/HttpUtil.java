package kontomatik;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by eduarddedu on 02/07/16.
 */
class HttpUtil {
    /**
     * Http utility class based on java.net client library for Http requests.
     * The class has methods that fire up an Http GET or POST request and initialise a String variable with the remote
     * server response
     * The response can then be retrieved using getResponse(). Likewise, the response code can be obtained
     * with getResponseCode()
     */
    private final String USER_AGENT = "Mozilla/5.0";
    private URL obj;
    private HttpURLConnection con = null;

    protected int getResponseCode() throws IOException {
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


    protected InputStream getConnectionInputStream() throws IOException {
        try {
            return new BufferedInputStream(con.getInputStream());
        } catch (IOException e) { // Happens when the remote server responds with a code > 400
            InputStream in = con.getErrorStream();
            if (in == null)
                throw new IOException("No error stream available");
            return in;
        }
    }

}
