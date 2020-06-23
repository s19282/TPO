
import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.concurrent.Executors;


public class FullDuplexCommunication
{
    public static void main(String[] args) throws NamingException, JMSException, InterruptedException, IOException {
        System.out.print("Type your nick: ");
        Scanner s = new Scanner(System.in);
        String name = s.nextLine();

        Connection con = null;
        Context ctx = new InitialContext();
        ConnectionFactory factory = (ConnectionFactory) ctx.lookup("myCF");
        String admTopicName = "simpleChat";
        Destination destination = (Destination) ctx.lookup(admTopicName);
        con = factory.createConnection();
        Session ses = con.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Recieve(ses,destination);
        Send(con,ses,destination,name);
    }
    public static void Recieve(Session ses,Destination destination) throws JMSException, InterruptedException {
        MessageConsumer receiver = ses.createConsumer(destination);
        Listener listener = new Listener();
        receiver.setMessageListener(listener);
    }
    public static void Send(Connection con, Session ses,Destination destination,String name) throws JMSException, IOException {
        MessageProducer ms = ses.createProducer(destination);
        con.start();
        BufferedReader b=new BufferedReader(new InputStreamReader(System.in));
        while(true)
        {
            String s=b.readLine();
            if(s.equalsIgnoreCase("end"))
            {
                con.close();
                System.exit(0);
            }
            else
            {
                TextMessage tm = ses.createTextMessage();
                tm.setText(name+": "+s);
                ms.send(tm);
            }
        }
    }
}

