package com.trade_accounting.components.purchases.print;

import com.trade_accounting.components.util.PrintExcelDocument;
import com.trade_accounting.models.dto.InvoiceReceivedDto;
import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.ContractorService;
import com.trade_accounting.services.interfaces.EmployeeService;
import org.apache.poi.ss.usermodel.Cell;

import java.time.LocalDate;
import java.util.List;

public class PrintInvoiceReceivedXls extends PrintExcelDocument<InvoiceReceivedDto> {
    private final ContractorService contractorService;

    private final CompanyService companyService;
    private final EmployeeService employeeService;
    private final List<String> sumList;
    private int lengthOfsumList = 0;


    public PrintInvoiceReceivedXls(String pathToXlsTemplate, List<InvoiceReceivedDto> list,
                                   ContractorService contractorService, CompanyService companyService, List<String> sumList, EmployeeService employeeService) {
        super(pathToXlsTemplate, list);
        this.contractorService = contractorService;

        this.companyService = companyService;
        this.employeeService = employeeService;
        this.sumList = sumList;
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
    protected void tableSelectValue(String value, InvoiceReceivedDto model, Cell editCell) {
        switch (value) {
            case ("<id>"):
                editCell.setCellValue(String.valueOf(model.getId()));
                break;
            case ("<date>"):
                editCell.setCellValue(model.getData());
                break;
            case ("<contractor>"):
                editCell.setCellValue(contractorService.getById(model.getContractorId()).getName());
                break;
            case ("<company>"):
                editCell.setCellValue(companyService.getById(model.getCompanyId()).getName());
                break;
            case ("<sum>"):
                editCell.setCellValue(sumList.get(lengthOfsumList++));
                if (lengthOfsumList >= sumList.size()) {
                    lengthOfsumList = 0;
                }
                break;

            case ("<incomNumber>"):
                editCell.setCellValue(model.getIncomNumber());
                break;

            case ("<incomData>"):
                editCell.setCellValue(model.getIncomData());
                break;


            case ("<send>"):
                if (model.getIsSend()) {
                    editCell.setCellValue("+");
                } else {
                    editCell.setCellValue("-");
                }
                break;
            case ("<print>"):
                if (model.getIsPrint()) {
                    editCell.setCellValue("+");
                } else {
                    editCell.setCellValue("-");
                }
                break;
            case ("<comment>"):
                editCell.setCellValue(model.getComment());
                break;
        }
    }
}
