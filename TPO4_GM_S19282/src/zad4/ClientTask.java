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

    public String get() throws InterruptedException, ExecutionException {
        if(Thread.interrupted())
            throw new InterruptedException();
        if(this.getCause()!=null)
            throw new ExecutionException(this.getCause());
        return c.getLog();
    }

    @Override
    public  void run()
    {
            c.connect();
            if (showSendRes)
            {
                System.out.println(c.send("login " + c.getClientID()));
                for (String request : reqs)
                    System.out.println(c.send(request));
                c.setLog(c.send("bye and log transfer"));
                try {
                    System.out.println(c.getLog());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                c.send("login " + c.getClientID());
                for (String request : reqs)
                    c.send(request);
                c.setLog(c.send("bye and log transfer"));
            }
    }
}
