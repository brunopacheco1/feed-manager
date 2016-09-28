package com.dev.bruno.queue.consumer;

import java.util.logging.Level;

import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import com.dev.bruno.exception.EntityExistsException;
import com.dev.bruno.exception.GenericException;
import com.dev.bruno.model.DocumentURL;
import com.dev.bruno.queue.service.DocumentURLQueueService;
import com.dev.bruno.service.DocumentURLService;

public class DocumentURLQueueConsumer extends AbstractConsumer implements MessageListener {
	
	@Inject
	private DocumentURLService urlService;
	
	@Inject
	private DocumentURLQueueService urlQueueService;
	
	@Override
    public void onMessage(Message message) {
        ObjectMessage objMsg = (ObjectMessage) message;
        
        DocumentURL url = null;
        try {
        	url = (DocumentURL) objMsg.getObject();
        } catch (JMSException e) {
        	logger.log(Level.SEVERE, e.getMessage(), e);
		}
        
        if(url == null) {
        	return;
        }
        
    	try {
			urlService.add(url);
		} catch (EntityExistsException e) {
			// Validacao de existencia, nesse caso, pode ser ignorada
        } catch (GenericException e) {
        	logger.log(Level.SEVERE, e.getMessage(), e);
		}
    	
    	urlQueueService.remove(url);
    	
    	urlQueueService.finish(url.getRobot().getId());
    }
}