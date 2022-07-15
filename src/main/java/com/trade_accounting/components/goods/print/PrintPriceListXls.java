package com.trade_accounting.components.goods.print;

import com.trade_accounting.components.util.PrintExcelDocument;
import com.trade_accounting.models.dto.company.PriceListDto;
import com.trade_accounting.services.interfaces.company.CompanyService;
import org.apache.poi.ss.usermodel.Cell;

import java.math.BigDecimal;
import java.util.List;

public class PrintPriceListXls extends PrintExcelDocument<PriceListDto> {

    private final CompanyService companyService;
    public PrintPriceListXls(String pathToXlsTemplate, List<PriceListDto> list,
                             CompanyService companyService) {
        super(pathToXlsTemplate, list);
        this.companyService = companyService;
    }

    @Override
    protected void selectValue(Cell editCell) {
        //ignored
    }

    @Override
    protected void tableSelectValue(String value, PriceListDto model, Cell editCell) {
        switch (value) {
            case ("<id>"):
                editCell.setCellValue("Прайс-лсит");
                break;
            case ("<number>"):
                editCell.setCellValue(model.getNumber());
                break;
            case ("<isSpend>"):
                if (model.getIsSpend()) {
                    editCell.setCellValue("Да");
                } else {
                    editCell.setCellValue("");
                }
                break;
            case ("<date>"):
                editCell.setCellValue(String.valueOf(model.getDate()));
                break;
            case ("<sum>"):
                editCell.setCellValue(String.valueOf(BigDecimal.ZERO.doubleValue()));
                break;
            case ("<contractor>"):
                editCell.setCellValue(companyService.getById(model.getCompanyId()).getName());
                break;
            case ("<status>"):
//                editCell.setCellValue(String.valueOf(model.getStatus()));
                editCell.setCellValue("");
                break;
        }
    }
}
