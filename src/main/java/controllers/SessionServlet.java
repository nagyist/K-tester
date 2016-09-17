package controllers;

import tools.ResourcesBean;
import kontomatik.Session;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Author: Eduard Dedu
 * eduardcdedu@gmail.com
 * Created on: 09/06/16
 */
@WebServlet("session/data")
public class SessionServlet extends HttpServlet {

    @Inject
    Session session;
    @Inject
    ResourcesBean resourcesBean;

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect("signin.xhtml");
    }


    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String apiKey = resourcesBean.getApiKey();
        String sessionId = req.getParameter("sessionId");
        String sessionIdSignature = req.getParameter("sessionIdSignature");
        session.setSignature(sessionId, sessionIdSignature, apiKey);
        session.setOwnerId(req.getParameter("ownerId"));
        HttpSession session = req.getSession(true); // Create a session if it doesn't exit
        session.setAttribute("logged", true);

    }

}
