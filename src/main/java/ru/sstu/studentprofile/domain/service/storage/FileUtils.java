package ru.sstu.studentprofile.domain.service.storage;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.sstu.studentprofile.domain.exception.UnprocessableEntityException;

import java.util.Objects;

@Component
public class FileUtils {
    private static final String[] ALLOWED_EXTENSIONS = {".jpg", ".jpeg", ".png", ".gif"};

    /**
     * Проверяет разрешен ли файл для загрузки
     * @param file - файл
     * @return - true если файл разрешен, иначе false
     */
    public static boolean isAllowedFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (fileName != null) {
            String extension = getFileExtension(fileName);
            for (String allowedExtension : ALLOWED_EXTENSIONS) {
                if (extension.equalsIgnoreCase(allowedExtension)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Возвращает расширение файла. Если файл не имеет расширения, возвращает пустую строку
     * @param fileName - имя файла
     * @return - расширение файла
     */
    private static String getFileExtension(String fileName) {
        if (fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
        }
        return "";
    }


    /**
     * Возвращает расширение файла. Если файл не имеет расширения, возвращает пустую строку
     * @param file - файл
     * @return - расширение файла
     * @throws UnprocessableEntityException - если файл не имеет расширения
     */
    public static String getFileExtension(MultipartFile file) {
        String filename = getFileExtension(Objects.requireNonNull(file.getOriginalFilename()));
        if (filename.isEmpty()) {
            throw new UnprocessableEntityException("Файл не имеет расширения");
        }
        return filename;
    }
}
