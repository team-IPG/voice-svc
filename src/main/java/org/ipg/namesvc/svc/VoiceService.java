package org.ipg.namesvc.svc;

import com.google.cloud.texttospeech.v1beta1.*;
import com.google.protobuf.ByteString;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
public class VoiceService {

    public Optional<ByteString> convert(String text, VoicePreset preset) {

        // Instantiates a client
        try (TextToSpeechClient textToSpeechClient = TextToSpeechClient.create()) {
            // Set the text input to be synthesized
            SynthesisInput input = SynthesisInput.newBuilder().setText(text).build();

            // Build the voice request
            VoiceSelectionParams voice =
                    VoiceSelectionParams.newBuilder()
                            .setName(preset.getVoiceName())
                            .setLanguageCode(preset.getLanguageCode()) // languageCode = "en_us"
                            .setSsmlGender(preset.getVoiceGender()) // ssmlVoiceGender = SsmlVoiceGender.FEMALE
                            .build();

            // Select the type of audio file you want returned
            AudioConfig audioConfig =
                    AudioConfig.newBuilder()
                            .setAudioEncoding(preset.getEncoding()) // MP3 audio.
                            .setSpeakingRate(preset.getRate())
                            .build();

            // Perform the text-to-speech request
            SynthesizeSpeechResponse response =
                    textToSpeechClient.synthesizeSpeech(input, voice, audioConfig);

            // Get the audio contents from the response
            ByteString audioContents = response.getAudioContent();

            return Optional.of(audioContents);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

}
