package com.trade_accounting.services.impl.finance;

import com.trade_accounting.models.dto.finance.ReturnAmountByProductDto;
import com.trade_accounting.services.impl.CallExecuteService;
import com.trade_accounting.services.interfaces.finance.ReturnAmountByProductService;
import com.trade_accounting.services.api.finance.ReturnAmountByProductApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Retrofit;

@Service
public class ReturnAmountByProductServiceImpl implements ReturnAmountByProductService {

    private final ReturnAmountByProductApi returnAmountByProductApi;
    private final String returnAmountByProductsUrl;
    private final CallExecuteService<ReturnAmountByProductDto> dtoCallExecuteService;

    public ReturnAmountByProductServiceImpl(
            Retrofit retrofit,
            @Value("${return_amount_by_product_url}") String returnAmountByProductsUrl,
            CallExecuteService<ReturnAmountByProductDto> dtoCallExecuteService) {
        this.returnAmountByProductApi = retrofit.create(ReturnAmountByProductApi.class);
        this.returnAmountByProductsUrl = returnAmountByProductsUrl;
        this.dtoCallExecuteService = dtoCallExecuteService;
    }

    @Override
    public ReturnAmountByProductDto getTotalReturnAmountByProduct(Long productId, Long invoiceId) {
        return dtoCallExecuteService.callExecuteBodyById(
                returnAmountByProductApi.getTotalReturnAmountByProduct(
                        returnAmountByProductsUrl, productId, invoiceId),
                ReturnAmountByProductDto.class, productId);
    }
}
