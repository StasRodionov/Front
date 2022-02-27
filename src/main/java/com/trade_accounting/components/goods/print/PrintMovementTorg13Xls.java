package com.trade_accounting.components.goods.print;

import com.trade_accounting.components.util.PrintExcelDocument;
import com.trade_accounting.models.dto.warehouse.MovementProductDto;
import com.trade_accounting.services.interfaces.units.UnitService;
import com.trade_accounting.services.interfaces.warehouse.ProductService;
import org.apache.poi.ss.usermodel.Cell;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class PrintMovementTorg13Xls extends PrintExcelDocument<MovementProductDto> {

    private Map<String, String> params;
    private final ProductService productService;
    private final UnitService unitService;

    public PrintMovementTorg13Xls(String pathToXlsTemplate, List<MovementProductDto> list, Map<String, String> params, ProductService productService, UnitService unitService) {
        super(pathToXlsTemplate, list);
        this.params = params;
        this.productService = productService;
        this.unitService = unitService;
    }

    @Override
    protected void selectValue(Cell editCell) {
        String formula = editCell.getStringCellValue();
        switch (formula) {
            case ("<resultAmount>"):
                editCell.setCellValue(params.get("resultAmount"));
                break;
            case ("<resultSum>"):
                editCell.setCellValue(params.get("resultSum"));
                break;
            case ("<date>"):
                editCell.setCellValue(LocalDate.now().toString());
                break;
            case ("<organization>"):
                editCell.setCellValue(params.get("organization"));
                break;
            case ("<okpo>"):
                editCell.setCellValue(params.get("okpo"));
                break;
            case ("<id>"):
                editCell.setCellValue(params.get("id"));
                break;
            case ("<receiver>"):
                editCell.setCellValue(params.get("receiver"));
                break;
            case ("<recipient>"):
                editCell.setCellValue(params.get("recipient"));
                break;
            case ("<correspondentAccount>"):
                editCell.setCellValue(params.get("correspondentAccount"));
                break;
        }
    }

    @Override
    protected void tableSelectValue(String value, MovementProductDto model, Cell editCell) {
        switch (value) {
            case ("<name>"):
                editCell.setCellValue(productService.getById(model.getId()).getName());
                break;
            case ("<code>"):
                editCell.setCellValue(productService.getById(model.getId()).getId());
                break;
            case ("<unit>"):
                editCell.setCellValue(unitService.getById(productService.getById(model.getId()).getUnitId()).getShortName());
                break;
            case ("<amount>"):
                editCell.setCellValue(model.getAmount().toPlainString());
                break;
            case ("<price>"):
                editCell.setCellValue(model.getPrice().toPlainString());
                break;
            case ("<sum>"):
                editCell.setCellValue(model.getPrice().multiply(model.getAmount()).toPlainString());
                break;
        }

    }
}
