package com.jmsweblogic;

import java.util.Hashtable;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class QueueReceive implements MessageListener {

	// Defines the JNDI context factory.
	public final static String JNDI_FACTORY = "weblogic.jndi.WLInitialContextFactory";

	// Defines the JMS context factory.
	public final static String JMS_FACTORY = "jms/TestConnectionFactory";

	// Defines the queue.
	public final static String QUEUE = "jms/TestJMSQueue";

	private QueueConnectionFactory qconFactory;
	private QueueConnection qcon;
	private QueueSession qsession;
	private QueueReceiver qreceiver;
	private Queue queue;

	public void init(Context ctx, String queueName) throws NamingException, JMSException {
		qconFactory = (QueueConnectionFactory) ctx.lookup(JMS_FACTORY);
		qcon = qconFactory.createQueueConnection();
		qsession = qcon.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
		queue = (Queue) ctx.lookup(queueName);
		qreceiver = qsession.createReceiver(queue);
		qreceiver.setMessageListener(this);
		qcon.start();
	}

	public void close() throws JMSException {
		qreceiver.close();
		qsession.close();
		qcon.close();
	}

	public static void main(String[] args) throws Exception {
		InitialContext ic = getInitialContext();
		QueueReceive qs = new QueueReceive();
		qs.init(ic, QUEUE);
		//qs.close();
	}

	private static InitialContext getInitialContext() throws NamingException {
		Hashtable env = new Hashtable();
		env.put(Context.INITIAL_CONTEXT_FACTORY, JNDI_FACTORY);
		env.put(Context.PROVIDER_URL, "t3://localhost:7001");
		return new InitialContext(env);
	}

	@Override
	public void onMessage(Message message) {
		if (message != null) {
			try {
				if (message instanceof TextMessage) {
					System.out.println(((TextMessage) message).getText());
				} else {
					System.out.println(message.toString());
				}
			} catch (JMSException e) {
				System.err.println("An error occurred:" + e.getMessage());
			}
		}
	}
}