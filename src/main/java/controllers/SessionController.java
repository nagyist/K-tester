package controllers;

import kontomatik.ResourcesBean;
import kontomatik.Session;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Eduard Dedu
 * eduardcdedu@gmail.com
 * Created on: 09/06/16
 */
@WebServlet("session-params")
public class SessionController extends HttpServlet {

    @Inject
    Session ks;
    @Inject
    ResourcesBean resourcesBean;

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ks.setSignature(
                req.getParameter("sessionId"),
                req.getParameter("sessionIdSignature"),
                resourcesBean.getApiKey());
        HttpSession session = req.getSession(true); // Create a session if it doesn't exit
        session.setAttribute("logged", true);
    }

}
