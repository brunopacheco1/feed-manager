package com.dev.bruno.queue.service;

import java.util.List;
import java.util.logging.Level;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.dev.bruno.exception.GenericException;
import com.dev.bruno.exception.NothingToDoException;
import com.dev.bruno.model.DocumentURL;
import com.dev.bruno.model.Robot;
import com.dev.bruno.service.AbstractService;
import com.dev.bruno.service.NormalizationService;
import com.dev.bruno.service.RobotService;

@Singleton
public class DocumentURLQueueService extends AbstractService {
	
//	@Resource(mappedName = "java:/JmsXA")
	@Resource(mappedName = "java:/ConnectionFactory")
    private ConnectionFactory connectionFactory;
 
	@Resource(name="documentURLQueue")
	private String documentURLQueue;
	
	@Inject
	private DocumentURLQueueController queueController;
	
	@Inject
	private NormalizationService normalizationService;
	
	@Inject
	private RobotService robotService;
	
	private Connection connection;
    private Session session;
    private MessageProducer producer;
    
    @PostConstruct
    public void init() {
        try {
        	Context ctx = new InitialContext();
            connection = connectionFactory.createConnection();
//          session = connection.createSession(true, Session.SESSION_TRANSACTED);
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = (Destination) ctx.lookup(documentURLQueue);
            producer = session.createProducer(destination);
        } catch (JMSException | NamingException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
    
    @PreDestroy
    public void destroy() {
        if (connection != null) {
            try {
                connection.close();
            } catch (JMSException e) {
            	logger.log(Level.SEVERE, e.getMessage(), e);
            }
        }
    }
    
    public void add(Long robotId, List<DocumentURL> urls) throws GenericException {
    	Robot robot = robotService.get(robotId);
    	
    	for(DocumentURL url : urls) {
			add(robot, url);
		}
	}
    
    private void add(Robot robot, DocumentURL url) {
		url.setRobot(robot);
		
		add(url);
	}
	
    private void add(DocumentURL url) {
		ObjectMessage message;
        try {
        	queueController.add(url);
        	
            message = session.createObjectMessage(url);
            producer.send(message);
        } catch (JMSException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
	}
	
	public void remove(DocumentURL url) {
		queueController.remove(url);
	}
	
	public void finish(Long robotId) {
    	if (queueController.hasURLs()) {
			return;
		}
    	
    	try {
    		normalizationService.runNormalizer(robotId);
		} catch (NothingToDoException e) {
			//Ignorar mensagens de nada a ser feito
		} catch (GenericException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
    	
    	queueController.finish();
    }
}