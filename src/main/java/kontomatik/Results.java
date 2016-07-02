package kontomatik;

import beans.ClientBean;
import beans.SessionBean;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by eduarddedu on 30/06/16.
 */
@WebServlet("/results")
public class Results extends HttpServlet {

    @Inject
    SessionBean sessionBean;
    @Inject
    ClientBean clientBean;

    private final String USER_AGENT = "Mozilla/5.0";

    private Map<String, String> map = new HashMap<>();

    {
        map.put("default-import", "https://test.api.kontomatik.com/v1/command/default-import.xml");
        map.put("import-owners", "https://test.api.kontomatik.com/v1/command/import-owners.xml");
        map.put("import-accounts", "https://test.api.kontomatik.com/v1/command/import-accounts.xml");
        map.put("import-transactions", "https://test.api.kontomatik.com/v1/command/import-account-transactions.xml");
    }

    private ExecutorService service = Executors.newSingleThreadExecutor();


    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Infer command from the query string and delegate to appropriate executor method:
        PrintWriter out = resp.getWriter();
        String cmd = req.getParameter("action");
        switch (cmd) {
            case "import-owners":
                String xml;
                try {
                    xml = executeImportOwners();
                    System.out.println(xml);
                    out.println(xml);
                } catch (IOException e) {
                    out.println("A network error has occurred: " + e.toString());
                }
                break;
            case "default-import":
                break;
            case "import-transactions":
                break;
            case "import-accounts":
                break;
            case "aggregates":
                break;
        }


    }

    private HttpURLConnection doPostRequest(String POST_URL, String POST_PARAMS) throws IOException {
        URL obj = new URL(POST_URL);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setReadTimeout(500);
        con.setDoOutput(true);

        OutputStream os = con.getOutputStream();
        os.write(POST_PARAMS.getBytes());
        os.flush();
        os.close();
        return con;
    }


    private String executeImportOwners() throws IOException {

        String POST_URL = map.get("import-owners");
        String POST_PARAMS = "apiKey=" + clientBean.getApiKey()
                + "&sessionId=" + sessionBean.getSessionId()
                + "&sessionIdSignature=" + sessionBean.getSessionIdSignature();
        HttpURLConnection con = doPostRequest(POST_URL, POST_PARAMS);
        int responseCode = con.getResponseCode();
        System.out.println("POST Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_ACCEPTED) { //success
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            StringBuilder response = new StringBuilder();
            String s;
            while ((s = in.readLine()) != null) {
                response.append(s);
            }
            in.close();
            return response.toString();
        } else {
            return ("Error. Http POST request failed with response code: " + responseCode);
        }
    }
}
