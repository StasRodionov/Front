package com.trade_accounting.components.goods.print;

import com.trade_accounting.components.util.PrintExcelDocument;
import com.trade_accounting.models.dto.company.PriceListDto;
import com.trade_accounting.models.dto.company.PriceListProductDto;
import com.trade_accounting.services.interfaces.company.PriceListProductPercentsService;
import com.trade_accounting.services.interfaces.warehouse.ProductService;
import org.apache.poi.ss.usermodel.Cell;

import java.util.List;

public class PrintPriceListProductXls extends PrintExcelDocument<PriceListProductDto> {

    private final PriceListProductPercentsService priceListProductPercentsService;
    private final ProductService productService;
    private final PriceListDto priceListDto;

    public PrintPriceListProductXls(String pathToXlsTemplate, List<PriceListProductDto> list,
                                    PriceListProductPercentsService priceListProductPercentsService,
                                    ProductService productService,
                                    PriceListDto priceListDto) {
        super(pathToXlsTemplate, list);
        this.priceListProductPercentsService = priceListProductPercentsService;
        this.productService = productService;
        this.priceListDto = priceListDto;
    }

    @Override
    protected void selectValue(Cell editCell) {
        String formula = editCell.getStringCellValue();
        switch (formula) {
            case ("<date>"):
                editCell.setCellValue(priceListDto.getDate());
                break;
            case ("<number>"):
                editCell.setCellValue(priceListDto.getNumber());
                break;
            case ("<column>"):
                editCell.setCellValue(String.valueOf(priceListProductPercentsService
                        .getByPriceListId(priceListDto.getId()).get(0).getName()));
                break;
        }
    }

    @Override
    protected void tableSelectValue(String value, PriceListProductDto model, Cell editCell) {
                switch (value) {
            case ("<id>"):
                editCell.setCellValue(String.valueOf(model.getProductId()));
                break;
            case ("<name>"):
                editCell.setCellValue(String.valueOf((productService.getById(model.getProductId())).getName()));
                break;
            case ("<article>"):
//                editCell.setCellValue(model.getArticleNumber());
                editCell.setCellValue("");
                break;
            case ("<price>"):
                editCell.setCellValue(String.valueOf(model.getPrice()));
                break;
        }
    }
}
