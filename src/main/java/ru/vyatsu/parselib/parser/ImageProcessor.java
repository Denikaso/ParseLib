package ru.vyatsu.parselib.parser;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import ru.vyatsu.parselib.exception.ParsingRuntimeException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Data
@Slf4j
public class ImageProcessor {
    private static final String FOLDER_NAME = "images";
    private static final Path ROOT_PATH = Paths.get(".").toAbsolutePath();
    private final Path folderPath;

    public ImageProcessor() {
        this.folderPath = ROOT_PATH.resolve(FOLDER_NAME);
    }

    public void createImageDirectory() {
        try {
            if (Files.notExists(folderPath)) {
                Files.createDirectories(folderPath);
                log.info("Создана директория для сохранения изображений в {}", folderPath);
            } else {
                log.info("Директория для сохранения изображений уже существует в {}", folderPath);
            }
        } catch (IOException exception) {
            log.error("Ошибка при создании директории", exception);
            throw new ParsingRuntimeException("Ошибка при создании директории", exception);
        }
    }

    public void copyImage(String imageUrl) {
        try {
            URL url = new URI(imageUrl).toURL();

            val imagePath = Paths.get(folderPath.toString(), imageUrl.substring(imageUrl.lastIndexOf('/') + 1));
            downloadImage(url, imagePath);
        } catch (URISyntaxException | MalformedURLException exception) {
            log.error("Ошибка при обработке URL", exception);
            throw new ParsingRuntimeException("Ошибка при обработке URL", exception);
        }
    }

    private void downloadImage(final URL url, final Path imagePath) {
        try (val in = url.openStream()) {
            Files.copy(in, imagePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException exception) {
            log.error("Ошибка при загрузке изображения", exception);
            throw new ParsingRuntimeException("Ошибка при загрузке изображения", exception);
        }
    }
}
