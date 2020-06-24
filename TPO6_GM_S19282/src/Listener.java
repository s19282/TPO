import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class Listener implements MessageListener
{
    public void onMessage(Message m)
    {
        try
        {
            TextMessage msg=(TextMessage)m;
            System.out.println(msg.getText());
        }
        catch(JMSException e)
        {
            System.out.println(e);
        }
    }
}
