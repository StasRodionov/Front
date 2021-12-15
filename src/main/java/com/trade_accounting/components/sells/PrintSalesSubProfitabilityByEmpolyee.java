package com.trade_accounting.components.sells;

import com.trade_accounting.components.util.PrintExcelDocument;
import com.trade_accounting.models.dto.EmployeeDto;
import com.trade_accounting.models.dto.RetailStoreDto;
import com.trade_accounting.services.interfaces.ContractorService;
import com.trade_accounting.services.interfaces.PositionService;
import com.trade_accounting.services.interfaces.RetailStoreService;
import org.apache.poi.ss.usermodel.Cell;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class PrintSalesSubProfitabilityByEmpolyee extends PrintExcelDocument<EmployeeDto> {
    private ContractorService contractorService;
    private PositionService positionService;
    private RetailStoreService retailStoreService;
    private Map<Long, BigDecimal> cashiersIdsTotalRevenues;

    public PrintSalesSubProfitabilityByEmpolyee(String pathToXlsTemplate,
                                                List<EmployeeDto> list,
                                                ContractorService contractorService,
                                                PositionService positionService,
                                                RetailStoreService retailStoreService,
                                                Map<Long, BigDecimal> cashiersIdsTotalRevenues) {
        super(pathToXlsTemplate, list);
        this.contractorService = contractorService;
        this.positionService = positionService;
        this.retailStoreService = retailStoreService;
        this.cashiersIdsTotalRevenues = cashiersIdsTotalRevenues;
    }

    @Override
    protected void selectValue(Cell editCell) {
    }

    @Override
    protected void tableSelectValue(String value, EmployeeDto model, Cell editCell) {

        switch (value) {
            case ("<employeeDto>"):
                editCell.setCellValue(model.getFirstName() + " " + model.getLastName());
                break;

            case ("<position>"):
                editCell.setCellValue(positionService.getById(model.getPositionDtoId()).getName());
                break;

            case ("<average>"):
                editCell.setCellValue(String.format("%.2f", BigDecimal.valueOf(0.0)));
                break;

            case ("<profit>"):
                editCell.setCellValue(String.valueOf(cashiersIdsTotalRevenues.get(model.getId())));
                break;
        }
    }

}
