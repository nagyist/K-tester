package kontomatik;


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
 * Created on: 19/07/16
 */
@WebServlet("error")
public class Error extends HttpServlet {
    private static String xmlResponse;
    public static void setXmlResponse(String s) { xmlResponse = s; }

    @Override public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        out.print(xmlResponse);
    }
}
