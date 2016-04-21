import org.apache.activemq.ActiveMQConnectionFactory;
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

public class helloWorldPC implements ExceptionListener {

	void processProducer() {
		try {
			// Create a ConnectionFactory
			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
					"tcp://localhost:61616");

			// Create a Connection
			Connection connection = connectionFactory.createConnection();
			connection.start();

			// Create a Session
			Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);

			// Create the destination (Topic or Queue)
			Destination destination = session.createQueue("MyQUEUE");
			//Destination destination = session.createTopic("MyTOPIC");

			// Create a MessageProducer from the Session to the Topic or Queue
			MessageProducer producer = session.createProducer(destination);


			int i = 0;

			while (i < 20) {

				// Create a messages
				String text = "Hello world!" + i;
				TextMessage message = session.createTextMessage(text);

				// Tell the producer to send the message
				System.out.println("Sent message: " + text);
				producer.send(message);
				i++;
			}

			// Clean up
			session.close();
			connection.close();
		} catch (Exception e) {
			System.out.println("Caught: " + e);
			e.printStackTrace();
		}
	}

	public synchronized void onException(JMSException ex) {
		System.out.println("JMS Exception occured.  Shutting down client.");
	}

	public static void main(String[] args) throws Exception {
		helloWorldPC hw = new helloWorldPC();
		if (args.length == 1) {
			if (args[0].equals("P")) {
				System.out.println("Running Producer...");
				hw.processProducer();
			} else if (args[0].equals("C")) {
				System.out.println("Running Consumer...");
				hw.processConsumer();
			} else {
				System.out.println("Producer or Consumer must be specified");

			}
		}
	}
	
}
