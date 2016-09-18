package tools;

import controllers.SessionServlet;

import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Author: Eduard Dedu
 * eduardcdedu@gmail.com
 * Created on: 17/09/16
 */

@WebServlet("/results")
public class ResultsServlet extends HttpServlet {
    @Inject ResourcesBean resourcesBean;

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) {
        // Do not accept direct requests from the browser, redirect to the sign-in page
        try {
            resp.sendRedirect("signin.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            if (new HttpRequest("GET", KontomatikServiceURL.GET_HEALTH_CHECK).getResponseCode() != 200) { // set timeout
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Kontomatik Service appears to be down.");
            }
            HttpSession clientSession = req.getSession(false);
            if (clientSession == null || clientSession.getAttribute("logged") == null // when is this the case?
                    || !(boolean) clientSession.getAttribute("logged")) {
                resp.sendRedirect("signin.xhtml");
                return;
            }
            SessionServlet sessionServlet = (SessionServlet) clientSession.getAttribute("servlet");
            String sessionSignature = sessionServlet.getSignature();
            org.w3c.dom.Document document;
            String commandName = req.getParameter("command");
            KontomatikServiceExecutor executor = new KontomatikServiceExecutor();

            switch(commandName)  {
                case "import-owners-details" :
                    document = executor.callImportOwners(sessionSignature); break;

                case "import-accounts" :
                    document = executor.callImportAccounts(sessionSignature); break;

                case "import-account-transactions" :
                    String data = String.format("%s&iban=%s&since=%s",
                            sessionSignature,
                            req.getParameter("iban"),
                            req.getParameter("since"));
                    document = executor.callImportAccountTransactions(sessionSignature, data); break;

                case "default-import": data = String.format("%s&since=%s", sessionSignature, req.getParameter("since"));
                    document = executor.callDefaultImport(sessionSignature, data); break;

                case "aggregated-values" :
                    data = String.format("%s&monthsPeriod=%s&ownerExternalId=%s", sessionSignature,
                            req.getParameter("monthsPeriod"),
                            resourcesBean.getOwnerExternalId());
                    document = executor.callAggregatedValues(data); break;

                case "sign-out" : document = executor.callSignOut(sessionSignature); break;

                default: throw new IOException("No such command!");
            }

            resp.setContentType("text/xml");
            resp.setCharacterEncoding("UTF-8");
            OutputStream out = resp.getOutputStream();
            XmlParser.writeToOutputStream(document, out);
        } catch (IOException ex) {
            try {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, ex.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }




}
