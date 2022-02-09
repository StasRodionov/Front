package com.trade_accounting.services.interfaces.finance;

import com.trade_accounting.models.dto.finance.ReturnAmountByProductDto;

public interface ReturnAmountByProductService {
    ReturnAmountByProductDto getTotalReturnAmountByProduct(Long id, Long invoiceId);
}
