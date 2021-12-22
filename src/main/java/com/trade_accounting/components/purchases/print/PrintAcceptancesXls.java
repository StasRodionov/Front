package com.trade_accounting.components.purchases.print;

import com.trade_accounting.components.util.PrintExcelDocument;
import com.trade_accounting.models.dto.AcceptanceDto;
import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.ContractorService;
import com.trade_accounting.services.interfaces.EmployeeService;
import com.trade_accounting.services.interfaces.WarehouseService;
import org.apache.poi.ss.usermodel.Cell;

import java.time.LocalDate;
import java.util.List;

public class PrintAcceptancesXls extends PrintExcelDocument<AcceptanceDto> {
    private final ContractorService contractorService;
    private final WarehouseService warehouseService;
    private final CompanyService companyService;
    private final EmployeeService employeeService;
    private final List<String> sumList;
    private int lengthOfsumList = 0;


    public PrintAcceptancesXls(String pathToXlsTemplate, List<AcceptanceDto> list,
                               ContractorService contractorService, WarehouseService warehouseService, CompanyService companyService, List<String> sumList, EmployeeService employeeService) {
        super(pathToXlsTemplate, list);
        this.contractorService = contractorService;
        this.warehouseService = warehouseService;
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
    protected void tableSelectValue(String value, AcceptanceDto model, Cell editCell) {
        switch (value) {
            case ("<id>"):
                editCell.setCellValue(String.valueOf(model.getId()));
                break;
            case ("<date>"):
                editCell.setCellValue(model.getDate());
                break;
            case ("<warehouse>"):
                editCell.setCellValue(warehouseService.getById(model.getWarehouseId()).getName());
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
            case ("<send>"):
                editCell.setCellValue(model.getIsSent());
                break;
            case ("<print>"):
                editCell.setCellValue(model.getIsPrint());
                break;
            case ("<comment>"):
                editCell.setCellValue(model.getComment());
                break;
        }
    }
}
