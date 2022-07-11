package com.trade_accounting.components.goods.print;

import com.aspose.cells.SaveFormat;
import com.trade_accounting.models.dto.company.PriceListDto;
import com.trade_accounting.models.dto.company.PriceListProductDto;
import com.trade_accounting.services.interfaces.client.EmployeeService;
import com.trade_accounting.services.interfaces.company.CompanyService;
import com.trade_accounting.services.interfaces.company.PriceListProductPercentsService;
import com.trade_accounting.services.interfaces.units.CountryService;
import com.trade_accounting.services.interfaces.units.UnitService;
import com.trade_accounting.services.interfaces.warehouse.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class PrintPriceListProductTagsXls {

    private final PriceListProductPercentsService priceListProductPercentsService;
    private final ProductService productService;
    private final CompanyService companyService;
    private final EmployeeService employeeService;
    private final UnitService unitService;
    private final CountryService countryService;
    private final PriceListDto priceListDto;

    private final List<PriceListProductDto> list;
    private final String pathToXlsTemplate;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");


    public PrintPriceListProductTagsXls(String pathToXlsTemplate, List<PriceListProductDto> list,
                                        PriceListProductPercentsService priceListProductPercentsService,
                                        ProductService productService,
                                        CompanyService companyService,
                                        EmployeeService employeeService,
                                        UnitService unitService, CountryService countryService, PriceListDto priceListDto) {
        this.pathToXlsTemplate = pathToXlsTemplate;
        this.list = list;
        this.priceListProductPercentsService = priceListProductPercentsService;
        this.productService = productService;
        this.companyService = companyService;
        this.employeeService = employeeService;
        this.unitService = unitService;
        this.countryService = countryService;
        this.priceListDto = priceListDto;
    }

    protected void selectValue(Cell editCell) {
        String formula = editCell.getStringCellValue();
        switch (formula) {
            case ("<date>"):
                editCell.setCellValue(dateTimeFormatter.format(LocalDateTime.parse(priceListDto.getDate())));
                break;
            case ("<dateOfCreation>"):
                editCell.setCellValue(LocalDateTime.now());
                break;
            case ("<number>"):
                editCell.setCellValue(priceListDto.getNumber());
                break;
            case ("<authorName>"):
                editCell.setCellValue(employeeService.getPrincipal().getEmail());
                break;
        }
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

    public InputStream createReportPDF() {
        try (FileInputStream fiz = new FileInputStream(pathToXlsTemplate);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            Workbook workbook = WorkbookFactory.create(fiz);
            fiz.close();

            printExcelWorkBook(workbook);
            workbook.write(outputStream);
            workbook.close();

            ByteArrayInputStream inStream = new ByteArrayInputStream(outputStream.toByteArray());
            final com.aspose.cells.Workbook doc = new com.aspose.cells.Workbook(inStream);
            final com.aspose.cells.PdfSaveOptions options = new com.aspose.cells.PdfSaveOptions();
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            options.setOnePagePerSheet(true);
            doc.save(baos, options);

            return new ByteArrayInputStream(baos.toByteArray());

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

            ByteArrayInputStream inStream = new ByteArrayInputStream(outputStream.toByteArray());
            final com.aspose.cells.Workbook doc = new com.aspose.cells.Workbook(inStream);
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            doc.save(baos, SaveFormat.ODS);

            return new ByteArrayInputStream(baos.toByteArray());

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

            ByteArrayInputStream inStream = new ByteArrayInputStream(outputStream.toByteArray());
            final com.aspose.cells.Workbook doc = new com.aspose.cells.Workbook(inStream);
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            doc.save(baos, SaveFormat.HTML);

            return new ByteArrayInputStream(baos.toByteArray());

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
            } catch (NullPointerException ignored) {
            }
        }
        return false;
    }

    private String getNotNullCellValue(Cell cell) {
        try {
            return cell.getStringCellValue();
        } catch (NullPointerException e) {
            return "";
        }
    }

    private void printTable(Sheet sheet, Row templateRow) {
        int count = templateRow.getRowNum() - 1;
        for (PriceListProductDto priceListProductDto : list) {
            List<CellStyle> rowList = getCellStylesList(sheet.getRow(sheet.getFirstRowNum() + 7));
            Row row = sheet.createRow(count);
            Cell newCell = row.createCell(1);
            newCell.setCellValue(companyService.getById(priceListDto.getCompanyId()).getName());
            newCell.setCellStyle(rowList.get(1));
            sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 1, 3));
            count++;

            List<CellStyle> rowList1 = getCellStylesList(sheet.getRow(sheet.getFirstRowNum() + 8));
            Row row4 = sheet.createRow(count);
            Cell newCell4 = row4.createCell(1);
            newCell4.setCellStyle(rowList1.get(1));
            newCell4.setCellValue("Наименование: ");
            count++;

            List<CellStyle> rowList2 = getCellStylesList(sheet.getRow(sheet.getFirstRowNum() + 9));
            Row row1 = sheet.createRow(count);
            Cell newCell1 = row1.createCell(1);
            newCell1.setCellStyle(rowList2.get(1));
            newCell1.setCellValue(String.valueOf((productService.getById(priceListProductDto.getProductId())).getName()));
            sheet.addMergedRegion(new CellRangeAddress(row1.getRowNum(), row1.getRowNum(), 1, 3));
            count++;

            List<CellStyle> rowList3 = getCellStylesList(sheet.getRow(sheet.getFirstRowNum() + 10));
            Row row5 = sheet.createRow(count);
            Cell newCell5 = row5.createCell(1);
            newCell5.setCellValue("Ед.: ");
            newCell5.setCellStyle(rowList3.get(1));
            Cell newCell6 = row5.createCell(2);
            newCell6.setCellStyle(rowList3.get(2));
            newCell6.setCellValue(String.valueOf((unitService.getById(productService
                    .getById(priceListProductDto.getProductId()).getId()).getShortName())));
            count++;

            List<CellStyle> rowList4 = getCellStylesList(sheet.getRow(sheet.getFirstRowNum() + 11));
            Row row6 = sheet.createRow(count);
            Cell newCell7 = row6.createCell(1);
            newCell7.setCellStyle(rowList4.get(1));
            newCell7.setCellValue("Цена, руб.: ");
            Cell newCell8 = row6.createCell(2);
            newCell8.setCellStyle(rowList4.get(2));
            newCell8.setCellValue(String.valueOf(priceListProductPercentsService
                    .getByPriceListId(priceListDto.getId()).get(0).getName()));
            count++;

            List<CellStyle> rowList5 = getCellStylesList(sheet.getRow(sheet.getFirstRowNum() + 12));
            Row row2 = sheet.createRow(count);
            Cell newCell2 = row2.createCell(1);
            newCell2.setCellStyle(rowList5.get(1));
            newCell2.setCellValue(String.valueOf(priceListProductDto.getPrice()));
            sheet.addMergedRegion(new CellRangeAddress(row2.getRowNum(), row2.getRowNum(), 1, 3));
            count++;

            List<CellStyle> rowList6 = getCellStylesList(sheet.getRow(sheet.getFirstRowNum() + 13));
            Row row3 = sheet.createRow(count);
            Cell newCell3 = row3.createCell(1);
            newCell3.setCellStyle(rowList6.get(1));
            newCell3.setCellValue(countryService.getById(productService.getById(priceListProductDto
                    .getProductId()).getCountryId()).getShortName());
            sheet.addMergedRegion(new CellRangeAddress(row3.getRowNum(), row3.getRowNum(), 1, 3));
            count++;

            List<CellStyle> rowList7 = getCellStylesList(sheet.getRow(sheet.getFirstRowNum() + 14));
            Row row7 = sheet.createRow(count);
            Cell newCell9 = row7.createCell(1);
            newCell9.setCellStyle(rowList7.get(1));
            newCell9.setCellValue("Подпись ответственного лица: ");
            count++;

            List<CellStyle> rowList8 = getCellStylesList(sheet.getRow(sheet.getFirstRowNum() + 15));
            Row row8 = sheet.createRow(count);
            Cell newCell10 = row8.createCell(3);
            newCell10.setCellStyle(rowList8.get(1));
            newCell10.setCellValue(dateTimeFormatter.format(LocalDateTime.now()));
            count += 2;
        }
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
}
