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
import utils.QueryParser;

/**
 * Author: Eduard Dedu
 * eduardcdedu@gmail.com
 * Created on: 09/06/16
 */
@WebServlet("/client-params")
public class ClientController extends HttpServlet {

    //TODO: research and use action methods if possible
    @Inject private ClientBean clientBean;



    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain");
        PrintWriter writer = resp.getWriter();
        writer.println(req.getQueryString());
        Hashtable<String, String> table = new QueryParser().parseQueryString(req.getQueryString());
        clientBean.setName(table.get("clientname"));
        clientBean.setApiKey(table.get("apikey"));


    }


    /*
    @Named
    @Produces
    @ApplicationScoped
    private void setClient() {
        clientBean = new ClientBean();
    }
    */


}
