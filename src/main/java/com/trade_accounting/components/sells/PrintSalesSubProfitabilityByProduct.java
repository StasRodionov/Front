package com.trade_accounting.components.sells;

import com.trade_accounting.components.util.PrintExcelDocument;
import com.trade_accounting.models.dto.BuyersReturnDto;
import com.trade_accounting.models.dto.InvoiceProductDto;
import com.trade_accounting.models.dto.ProductDto;
import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.ContractorService;
import com.trade_accounting.services.interfaces.ProductService;
import com.trade_accounting.services.interfaces.ReturnAmountByProductService;
import org.apache.poi.ss.usermodel.Cell;

import java.util.List;

public class PrintSalesSubProfitabilityByProduct extends PrintExcelDocument<InvoiceProductDto>  {
    private ProductService productService;

    public PrintSalesSubProfitabilityByProduct(String pathToXlsTemplate,
                                               List <InvoiceProductDto> list, ProductService productService) {
        super(pathToXlsTemplate, list);
        this.productService = productService;

    }

    @Override
    protected void selectValue(Cell editCell) {

    }

    @Override
    protected void tableSelectValue(String value, InvoiceProductDto model, Cell editCell) {
        List<ProductDto> productDtos = productService.getAll();

        switch (value) {
            case ("<productDto>"):
                //iDto -> productDtos.get(invoiceProductDtos.get(iDto.getId().intValue() - 1).getProductId().intValue() - 1).getName())

                editCell.setCellValue(productService.getById(model.getProductId()).getName());
                break;
            case ("<description>"):
                editCell.setCellValue(productService.getById(model.getProductId()).getDescription());
                break;
            case ("<amount>"):
                editCell.setCellValue(model.getAmount().toString());
                //editCell.setCellValue(contractorService.getById(model.getContractorId()).getName());
                break;
            case ("<price>"):
                editCell.setCellValue(model.getPrice().toString());
                //editCell.setCellValue(companyService.getById(model.getCompanyId()).getName());
                break;

            case ("<costPrice>"):
                editCell.setCellValue(productService.getById(model.getProductId()).getPurchasePrice().toString());
                //editCell.setCellValue(String.valueOf(model.getSum()));
                break;

            case ("<totalSalesPrice>"):
                editCell.setCellValue(((model.getPrice()).multiply(model.getAmount())).toString());
                //editCell.setCellValue(model.getIsSent());
                break;
            case ("<totalCostPrice>"):
                editCell.setCellValue(((productService.getById(model.getProductId()).getPurchasePrice()).multiply(model.getAmount())).toString());
                //editCell.setCellValue(model.getIsPrint());
                break;
            case ("<totalReturnPrice>"):
                editCell.setCellValue(((productService.getById(model.getProductId()).getPurchasePrice()).multiply(model.getAmount())).toString());
                //editCell.setCellValue(model.getComment());
                break;
            case ("<profit>"):
                editCell.setCellValue(((productService.getById(model.getProductId()).getPurchasePrice()).multiply(model.getAmount())).toString());
                //editCell.setCellValue(model.getComment());
                break;
        }
    }


}
