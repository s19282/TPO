import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Executors;

public class FullDuplexCommunication
{
    public static void main(String[] args) throws IOException, NamingException, JMSException, InterruptedException {
        System.out.print("Type your nick: ");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String userName = reader.readLine();

        InitialContext ctx=new InitialContext();
        QueueConnectionFactory f=(QueueConnectionFactory)ctx.lookup("myQueueConnectionFactory");
        QueueConnection con=f.createQueueConnection();
        con.start();
        QueueSession ses=con.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue t=(Queue)ctx.lookup("myQueue");

        Recieve(ses,t);
        Send(ses,t,userName);

    }
    public static void Recieve(QueueSession ses,Queue queue) throws JMSException, InterruptedException {
        Executors.newSingleThreadExecutor().submit(() -> {
            QueueReceiver receiver=ses.createReceiver(queue);
            Listener listener = new Listener();
            receiver.setMessageListener(listener);
            while(true)
            {
                Thread.sleep(1000);
            }
        });
    }
    public static void Send(QueueSession ses,Queue queue,String name){
        Executors.newSingleThreadExecutor().submit(() -> {
            try
            {
                QueueSender sender=ses.createSender(queue);
                TextMessage msg=ses.createTextMessage();

                BufferedReader b=new BufferedReader(new InputStreamReader(System.in));
                while(true)
                {
                    String s=b.readLine();
                    msg.setText(name+": "+s);
                    sender.send(msg);
                }
            }
            catch (JMSException | IOException e)
            {
                System.out.println(e);
            }

        });

    }
}
