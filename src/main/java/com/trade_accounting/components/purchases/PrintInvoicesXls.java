package com.trade_accounting.components.purchases;

import com.trade_accounting.components.util.PrintExcelDocument;
import com.trade_accounting.models.dto.ContractorDto;
import com.trade_accounting.models.dto.InvoiceDto;
import org.apache.poi.ss.usermodel.Cell;

import java.time.LocalDate;
import java.util.List;

public class PrintInvoicesXls extends PrintExcelDocument<InvoiceDto> {
    String type;
    PurchasesSubSuppliersOrders purchasesSubSuppliersOrders;

    protected PrintInvoicesXls(String pathToXlsTemplate, List<InvoiceDto> list) {
        super(pathToXlsTemplate, list);
    }

    @Override
    protected void selectValue(Cell editCell) {
        String formula = editCell.getStringCellValue();
        switch (formula) {
            case ("<typeOfInvoice>"):
                editCell.setCellValue(type);
            case ("<date>"):
                editCell.setCellValue(LocalDate.now());
                break;
            case ("<authorName>"):
                editCell.setCellValue("Senya Sheykin");
                break;
        }
    }

    @Override
    protected void tableSelectValue(String value, InvoiceDto model, Cell editCell) {
        switch (value) {
            case ("<id>"):
                editCell.setCellValue(String.valueOf(model.getId()));
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
            case ("<isSpend>"):
                editCell.setCellValue(String.valueOf(model.isSpend()));
                break;
            case ("<sum>"):
                editCell.setCellValue(purchasesSubSuppliersOrders.getTotalPrice(model));
                break;
        }
    }

    public void setType(String type) {
        this.type = type;
    }
}
