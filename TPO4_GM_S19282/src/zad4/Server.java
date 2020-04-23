/**
 *
 *  @author Godlewski Mateusz S19282
 *
 */

package zad4;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server
{
    String host;
    int port;
    ServerSocket server;
    InetSocketAddress isa;
    HashMap<String, List<String>> clientLogs;
    List<String> clientsRequests;
    HashMap<String,String> protocol;
    boolean serverIsRunning = true;

    public Server(String host, int port)
    {
        this.host = host;
        this.port = port;
    }

    public void startServer()
    {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(()->
        {
            try
            {
                server = new ServerSocket();
                server.bind(isa);
                System.out.println("Server started on addres: " + server.getInetAddress().getHostName());
                System.out.println("Server listens on port: " + server.getLocalPort());
                while (serverIsRunning)
                {
                    Socket client = server.accept();
                    new ServerThread(client).start();
                }
                server.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        });

    }


    public void stopServer() throws IOException
    {
        serverIsRunning = false;
    }

    String getServerLog()
    {
        return "";
    }

}
