package ru.sstu.studentprofile.domain.service.storage;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface Loader {
    String load(MultipartFile file) throws IOException;
}
