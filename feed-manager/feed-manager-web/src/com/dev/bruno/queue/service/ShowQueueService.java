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
import com.dev.bruno.model.Show;
import com.dev.bruno.service.AbstractService;
import com.dev.bruno.service.NormalizationService;

@Singleton
public class ShowQueueService extends AbstractService {
	
//	@Resource(mappedName = "java:/JmsXA")
	@Resource(mappedName = "java:/ConnectionFactory")
    private ConnectionFactory connectionFactory;
 
	@Resource(name="showQueue")
	private String showQueue;
	
	@Inject
	private ShowQueueController queueController;
	
	@Inject
	private NormalizationService normalizationService;
	
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
            Destination destination = (Destination) ctx.lookup(showQueue);
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
    
	public void add(Long robotId, List<Show> shows) throws GenericException {
		queueController.addRobot(robotId);
		
    	for(Show show : shows) {
			add(show);
		}
	}
	
	public void add(Show show) {
		ObjectMessage message;
        try {
        	queueController.add(show);
        	
            message = session.createObjectMessage(show);
            producer.send(message);
        } catch (JMSException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
	}
	
	public void remove(Show show) {
		queueController.remove(show);
	}
	
	public void finish() {
    	if (queueController.hasShows()) {
			return;
		}
    	
    	for(Long robotId : queueController.getRobotIds()) {
	    	try {
	    		normalizationService.runNormalizer(robotId);
			} catch (NothingToDoException e) {
				//Ignorar mensagens de nada a ser feito
			} catch (GenericException e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
			}
    	}
    	
    	queueController.finish();
    }
}