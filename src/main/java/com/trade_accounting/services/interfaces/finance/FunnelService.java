package com.trade_accounting.services.interfaces.finance;

import com.trade_accounting.models.dto.finance.FunnelDto;
import com.trade_accounting.models.dto.invoice.InvoiceDto;

import java.util.List;
import java.util.Map;

public interface FunnelService {

    List<FunnelDto> getAll();

    List<FunnelDto> searchByFilter(Map<String ,String> query);

    List<FunnelDto> getAllByType(String type);

    FunnelDto getById(Long id);
}
