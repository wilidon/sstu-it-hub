package ru.sstu.studentprofile.domain.service.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.sstu.studentprofile.domain.exception.UnprocessableEntityException;

import java.io.IOException;
import java.util.UUID;

@Service
public class UserBackgroundLoader extends FileLoader{
    public UserBackgroundLoader(final @Value("${s3.uploadEndpoint}") String uploadEndpoint,
                            final @Value("${s3.endpoint}") String endpoint,
                            final @Value("${s3.bucket}") String bucketName,
                            final @Value("${s3.accessKey}") String accessKey,
                            final @Value("${s3.secretKey}") String secretKey) {
        super(uploadEndpoint, endpoint, bucketName, accessKey, secretKey);
    }


    @Override
    public String load(MultipartFile file) throws IOException {
        if (!FileUtils.isAllowedFile(file)) {
            throw new UnprocessableEntityException("Недопустимый формат файла");
        }

        String fileExtension = FileUtils.getFileExtension(file);

        String filename = "background/" + UUID.randomUUID() + fileExtension;
        return this.loadFile(filename, file.getInputStream());
    }
}
