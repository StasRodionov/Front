package com.trade_accounting.components.sells;

import com.trade_accounting.components.util.PrintExcelDocument;
import com.trade_accounting.models.dto.InvoiceDto;
import org.apache.poi.ss.usermodel.Cell;

import java.util.List;

public class PrintSalesXls extends PrintExcelDocument<InvoiceDto> {

    protected List<String> sum;
    private int lengthOfSumList = 0;

    protected PrintSalesXls(String pathToXlsTemplate, List<InvoiceDto> list, List<String> sum) {
        super(pathToXlsTemplate, list);
        this.sum = sum;
    }

    @Override
    protected void selectValue(Cell editCell) {
        String formula = editCell.getStringCellValue();
        switch (formula) {
            case ("<date>"):
                editCell.setCellValue("15.04.2021");
                break;
            case ("<authorName>"):
                editCell.setCellValue("Ivan Saushin");
                break;
        }
    }

    @Override
    protected void tableSelectValue(String value, InvoiceDto model, Cell editCell) {
        switch (value) {
            case ("<id>"):
                editCell.setCellValue(model.getId());
                break;
            case ("<date>"):
                editCell.setCellValue(model.getDate());
                break;
            case ("<contractor>"):
                editCell.setCellValue(model.getContractorDto().getName());
                break;
            case ("<company>"):
                editCell.setCellValue(model.getCompanyDto().getName());
                break;
            case ("<sum>"):
                editCell.setCellValue(sum.get(lengthOfSumList++));
                if (lengthOfSumList >= sum.size()) {
                    lengthOfSumList = 0;
                }
                break;
        }
    }
}
