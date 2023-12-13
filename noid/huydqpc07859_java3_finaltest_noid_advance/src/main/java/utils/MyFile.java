/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.util.List;
import models.Entity;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author dinhh
 */
public class MyFile {

    public static Object readObject(String path) throws FileNotFoundException, IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(path);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        return objectInputStream.readObject();
    }

    public static void writeObject(String path, Object data)
            throws FileNotFoundException,
            IOException,
            ClassNotFoundException {
        FileOutputStream fileOutputStream = new FileOutputStream(path);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(data);
        objectOutputStream.flush();
        objectOutputStream.close();
        fileOutputStream.close();
    }

      public static String writeExcel(List<Entity> entityList, String path) throws Exception {
        path = path.replaceAll(" ", "_");
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();

        // Create header row using declared fields from the first entity
        Entity firstEntity = entityList.isEmpty() ? new Entity() : entityList.get(0);
        Field[] fields = firstEntity.getClass().getDeclaredFields();
        XSSFRow headerRow = sheet.createRow(0);
        for (int i = 0; i < fields.length; i++) {
            Cell headerCell = headerRow.createCell(i, CellType.STRING);
            headerCell.setCellValue(fields[i].getName());
        }

        // Create data rows
        for (int rowIndex = 0; rowIndex < entityList.size(); rowIndex++) {
            Entity entity = entityList.get(rowIndex);
            XSSFRow dataRow = sheet.createRow(rowIndex + 1);

            for (int columnIndex = 0; columnIndex < fields.length; columnIndex++) {
                Cell dataCell = dataRow.createCell(columnIndex, CellType.STRING);

                // Use reflection to dynamically get field value
                Field field = fields[columnIndex];
                field.setAccessible(true); // Make private fields accessible
                try {
                    Object fieldValue = field.get(entity);
                    // Assuming the field type is String; adjust as needed
                    String attributeValue = (fieldValue != null) ? fieldValue.toString() : "";
                    dataCell.setCellValue(attributeValue);
                } catch (IllegalAccessException e) {
                    // Handle IllegalAccessException
                    e.printStackTrace();
                }
            }
        }

        String downloadPath = path + ".xlsx";
        try (FileOutputStream fos = new FileOutputStream(downloadPath)) {
            workbook.write(fos);
        }

        return downloadPath;
    }
}
