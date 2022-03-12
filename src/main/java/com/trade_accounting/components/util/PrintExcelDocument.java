package com.trade_accounting.components.util;

import com.aspose.cells.PdfCompliance;
import com.aspose.cells.SaveFormat;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Senya Sheykin
 *
 * @param <T>
 */

@Slf4j
public abstract class PrintExcelDocument<T> {

    private final List<T> list;
    private final String pathToXlsTemplate;

    protected PrintExcelDocument(String pathToXlsTemplate, List<T> list) {
        this.pathToXlsTemplate = pathToXlsTemplate;
        this.list = list;
    }

    public InputStream createReport() {
        try (FileInputStream fiz = new FileInputStream(pathToXlsTemplate);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            Workbook workbook = WorkbookFactory.create(fiz);
            fiz.close();

            printExcelWorkBook(workbook);

            workbook.write(outputStream);
            workbook.close();

            return new ByteArrayInputStream(outputStream.toByteArray());
        } catch (FileNotFoundException e) {
            log.error("XLS шаблон с таким именем не найден");
        } catch (IOException ex) {
            log.error("произошла ошибка при создании или записи нового XLS отчета: " + ex.getMessage());
        }

        return null;
    }

    public InputStream createReportPDF(){
        try (FileInputStream fiz = new FileInputStream(pathToXlsTemplate);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            Workbook workbook = WorkbookFactory.create(fiz);
            fiz.close();

            printExcelWorkBook(workbook);
            workbook.write(outputStream);
            workbook.close();

            ByteArrayInputStream inStream = new  ByteArrayInputStream(outputStream.toByteArray());
            final com.aspose.cells.Workbook doc = new com.aspose.cells.Workbook(inStream);
            final com.aspose.cells.PdfSaveOptions options = new com.aspose.cells.PdfSaveOptions();
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            options.setOnePagePerSheet(true);
            doc.save(baos, options);

            return  new ByteArrayInputStream(baos.toByteArray());

        } catch (FileNotFoundException e) {
            log.error("PDF шаблон с таким именем не найден");
        } catch (IOException ex) {
            log.error("Произошла ошибка при создании или записи нового PDF отчета");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public InputStream createReportODS() {
        try (FileInputStream fiz = new FileInputStream(pathToXlsTemplate);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            Workbook workbook = WorkbookFactory.create(fiz);
            fiz.close();

            printExcelWorkBook(workbook);
            workbook.write(outputStream);
            workbook.close();

            ByteArrayInputStream inStream = new  ByteArrayInputStream(outputStream.toByteArray());
            final com.aspose.cells.Workbook doc = new com.aspose.cells.Workbook(inStream);
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            doc.save(baos, SaveFormat.ODS);

            return  new ByteArrayInputStream(baos.toByteArray());

        } catch (FileNotFoundException e) {
            log.error("ODS шаблон с таким именем не найден");
        } catch (IOException ex) {
            log.error("Произошла ошибка при создании или записи нового ODS отчета");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public InputStream createReportHTML() {
        try (FileInputStream fiz = new FileInputStream(pathToXlsTemplate);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            Workbook workbook = WorkbookFactory.create(fiz);
            fiz.close();

            printExcelWorkBook(workbook);
            workbook.write(outputStream);
            workbook.close();

            ByteArrayInputStream inStream = new  ByteArrayInputStream(outputStream.toByteArray());
            final com.aspose.cells.Workbook doc = new com.aspose.cells.Workbook(inStream);
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            doc.save(baos, SaveFormat.HTML);

            return  new ByteArrayInputStream(baos.toByteArray());

        } catch (FileNotFoundException e) {
            log.error("ODS шаблон с таким именем не найден");
        } catch (IOException ex) {
            log.error("Произошла ошибка при создании или записи нового ODS отчета");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private void printExcelWorkBook(Workbook workbook) {
//        TODO при необходимости можно сделать обработку нескольких листов
        printExcelSheet(workbook.getSheetAt(0));
    }

    private void printExcelSheet(Sheet sheet) {
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            if (printRow(sheet.getRow(i))) {
                printTable(sheet, sheet.getRow(i + 1));
                break;
            }
        }
    }

    private boolean printRow(Row row) {
        for (int i = 0; i <= row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            if (getNotNullCellValue(cell).equals("<table>")) {
                return true;
            }
            try {
                selectValue(row.getCell(i));
            } catch (NullPointerException ignored) {}
        }
        return false;
    }

    private void printTable(Sheet sheet, Row templateRow) {
        List<String> formulasList = getFormulasList(templateRow);
        List<CellStyle> cellStylesList = getCellStylesList(templateRow);

        int count = templateRow.getRowNum() - 1;
        for (T model : list) {
            printRowOfTable(sheet.createRow(count), formulasList, cellStylesList, model);
            count++;
        }
    }

    private void printRowOfTable(Row editRow, List<String> listFormulas, List<CellStyle> cellStyleList, T model) {
        for (int i = 0; i < listFormulas.size(); i++) {
            Cell newCell = editRow.createCell(i);
            tableSelectValue(listFormulas.get(i), model, newCell);
            try {
                newCell.setCellStyle(cellStyleList.get(i - 1));
            } catch (IndexOutOfBoundsException ignored) {}
        }
    }

    private List<String> getFormulasList(Row templateRow) {
        List<String> listFormulas = new ArrayList<>();
        for (int i = 0; i < templateRow.getLastCellNum(); i++) {
            listFormulas.add(getNotNullCellValue(templateRow.getCell(i)));
        }
        return listFormulas;
    }

    private List<CellStyle> getCellStylesList(Row templateRow) {
        List<CellStyle> sellStylesList = new ArrayList<>();
        for (int i = 0; i < templateRow.getLastCellNum() + 1; i++) {
            try {
                sellStylesList.add(templateRow.getCell(i).getCellStyle());
            } catch (NullPointerException ignored) {}
        }
        return sellStylesList;
    }

    private String getNotNullCellValue(Cell cell) {
        try {
            return cell.getStringCellValue();
        } catch (NullPointerException e) {
            return "";
        }
    }

    protected abstract void selectValue(Cell editCell);

    protected abstract void tableSelectValue(String value, T model, Cell editCell);
}
