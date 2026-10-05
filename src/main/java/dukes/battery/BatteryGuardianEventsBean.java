package dukes.battery;

import jakarta.ejb.ActivationConfigProperty;
import jakarta.ejb.EJBException;
import jakarta.ejb.MessageDriven;
import jakarta.jms.JMSDestinationDefinition;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.TextMessage;
import java.util.logging.Level;
import java.util.logging.Logger;

@JMSDestinationDefinition(
    // Application-scoped JNDI name used to look up the topic.
        name = "java:app/jms/BatteryGuardianEvents",
    // Defines a publish-subscribe topic rather than a queue.
        interfaceName = "jakarta.jms.Topic",
    // Physical topic name on the JMS broker.
        destinationName = "battery.guardian.events")
@MessageDriven(activationConfig = {
    // Subscribe to the topic defined above using its JNDI name.
    @ActivationConfigProperty(propertyName = "destinationLookup",
            propertyValue = "java:app/jms/BatteryGuardianEvents"),
    // Tell the container that the destination is a topic.
    @ActivationConfigProperty(propertyName = "destinationType",
            propertyValue = "jakarta.jms.Topic"),
    // Use the application server's default JMS connection factory.
    @ActivationConfigProperty(propertyName = "connectionFactoryLookup",
            propertyValue = "java:comp/DefaultJMSConnectionFactory"),
    // Do not retain messages for this subscription while it is inactive.
    @ActivationConfigProperty(propertyName = "subscriptionDurability",
            propertyValue = "NonDurable")
})
public class BatteryGuardianEventsBean implements MessageListener {

    private static final Logger LOGGER = Logger.getLogger(BatteryGuardianEventsBean.class.getName());

    @Override
    public void onMessage(Message message) {
        try {
            if (message instanceof TextMessage textMessage) {
                LOGGER.log(Level.INFO, "Battery guardian event: {0}", textMessage.getText());
            } else {
                LOGGER.log(Level.INFO, "Battery guardian event received: {0}", message.getJMSMessageID());
            }
        } catch (JMSException exception) {
            throw new EJBException("Unable to read battery guardian event", exception);
        }
    }
}