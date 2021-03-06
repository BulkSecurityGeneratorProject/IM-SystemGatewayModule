package za.co.ajk.systemgateway.messaging.impl;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import za.co.ajk.systemgateway.enums.EventType;
import za.co.ajk.systemgateway.enums.IncidentPriority;
import za.co.ajk.systemgateway.enums.PubSubMessageType;
import za.co.ajk.systemgateway.messaging.InterModulePubSubMessage;
import za.co.ajk.systemgateway.messaging.MessageImplementationCondition;
import za.co.ajk.systemgateway.messaging.MessageSender;
import za.co.ajk.systemgateway.messaging.googlepubsub.GoogleChannelManager;


/**
 * Utility class that will send a message to the different topics configured for the module and message type.
 */
@Service
@Conditional(MessageImplementationCondition.class)
public class MessageSenderImpl implements MessageSender {
    
    private GoogleChannelManager googleChannelManager;
    
    @Value("${spring.application.name}")
    private String moduleName;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    public MessageSenderImpl(GoogleChannelManager googleChannelManager) {
        this.googleChannelManager = googleChannelManager;
    }
    
    /**
     * This is a test message that will be submitted.
     * @throws Exception
     */
    @Override
    public void sendIncidentTestMessage() throws Exception {
        
        InterModulePubSubMessage interModulePubSubMessage = new InterModulePubSubMessage();
        
        interModulePubSubMessage.setEventType(EventType.START_EVENT);
        interModulePubSubMessage
            .setIncidentDescription("Die ding het ontplof en die hele wereld aan die brand gesteek...");
        interModulePubSubMessage.setIncidentHeader("Die ding het ontplof");
        interModulePubSubMessage.setIncidentNumber(100L);
        interModulePubSubMessage.setIncidentPriority(IncidentPriority.CRITICAL);
        interModulePubSubMessage.setMessageDateCreated(Instant.now());
        interModulePubSubMessage.setOperatorName("Andre");
        interModulePubSubMessage.setOriginatingApplicationModuleName("TestModule");
        interModulePubSubMessage.setPubSubMessageType(PubSubMessageType.INCIDENT);
        
        googleChannelManager.pubSubMessageSender(interModulePubSubMessage);
    }
    
    @Override
    public void sendGenericMessage() throws Exception{
    
        InterModulePubSubMessage interModulePubSubMessage = new InterModulePubSubMessage();
    
        interModulePubSubMessage.setEventType(EventType.GENERIC_MESSAGE);
        interModulePubSubMessage
            .setIncidentDescription("Generic message to test communications");
        interModulePubSubMessage.setIncidentHeader("Gen Meesage");
        interModulePubSubMessage.setIncidentNumber(null);
        interModulePubSubMessage.setIncidentPriority(IncidentPriority.LOW);
        interModulePubSubMessage.setMessageDateCreated(Instant.now());
        interModulePubSubMessage.setOperatorName("Andre");
        interModulePubSubMessage.setOriginatingApplicationModuleName(moduleName);
        interModulePubSubMessage.setPubSubMessageType(PubSubMessageType.GENERIC);
    
        googleChannelManager.pubSubMessageSender(interModulePubSubMessage);
    }
    @Override
    public void sendObjMessage(InterModulePubSubMessage interModulePubSubMessage) {
        googleChannelManager.pubSubMessageSender(interModulePubSubMessage);
    }
}
