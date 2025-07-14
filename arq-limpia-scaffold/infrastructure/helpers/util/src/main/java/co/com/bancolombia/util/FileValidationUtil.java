package co.com.bancolombia.util;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class FileValidationUtil {

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5 MB

    public Mono<Void> validateFile(String contentType, long fileSize) {
        if (!"text/csv".equals(contentType)) {
            return Mono.error(new IllegalArgumentException("Invalid file type. Only text/csv is allowed."));
        }
        if (fileSize > MAX_FILE_SIZE) {
            return Mono.error(new IllegalArgumentException("File size exceeds the maximum limit of 5 MB."));
        }
        return Mono.empty();
    }
}