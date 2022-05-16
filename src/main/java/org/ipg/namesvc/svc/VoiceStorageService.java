package org.ipg.namesvc.svc;

import com.google.protobuf.ByteString;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VoiceStorageService {

    private final String STORAGE_URL = "https://storage.cloud.google.com/";

    //TODO: implement store(voice to storage)

    public Optional<ByteString> load(String filename) {
        //TODO: implement retrieve voice from storage bucket byu filename
        return Optional.empty();
    }
}

