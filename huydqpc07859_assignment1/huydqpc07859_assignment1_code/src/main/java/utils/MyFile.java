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
import java.util.List;
import models.Student;
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

    public static String writeExcel(List<Student> employeeRepository, String path) throws Exception {
        path.replaceAll(" ", "_");
        XSSFWorkbook wordBook = new XSSFWorkbook();
        XSSFSheet sheet = wordBook.createSheet("data");
        XSSFRow row = null;
        Cell cell = null;
        row = sheet.createRow(0);
        cell = row.createCell(0, CellType.STRING);
        cell.setCellValue("Id");
        cell = row.createCell(1, CellType.STRING);
        cell.setCellValue("Fullname");
        cell = row.createCell(2, CellType.STRING);
        cell.setCellValue("Email");
        cell = row.createCell(3, CellType.STRING);
        cell.setCellValue("Phone number");
        cell = row.createCell(4, CellType.STRING);
        cell.setCellValue("Sex");
        cell = row.createCell(5, CellType.STRING);
        cell.setCellValue("English");
        cell = row.createCell(6, CellType.STRING);
        cell.setCellValue("IT");
        cell = row.createCell(7, CellType.STRING);
        cell.setCellValue("Physical");
        cell = row.createCell(8, CellType.STRING);
        cell.setCellValue("GPA");

        for (int i = 0; i < employeeRepository.size(); i++) {
            Student el = employeeRepository.get(i);
            row = sheet.createRow(i + 1);
            cell = row.createCell(0, CellType.STRING);
            cell.setCellValue(el.getId());

            cell = row.createCell(1, CellType.STRING);
            cell.setCellValue(el.getFullName());

            cell = row.createCell(2, CellType.NUMERIC);
            cell.setCellValue(el.getEmail());

            cell = row.createCell(3, CellType.STRING);
            cell.setCellValue(el.getPhoneNumber());

            cell = row.createCell(4, CellType.NUMERIC);
            cell.setCellValue(el.getSex() == 1 ? "Nam" : "Nữ");

            cell = row.createCell(5, CellType.NUMERIC);
            cell.setCellValue(el.getEnglish());
            
            cell = row.createCell(6, CellType.NUMERIC);
            cell.setCellValue(el.getTech());
            
            cell = row.createCell(7, CellType.NUMERIC);
            cell.setCellValue(el.getPhysicalEducation());

            cell = row.createCell(8, CellType.NUMERIC);
            cell.setCellValue((el.getEnglish() + el.getTech() + el.getPhysicalEducation()) / 3);
        }

        String downloadFolder = path + ".xlsx";
        try (FileOutputStream fis = new FileOutputStream(new java.io.File(downloadFolder))) {
            wordBook.write(fis);
            return downloadFolder;
        }

    }
}
