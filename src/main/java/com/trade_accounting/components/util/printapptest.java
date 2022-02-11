//package com.trade_accounting.components.util;
//
//import com.trade_accounting.models.dto.ReturnToSupplierDto;
//import com.trade_accounting.services.interfaces.*;
//
//import java.io.FileNotFoundException;
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.List;
//
//public class printapptest {
//
//
//    private static EmployeeService employeeService;
//    private static ReturnToSupplierService returnToSupplierService;
//    private static WarehouseService warehouseService;
//    private static CompanyService companyService;
//    private static ContractorService contractorService;
//    private static ContractService contractService;
//
//    private static String getTotalPrice(ReturnToSupplierDto dto) {
//        BigDecimal totalPrice = BigDecimal.valueOf(0.0);
//        return String.format("%.2f", totalPrice);
//    }
//
//    public static void main(String[] args) throws FileNotFoundException {
//        List<String> sumList = new ArrayList<>();
//        List<ReturnToSupplierDto> list1 = returnToSupplierService.getAll();
//        for (ReturnToSupplierDto returnDto : list1) {
//            sumList.add(getTotalPrice(returnDto));
//        }
//        PrintTestServiceJXLS service = new PrintTestServiceJXLS();
//
//        service.createClientReport();
//
//    }
//}
