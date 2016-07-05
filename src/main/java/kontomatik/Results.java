package kontomatik;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by eduarddedu on 30/06/16.
 */
@WebServlet("/results")
public class Results extends HttpServlet {
    @Inject
    KontomatikSession kontomatikSession;

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        String cmd = req.getParameter("command");
        switch (cmd) {
            case "import-owners":
                String result;
                result = kontomatikSession.executeImportOwners();
                out.println(result);
                break;
            case "default-import":
                break;
            case "import-transactions":
                break;
            case "import-accounts":
                break;
            case "aggregates":
                break;
        }


    }
}
