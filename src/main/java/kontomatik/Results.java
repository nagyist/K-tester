package kontomatik;

import org.w3c.dom.Document;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by eduarddedu on 30/06/16.
 */
@WebServlet("results")
public class Results extends HttpServlet {
    @Inject
    SessionBean sessionBean;



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
        try ( PrintWriter out = resp.getWriter() ) {
            switch (cmd) {
                case "import-owners-details":
                    document = sessionBean.getCommandResponse(Urls.IMPORT_OWNERS, null, 10000);
                    break;
                case "import-accounts":
                    document = sessionBean.getCommandResponse(Urls.IMPORT_ACCOUNTS, null, 15000);
                    break;
                case "import-account-transactions":
                    String params = "&iban=" + req.getParameter("iban") + "&since=" + req.getParameter("since");
                    document = sessionBean.getCommandResponse(Urls.IMPORT_ACCOUNT_TRANSACTIONS, params, 15000);
                    break;
                case "default-import":
                    hs.setAttribute("logged", false);
                    params = "&since=" + req.getParameter("since");
                    document = sessionBean.getCommandResponse(Urls.DEFAULT_IMPORT, params, 24000);
                    break;
                case "aggregated-values":
                    document = sessionBean.getAggregatesResponse(req.getParameter("periodMonths"));
                    break;
                case "sign-out":
                    document = sessionBean.getCommandResponse(Urls.SIGN_OUT);
                    break;
            }
            XmlParser.writeToOutputStream(document, out);
        } catch (TransformerException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            System.out.println(e.toString());
        }
    }
}
