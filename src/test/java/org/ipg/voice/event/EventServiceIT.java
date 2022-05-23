package org.ipg.voice.event;

import com.google.cloud.spring.pubsub.support.converter.ConvertedBasicAcknowledgeablePubsubMessage;

import org.ipg.common.EmployeeDTO;
import org.ipg.common.EmployeeEvent;
import org.ipg.common.EventType;
import org.ipg.common.VoicePreset;
import org.ipg.voice.svc.VoiceService;
import org.ipg.voice.svc.VoiceStorageService;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

@SpringBootTest
class EventServiceIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventServiceIT.class);

    @Autowired
    EventService eventService;
    @Autowired VoiceService voiceService;
    @Autowired VoiceStorageService voiceStorageService;

    final static String TOPIC = "test-topic";
    final static String SUB = "test-subscription";

    EmployeeDTO testEmployee = new EmployeeDTO("jane-doe", "jane", "doe","jaynee dozey", VoicePreset.PRESET_8, .5d, true);
    EmployeeEvent testEvent = new EmployeeEvent(EventType.INSERT, testEmployee);

    @BeforeEach
    public void setup() {
        eventService.createTopic(TOPIC);
        eventService.createSubscription(SUB, TOPIC);
    }

    @AfterEach
    public void tearDown() {
        eventService.deleteSubscription(SUB);
        eventService.deleteTopic(TOPIC);
    }

    @Test
    public void testEmployeeEventSubscription() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        LatchingEmployeeEventConsumer consumer = new LatchingEmployeeEventConsumer(voiceService, voiceStorageService, latch);
        eventService.addSubscriber(SUB, consumer, EmployeeEvent.class);
        eventService.publish(TOPIC, testEvent);
        latch.await();
    }

    /**
     * Process messages and decrement the countDown latch to ensure
     * test harness blocks until message is received and accepted.
     */
    private class LatchingEmployeeEventConsumer extends EmployeeEventConsumer {

        CountDownLatch latch;

        public LatchingEmployeeEventConsumer(VoiceService voiceService, VoiceStorageService voiceStorageService, CountDownLatch latch) {
            super(voiceService, voiceStorageService);
            this.latch = latch;
        }

        @Override
        public void accept(ConvertedBasicAcknowledgeablePubsubMessage<EmployeeEvent> msg) {
            super.accept(msg);
            latch.countDown();
        }

    }

}