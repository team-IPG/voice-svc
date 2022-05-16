package org.ipg.namesvc.svc;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.*;

class VoiceStorageServiceTest {

    VoiceStorageService storageService = new VoiceStorageService();

    @Test
    void list() {
        List<String> files = storageService.list(VoiceStorageService.BUCKET);
        assertThat(files, hasItem("don.mp3"));
    }
}