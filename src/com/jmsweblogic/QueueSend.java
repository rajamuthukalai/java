package com.jmsweblogic;

import java.util.Hashtable;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.jmsweblogic.config.AppConfig;
import com.jmsweblogic.model.ChatMessage;

public class QueueSend {

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
	private QueueSender qsender;
	private Queue queue;

	public void init() {
		try {
			qconFactory = (QueueConnectionFactory) INITIAL_CONTEXT.lookup(JMS_CONNECTION_FACTORY);
			qcon = qconFactory.createQueueConnection();
			qsession = qcon.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			queue = (Queue) INITIAL_CONTEXT.lookup(JMS_QUEUE);
			qsender = qsession.createSender(queue);
			qcon.start();
		} catch (JMSException | NamingException e) {
			System.err.println("JMS connection failed: " + e.getMessage());
		}
	}
	
	private QueueSession getQueueSession() {
		if (qsession == null) {
			init();
		}
		return qsession;
	}

	public void send(ChatMessage message) {
		ObjectMessage msg = null;
		try {
			msg = getQueueSession().createObjectMessage();
			msg.setObject(message);
			qsender.send(msg);
		} catch (JMSException e) {
			System.err.println("Error occurred while sending chat message to queue: " + e.getMessage());
		}
	}

	public void close() throws JMSException {
		if (qsender != null) {
			qsender.close();
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
	
	public static void main(String[] args) {
		QueueSend sender = new QueueSend();
		ChatMessage chatMessage = new ChatMessage();
		chatMessage.setMessage("Hi");
		sender.send(chatMessage);
		System.out.println("Message sent successfully");
	}
}