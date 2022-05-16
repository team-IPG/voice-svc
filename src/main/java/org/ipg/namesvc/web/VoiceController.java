package org.ipg.namesvc.web;

import com.google.protobuf.ByteString;
import org.ipg.namesvc.svc.VoicePreset;
import org.ipg.namesvc.svc.VoiceService;
import org.ipg.namesvc.svc.VoiceStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class VoiceController {

    private static final Logger LOGGER = LoggerFactory.getLogger(VoiceController.class);

    VoiceService voiceService;

    VoiceStorageService voiceStorageService;

    public VoiceController(VoiceService voiceService, VoiceStorageService voiceStorageService) {
        this.voiceService = voiceService;
        this.voiceStorageService = voiceStorageService;
    }

    @GetMapping("/voice/{text}")
    public ResponseEntity<byte[]> voice(@PathVariable String text,
                                        @RequestParam(name = "preset", required = false, defaultValue = VoicePreset.DEFAULT) VoicePreset preset) {
        LOGGER.info("converting text='{}' using preset='{}'", text, preset);
        Optional<ByteString> voice = voiceService.convert(text, preset);
        if (voice.isEmpty()) {
            throw new IllegalArgumentException("unable to convert text to voice");
        }
        HttpHeaders headers = buildMediaHeaders();
        byte[] media = voice.get().toByteArray();
        return new ResponseEntity<>(media, headers, HttpStatus.OK);
    }

    @GetMapping("/voicefile/{file}")
    public ResponseEntity<byte[]> voice(@PathVariable String file) {
        Optional<ByteString> voice = voiceStorageService.load(file);
        if (voice.isEmpty()) {
            throw new IllegalArgumentException("unable to find voice file + " + file);
        }
        HttpHeaders headers = buildMediaHeaders();
        byte[] media = voice.get().toByteArray();
        return new ResponseEntity<>(media, headers, HttpStatus.OK);
    }

    private HttpHeaders buildMediaHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf("audio/mpeg"));
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());
        return headers;
    }

}
