package kontomatik;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
        // Check if we're logged in
        HttpSession hs = req.getSession(false);
        if ( hs.getAttribute("logged") == null || !(boolean) hs.getAttribute("logged") ) {
            resp.sendRedirect("signin.xhtml");
            return;
        }
        PrintWriter out = resp.getWriter();
        String cmd = req.getParameter("command");
        String xml = null;
        try {
            switch (cmd) {
                case "import-owners-details":
                    xml = session.executeCommand(Urls.IMPORT_OWNERS, null, 10000);
                    break;
                case "import-accounts":
                    xml = session.executeCommand(Urls.IMPORT_ACCOUNTS, null, 15000);
                    break;
                case "import-account-transactions":
                    String params = "&iban=" + req.getParameter("iban") + "&since=" + req.getParameter("since");
                    xml = session.executeCommand(Urls.IMPORT_ACCOUNT_TRANSACTIONS, params, 15000);
                    break;
                case "default-import":
                    // Mark HttpSession as not logged in
                    hs.setAttribute("logged", false);
                    params = "&since=" + req.getParameter("since");
                    xml = session.executeCommand(Urls.DEFAULT_IMPORT, params, 24000);
                    break;
                case "aggregated-values":
                    xml = session.getAggregates(req.getParameter("periodMonths"));
                    break;
            }
            resp.setContentType("text/xml");
            out.println(xml);
        } catch (IOException ex) {
            resp.setContentType("text/html");
            out.println(ex.getMessage());
        } finally {
            out.close();
        }
    }
}
