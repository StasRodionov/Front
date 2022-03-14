package com.trade_accounting.services.interfaces.finance;

import com.trade_accounting.models.dto.finance.ReturnAmountByProductDto;

import java.math.BigDecimal;
import java.util.List;

public interface ReturnAmountByProductService {
    ReturnAmountByProductDto getTotalReturnAmountByProduct(Long id, Long invoiceId);

    List<ReturnAmountByProductDto> getByAmount(BigDecimal amount);
}
