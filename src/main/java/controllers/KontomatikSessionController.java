package controllers;

import kontomatik.KontomatikClient;
import kontomatik.KontomatikSession;

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
@WebServlet("/session-params")
public class KontomatikSessionController extends HttpServlet {

    @Inject
    KontomatikSession kontomatikSession;
    @Inject
    KontomatikClient kontomatikClient;

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        kontomatikSession.setSignature(
                req.getParameter("sessionId"),
                req.getParameter("sessionIdSignature"),
                kontomatikClient.getApiKey());
        PrintWriter out = resp.getWriter();
        out.println("Hello from the server-side. Session params successfully transmitted.");
    }

}
