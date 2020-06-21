import javax.jms.*;
import javax.naming.InitialContext;

public class Reciever
{
    public static void main(String[] args)
    {
        try
        {
            InitialContext ctx=new InitialContext();
            QueueConnectionFactory f=(QueueConnectionFactory)ctx.lookup("myQueueConnectionFactory");
            QueueConnection con=f.createQueueConnection();
            con.start();

            QueueSession ses=con.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue t=(Queue)ctx.lookup("myQueue");
            QueueReceiver receiver=ses.createReceiver(t);

            Listener listener=new Listener();
            receiver.setMessageListener(listener);

            System.out.println("Receiver is ready, waiting for messages...");
            System.out.println("press Ctrl+c to shutdown...");
            while(true)
            {
                Thread.sleep(1000);
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
}
