import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/getparams")
public class GetParamsServ extends HttpServlet
{
    private ServletContext context;
    private String resBundleServ;

    public void init()
    {
        context = getServletContext();
        resBundleServ = context.getInitParameter("resBundleServ");
    }

    public void serviceRequest(HttpServletRequest req,
                               HttpServletResponse resp)
            throws ServletException, IOException
    {
        RequestDispatcher disp = context.getRequestDispatcher(resBundleServ);
        disp.include(req, resp);

        String charset = BundleInfo.getCharset();
        String[] headers = BundleInfo.getHeaders();
        String[] pnames = BundleInfo.getCommandParamNames();
        String[] pdes   = BundleInfo.getCommandParamDescr();
        String submitMsg = BundleInfo.getSubmitMsg();

        req.setCharacterEncoding(charset);

        HttpSession session = req.getSession();

        resp.setCharacterEncoding(charset);
        PrintWriter out = resp.getWriter();

        out.println("<center><h2>");
        for (String header : headers)
            out.println(header);
        out.println("</center></h2><hr>");

        out.println("<form method=\"post\">");
        for (int i=0; i<pnames.length; i++)
        {
            out.println(pdes[i] + "<br>");
            out.print("<input type=\"text\" size=\"30\" name=\"" +
                    pnames[i] +  "\"");
            String pval = (String) session.getAttribute("param_"+pnames[i]);

            if (pval != null)
                out.print(" value=\"" + pval + "\"");
            out.println("><br>");
        }
        out.println("<br><input type=\"submit\" value=\"" + submitMsg + "\">");
        out.println("</form>");

        for (String pname : pnames)
        {
            String paramVal = req.getParameter(pname);
            if (paramVal == null) return;
            session.setAttribute("param_" + pname, paramVal);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        serviceRequest(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        serviceRequest(req,resp);
    }
}