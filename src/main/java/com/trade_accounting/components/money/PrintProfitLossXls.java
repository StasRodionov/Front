package com.trade_accounting.components.money;

import com.trade_accounting.components.util.PrintExcelDocument;
import com.trade_accounting.models.dto.client.EmployeeDto;
import com.trade_accounting.models.dto.finance.MoneySubProfitLossDto;
import com.trade_accounting.services.interfaces.client.EmployeeService;
import org.apache.poi.ss.usermodel.Cell;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PrintProfitLossXls extends PrintExcelDocument<MoneySubProfitLossDto> {

    private EmployeeDto principal;

    protected PrintProfitLossXls(String pathToXlsTemplate, List<MoneySubProfitLossDto> list, EmployeeService employeeService) {
        super(pathToXlsTemplate, list);
        this.principal = employeeService.getPrincipal();
    }

    @Override
    protected void selectValue(Cell editCell) {
        String formula = editCell.getStringCellValue();
        switch (formula) {
            case ("<date>"):
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                editCell.setCellValue(dtf.format(now));
                break;
            case ("<authorName>"):
                editCell.setCellValue("Администратор " + "(" + principal.getEmail() + " )");
                break;
            case ("<revenue>"):
                editCell.setCellValue(list.get(0).getRevenue().toString());
                break;
            case ("<costPrice>"):
                editCell.setCellValue(list.get(0).getCostPrice().toString());
                break;
            case ("<grossProfit>"):
                editCell.setCellValue(list.get(0).getGrossProfit().toString());
                break;
            case ("<operatingExpenses>"):
                editCell.setCellValue(list.get(0).getOperatingExpenses().toString());
                break;
            case ("<writeOffs>"):
                editCell.setCellValue(list.get(0).getWriteOffs().toString());
                break;
            case ("<rental>"):
                editCell.setCellValue(list.get(0).getRental().toString());
                break;
            case ("<salary>"):
                editCell.setCellValue(list.get(0).getSalary().toString());
                break;
            case ("<marketing>"):
                editCell.setCellValue(list.get(0).getMarketing().toString());
                break;
            case ("<operatingProfit>"):
                editCell.setCellValue(list.get(0).getOperatingProfit().toString());
                break;
            case ("<taxesAndFees>"):
                editCell.setCellValue(list.get(0).getTaxesAndFees().toString());
                break;
            case ("<netProfit>"):
                editCell.setCellValue(list.get(0).getNetProfit().toString());
                break;
            default:
        }
    }

    @Override
    protected void tableSelectValue(String value, MoneySubProfitLossDto model, Cell editCell) {}
}

