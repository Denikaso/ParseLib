package org.example.parser;

import lombok.val;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.exceptions.ParsingRuntimeException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class ImageProcessor {
    private static final Logger logger = LogManager.getLogger(ImageProcessor.class);
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
                logger.info("Создана директория для сохранения изображений в {}", folderPath);
            } else {
                logger.info("Директория для сохранения изображений уже существует в {}", folderPath);
            }
        } catch (IOException exception) {
            logger.error("Ошибка при создании директории", exception);
            throw new ParsingRuntimeException("Ошибка при создании директории", exception);
        }
    }

    public void copyImage(String imageUrl) {
        try {
            URI uri = new URI(imageUrl);
            URL url = uri.toURL();

            String fileName = imageUrl.substring(imageUrl.lastIndexOf('/') + 1);
            Path imagePath = Paths.get(folderPath.toString(), fileName);
            downloadImage(url, imagePath);
            logger.info("Изображение скопировано успешно: {}", fileName);
        } catch (URISyntaxException | MalformedURLException exception) {
            logger.error("Ошибка при обработке URL", exception);
            throw new ParsingRuntimeException("Ошибка при обработке URL", exception);
        }
    }

    private void downloadImage(URL url, Path imagePath) {
        try (val in = url.openStream()) {
            Files.copy(in, imagePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException exception) {
            logger.error("Ошибка при загрузке изображения", exception);
            throw new ParsingRuntimeException("Ошибка при загрузке изображения", exception);
        }
    }
}
