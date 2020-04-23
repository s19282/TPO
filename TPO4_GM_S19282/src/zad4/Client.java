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
    private SocketChannel channel;
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
            channel = SocketChannel.open();
            channel.configureBlocking(false);
            channel.connect(new InetSocketAddress(host,port));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public String send(String req)
    {
        StringBuilder sb = new StringBuilder();
        try
        {
            ByteBuffer outBuffer = StandardCharsets.UTF_8.encode(req);
            channel.write(outBuffer);
            while (true)
            {
                inBuf.clear();
                int readBytes = channel.read(inBuf);
                if(readBytes == 0)
                {
                    Thread.sleep(200);
                }
                else if(readBytes == -1)
                {
                    channel.socket().close();
                    channel.close();
                    break;
                }
                else
                {
                    inBuf.flip();
                    sb.append(StandardCharsets.UTF_8.decode(inBuf));
                }
            }
        }
        catch (IOException | InterruptedException ignored){}
        return sb.toString();
    }
}
