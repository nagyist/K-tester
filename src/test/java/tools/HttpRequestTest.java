package tools;

import junit.framework.TestCase;

import java.io.IOException;
import java.net.URL;

/**
 * Created by eduarddedu on 18/09/16.
 */
public class HttpRequestTest extends TestCase {

    public void testGetUrl() {
        try {
            HttpRequest request = new HttpRequest("GET", new URL("http://wwww.example.com"));
            assertTrue(request.getUrlObject().toString() != null && !request.getUrlObject().toString().isEmpty());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}