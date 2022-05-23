package org.ipg.voice.event;

import com.google.cloud.spring.pubsub.support.converter.ConvertedBasicAcknowledgeablePubsubMessage;
import com.google.protobuf.ByteString;
import org.ipg.common.EmployeeDTO;
import org.ipg.common.EmployeeEvent;
import org.ipg.voice.svc.VoiceService;
import org.ipg.voice.svc.VoiceStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class EmployeeEventConsumer implements
        Consumer<ConvertedBasicAcknowledgeablePubsubMessage<EmployeeEvent>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeEventConsumer.class);

    VoiceService voiceService;
    VoiceStorageService voiceStorageService;

    public EmployeeEventConsumer(VoiceService voiceService, VoiceStorageService voiceStorageService) {
        this.voiceService = voiceService;
        this.voiceStorageService = voiceStorageService;
    }

    @Override
    public void accept(ConvertedBasicAcknowledgeablePubsubMessage<EmployeeEvent> msg) {

        EmployeeEvent event = msg.getPayload();
        LOGGER.info("Message received {}", event);

        EmployeeDTO dto = event.employee();

        // Create voice record
        Optional<ByteString> voiceRecord = voiceService.convert(dto.preferredName(),
                dto.preferredPreset(),dto.preferredSpeed());

        if (voiceRecord.isEmpty()) {
            LOGGER.warn("Failed to created recording for {}", dto);
            return;
        }

        // Create the voice object in storage
        String mediaLink = voiceStorageService.createOrUpdate(VoiceStorageService.BUCKET,
                dto.id() + ".mp3",dto.preferredPreset().getEncoding().name(), voiceRecord.get());

        LOGGER.info("Link to object={}", mediaLink);

        // TODO: Update name database with new link

        // Confirm the bucket contains our object
        List<String> objects = voiceStorageService.list(VoiceStorageService.BUCKET);

        if(objects.contains(dto.id())) {
            LOGGER.info("Bucket contains expected objectId={}", dto.id());
        } else {
            LOGGER.warn("Bucket is missing objectId={}", dto.id());
        }

        // ack the message
        msg.ack();
    }


}
