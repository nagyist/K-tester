package kontomatik;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by eduarddedu on 30/06/16.
 */
@WebServlet("/results")
public class Results extends HttpServlet {
    @Inject
    Session session;

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Check if end user is signed in:
        if (session.getSignature() == null) {
            resp.sendRedirect("signin.xhtml");
            return;
        }
        PrintWriter out = resp.getWriter();
        String cmd = req.getParameter("command");
        String result = null;

        switch (cmd) {
            case "import-owners-details":
                result = session.executeCommand(Urls.IMPORT_OWNERS, null, 10000);
                break;
            case "import-accounts":
                result = session.executeCommand(Urls.IMPORT_ACCOUNTS, null, 15000);
                break;
            case "import-account-transactions":
                String params = "&iban=" + req.getParameter("iban") + "&since=" + req.getParameter("since");
                result = session.executeCommand(Urls.IMPORT_ACCOUNT_TRANSACTIONS, params, 15000);
                break;
            case "default-import":
                params = "&since=" + req.getParameter("since");
                result = session.executeCommand(Urls.DEFAULT_IMPORT, params, 24000);
                break;
            case "aggregated-values":
                result = session.getAggregates(req.getParameter("periodMonths"));
                break;
        }
        out.println(result);
    }
}
