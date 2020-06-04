import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

public class ErrorHandler extends HttpServlet {

    public void serviceRequest(HttpServletRequest req,
                               HttpServletResponse resp)
            throws ServletException, IOException {
        String charset = BundleInfo.getCharset();
        resp.setContentType("text/html; charset=" + charset);
        Throwable exc = (Throwable)
                req.getAttribute("javax.servlet.error.exception");

        if (exc != null) {
            PrintWriter out = resp.getWriter();
            out.println("<h2>" + exc.getMessage() + "</h2><hr>");
            Throwable cause = exc.getCause();
            if (cause instanceof SQLException) {
                SQLException sqlexc = (SQLException) cause;
                out.println(sqlexc.getMessage() + "<br><br>");
                out.println("Error code: " + sqlexc.getErrorCode() + "<br>");
                out.println("SQL state : " + sqlexc.getSQLState() + "<br>");
            }
            out.close();
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        serviceRequest(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        serviceRequest(req,resp);
    }
    //... metody doGet i doPost wołają serviceRequest
}