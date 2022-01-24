package com.trade_accounting.components.contractors;

import com.trade_accounting.components.util.PrintExcelDocument;
import com.trade_accounting.controllers.dto.ContractorDto;
import com.trade_accounting.services.interfaces.AddressService;
import com.trade_accounting.services.interfaces.LegalDetailService;
import org.apache.poi.ss.usermodel.Cell;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class PrintContractorsXls extends PrintExcelDocument<ContractorDto> {

    private final LegalDetailService legalDetailService;
    private final AddressService addressService;

    @Autowired
    public PrintContractorsXls(String pathToXlsTemplate, List<ContractorDto> list, LegalDetailService legalDetailService, AddressService addressService) {
        super(pathToXlsTemplate, list);
        this.legalDetailService = legalDetailService;
        this.addressService = addressService;
    }

//    protected PrintContractorsXls(String pathToXlsTemplate, List<ContractorDto> list) {
//        super(pathToXlsTemplate, list);
//    }

    @Override
    protected void selectValue(Cell editCell) {
        String formula = editCell.getStringCellValue();
        switch (formula) {
            case ("<date>"):
                editCell.setCellValue("08.02.2021");
                break;
            case ("<authorName>"):
                editCell.setCellValue("Senya Sheykin");
                break;
        }
    }

    @Override
    protected void tableSelectValue(String value, ContractorDto model, Cell editCell) {

        switch (value) {
            case ("<name>"):
                editCell.setCellValue(model.getName());
                break;
            case ("<inn>"):
                editCell.setCellValue(legalDetailService.getById(model.getLegalDetailId()).getInn());
                break;
            case ("<sortNumber>"):
                editCell.setCellValue(model.getSortNumber());
                break;
            case ("<phone>"):
                editCell.setCellValue(model.getPhone());
                break;
            case ("<fax>"):
                editCell.setCellValue(model.getFax());
                break;
            case ("<email>"):
                editCell.setCellValue(model.getEmail());
                break;
            case ("<address>"):
                editCell.setCellValue(addressService.getById(model.getAddressId()).toString());
                break;
            case ("<commentToAddress>"):
                editCell.setCellValue(model.getCommentToAddress());
                break;
            case ("<comment>"):
                editCell.setCellValue(model.getComment());
                break;
        }
    }
}
