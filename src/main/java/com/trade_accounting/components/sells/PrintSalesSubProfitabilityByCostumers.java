package com.trade_accounting.components.sells;

import com.trade_accounting.components.util.PrintExcelDocument;
import com.trade_accounting.models.dto.warehouse.BuyersReturnDto;
import com.trade_accounting.models.dto.company.ContractorDto;
import com.trade_accounting.models.dto.invoice.InvoiceDto;
import com.trade_accounting.models.dto.invoice.InvoiceProductDto;
import com.trade_accounting.models.dto.warehouse.ProductDto;
import com.trade_accounting.services.interfaces.warehouse.BuyersReturnService;
import com.trade_accounting.services.interfaces.company.ContractorService;
import org.apache.poi.ss.usermodel.Cell;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PrintSalesSubProfitabilityByCostumers extends PrintExcelDocument<ContractorDto> {
    ContractorService contractorService;
    private List<InvoiceDto> invoiceDtos;
    private List<InvoiceProductDto> invoiceProductDtos;
    private List<ProductDto> productDtos;
    private BuyersReturnService buyersReturnService;

    public PrintSalesSubProfitabilityByCostumers(String pathToXlsTemplate,
                                                 List<ContractorDto> list,
                                                 ContractorService contractorService,
                                                 List<InvoiceDto> invoiceDtos,
                                                 List<InvoiceProductDto> invoiceProductDtos,
                                                 List<ProductDto> productDtos,
                                                         BuyersReturnService buyersReturnService
                                                 ) {
        super(pathToXlsTemplate, list);
        this.contractorService = contractorService;
        this.invoiceDtos = invoiceDtos;
        this.invoiceProductDtos = invoiceProductDtos;
        this.productDtos = productDtos;
        this.buyersReturnService = buyersReturnService;

    }


    @Override
    protected void selectValue(Cell editCell) {
    }

    @Override
    protected void tableSelectValue(String value, ContractorDto model, Cell editCell) {

        switch (value) {
            case ("<productDto>"):
                editCell.setCellValue(contractorService.getById(model.getId()).getName());
                break;

            case ("<description>"):
                editCell.setCellValue(getTotalPrice(model.getId()));
                break;

            case ("<amount>"):
                editCell.setCellValue(getTotalCostPrice(model.getId()));
                break;

            case ("<price>"):
                editCell.setCellValue(String.format("%.2f", BigDecimal.valueOf(0.0)));
                break;

            case ("<costPrice>"):
                editCell.setCellValue(getReturnsTotalPrice(model.getId()));
                break;

            case ("<totalSalesPrice>"):
                editCell.setCellValue(getProfit(model.getId()));
                break;
        }


    }
    private String getTotalPrice(Long id) {
        BigDecimal totalPrice = BigDecimal.valueOf(0.0);

        List<InvoiceDto> list = invoiceDtos.stream()
                .filter(a -> a.getContractorId().equals(id))
                .collect(Collectors.toList());
        List<InvoiceProductDto> list2 = new ArrayList<>();

        for (InvoiceDto ids : list) {
            list2.addAll(invoiceProductDtos.stream().filter(a -> a.getInvoiceId().
                    equals(ids.getId())).collect(Collectors.toList()));
        }

        for (InvoiceProductDto dto : list2) {
            totalPrice = totalPrice.add(dto.getPrice().multiply(dto.getAmount()));
        }
        return String.format("%.2f", totalPrice);
    }

    private String getTotalCostPrice(Long id) {
        BigDecimal totalPrice = BigDecimal.valueOf(0.0);

        List<InvoiceDto> list = invoiceDtos.stream()
                .filter(a -> a.getContractorId().equals(id))
                .collect(Collectors.toList());
        List<InvoiceProductDto> list2 = new ArrayList<>();

        for (InvoiceDto ids : list) {
            list2.addAll(invoiceProductDtos.stream().filter(a ->
                    a.getInvoiceId().equals(ids.getId())).collect(Collectors.toList()));
        }

        for (InvoiceProductDto dto : list2) {
            totalPrice = totalPrice.add(productDtos.get(dto.getProductId().intValue() - 1)
                    .getPurchasePrice().multiply(dto.getAmount()));
        }
        return String.format("%.2f", totalPrice);
    }

    private String getReturnsTotalPrice(Long id) {
        BigDecimal totalPrice = BigDecimal.valueOf(0.0);

        List<BuyersReturnDto> list = buyersReturnService.getByContractorId(id);

        for (BuyersReturnDto dto : list) {
            totalPrice = totalPrice.add(dto.getSum());
        }
        return String.format("%.2f", totalPrice);
    }

    private String getProfit(Long id) {
        BigDecimal profit;

        String buyerBuying = getTotalPrice(id);
        buyerBuying = buyerBuying.replace(',', '.');
        String buyersReturn = getReturnsTotalPrice(id);
        buyersReturn = buyersReturn.replace(',', '.');
        String costPrice = getTotalCostPrice(id);
        costPrice = costPrice.replace(',', '.');

        profit = new BigDecimal(buyerBuying).subtract(new BigDecimal(costPrice)).subtract(new BigDecimal(buyersReturn));
        return String.format("%.2f", profit);
    }

}
