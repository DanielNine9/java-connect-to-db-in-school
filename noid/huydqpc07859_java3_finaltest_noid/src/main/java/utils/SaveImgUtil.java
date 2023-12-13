/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import javax.imageio.ImageIO;

/**
 *
 * @author tiend
 */
public class SaveImgUtil {

    public static boolean saveImageToResources(BufferedImage image, String filename) {
        String resourcesDirectory = "src/main/resources/img/";
        File imgDir = new File(resourcesDirectory);

        if (!imgDir.exists()) {
            if (!imgDir.mkdirs()) {
                System.err.println("Không thể tạo thư mục img trong resources.");
                return false;
            }
        }
        File imgFileName = new File(resourcesDirectory + filename + ".png");
        try {
            if (ImageIO.write(image, "png", imgFileName)) {
                System.out.println("Hình ảnh đã được lưu vào: " + imgFileName.getAbsolutePath());
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String generateRandomFileName() {
        return UUID.randomUUID().toString();
    }
}
