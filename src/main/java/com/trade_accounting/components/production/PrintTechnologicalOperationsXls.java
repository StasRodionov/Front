package com.trade_accounting.components.production;

import com.trade_accounting.components.util.PrintExcelDocument;
import com.trade_accounting.controllers.dto.TechnicalOperationsDto;
import com.trade_accounting.services.interfaces.TechnicalCardService;
import com.trade_accounting.services.interfaces.WarehouseService;
import org.apache.poi.ss.usermodel.Cell;

import java.util.List;

public class PrintTechnologicalOperationsXls extends PrintExcelDocument<TechnicalOperationsDto> {


    private final WarehouseService warehouseService;
    private final TechnicalCardService technicalCardService;

    protected PrintTechnologicalOperationsXls(String pathToXlsTemplate, List<TechnicalOperationsDto> list,
                                              WarehouseService warehouseService,
                                              TechnicalCardService technicalCardService) {
        super(pathToXlsTemplate, list);
        this.warehouseService = warehouseService;
        this.technicalCardService = technicalCardService;
    }

    @Override
    protected void selectValue(Cell editCell) {

    }

    @Override
    protected void tableSelectValue(String value, TechnicalOperationsDto model, Cell editCell) {

        switch (value) {
            case ("<date>"):
                editCell.setCellValue(model.getDate());
                break;
            case ("<comment>"):
                editCell.setCellValue(model.getComment());
                break;
            case ("<warehouse>"):
                editCell.setCellValue(warehouseService.getById(model.getWarehouse()).getName());
                break;
            case ("<technicalCard>"):
                editCell.setCellValue(technicalCardService.getById(model.getTechnicalCard()).getName());
                break;
            case("<volume>"):
                editCell.setCellValue(model.getVolume().toString());
                break;
            case ("<isSent>"):
                editCell.setCellValue(model.getIsSent());
                break;
            case ("<isPrint>"):
                editCell.setCellValue(model.getIsPrint());
                break;
        }
    }
}
