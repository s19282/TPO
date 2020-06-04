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
import java.util.List;
import java.util.concurrent.locks.Lock;

@WebServlet("/presentation")
public class ResultPresent extends HttpServlet
{
    public void serviceRequest(HttpServletRequest req,
                               HttpServletResponse resp)
            throws ServletException, IOException
    {
        ServletContext context = getServletContext();
        String getParamsServ = context.getInitParameter("getParamsServ");
        RequestDispatcher disp = context.getRequestDispatcher(getParamsServ);
        disp.include(req,resp);

        HttpSession ses = req.getSession();
        Lock mainLock = (Lock) ses.getAttribute("Lock");
        mainLock.unlock();
        List results = (List) ses.getAttribute("Results");
        Integer code = (Integer) ses.getAttribute("StatusCode");


        PrintWriter out = resp.getWriter();
        out.println("<hr>");

        String msg = BundleInfo.getStatusMsg()[code];
        out.println("<h2>" + msg + "</h2>");

        String[] dopiski = BundleInfo.getResultDescr();

        out.println("<ul>");
        for (Object result : results)
        {
            out.println("<li>");
            int dlen = dopiski.length;
            if (result.getClass().isArray())
            {
                Object[] res1 = (Object[]) result;
                int i;
                for (i = 0; i < res1.length; i++)
                {
                    String dopisek = (i < dlen ? dopiski[i] + " " : "");
                    out.print(dopisek + res1[i] + " ");
                }
                if (dlen > res1.length)
                    out.println(dopiski[i]);
            }
            else
            {
                if (dlen > 0)
                    out.print(dopiski[0] + " ");
                out.print(result);
                if (dlen > 1)
                    out.println(" " + dopiski[1]);
            }
            out.println("</li>");
        }
        out.println("</ul>");
    }


    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
            throws ServletException, IOException
    {
        serviceRequest(request, response);
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response)
            throws ServletException, IOException
    {
        serviceRequest(request, response);
    }

}