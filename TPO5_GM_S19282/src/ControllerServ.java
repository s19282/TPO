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
@WebServlet("/regex")
public class ControllerServ extends HttpServlet {

    private ServletContext context;
    private Command command;            // obiekt klasy wykonawczej
    private String presentationServ;    // nazwa serwlet prezentacji
    private String getParamsServ;       // mazwa serwletu pobierania parametrów
    private Object lock = new Object(); // semafor dla synchronizacji
    // odwołań wielu wątków
    public void init() {
        context = getServletContext();

        presentationServ = context.getInitParameter("presentationServ");
        getParamsServ = context.getInitParameter("getParamsServ");
        String commandClassName = context.getInitParameter("commandClassName");

        // Załadowanie klasy Command i utworzenie jej egzemplarza
        // który będzie wykonywał pracę
        try {
            Class commandClass = Class.forName(commandClassName);
            command = (Command) commandClass.newInstance();
        } catch (Exception exc) {
            throw new NoCommandException("Couldn't find or instantiate " +
                    commandClassName);
        }
    }

    // Obsługa zleceń
    public void serviceRequest(HttpServletRequest req,
                               HttpServletResponse resp)
            throws ServletException, IOException
    {
        System.out.println("Controller");
        resp.setContentType("text/html");

        // Wywolanie serwletu pobierania parametrów
        RequestDispatcher disp = context.getRequestDispatcher(getParamsServ);
        disp.include(req,resp);

        // Pobranie bieżącej sesji
        // i z jej atrybutów - wartości parametrów
        // ustalonych przez servlet pobierania parametrów
        // Różne informacje o aplikacji (np. nazwy parametrów)
        // są wygodnie dostępne poprzez własną klasę BundleInfo

        HttpSession ses = req.getSession();

        String[] pnames = BundleInfo.getCommandParamNames();
        for (int i=0; i<pnames.length; i++) {

            String pval = (String) ses.getAttribute("param_"+pnames[i]);

            if (pval == null) return;  // jeszcze nie ma parametrów

            // Ustalenie tych parametrów dla Command
            command.setParameter(pnames[i], pval);
        }

        // Wykonanie działań definiowanych przez Command
        // i pobranie wyników
        // Ponieważ do serwletu może naraz odwoływać sie wielu klientów
        // (wiele watków) - potrzebna jest synchronizacja
        // przy czym rrygiel zamkniemy tutaj, a otworzymy w innym fragmnencie kodu
        // - w serwlecie przentacji (cały cykl od wykonania cmd do poazania wyników jest sekcją krytyczną)

        Lock mainLock = new ReentrantLock();

        mainLock.lock();
        // wykonanie
        command.execute();

        // pobranie wyników
        List results = (List) command.getResults();

        // Pobranie i zapamiętanie kodu wyniku (dla servletu prezentacji)
        ses.setAttribute("StatusCode", new Integer(command.getStatusCode()));

        // Wyniki - będą dostępne jako atrybut sesji
        ses.setAttribute("Results", results);
        ses.setAttribute("Lock", mainLock);    // zapiszmy lock, aby mozna go było otworzyć później


        // Wywołanie serwletu prezentacji
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