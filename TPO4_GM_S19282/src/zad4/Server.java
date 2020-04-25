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
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server
{
    String host;
    int port;
    ServerSocketChannel server;
    InetSocketAddress isa;
    HashMap<String, List<String>> clientsRequests;
    List<String> serverLog;
    HashMap<String,String> protocol;
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
                            answerWithEcho(key);
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
    private static void answerWithEcho(SelectionKey key) throws IOException
    {
        SocketChannel client = (SocketChannel) key.channel();
        client.read(inBuf);
        inBuf.flip();
        System.out.println(client.getRemoteAddress());
        System.out.println("s> "+StandardCharsets.UTF_8.decode(inBuf));
        client.write(StandardCharsets.UTF_8.encode("test"));
        inBuf.clear();
    }

    private static void register(Selector selector, ServerSocketChannel serverSocket) throws IOException
    {
        SocketChannel client = serverSocket.accept();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);
    }




    public void stopServer()
    {
        serverIsRunning = false;
        executor.shutdownNow();
    }

    String getServerLog()
    {
        return serverLog.toString();
    }

}
