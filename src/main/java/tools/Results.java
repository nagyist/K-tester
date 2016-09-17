package tools;

import kontomatik.URLs;
import org.w3c.dom.Document;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by eduarddedu on 30/06/16.
 */
@WebServlet("results")
public class Results extends HttpServlet {
    @Inject
    kontomatik.Session session;

    private kontomatik.URLs urls = new URLs();



    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Do not accept direct requests from the browser, redirect to the sign-in page
        resp.sendRedirect("signin.xhtml");
    }


    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Are we logged in ?
        HttpSession hs = req.getSession(false);
        if (hs == null || hs.getAttribute("logged") == null || !(boolean) hs.getAttribute("logged")) {
            resp.sendRedirect("signin.xhtml");
            return;
        }
        String cmd = req.getParameter("command");
        resp.setContentType("text/xml");
        resp.setCharacterEncoding("UTF-8");
        Document document = null;
        try ( OutputStream out = resp.getOutputStream() ) {
            switch (cmd) {
                case "import-owners-details":
                    document = session.getDocument(urls.POST_IMPORT_OWNERS, null, 30);
                    break;
                case "import-accounts":
                    document = session.getDocument(urls.POST_IMPORT_ACCOUNTS, null, 60);
                    break;
                case "import-account-transactions":
                    String params = "&iban=" + req.getParameter("iban") + "&since=" + req.getParameter("since");
                    document = session.getDocument(urls.POST_IMPORT_ACCOUNT_TRANSACTIONS, params, 60);
                    break;
                case "default-import":
                    params = "&since=" + req.getParameter("since");
                    document = session.getDocument(urls.POST_DEFAULT_IMPORT, params, 60);
                    break;
                case "aggregated-values":
                    document = session.getAggregatesResponse(req.getParameter("periodMonths"));
                    break;
                case "sign-out":
                    document = session.getDocument(urls.POST_SIGN_OUT);
                    break;
            }
            XmlParser.writeToOutputStream(document, out);
        } catch (Exception e) {
            System.out.println(e.toString());
            resp.setContentType("text/html"); // ? problem?
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
