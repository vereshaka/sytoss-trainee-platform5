package com.sytoss.provider.services;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.UUID;

@Service
public class ImageService {

    @Getter
    @Value("${image-provider.image-folder}")
    private String imageFolder;

    public String saveImage(byte[] imageBytes) throws IOException {
        UUID uuid = UUID.randomUUID();
        String imageName = uuid + ".jpg";
        FileOutputStream fos = new FileOutputStream(new File(getImageFolder(), imageName));
        fos.write(imageBytes);
        fos.flush();
        fos.close();
        return imageName;
    }

    public void saveImageWithName(MultipartFile image, String name) throws IOException {
        FileOutputStream fos = new FileOutputStream(new File(getImageFolder(), name));
        fos.write(image.getBytes());
        fos.flush();
        fos.close();
    }

    public byte[] getImageByName(String name) throws IOException {
        Path imagePath = Path.of(getImageFolder() + "/" + name);

        if (!Files.exists(imagePath)) {
            throw new IOException("File not exist");
        }

        return Files.readAllBytes(imagePath);
    }
}
