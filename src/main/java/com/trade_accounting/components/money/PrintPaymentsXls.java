package com.trade_accounting.components.money;

import com.trade_accounting.components.util.PrintExcelDocument;
import com.trade_accounting.models.dto.PaymentDto;
import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.ContractService;
import com.trade_accounting.services.interfaces.ContractorService;
import com.trade_accounting.services.interfaces.ProjectService;
import org.apache.poi.ss.usermodel.Cell;

import java.time.LocalDateTime;
import java.util.List;

public class PrintPaymentsXls extends PrintExcelDocument<PaymentDto> {
    private final CompanyService companyService;
    private final ContractService contractService;
    private final ContractorService contractorService;
    private final ProjectService projectService;

    protected PrintPaymentsXls(String pathToXlsTemplate, List<PaymentDto> list, CompanyService companyService,
                               ContractorService contractorService, ContractService contractService, ProjectService projectService) {
        super(pathToXlsTemplate, list);
        this.companyService = companyService;
        this.contractorService = contractorService;
        this.contractService = contractService;
        this.projectService = projectService;
    }

    @Override
    protected void selectValue(Cell editCell) {
        String formula = editCell.getStringCellValue();
        switch (formula) {
            case ("<date>"):
                editCell.setCellValue(LocalDateTime.now());
                break;
            case ("<authorName>"):
                editCell.setCellValue("Author");
                break;
            default:
        }
    }

    @Override
    protected void tableSelectValue(String value, PaymentDto model, Cell editCell) {
        switch (value) {
            case ("<id>"):
                editCell.setCellValue(model.getId());
                break;
            case ("<time>"):
                editCell.setCellValue(model.getTime());
                break;
            case ("<company>"):
                editCell.setCellValue(companyService.getById(model.getCompanyId()).getName());
                break;
            case ("<sum>"):
                editCell.setCellValue(String.valueOf(model.getSum()));
                break;
            case ("<number>"):
                editCell.setCellValue(model.getNumber());
                break;
            case ("<typeOfPayment>"):
                editCell.setCellValue(model.getTypeOfPayment());
                break;
            case ("<contractor>"):
                editCell.setCellValue(contractorService.getById(model.getContractorId()).getName());
                break;
            case ("<contract>"):
                editCell.setCellValue(contractService.getById(model.getContractId()).getNumber());
                break;
            case ("<project>"):
                editCell.setCellValue(projectService.getById(model.getProjectId()).getName());
                break;
            default:
        }
    }
}

