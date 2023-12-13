/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.UUID;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import org.apache.poi.ss.util.ImageUtils;

/**
 *
 * @author dinhh
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
//        File imgFileName = new File(resourcesDirectory + filename + ".png");
        File imgFileName = new File(resourcesDirectory + filename);

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

    public static String generateRandomFileName(String pathName) {
        String[] keywords = {"Hu Tao", "Kazuha", "Kokomi", "Nahida", "Neuvillette", "Raiden", "Shenhe", "Yelan", "Furina", "Alhaitham"};

        for (String keyword : keywords) {
            if (pathName.contains(keyword)) {
                return keyword + ".png";
            }
        }

        return UUID.randomUUID().toString();
    }
    
    public static BufferedImage loadImageFromResources(String filename) {
        try {
            ClassLoader classLoader = SaveImgUtil.class.getClassLoader();
            System.out.println("Resource Path: " + classLoader.getResource("img/" + filename));

            InputStream inputStream = classLoader.getResourceAsStream("img/" + filename);
            if (inputStream != null) {
                return ImageIO.read(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static ImageIcon loadImageIconFromResources(String filename, int width, int height) {
        InputStream inputStream = SaveImgUtil.class.getResourceAsStream("/img/" + filename);
        if (inputStream != null) {
            try {
                Image image = new ImageIcon(ImageIO.read(inputStream)).getImage();
                return new ImageIcon(image.getScaledInstance(width, height, Image.SCALE_SMOOTH));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
