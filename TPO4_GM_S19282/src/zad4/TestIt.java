package zad4;

public class TestIt
{
    public static void main(String[] args)
    {
        Server server = new Server("localhost",2002);
        server.startServer();
        Client client = new Client("localhost",2002,"1");
        client.connect();
        System.out.println("c> "+client.send("login 1"));
    }
}
