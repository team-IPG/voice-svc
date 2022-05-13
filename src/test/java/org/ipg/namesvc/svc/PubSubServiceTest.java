package org.ipg.namesvc.svc;

import com.google.cloud.spring.pubsub.PubSubAdmin;
import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.pubsub.v1.Subscription;
import com.google.pubsub.v1.Topic;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PubSubServiceTest {

    @InjectMocks
    PubSubService pubSubService;

    @Mock PubSubTemplate pubSubTemplate;
    @Mock PubSubAdmin pubSubAdmin;

    @Test
    void getAllTopicsTest() {
        Topic testTopic = Topic.newBuilder().setName("topic1").build();
        when(pubSubAdmin.listTopics()).thenReturn(Collections.singletonList(testTopic));
        List<String> topics = pubSubService.getAllTopics();
        assertThat(topics, hasItem("topic1"));
    }

    @Test
    void getAllSubscriptionsTest() {
        Subscription testSub = Subscription.newBuilder().setName("sub1").setTopic("topic1").build();
        when(pubSubAdmin.listSubscriptions()).thenReturn(Collections.singletonList(testSub));
        List<String> subs = pubSubService.getAllSubscriptions();
        assertThat(subs, hasItem("sub1:topic1"));
    }

    @Test
    void testCreateTopic(){
        pubSubService.createTopic("topic1");
        verify(pubSubAdmin, times(1)).createTopic("topic1");
    }

    @Test
    void testCreateSubscription(){
        pubSubService.createSubscription("sub1", "topic1");
        verify(pubSubAdmin, times(1)).createSubscription("sub1","topic1");
    }

    @Test
    void publish() {
        pubSubService.publish("topic", "message");
        verify(pubSubTemplate).publish("topic", "message");
    }

    @Test
    void deleteTopic() {
        pubSubService.deleteTopic("topic");
        verify(pubSubAdmin).deleteTopic("topic");
    }

    @Test
    void deleteSubscription() {
        pubSubService.deleteSubscription("sub");
        verify(pubSubAdmin).deleteSubscription("sub");
    }

}