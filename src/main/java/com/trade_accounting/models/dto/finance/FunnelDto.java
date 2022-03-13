package com.trade_accounting.models.dto.finance;

import com.trade_accounting.models.dto.company.ContractorDto;
import com.trade_accounting.models.dto.invoice.InvoiceDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class FunnelDto {

    private Long id;

    private String statusName;

    private Long count;

    private String time;

    private String conversion;

    private String price;

    private String type;

    private List<ContractorDto> contractorDtoList;

    private List<InvoiceDto> invoiceDtoList;

    //Конструктор для listOrdersDataView
    public FunnelDto(String statusName, Long count, String time, String conversion, String price) {
        this.statusName = statusName;
        this.count = count;
        this.time = time;
        this.conversion = conversion;
        this.price = price;
    }

    //Конструктор для listContractorsDataView
    public FunnelDto(String statusName, Long count, String conversion) {
        this.statusName = statusName;
        this.count = count;
        this.conversion = conversion;
    }
}
