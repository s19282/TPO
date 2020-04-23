package zad4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread extends Thread
{
    private Socket socket;

    public ServerThread(Socket socket)
    {
        super();
        this.socket = socket;
    }
    public void run()
    {
        try
        {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            String line;
            while((line = in.readLine()) != null && !line.isEmpty())
            {
                System.out.println(line);
            }
        }
        catch (IOException ignored){}

        try
        {
            socket.close();
        }
        catch (IOException ignored) {}
    }
}
