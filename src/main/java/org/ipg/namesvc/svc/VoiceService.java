package org.ipg.namesvc.svc;

import com.google.cloud.spring.pubsub.support.BasicAcknowledgeablePubsubMessage;
import com.google.cloud.texttospeech.v1beta1.*;
import com.google.protobuf.ByteString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
public class VoiceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(VoiceService.class);


    public Optional<ByteString> convert(String text, VoicePreset preset, double desiredRate) {
        return convert(text, preset.getLanguageCode(), preset.getVoiceName(), preset.getVoiceGender(), preset.getEncoding(), desiredRate);
    }

    public Optional<ByteString> convert(String text, String language, String voiceName, SsmlVoiceGender gender, AudioEncoding encoding, double desiredRate) {

        // Box the rate into acceptable range
        double rate = adjustRate(desiredRate);

        // Instantiates a client
        try (TextToSpeechClient textToSpeechClient = TextToSpeechClient.create()) {
            // Set the text input to be synthesized
            SynthesisInput input = SynthesisInput.newBuilder().setText(text).build();

            // Build the voice request
            VoiceSelectionParams voice =
                    VoiceSelectionParams.newBuilder()
                            .setName(voiceName)
                            .setLanguageCode(language) // languageCode = "en_us"
                            .setSsmlGender(gender) // ssmlVoiceGender = SsmlVoiceGender.FEMALE
                            .build();

            // Select the type of audio file you want returned
            AudioConfig audioConfig =
                    AudioConfig.newBuilder()
                            .setAudioEncoding(encoding) // MP3 audio.
                            .setSpeakingRate(rate)
                            .build();

            // Perform the text-to-speech request
            SynthesizeSpeechResponse response =
                    textToSpeechClient.synthesizeSpeech(input, voice, audioConfig);

            // Get the audio contents from the response
            ByteString audioContents = response.getAudioContent();

            return Optional.of(audioContents);

        } catch (IOException e) {
            LOGGER.warn("failed to convert text to voice", e);
        }

        return Optional.empty();
    }

    double adjustRate(double desiredRate) {
        if (desiredRate < .5) {
            LOGGER.info("Desired rate is too low, trimming rate to min=.5");
            return .5d;
        }
        if (desiredRate > 3d) {
            LOGGER.info("Desired rate is too high, trimming rate to max=3");
            return 3d;
        }
        return desiredRate;
    }

    // TODO: implement voice message batch processing
    // These messages will be emitted by Name Service
    // Result should re-generate voice recording, persist to storage and ack message
    public void handleVoiceMessage(String subscriptionName, BasicAcknowledgeablePubsubMessage message) {
        LOGGER.info("Message received from subscription={}", subscriptionName);
        String messageTxt = message.getPubsubMessage().getData().toStringUtf8();
        // handle the voice message
        // extract preferred name
        // extract chosen preset
        // convert to mp3
        // persist to object storage
        //ack the message
        message.ack();
    }

}
