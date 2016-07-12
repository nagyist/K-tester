package controllers;

import kontomatik.ResourcesBean;
import kontomatik.Session;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Author: Eduard Dedu
 * eduardcdedu@gmail.com
 * Created on: 09/06/16
 */
@WebServlet("session-params")
public class SessionController extends HttpServlet {

    @Inject
    Session session;
    @Inject
    ResourcesBean resourcesBean;

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        session.setSignature(
                req.getParameter("sessionId"),
                req.getParameter("sessionIdSignature"),
                resourcesBean.getApiKey());
        PrintWriter out = resp.getWriter();
        out.println("Session params successfully set.");
    }

}
