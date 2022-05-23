package org.ipg.voice.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.cloud.spring.pubsub.PubSubAdmin;
import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.cloud.spring.pubsub.support.converter.ConvertedBasicAcknowledgeablePubsubMessage;
import com.google.cloud.spring.pubsub.support.converter.JacksonPubSubMessageConverter;
import com.google.pubsub.v1.Subscription;
import com.google.pubsub.v1.Topic;
import org.ipg.common.EmployeeEvent;
import org.ipg.voice.svc.VoiceService;
import org.ipg.voice.svc.VoiceStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
public class EventService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventService.class);
    private static final String UPDATE_SUBSCRIPTION = "employee-event-sub";

    private final PubSubTemplate pubSubTemplate;
    private final PubSubAdmin pubSubAdmin;
    private final ObjectMapper mapper;
    private final VoiceService voiceService;
    private final VoiceStorageService voiceStorageService;

    public EventService(PubSubTemplate pubSubTemplate, PubSubAdmin pubSubAdmin, ObjectMapper mapper, VoiceService voiceService, VoiceStorageService voiceStorageService) {
        this.pubSubTemplate = pubSubTemplate;
        this.pubSubAdmin = pubSubAdmin;
        this.mapper = mapper;
        this.voiceService = voiceService;
        this.voiceStorageService = voiceStorageService;

        // Configure the pubSubTemplate to use Jackson mapper from Spring
        this.pubSubTemplate.setMessageConverter(new JacksonPubSubMessageConverter(mapper));

        // Subcribe to Employee Update events
        this.createTopic(EmployeeEvent.UPDATE_TOPIC);
        this.createSubscription(UPDATE_SUBSCRIPTION, EmployeeEvent.UPDATE_TOPIC);
        this.addSubscriber(UPDATE_SUBSCRIPTION, new EmployeeEventConsumer(voiceService, voiceStorageService), EmployeeEvent.class);
    }

    public List<String> getAllTopics() {
        return this.pubSubAdmin.listTopics()
                .stream()
                .map(Topic::getName)
                .collect(Collectors.toList());
    }

    public List<String> getAllSubscriptions() {
        return this.pubSubAdmin.listSubscriptions()
                .stream()
                .map(subscription -> subscription.getName() + ":" + subscription.getTopic())
                .collect(Collectors.toList());
    }

    public Topic createTopic(String topicName) {
        Topic topic = this.pubSubAdmin.getTopic(topicName);
        if (topic != null) {
            LOGGER.info("Topic '{}' already exists", topicName);
            return topic;
        }
        return this.pubSubAdmin.createTopic(topicName);
    }

    public Subscription createSubscription(String subscriptionName, String topicName) {
        Subscription sub = this.pubSubAdmin.getSubscription(subscriptionName);
        if (sub != null) {
            LOGGER.info("Subscription '{}' already exists", sub.getName());
            return sub;
        }
        return this.pubSubAdmin.createSubscription(subscriptionName, topicName);
    }

    public <T> Subscriber addSubscriber(String subscriptionName,
                                        Consumer<ConvertedBasicAcknowledgeablePubsubMessage<T>> consumer, Class<T> payLoadType) {
        return this.pubSubTemplate.subscribeAndConvert(subscriptionName, consumer, payLoadType);
    }

    public <T> void publish(String topicName, T message) {
        this.pubSubTemplate.publish(topicName, message);
    }

    public void deleteTopic(String topicName) {
        this.pubSubAdmin.deleteTopic(topicName);
    }

    public void deleteSubscription(String subscriptionName) {
        this.pubSubAdmin.deleteSubscription(subscriptionName);
    }

}
