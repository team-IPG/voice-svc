package org.ipg.namesvc.svc;

import com.google.api.gax.paging.Page;
import com.google.cloud.storage.*;
import com.google.common.io.ByteStreams;
import com.google.protobuf.ByteString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Spliterator;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class VoiceStorageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(VoiceStorageService.class);

    protected final static String BUCKET = "voice_bucket_ipg";

    //TODO: implement store(voice file to storage)

    /**
     * Retrieve object from bucket
     *
     * @param filename
     * @return object as ByteString
     */
    public Optional<ByteString> load(String filename) {
        return getObjectBytes(BUCKET, filename);
    }

    public List<String> list(String bucketName) {
        Storage storage = StorageOptions.getDefaultInstance().getService();
        Page<Blob> blobs = storage.list(bucketName,
                Storage.BlobListOption.currentDirectory());
        //        Storage.BlobListOption.prefix(directory));
        Spliterator<Blob> blobIterator = blobs.iterateAll().spliterator();
        List<String> files = StreamSupport.stream(blobIterator, false)
                .map(blob -> {
                    LOGGER.info("found file={} of size={}", blob.getName(), blob.getSize());
                    return blob.getName();
                }).toList();
        return files;
    }

    /**
     * Given a bucket and object name, return the contents if they exist
     *
     * @param bucketName
     * @param objectName
     * @return optional ByteString
     */
    protected Optional<ByteString> getObjectBytes(String bucketName, String objectName) {
        Storage storage = StorageOptions.getDefaultInstance().getService();
        BlobId blobId = BlobId.of(bucketName, objectName);
        try {
            return Optional.of(ByteString.copyFrom(storage.readAllBytes(blobId,
                    Storage.BlobSourceOption.shouldReturnRawInputStream(true))));
        } catch ( StorageException se ) {
            LOGGER.warn("Failed to load requested file={}", objectName, se);
            return Optional.empty();
        }
    }

}

