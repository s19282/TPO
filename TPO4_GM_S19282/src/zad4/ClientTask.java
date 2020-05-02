/**
 *
 *  @author Godlewski Mateusz S19282
 *
 */

package zad4;


import java.util.List;
import java.util.concurrent.ExecutionException;

public class ClientTask extends Throwable implements Runnable
{
    Client c;
    List<String> reqs;
    boolean showSendRes;
    private String log;

    public ClientTask(Client c, List<String> reqs, boolean showSendRes)
    {
        this.c = c;
        this.reqs = reqs;
        this.showSendRes = showSendRes;
    }

    public static ClientTask create(Client c, List<String> reqs, boolean showSendRes)
    {
        return new ClientTask(c,reqs,showSendRes);
    }

    public String get() throws InterruptedException, ExecutionException
    {
        if(this.getCause()!=null)
            throw new ExecutionException(this.getCause());

        synchronized (this)
        {
            while (log == null)
                wait();
            return log;
        }
    }

    @Override
    public  void run()
    {
        synchronized (this)
        {
            c.connect();
            if (showSendRes)
            {
                System.out.println(c.send("login " + c.getClientID()));
                for (String request : reqs)
                    System.out.println(c.send(request));
                log=c.send("bye and log transfer");
                System.out.println(log);
            }
            else
            {
                c.send("login " + c.getClientID());
                for (String request : reqs)
                    c.send(request);
                log=c.send("bye and log transfer");
            }
            notify();
        }
    }
}
