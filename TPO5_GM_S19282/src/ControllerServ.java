import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@WebServlet("/db")
public class ControllerServ extends HttpServlet {

    private ServletContext context;
    private Command command;
    private String presentationServ;
    private String getParamsServ;
    private Object lock = new Object();

    public void init()
    {
        context = getServletContext();

        presentationServ = context.getInitParameter("presentationServ");
        getParamsServ = context.getInitParameter("getParamsServ");
        String commandClassName = context.getInitParameter("commandClassName");
        String dbName = context.getInitParameter("dbName");

        try
        {
            Class commandClass = Class.forName(commandClassName);
            command = (Command) commandClass.newInstance();
            command.setParameter("dbName", dbName);
            command.init();
        }
        catch (Exception exc)
        {
            throw new NoCommandException("Couldn't find or instantiate " + commandClassName);
        }
    }

    public void serviceRequest(HttpServletRequest req,
                               HttpServletResponse resp)
            throws ServletException, IOException
    {
        resp.setContentType("text/html");
        RequestDispatcher disp = context.getRequestDispatcher(getParamsServ);
        disp.include(req,resp);
        HttpSession ses = req.getSession();

        String[] pnames = BundleInfo.getCommandParamNames();
        for (String pname : pnames)
        {
            String pval = (String) ses.getAttribute("param_" + pname);
            if (pval == null) return;
            command.setParameter(pname, pval);
        }

        Lock mainLock = new ReentrantLock();
        mainLock.lock();

        command.execute();

        List results = (List) command.getResults();

        ses.setAttribute("StatusCode", command.getStatusCode());
        ses.setAttribute("Results", results);
        ses.setAttribute("Lock", mainLock);

        disp = context.getRequestDispatcher(presentationServ);
        disp.forward(req, resp);
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