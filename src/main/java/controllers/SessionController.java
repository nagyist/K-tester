package controllers;

import kontomatik.ResourcesBean;
import kontomatik.Session;
import kontomatik.XmlParser;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Author: Eduard Dedu
 * eduardcdedu@gmail.com
 * Created on: 09/06/16
 */
@WebServlet("session")
public class SessionController extends HttpServlet {

    @Inject
    Session ks;
    @Inject
    ResourcesBean resourcesBean;


    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String apiKey = resourcesBean.getApiKey();
        String sessionId = req.getParameter("sessionId");
        String sessionIdSignature = req.getParameter("sessionIdSignature");
        ks.setSignature(sessionId, sessionIdSignature, apiKey);
        HttpSession session = req.getSession(true); // Create a session if it doesn't exit
        session.setAttribute("logged", true);
        PrintWriter out = resp.getWriter();

        try {
            String targetName = req.getParameter("target");
            List<String> commands = new XmlParser().getBankCommandsList(ks.requestCatalog(), targetName);

/*
            if (commands.contains("ImportOwnersDetailsCommand"))

            if (commands.contains("ImportAccountsCommand"))

            if (commands.contains("ImportAccountTransactionsCommand"))

            if (commands.contains("DefaultImportCommand"))
            */


        } catch(IOException ex) {
            System.err.println(ex.toString());
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }



    }

}
