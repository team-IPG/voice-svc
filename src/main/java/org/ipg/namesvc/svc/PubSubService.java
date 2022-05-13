package org.ipg.namesvc.svc;

import com.google.cloud.pubsub.v1.Subscriber;
import com.google.cloud.spring.pubsub.PubSubAdmin;
import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.pubsub.v1.Topic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PubSubService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PubSubService.class);

    private final PubSubTemplate pubSubTemplate;

    private final PubSubAdmin pubSubAdmin;

    public PubSubService(PubSubTemplate pubSubTemplate, PubSubAdmin pubSubAdmin) {
        this.pubSubTemplate = pubSubTemplate;
        this.pubSubAdmin = pubSubAdmin;
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

    public void createTopic(String topicName) {
         this.pubSubAdmin.createTopic(topicName);
    }

    public void createSubscription(String subscriptionName, String topicName) {
        this.pubSubAdmin.createSubscription(subscriptionName, topicName);
    }

    public void addSubscriber(String subscriptionName) {
        Subscriber subscriber =
                this.pubSubTemplate.subscribe(
                        subscriptionName,
                        message -> {
                            LOGGER.info(
                                    "Message received from "
                                            + subscriptionName
                                            + " subscription: "
                                            + message.getPubsubMessage().getData().toStringUtf8());
                            message.ack();
                        });

    }

    public void publish(String topicName, String message) {
        this.pubSubTemplate.publish(topicName, message);
    }

    public void deleteTopic(String topicName) {
        this.pubSubAdmin.deleteTopic(topicName);
    }

    public void deleteSubscription(String subscriptionName) {
        this.pubSubAdmin.deleteSubscription(subscriptionName);
    }

}
