package org.ipg.namesvc.web;

import com.google.cloud.texttospeech.v1beta1.*;
import com.google.protobuf.ByteString;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@RestController
public class VoiceController {


    @GetMapping("voice/{text}")
    public ResponseEntity<byte[]> voice(@PathVariable String text) {

        // Instantiates a client
        try (TextToSpeechClient textToSpeechClient = TextToSpeechClient.create()) {
            // Set the text input to be synthesized
            SynthesisInput input = SynthesisInput.newBuilder().setText(text).build();

            // Build the voice request
            VoiceSelectionParams voice =
                    VoiceSelectionParams.newBuilder()
                            .setLanguageCode("en-US") // languageCode = "en_us"
                            .setSsmlGender(SsmlVoiceGender.FEMALE) // ssmlVoiceGender = SsmlVoiceGender.FEMALE
                            .build();

            // Select the type of audio file you want returned
            AudioConfig audioConfig =
                    AudioConfig.newBuilder()
                            .setAudioEncoding(AudioEncoding.MP3) // MP3 audio.
                            .build();

            // Perform the text-to-speech request
            SynthesizeSpeechResponse response =
                    textToSpeechClient.synthesizeSpeech(input, voice, audioConfig);

            // Get the audio contents from the response
            ByteString audioContents = response.getAudioContent();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.valueOf("audio/mpeg"));
            byte[] media = audioContents.toByteArray();
            headers.setCacheControl(CacheControl.noCache().getHeaderValue());

            ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(media, headers, HttpStatus.OK);
            return responseEntity;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
