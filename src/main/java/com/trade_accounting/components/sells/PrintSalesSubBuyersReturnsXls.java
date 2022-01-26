package com.trade_accounting.components.sells;

import com.trade_accounting.components.util.PrintExcelDocument;
import com.trade_accounting.models.dto.BuyersReturnDto;
import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.ContractorService;
import org.apache.poi.ss.usermodel.Cell;

import java.util.List;

public class PrintSalesSubBuyersReturnsXls extends PrintExcelDocument<BuyersReturnDto> {
    private final ContractorService contractorService;
    private final CompanyService companyService;
    protected List<String> sum;
    private int lengthOfSumList = 0;

    protected PrintSalesSubBuyersReturnsXls(String pathToXlsTemplate,List<BuyersReturnDto> list,
                                            ContractorService contractorService, CompanyService companyService, List<String> sum) {
        super(pathToXlsTemplate, list);
        this.contractorService = contractorService;
        this.companyService = companyService;
        this.sum = sum;
    }

    @Override
    protected void selectValue(Cell editCell) {

    }

    @Override
    protected void tableSelectValue(String value, BuyersReturnDto model, Cell editCell) {
        switch (value) {
            case ("<id>"):
                editCell.setCellValue(model.getId());
                break;
            case ("<date>"):
                editCell.setCellValue(model.getDate());
                break;
            case ("<contractor>"):
                editCell.setCellValue(contractorService.getById(model.getContractorId()).getName());
                break;
            case ("<company>"):
                editCell.setCellValue(companyService.getById(model.getCompanyId()).getName());
                break;

            case ("<sum>"):
                editCell.setCellValue(String.valueOf(model.getSum()));
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
