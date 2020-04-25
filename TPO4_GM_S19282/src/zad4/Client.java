/**
 *
 *  @author Godlewski Mateusz S19282
 *
 */

package zad4;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class Client
{
    private final String host;
    private final int port;
    private final String clientID;
    private SocketChannel server;
    private static final ByteBuffer inBuf = ByteBuffer.allocateDirect(1024);


    public Client(String host, int port, String id)
    {
        this.host = host;
        this.port = port;
        clientID = id;
    }

    public String getClientID()
    {
        return clientID;
    }

    public void connect()
    {
        try
        {
            server = SocketChannel.open();
            server.configureBlocking(false);
            server.connect(new InetSocketAddress(host,port));

            while (!server.finishConnect())
            {
 //               System.out.println("c> Connecting...");
                Thread.sleep(100);
            }
 //           System.out.println("c> Connected");
        }
        catch (IOException | InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    public String send(String req)
    {
        StringBuilder response = new StringBuilder();
        try
        {
            server.write(StandardCharsets.UTF_8.encode(req));

            server.read(inBuf);
            inBuf.flip();
            response.append(StandardCharsets.UTF_8.decode(inBuf));
            inBuf.clear();
        }
        catch (IOException ignored){}
        return response.toString();
    }
}
