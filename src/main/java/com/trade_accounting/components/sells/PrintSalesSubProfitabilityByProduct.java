package com.trade_accounting.components.sells;

import com.trade_accounting.components.util.PrintExcelDocument;
import com.trade_accounting.models.dto.InvoiceProductDto;
import com.trade_accounting.models.dto.ProductDto;
import com.trade_accounting.services.interfaces.ProductService;
import com.trade_accounting.services.interfaces.ReturnAmountByProductService;
import org.apache.poi.ss.usermodel.Cell;

import java.math.BigDecimal;
import java.util.List;

public class PrintSalesSubProfitabilityByProduct extends PrintExcelDocument<InvoiceProductDto> {
    private ProductService productService;
    private ReturnAmountByProductService returnAmountByProductService;

    public PrintSalesSubProfitabilityByProduct(String pathToXlsTemplate,
                                               List<InvoiceProductDto> list,
                                               ProductService productService,
                                               ReturnAmountByProductService returnAmountByProductService) {
        super(pathToXlsTemplate, list);
        this.productService = productService;
        this.returnAmountByProductService = returnAmountByProductService;

    }

    @Override
    protected void selectValue(Cell editCell) {

    }

    @Override
    protected void tableSelectValue(String value, InvoiceProductDto model, Cell editCell) {
        List<ProductDto> productDtos = productService.getAll();

        switch (value) {
            case ("<productDto>"):
                editCell.setCellValue(productService.getById(model.getProductId()).getName());
                break;

            case ("<description>"):
                editCell.setCellValue(productService.getById(model.getProductId()).getDescription());
                break;

            case ("<amount>"):
                editCell.setCellValue(model.getAmount().toString());
                break;

            case ("<price>"):
                editCell.setCellValue(model.getPrice().toString());
                break;

            case ("<costPrice>"):
                editCell.setCellValue(productService.getById(model.getProductId()).getPurchasePrice().toString());
                break;

            case ("<totalSalesPrice>"):
                BigDecimal totalSalesPrice = model.getPrice().multiply(model.getAmount());
                editCell.setCellValue(String.format("%.2f", totalSalesPrice));
                break;

            case ("<totalCostPrice>"):
                BigDecimal totalCostPrice = productService.getById(model.getProductId()).
                        getPurchasePrice().multiply(model.getAmount());
                editCell.setCellValue(String.format("%.2f", totalCostPrice));
                break;

            case ("<totalReturnPrice>"):
                BigDecimal returnPrice = returnAmountByProductService.
                        getTotalReturnAmountByProduct(model.getProductId(), model.getInvoiceId()).getAmount();
                editCell.setCellValue(String.format("%.2f", returnPrice));

                break;
            case ("<profit>"):
                editCell.setCellValue(getProfit(model));
                break;
        }
    }


    private String getProfit(InvoiceProductDto model) {
        BigDecimal totalSalesPrice = model.getPrice().multiply(model.getAmount());
        BigDecimal totalCostPrice = productService.getById(model.getProductId()).
                getPurchasePrice().multiply(model.getAmount());
        BigDecimal totalReturnPrice = returnAmountByProductService.
                getTotalReturnAmountByProduct(model.getProductId(), model.getInvoiceId()).getAmount();

        return String.format("%.2f", totalSalesPrice.subtract(totalCostPrice).subtract(totalReturnPrice));
    }


}
