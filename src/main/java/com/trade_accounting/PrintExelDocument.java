package com.trade_accounting;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author Senya Sheykin
 *
 * @param <T>
 */

@Slf4j
public abstract class PrintExelDocument <T> {

    private HSSFWorkbook templateBook;

    private HSSFWorkbook editBook;

    private final List<T> list;

    private int newBookRowNum = 0;

//    TODO шарина строки, которая будет просканирована пока равна 40, но должна быть динамической
    private int widthTable = 40;

    protected PrintExelDocument(String pathToXlsTemplate, List<T> list) {
        this.list = list;
        try (FileInputStream fis = new FileInputStream(pathToXlsTemplate);
             FileInputStream fiz = new FileInputStream(pathToXlsTemplate)){
            templateBook = new HSSFWorkbook(fis);
            editBook = new HSSFWorkbook(fiz);
        } catch (IOException e) {
            log.error("произошла ошибка при обработке xls шаблона");
        }
    }

    public InputStream createReport() {
        printExelSheet(templateBook.getSheetAt(0), editBook.getSheetAt(0));
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()){
            editBook.write(outputStream);
            editBook.close();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            templateBook.close();
            return inputStream;
        } catch (IOException exc) {
            log.error("не удалось корректно закрыть воркбуки");
        }
        return null;
    }

    public void printExelSheet (Sheet templateSheet, Sheet editSheet) {
        for (int i = 0; i < templateSheet.getLastRowNum(); i ++ ) {
            boolean check = printExelRow(templateSheet, editSheet, i);
            if (check) break;
        }
    }

    private boolean printExelRow(Sheet templateSheet, Sheet editSheet, int count) {
        boolean check = false;
        Row templateRow = templateSheet.getRow(count);
        Row editRow = editSheet.getRow(count);
        for (int k = 0; k < widthTable; k ++) {
            if (getNotNullCellValue(templateRow, k).equals("<table>")) {
                printTable(templateSheet, editSheet, templateRow.getCell(k));
                check = true;
                break;
            }
            try {
                printCell(templateRow.getCell(k), editRow.getCell(k));
            } catch (NullPointerException ignored) {}
        }
        newBookRowNum++;
        return check;
    }

    private void printTable(Sheet templateSheet, Sheet editSheet, Cell templateCell) {
        Row templateRow = templateSheet.getRow(templateCell.getRowIndex() + 1);
        for (int i = 0; i < list.size(); i++) {
            Row newRow = editSheet.createRow(newBookRowNum);
            for (int k = 0; k < templateRow.getLastCellNum(); k++) {
                printRowOfTable(templateRow, newRow, list.get(i));            }
            newBookRowNum++;
        }
    }

    private String getNotNullCellValue(Row row, int i) {
        try {
            if (row.getCell(i) == null) {
                return "";
            } else {
                return row.getCell(i).getStringCellValue();
            }
        } catch (NullPointerException e) {
            return "";
        }
    }

    private void printRowOfTable(Row templateRow, Row editRow, T model) {
        for (int i = 0; i < templateRow.getLastCellNum(); i++) {
            editRow.createCell(i).setCellValue(getTableSelectValue(getNotNullCellValue(templateRow, i), model));
        }
    }

    private void printCell(Cell templateCell, Cell editCell) {
        editCell.setCellValue(getSelectValue(templateCell.getStringCellValue()));
    }

    protected abstract String getSelectValue(String formula);
//        switch (formula) {
//            case("<date>"):
//                return "01.02.2021";
//            case("<name>"):
//                return "Senya Sheykin";
//        }
//        return formula;



    protected abstract String getTableSelectValue(String value, T model);
//        switch (value) {
//            case ("<firstName>"):
//                return model.getFirstName();
//            case ("<lastName>"):
//                return model.getLastName();
//            case ("<age>"):
//                return model.getAge();
//            case ("<email>"):
//                return model.getEmail();
//        }
//        return "";
}
