package tools;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by eduarddedu on 02/07/16.
 */

/**
 * Fire up an Http request using java.net.
 */

class HttpRequest {

    private HttpURLConnection con = null;
    private URL urlObject;

    public URL getUrlObject() {
        return urlObject;
    }

    public HttpURLConnection getConnnection() { return con; }

    public HttpRequest(String method, URL obj) throws IOException {
        this(method, obj, null);
    }

    public HttpRequest(String method, URL obj, String params) throws IOException {
        if (method == null || !(
                        method.equalsIgnoreCase("get") ||
                        method.equalsIgnoreCase("post") ||
                        method.equalsIgnoreCase("delete"))) {
            throw new IllegalArgumentException("No such method: " + method);
        }
        urlObject = obj;
        con = (HttpURLConnection) urlObject.openConnection();
        con.setRequestMethod(method);
        con.setRequestProperty("User-Agent", "Mozilla/5.0");

        if (params != null) {
            con.setDoOutput(true);
            OutputStream os = con.getOutputStream();
            os.write(params.getBytes());
            os.flush();
            os.close();
        }

    }


    public int getResponseCode() throws IOException {
        return con.getResponseCode();
    }



    public InputStream getInputStream() throws IOException {
        try {
            return new BufferedInputStream(con.getInputStream());
        } catch (IOException e) { // Happens when the remote server responds with a code > 400
            InputStream in = con.getErrorStream();
            if (in == null)
                throw new IOException("No stream available");
            return in;
        }
    }

}
