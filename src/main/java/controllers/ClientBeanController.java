package controllers;

import beans.ClientBean;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Author: Eduard Dedu
 * eduardcdedu@gmail.com
 * Created on: 09/06/16
 */
@WebServlet("/client-params")
public class ClientBeanController extends HttpServlet {

    @Inject
    private ClientBean clientBean;

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        clientBean.setName(req.getParameter("clientname"));
        clientBean.setApiKey(req.getParameter("apikey"));
        resp.sendRedirect("signin.xhtml");
    }


}
