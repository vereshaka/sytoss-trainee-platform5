package com.sytoss.provider.services;

import com.sytoss.provider.connector.ImageConnector;
import com.sytoss.provider.dto.ImageDTO;
import com.sytoss.provider.exceptions.ConvertToImageException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class ImageService extends AbstractService {

    private final ImageConnector imageConnector;

    public Long generatePngFromQuestion(String question) {
        File imageFile = convertToImage(question);
        byte[] imageBytes;
        try {
            imageBytes = Files.readAllBytes(Path.of(imageFile.getPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ImageDTO imageDTO = new ImageDTO();
        imageDTO.setImageBytes(imageBytes);
        imageDTO = imageConnector.save(imageDTO);
        return imageDTO.getId();
    }

    private File convertToImage(String question) {
        int width = 600;
        int height = 600;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = image.getGraphics();

        Font font = new Font("Arial", Font.BOLD, 72);
        graphics.setFont(font);

        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, width, height);
        graphics.setColor(Color.BLACK);
        graphics.drawString(question, 10, 75);
        graphics.dispose();

        File imageFile;
        try {
            imageFile = File.createTempFile("img", ".png");
            ImageIO.write(image, "png", imageFile);
        } catch (Exception e) {
            throw new ConvertToImageException("Error during image creating", e);
        }
        return imageFile;
    }

    public byte[] getById(Long id) {
        ImageDTO imageDTO = imageConnector.getReferenceById(id);
        return imageDTO.getImageBytes();
    }
}
