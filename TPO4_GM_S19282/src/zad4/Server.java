/**
 *
 *  @author Godlewski Mateusz S19282
 *
 */

package zad4;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server
{
    String host;
    int port;
    ServerSocketChannel server;
    InetSocketAddress isa;
    HashMap<String, List<String>> clientsLogs;
    List<String> serverRequests;
    boolean serverIsRunning = true;
    private Selector selector;
    private static final ByteBuffer inBuf = ByteBuffer.allocateDirect(1024);
    ExecutorService executor;
    public Server(String host, int port)
    {
        this.host = host;
        this.port = port;
        isa=new InetSocketAddress(host,port);
        executor = Executors.newSingleThreadExecutor();
        clientsLogs = new HashMap<>();
        serverRequests = new LinkedList<>();
    }

    public void startServer()
    {
        executor.execute(()->{
            serverIsRunning=true;
            try
            {
                server = ServerSocketChannel.open();
                server.socket().bind(isa);
                server.configureBlocking(false);
                selector = Selector.open();
                server.register(selector, SelectionKey.OP_ACCEPT);
                while (serverIsRunning)
                {
                    selector.select();
                    Set<SelectionKey> keys = selector.selectedKeys();
                    for (SelectionKey key : keys)
                    {
                        if (key.isAcceptable())
                        {
                            register(selector,server);
                        }
                        if(key.isReadable())
                        {
                            response(key);
                        }
                        keys.remove(key);
                    }
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        });
    }
    private void response(SelectionKey key) throws IOException
    {
        SocketChannel client = (SocketChannel) key.channel();
        client.read(inBuf);
        inBuf.flip();

        String request = StandardCharsets.UTF_8.decode(inBuf).toString().substring(1);
        String clientID = request.substring(0,request.indexOf('#'));
        request=request.substring(request.indexOf('#')+1);


        serverRequests.add(addRequestsLog(request,clientID));
        addClientLog(request,clientID);

        StringBuilder response = new StringBuilder();
        if(request.equals("bye"))
            response.append("logged out");
        else if(request.equals("bye and log transfer"))
        {
            response.append("=== ").append(clientID).append(" log start ===\n");
            for (String log : clientsLogs.get(clientID))
                response.append(log).append("\n");
            response.append("=== ").append(clientID).append(" log end ===\n");
        }
        else if(request.split(" ")[0].equals("login"))
            response.append("logged in");
        else
            response.append(Time.passed(request.split(" ")[0],request.split(" ")[1]));
        client.write(StandardCharsets.UTF_8.encode(response.toString()));
        inBuf.clear();
    }

    private void register(Selector selector, ServerSocketChannel serverSocket) throws IOException
    {
        SocketChannel client = serverSocket.accept();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);
    }
    private void addClientLog(String request, String clientID)
    {
        if(!clientsLogs.containsKey(clientID))
            clientsLogs.put(clientID, new LinkedList<>());

        switch (request.split(" ")[0])
        {
            case "login":
            {
                clientsLogs.get(clientID).add("logged in");
                break;
            }
            case  "bye":
            {
                clientsLogs.get(clientID).add("logged out");
                break;
            }
            default:
            {
                clientsLogs.get(clientID).add("Request: "+request);
                clientsLogs.get(clientID).add("Result:");
                clientsLogs.get(clientID).add(Time.passed(request.split(" ")[0],request.split(" ")[1]));
            }
        }
    }

    private String addRequestsLog(String request, String clientID)
    {
        StringBuilder log = new StringBuilder(clientID);
        switch (request.split(" ")[0])
        {
            case "login":
            {
                log.append(" logged in at ");
                break;
            }
            case  "bye":
            {
                log.append(" logged out at ");
                break;
            }
            default:
            {
                log.append(" request at ");
            }
        }
        log.append(new SimpleDateFormat("HH:mm:ss.SSS").format(System.currentTimeMillis()));
        return log.toString();
    }

    public void stopServer()
    {
        serverIsRunning = false;
        executor.shutdownNow();
    }

    String getServerRequests()
    {
        StringBuilder sb = new StringBuilder();
        for (String request : serverRequests)
            sb.append(request).append("\n");
        return sb.toString();
    }

}
