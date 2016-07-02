package controllers;

import beans.ClientBean;
import beans.SessionBean;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Hashtable;
import java.util.regex.PatternSyntaxException;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Author: Eduard Dedu
 * eduardcdedu@gmail.com
 * Created on: 09/06/16
 */
@WebServlet("/session-params")
public class SessionController extends HttpServlet {

    @Inject
    private SessionBean sessionBean;

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        sessionBean.setSessionId(req.getParameter("sessionId"));
        sessionBean.setSessionIdSignature(req.getParameter("sessionIdSignature"));
        sessionBean.setTarget(req.getParameter("target"));
        PrintWriter out = resp.getWriter();
        out.println("Hello from the server-side. Session params successfully transmitted.");
    }

}
