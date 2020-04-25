/**
 *
 *  @author Godlewski Mateusz S19282
 *
 */

package zad4;


import java.util.List;

public class ClientTask implements Runnable
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


    @Override
    public void run()
    {
        c.connect();
        c.send("login "+c.getClientID());
        for(String request : reqs)
            c.send(request);
        c.send("bye and log transfer");
    }
}
