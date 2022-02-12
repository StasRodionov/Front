package com.trade_accounting.components.purchases.print;

import com.trade_accounting.components.util.PrintExcelDocument;
import com.trade_accounting.models.dto.purchases.PurchaseControlDto;
import com.trade_accounting.services.interfaces.client.EmployeeService;
import com.trade_accounting.services.interfaces.warehouse.ProductPriceService;
import com.trade_accounting.services.interfaces.purchases.PurchaseHistoryOfSalesService;
import org.apache.poi.ss.usermodel.Cell;

import java.time.LocalDate;
import java.util.List;

public class PrintPurchasingManagementXls extends PrintExcelDocument<PurchaseControlDto> {
    private final EmployeeService employeeService;
    private final ProductPriceService productPriceService;
    private final PurchaseHistoryOfSalesService purchaseHistoryOfSalesService;

//    private final List<String> sumList;
//    private int lengthOfsumList = 0;


    public PrintPurchasingManagementXls(String pathToXlsTemplate, List<PurchaseControlDto> list,
                                         EmployeeService employeeService, ProductPriceService productPriceService, PurchaseHistoryOfSalesService purchaseHistoryOfSalesService) {
        super(pathToXlsTemplate, list);
        this.employeeService = employeeService;
        this.productPriceService = productPriceService;
        this.purchaseHistoryOfSalesService = purchaseHistoryOfSalesService;
    }

    @Override
    protected void selectValue(Cell editCell) {
        String formula = editCell.getStringCellValue();
        switch (formula) {
            case ("<date>"):
                editCell.setCellValue(LocalDate.now());
                break;
            case ("<authorName>"):
                editCell.setCellValue(employeeService.getPrincipal().getEmail());
                break;
        }
    }


    @Override
    protected void tableSelectValue(String value, PurchaseControlDto model, Cell editCell) {
        switch (value) {
            case ("<id>"):
                editCell.setCellValue(String.valueOf(model.getId()));
                break;
            case ("<name>"):
                editCell.setCellValue(model.getProductName());
                break;
            case ("<article>"):
                editCell.setCellValue(model.getArticleNumber());
                break;
            case ("<measure>"):
                editCell.setCellValue(model.getProductMeasure());
                break;
            case ("<quantity>"):
                editCell.setCellValue(model.getProductQuantity());
                break;
            case ("<sum>"):
                editCell.setCellValue(String.valueOf((purchaseHistoryOfSalesService.getById(model.getHistoryOfSalesId()).getSumOfProducts().doubleValue())));
                break;

            case ("<price>"):
                editCell.setCellValue(String.valueOf(productPriceService.getById(model.getHistoryOfSalesId()).getValue().doubleValue()));
                break;
            case ("<margin>"):
                editCell.setCellValue(String.valueOf(purchaseHistoryOfSalesService.getById(model.getHistoryOfSalesId()).getProductMargin().doubleValue()));
                break;
            case ("<profit_margin>"):
                editCell.setCellValue(String.valueOf(purchaseHistoryOfSalesService.getById(model.getHistoryOfSalesId()).getProductProfitMargin().doubleValue()));
                break;
            case ("<product_sales_per_day>"):
                editCell.setCellValue(purchaseHistoryOfSalesService.getById(model.getHistoryOfSalesId()).getProductSalesPerDay());
                break;
        }
    }
}
