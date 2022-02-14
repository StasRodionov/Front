package com.trade_accounting.components.util;

//import com.trade_accounting.models.dto.ReturnToSupplierDto;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PrintTestServiceJXLS {


    //Данный класс должен реализовать лёгкое формирование отчёта на основе библиотеки jxls. Это нужно для реализации
    //печати сложных форм, таких как ТОРГ-13. Сделать её через POI мне не удалось. Данный код работает локально через
    // main, но выдаёт эксепш в тестируемом классе PurchaseSubReturnToSuppliers. Нужно исправить ошибку.
    // для понимания реализации гуглите jxls spring.
    // для примера рабочей локальной реализации гуглите документацию jxls


//    private final EmployeeService employeeService;
//    private final ReturnToSupplierService returnToSupplierService;
//    private final WarehouseService warehouseService;
//    private final CompanyService companyService;
//    private final ContractorService contractorService;
//    private final ContractService contractService;
//    private final List<String> sumList;
//    private int lengthOfsumList = 0;
//
//    public PrintTestService(EmployeeService employeeService, ReturnToSupplierService returnToSupplierService, WarehouseService warehouseService, CompanyService companyService, ContractorService contractorService, ContractService contractService, List<String> sumList) {
//        this.employeeService = employeeService;
//        this.returnToSupplierService = returnToSupplierService;
//        this.warehouseService = warehouseService;
//        this.companyService = companyService;
//        this.contractorService = contractorService;
//        this.contractService = contractService;
//        this.sumList = sumList;
//    }


    public ByteArrayInputStream createClientReport() throws FileNotFoundException {
        try {
            createCommonClientReport("clientsTemplate", "target/clientsTemplate.xls");
            FileInputStream fileInputStream = new FileInputStream("target/clientsTemplate.xls");
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] bys = new byte[fileInputStream.available()];
            fileInputStream.read(bys);
            outputStream.write(bys);
            return new ByteArrayInputStream(outputStream.toByteArray());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void createCommonClientReport(String templateName, String outputName) throws IOException {
//        Report report = new Report();


        // Укажите тут свой путь к файлу-темплейту и найдите способ передавать инпутстрим через адрес директории
        //проекта, а не локально, как тут.

        String pathTemplateName = ("E:\\1java\\1_Prohect\\Project\\2_Prohect_front\\src\\main\\resources\\xls_templates\\purchases_templates\\return\\test\\").concat(templateName).concat(".xls");
        InputStream input = Files.newInputStream(Paths.get(pathTemplateName));
        FileOutputStream outStream = new FileOutputStream(outputName);
        Context context = new Context();
        context.putVar("createdAt2", "2022-01-01");
        JxlsHelper.getInstance().processTemplate(input, outStream, context);

    }
}