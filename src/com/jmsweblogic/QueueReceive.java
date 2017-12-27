
package com.jmsweblogic;

import java.util.Hashtable;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.jmsweblogic.config.AppConfig;
import com.jmsweblogic.model.ChatMessage;

public class QueueReceive implements MessageListener {

	// Defines the JNDI context factory.
	public static final String JMS_JNDI_FACTORY;

	public static final String JMS_PROVIDER_URL;

	// Defines the JMS context factory.
	public static final String JMS_CONNECTION_FACTORY;

	// Defines the queue.
	public static final String JMS_QUEUE;

	static {
		JMS_JNDI_FACTORY = AppConfig.JMS_CONFIG.getString("jms.jndi.factory");
		JMS_PROVIDER_URL = AppConfig.JMS_CONFIG.getString("jms.provider.url");
		JMS_CONNECTION_FACTORY = AppConfig.JMS_CONFIG.getString("jms.connection.factory");
		JMS_QUEUE = AppConfig.JMS_CONFIG.getString("jms.queue");
	}

	public static final InitialContext INITIAL_CONTEXT = initInitiallContext();

	private QueueConnectionFactory qconFactory;
	private QueueConnection qcon;
	private QueueSession qsession;
	private QueueReceiver qreceiver;
	private Queue queue;

	public void init() {
		try {
			qconFactory = (QueueConnectionFactory) INITIAL_CONTEXT.lookup(JMS_CONNECTION_FACTORY);
			qcon = qconFactory.createQueueConnection();
			qsession = qcon.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			queue = (Queue) INITIAL_CONTEXT.lookup(JMS_QUEUE);
			qreceiver = qsession.createReceiver(queue);
			qreceiver.setMessageListener(this);
			qcon.start();
		} catch (JMSException | NamingException e) {
			System.err.println("JMS connection failed: " + e.getMessage());
		}
	}

	public void close() throws JMSException {
		if (qreceiver != null) {
			qreceiver.close();
		}
		if (qsession != null) {
			qsession.close();
		}
		if (qcon != null) {
			qcon.close();
		}
	}

	private static InitialContext initInitiallContext() {
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, JMS_JNDI_FACTORY);
		env.put(Context.PROVIDER_URL, JMS_PROVIDER_URL);
		try {
			return new InitialContext(env);
		} catch (NamingException e) {
			System.err.println("Initial context failed: " + e.getMessage());
		}
		return null;
	}

	@Override
	public void onMessage(Message message) {
		if (message != null) {
			try {
				if (message instanceof ObjectMessage) {
					ObjectMessage msg = (ObjectMessage) message;
					ChatMessage cMsg;

					cMsg = (ChatMessage) msg.getObject();

					System.out.println(cMsg.getMessage());
				} else {
					System.out.println(message.toString());
				}
			} catch (JMSException e) {
				System.err.println("Error occured while receiving Chat Message: " + e.getMessage());
			}
		}
	}

	public static void main(String[] args) {
		QueueReceive queueReceive = new QueueReceive();
		queueReceive.init();
	}
}