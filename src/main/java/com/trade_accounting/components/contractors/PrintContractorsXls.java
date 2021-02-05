package com.trade_accounting.components.contractors;

import com.trade_accounting.PrintExelDocument;
import com.trade_accounting.models.dto.ContractorDto;

import java.util.List;

public class PrintContractorsXls extends PrintExelDocument <ContractorDto> {

    protected PrintContractorsXls(String pathToXlsTemplate, List<ContractorDto> list) {
        super(pathToXlsTemplate, list);
    }

    @Override
    protected String getSelectValue(String formula) {
        switch (formula) {
            case ("<date>"):
                return "01.02.2021";
            case ("<authorName>"):
                return "Senya Sheykin";
        }
        return formula;
    }

    @Override
    protected String getTableSelectValue(String value, ContractorDto model) {
        switch (value) {
            case ("<name>"):
                return model.getName();
            case ("<inn>"):
                return model.getInn();
            case ("<sortNumber>"):
                return model.getSortNumber();
            case ("<phone>"):
                return model.getPhone();
            case ("<fax>"):
                return model.getFax();
            case ("<email>"):
                return model.getEmail();
            case ("<address>"):
                return model.getAddress();
            case ("<commentToAddress>"):
                return model.getCommentToAddress();
            case ("<comment>"):
                return model.getComment();
        }
        return "";

    }
}
