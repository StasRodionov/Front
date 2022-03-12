package com.trade_accounting.components.production;

import com.trade_accounting.components.util.PrintExcelDocument;
import com.trade_accounting.models.dto.production.OrdersOfProductionDto;
import com.trade_accounting.services.interfaces.company.CompanyService;
import com.trade_accounting.services.interfaces.production.TechnicalCardService;
import org.apache.poi.ss.usermodel.Cell;

import java.util.List;

public class PrintOrdersOfProductionXls extends PrintExcelDocument<OrdersOfProductionDto> {
    private final CompanyService companyService;
    private final TechnicalCardService technicalCardService;

    protected PrintOrdersOfProductionXls(String pathToXlsTemplate, List<OrdersOfProductionDto> list,
                                         CompanyService companyService,
                                         TechnicalCardService technicalCardService) {
        super(pathToXlsTemplate, list);
        this.companyService = companyService;
        this.technicalCardService = technicalCardService;
}

    @Override
    protected void selectValue(Cell editCell) {

    }

    @Override
    protected void tableSelectValue(String value, OrdersOfProductionDto model, Cell editCell) {
        switch (value) {
            case ("<date>"):
                editCell.setCellValue(model.getDate());
                break;
            case ("<company>"):
                editCell.setCellValue(companyService.getById(model.getCompanyId()).getName());
                break;
            case ("<technicalCard>"):
                editCell.setCellValue(technicalCardService.getById(model.getTechnicalCardId()).getName());
                break;
            case ("<volume>"):
                editCell.setCellValue(model.getVolume().toString());
                break;
            case ("<produce>"):
                editCell.setCellValue(model.getProduce());
                break;
            case ("<PlannedProductionDate>"):
                editCell.setCellValue(model.getPlannedProductionDate());
                break;
            case ("<isSent>"):
                editCell.setCellValue(model.getIsSent());
                break;
            case ("<isPrint>"):
                editCell.setCellValue(model.getIsPrint());
                break;
            case ("<comment>"):
                editCell.setCellValue(model.getComment());
                break;
        }
    }
}
