package com.dev.bruno.queue.consumer;

import java.util.logging.Level;

import javax.ejb.EJB;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import com.dev.bruno.exception.EntityExistsException;
import com.dev.bruno.exception.GenericException;
import com.dev.bruno.model.Show;
import com.dev.bruno.queue.service.ShowQueueService;
import com.dev.bruno.service.ShowService;

public class ShowQueueConsumer extends AbstractConsumer implements MessageListener {
	
	@EJB
	private ShowService showService;
	
	@EJB
	private ShowQueueService queueService;
	
	@Override
    public void onMessage(Message message) {
		ObjectMessage objMsg = (ObjectMessage) message;
        
        Show show = null;
        try {
        	show = (Show) objMsg.getObject();
        } catch (JMSException e) {
        	logger.log(Level.SEVERE, e.getMessage(), e);
		}
        
        if(show == null) {
        	return;
        }
        
    	try {
    		showService.add(show);
		} catch (EntityExistsException e) {
			// Validacao de existencia, nesse caso, pode ser ignorada
        } catch (GenericException e) {
        	logger.log(Level.SEVERE, e.getMessage(), e);
		}
    	
    	
    	queueService.remove(show);
    	
    	queueService.finish();
    }
}