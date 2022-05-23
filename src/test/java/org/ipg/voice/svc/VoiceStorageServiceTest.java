package org.ipg.voice.svc;

import com.google.protobuf.ByteString;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VoiceStorageServiceTest {

    VoiceStorageService storageService = new VoiceStorageService();

    @Test
    void list() {
        List<String> files = storageService.list(VoiceStorageService.BUCKET);
        assertThat(files, hasItem("don.mp3"));
    }

    @Test
    void getExistingObject() {
        Optional<ByteString> object = storageService.getObject(VoiceStorageService.BUCKET, "don.mp3");
        assertTrue(object.isPresent());
    }

    @Test
    void getMissingObject() {
        Optional<ByteString> object = storageService.getObject(VoiceStorageService.BUCKET, "MISSING_FILE.mp3");
        assertTrue(object.isEmpty());
    }
}