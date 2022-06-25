package com.trade_accounting.components.apps.impl.finance;

import com.trade_accounting.models.dto.finance.CorrectionProductDto;
import com.trade_accounting.components.apps.impl.CallExecuteService;
import com.trade_accounting.services.interfaces.finance.CorrectionProductService;
import com.trade_accounting.services.api.finance.CorrectionProductApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class CorrectionProductServiceImpl implements CorrectionProductService {

    private final CorrectionProductApi correctionProductApi;
    private final String correctionProductUrl;
    private final CallExecuteService<CorrectionProductDto> callExecuteService;

    public CorrectionProductServiceImpl(Retrofit retrofit, @Value("${correction_product_url}") String correctionProductUrl, CallExecuteService<CorrectionProductDto> callExecuteService) {
        correctionProductApi = retrofit.create(CorrectionProductApi.class);
        this.correctionProductUrl = correctionProductUrl;
        this.callExecuteService = callExecuteService;
    }

    @Override
    public List<CorrectionProductDto> getAll() {
        return null;
    }

    @Override
    public CorrectionProductDto getById(Long id) {
        Call<CorrectionProductDto> correctionProductDtoCall = correctionProductApi.getById(correctionProductUrl, id);
        return callExecuteService.callExecuteBodyById(correctionProductDtoCall, CorrectionProductDto.class, id);
    }

    @Override
    public CorrectionProductDto create(CorrectionProductDto correctionProductDto) {
        Call<CorrectionProductDto> correctionProductDtoCall = correctionProductApi.create(correctionProductUrl, correctionProductDto);

        try {
            correctionProductDto = correctionProductDtoCall.execute().body();
            log.info("Успешно выполнен запрос на создание CorrectionProduct");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на получение CorrectionProduct - {}", e);
        }

        return correctionProductDto;
    }

    @Override
    public void update(CorrectionProductDto correctionProductDto) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
