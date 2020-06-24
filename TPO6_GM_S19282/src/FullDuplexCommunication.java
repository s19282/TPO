import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;



public class FullDuplexCommunication
{
    public static void main(String[] args) throws NamingException, JMSException, IOException {
        System.out.print("Type your nick: ");
        Scanner s = new Scanner(System.in);
        String name = s.nextLine();

        Connection con;
        Context ctx = new InitialContext();
        ConnectionFactory factory = (ConnectionFactory) ctx.lookup("myCF");
        String admTopicName = "simpleChat";
        Destination destination = (Destination) ctx.lookup(admTopicName);
        con = factory.createConnection();
        Session ses = con.createSession(false, Session.AUTO_ACKNOWLEDGE);

        //recieve
        MessageConsumer receiver = ses.createConsumer(destination);
        Listener listener = new Listener();
        receiver.setMessageListener(listener);
        //send
        MessageProducer ms = ses.createProducer(destination);
        con.start();
        BufferedReader b=new BufferedReader(new InputStreamReader(System.in));
        while(true)
        {
            String m=b.readLine();
            if(m.equalsIgnoreCase("end"))
            {
                con.close();
                System.exit(0);
            }
            else
            {
                TextMessage tm = ses.createTextMessage();
                tm.setText(name+": "+m);
                ms.send(tm);
            }
        }
    }
}

