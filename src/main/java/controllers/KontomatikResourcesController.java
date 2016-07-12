package controllers;

import kontomatik.ResourcesBean;

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
@WebServlet("client-params")
public class KontomatikResourcesController extends HttpServlet {

    @Inject
    private ResourcesBean resourcesBean;

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resourcesBean.setClientName(req.getParameter("clientname"));
        resourcesBean.setApiKey(req.getParameter("apikey"));
        resp.sendRedirect("signin.xhtml");
    }


}
