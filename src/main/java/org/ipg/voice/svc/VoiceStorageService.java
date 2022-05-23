package org.ipg.voice.svc;

import com.google.api.gax.paging.Page;
import com.google.cloud.storage.*;
import com.google.protobuf.ByteString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Spliterator;
import java.util.stream.StreamSupport;

@Service
public class VoiceStorageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(VoiceStorageService.class);

    public final static String BUCKET = "voice_bucket_ipg";

    /**
     * Retrieve object from bucket
     *
     * @param filename
     * @return object as ByteString
     */
    public Optional<ByteString> load(String filename) {
        return getObject(BUCKET, filename);
    }

    public List<String> list(String bucketName) {
        Storage storage = StorageOptions.getDefaultInstance().getService();
        Page<Blob> blobs = storage.list(bucketName,
                Storage.BlobListOption.currentDirectory());
        //        Storage.BlobListOption.prefix(directory));
        Spliterator<Blob> blobIterator = blobs.iterateAll().spliterator();
        List<String> files = StreamSupport.stream(blobIterator, false)
                .map(blob -> {
                    LOGGER.debug("found file={} of size={}", blob.getName(), blob.getSize());
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
    protected Optional<ByteString> getObject(String bucketName, String objectName) {
        Storage storage = StorageOptions.getDefaultInstance().getService();
        BlobId blobId = BlobId.of(bucketName, objectName);
        try {
            return Optional.of(ByteString.copyFrom(storage.readAllBytes(blobId,
                    Storage.BlobSourceOption.shouldReturnRawInputStream(true))));
        } catch ( StorageException se ) {
            LOGGER.warn("Failed to load requested object={}", objectName, se);
            return Optional.empty();
        }
    }

    /**
     *
     * @param bucketName
     * @param id
     * @param bytes
     * @return fully qualified link to object
     */
    public String createOrUpdate(String bucketName, String id, String contentType, ByteString bytes) {
        Storage storage = StorageOptions.getDefaultInstance().getService();
        BlobId blobId = BlobId.of(bucketName, id);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(contentType).build();
        Blob blob = storage.create(blobInfo, bytes.toByteArray());
        return blob.getMediaLink();
    }
}

