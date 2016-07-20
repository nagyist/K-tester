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
    private String response = null;


    protected String getResponse() {
        return response;
    }

    private void setResponse() throws IOException {
        StringBuilder sb = new StringBuilder();
        int x;
        try ( InputStream in = new BufferedInputStream(con.getInputStream()) ) {
            while ((x = in.read()) != -1)
                sb.append((char) x);
            response = sb.toString();
        } catch (IOException e) { // When the remote server responds with a 404 getInputStream() throws an IOException
            try ( InputStream in = con.getErrorStream()) {
                while ((x = in.read()) != -1)
                    sb.append((char) x);
                throw new IOException(sb.toString()); // Server error message will be retrieved and propagated to the frontend layer
            } catch(NullPointerException ex) { // We don't have an error stream available
                throw new IOException("No message available");
            }
        }
    }

    protected int getResponseCode() throws IOException {
        return con.getResponseCode();
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


    protected InputStream getConnectionInputStream(String GET_URL) throws IOException {
        obj = new URL(GET_URL);
        con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);
        try {
            return new BufferedInputStream(con.getInputStream());
        } catch(IOException e) {
            try {
                InputStream in = con.getErrorStream();
                int x;
                StringBuilder sb = new StringBuilder();
                while ((x = in.read()) != -1)
                    sb.append((char) x);
                throw new IOException(sb.toString());
            } catch(Exception ex) {
                throw new IOException(ex.toString());
            }
        }
    }


}
