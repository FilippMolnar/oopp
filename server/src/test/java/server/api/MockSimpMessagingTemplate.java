package server.api;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.core.MessagePostProcessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import java.util.Map;

public class MockSimpMessagingTemplate implements SimpMessageSendingOperations {
    public boolean sendMesasgeToUser = false;
    public Object objectSend;

    @Override
    public void convertAndSendToUser(String user, String destination, Object payload) throws MessagingException {

    }

    @Override
    public void convertAndSendToUser(String user, String destination, Object payload, Map<String, Object> headers) throws MessagingException {

    }

    @Override
    public void convertAndSendToUser(String user, String destination, Object payload, MessagePostProcessor postProcessor) throws MessagingException {

    }

    @Override
    public void convertAndSendToUser(String user, String destination, Object payload, Map<String, Object> headers, MessagePostProcessor postProcessor) throws MessagingException {

    }

    @Override
    public void send(Message<?> message) throws MessagingException {

    }

    @Override
    public void send(String destination, Message<?> message) throws MessagingException {

    }

    @Override
    public void convertAndSend(Object payload) throws MessagingException {

    }

    @Override
    public void convertAndSend(String destination, Object payload) throws MessagingException {
        sendMesasgeToUser = true;
        objectSend = payload;
    }

    @Override
    public void convertAndSend(String destination, Object payload, Map<String, Object> headers) throws MessagingException {

    }

    @Override
    public void convertAndSend(Object payload, MessagePostProcessor postProcessor) throws MessagingException {

    }

    @Override
    public void convertAndSend(String destination, Object payload, MessagePostProcessor postProcessor) throws MessagingException {

    }

    @Override
    public void convertAndSend(String destination, Object payload, Map<String, Object> headers, MessagePostProcessor postProcessor) throws MessagingException {

    }
}
