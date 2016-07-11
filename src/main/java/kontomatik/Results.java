package kontomatik;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * Created by eduarddedu on 30/06/16.
 */
@WebServlet("/results")
public class Results extends HttpServlet {
    @Inject
    KontomatikSession ks;

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Check if end user is signed in:
        if (ks.getSignature() == null) {
            resp.sendRedirect("signin.xhtml");
            return;
        }
        PrintWriter out = resp.getWriter();
        String cmd = req.getParameter("command");
        String result = null;
        try {
            switch (cmd) {
                case "import-owners-details":
                    result = ks.executeCommand(Urls.IMPORT_OWNERS, null, 10000);
                    break;
                case "import-accounts":
                    result = ks.executeCommand(Urls.IMPORT_ACCOUNTS, null, 15000);
                    break;
                case "import-account-transactions":
                    String params = "&iban=" + req.getParameter("iban") + "&since=" + req.getParameter("since");
                    result = ks.executeCommand(Urls.IMPORT_ACCOUNT_TRANSACTIONS, params, 15000);
                    break;
                case "default-import":
                    params = "&since=" + req.getParameter("since");
                    result = ks.executeCommand(Urls.DEFAULT_IMPORT, params, 24000);
                    break;
                case "aggregates":
                    result = null;
                    break;
            }
            out.println(result);
        } catch(IOException ex) {
            /*HttpConnection artificially throws an IOException if the remote server returns a response code >= 400
              (Additionally see https://bugs.openjdk.java.net/browse/JDK-6400786)
              Most likely cause for 400 response is that the session with the remote bank has expired either because
              of timeout or because after executing default-import the end-user is effectively signed out.*/
            ex.printStackTrace();
            resp.sendRedirect("signin.xhtml");
        }
    }
}
