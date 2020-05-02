/**
 *
 *  @author Godlewski Mateusz S19282
 *
 */

package zad4;


import java.util.List;
import java.util.concurrent.*;

public class ClientTask extends FutureTask<String>
{

    public ClientTask(Callable<String> c)
    {
        super(c);
    }

    public static ClientTask create(Client c, List<String> reqs, boolean showSendRes)
    {
        return new ClientTask(()->{
            String log;
            c.connect();
            if (showSendRes)
            {
                System.out.println(c.send("login " + c.getClientID()));
                for (String request : reqs)
                    System.out.println(c.send(request));
                log=c.send("bye and log transfer");
                System.out.println(log);
                return log;
            }
            else
            {
                c.send("login " + c.getClientID());
                for (String request : reqs)
                    c.send(request);
                return c.send("bye and log transfer");
            }
        });
    }

    public String get() throws InterruptedException, ExecutionException
    {
        return super.get();
    }
}
