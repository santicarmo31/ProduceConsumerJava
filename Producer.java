/*
Producer.java

Simple JMS producer for Apache ActiveMQ

(c)2013 Kevin Boone
 */

package net.kevinboone.amqtest;

// Note that the only Apache-specific class referred to in the source is
//  the one that provides the initial broker connection. The rest is
//  standard JMS
import org.apache.activemq.ActiveMQConnectionFactory;
import javax.jms.*;

public class Producer
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

    // Create a producer for test_queue
    MessageProducer producer = session.createProducer(queue);

    // Create a simple text message and send it
    TextMessage message = session.createTextMessage ("Hello, world!");
    producer.send(message);

    // Stop the connection — good practice but redundant here
    connection.stop();

    System.exit(0);
    }
  }
