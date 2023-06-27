package com.sytoss.provider.services;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImageService extends AbstractService {

    private int paragraphStart;
    private int paragraphEnd;

    public Long generatePngFromQuestion(String question) {
        convertToImage(question);
        return 1L;
    }

    private File convertToImage(String question) {
        int width = 1920;
        int height = 1080;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = image.getGraphics();

        Font font = new Font("Arial", Font.BOLD, 72);
        graphics.setFont(font);

        graphics.setColor(Color.WHITE);
        graphics.fillRect(0,0,width,height);
        graphics.setColor(Color.BLACK);
        graphics.drawString(question, 10, 75);
        graphics.dispose();

        File imageFile = null;
        try {
            imageFile = File.createTempFile("img", ".png", new File("D:/sytoss-trainee-platform5/stp-ms-image-provider/src/main/resources/images/"));
            ImageIO.write(image, "png", imageFile);
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
        System.out.println(imageFile.getAbsolutePath());
        return imageFile;
    }

}
