package controllers;

import kontomatik.ResourcesBean;
import kontomatik.SessionBean;
import kontomatik.XmlParser;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * Author: Eduard Dedu
 * eduardcdedu@gmail.com
 * Created on: 09/06/16
 */
@WebServlet("session")
public class SessionController extends HttpServlet {

    @Inject
    SessionBean sessionBean;
    @Inject
    ResourcesBean resourcesBean;
    @Inject kontomatik.Error error;

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect("signin.xhtml");
    }


    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String apiKey = resourcesBean.getApiKey();
        String sessionId = req.getParameter("sessionId");
        String sessionIdSignature = req.getParameter("sessionIdSignature");
        sessionBean.setSignature(sessionId, sessionIdSignature, apiKey);
        HttpSession session = req.getSession(true); // Create a session if it doesn't exit
        session.setAttribute("logged", true);

        try {
            String targetName = req.getParameter("target");
            List<String> commands = new XmlParser(sessionBean.requestCatalog()).getBankCommandsList(targetName);
            if (commands.contains("SignOutCommand"))
                sessionBean.setFormStyle("visibility:visible");
            else
                sessionBean.setFormStyle("visibility:hidden");
        } catch (IOException ex) {
            kontomatik.Error.setXmlResponse(ex.getMessage());
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }



    }

}
