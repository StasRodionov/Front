package com.trade_accounting.components.sells;

import com.trade_accounting.components.util.PrintExcelDocument;
import com.trade_accounting.models.dto.finance.FunnelDto;
import org.apache.poi.ss.usermodel.Cell;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class PrintFunnelXls extends PrintExcelDocument<FunnelDto> {



    @Autowired
    public PrintFunnelXls(String pathToXlsTemplate, List<FunnelDto> list) {
        super(pathToXlsTemplate, list);
    }

    @Override
    protected void selectValue(Cell editCell) {
        String formula = editCell.getStringCellValue();
        switch (formula) {
            case ("<date>"):
                editCell.setCellValue("16.03.2022");
                break;
            case ("<authorName>"):
                editCell.setCellValue("An Dmitrii");
                break;
        }
    }

    @Override
    protected void tableSelectValue(String value, FunnelDto model, Cell editCell) {

        switch (value) {
            case ("<statusName>"):
                editCell.setCellValue(model.getStatusName());
                break;
            case ("<count>"):
                editCell.setCellValue(model.getCount());
                break;
            case ("<time>"):
                editCell.setCellValue(model.getTime());
                break;
            case ("<conversion>"):
                editCell.setCellValue(model.getConversion());
                break;
            case ("<price>"):
                editCell.setCellValue(model.getPrice());
                break;
            case ("<type>"):
                editCell.setCellValue(model.getType());
                break;

        }
    }
}
