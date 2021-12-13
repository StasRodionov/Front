package com.trade_accounting.services.interfaces;

import com.trade_accounting.models.dto.ReturnAmountByProductDto;

public interface ReturnAmountByProductService {
    ReturnAmountByProductDto getTotalReturnAmountByProduct(Long id, Long invoiceId);
}
