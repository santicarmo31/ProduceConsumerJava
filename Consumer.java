/*
Consumer.java

Simple JMS consumer for Apache ActiveMQ

(c)2013 Kevin Boone
 */

package net.kevinboone.amqtest;

// Note that the only Apache-specific class referred to in the source is
//  the one that provides the initial broker connection. The rest is
//  standard JMS
import org.apache.activemq.ActiveMQConnectionFactory;
import javax.jms.*;

public class Consumer
  {
  public static void main (String[] args)
      throws Exception
    {
    // Create a connection factory referring to the broker host and port
    ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory
      ("tcp://localhost:61616");

    // Note that a new thread is created by createConnection, and it
    //  does not stop even if connection.stop() is called. We must
    //  shut down the JVM using System.exit() to end the program
    Connection connection = factory.createConnection();

    // Start the connection
    connection.start();

    // Create a non-transactional session with automatic acknowledgement
    Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

    // Create a reference to the queue test_queue in this session. Note
    //  that ActiveMQ has auto-creation enabled by default, so this JMS
    //  destination will be created on the broker automatically
    Queue queue = session.createQueue("test_queue");


    MessageConsumer consumer = session.createConsumer(queue);

    int messages = 0;
    final int MESSAGES_TO_CONSUME=10;
    do
     {
     TextMessage message = (TextMessage)consumer.receive();
     messages++;
     System.out.println ("Message #" + messages + ": " + message.getText());
     } while (messages < MESSAGES_TO_CONSUME);

    // Stop the connection — good practice but redundant here
    connection.stop();

    System.exit(0);
    }
  }
