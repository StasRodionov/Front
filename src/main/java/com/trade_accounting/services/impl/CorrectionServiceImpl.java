package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.CompanyDto;
import com.trade_accounting.models.dto.CorrectionDto;
import com.trade_accounting.models.dto.MovementDto;
import com.trade_accounting.services.interfaces.CorrectionService;
import com.trade_accounting.services.interfaces.api.CorrectionApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class CorrectionServiceImpl implements CorrectionService {

    private final CorrectionApi correctionApi;
    private final String correctionUrl;
    private final CallExecuteService<CorrectionDto> callExecuteService;

    public CorrectionServiceImpl(Retrofit retrofit, @Value("${correction_url}") String correctionUrl, CallExecuteService<CorrectionDto> callExecuteService) {
        correctionApi = retrofit.create(CorrectionApi.class);
        this.correctionUrl = correctionUrl;
        this.callExecuteService = callExecuteService;
    }

    @Override
    public List<CorrectionDto> search(Map<String, String> query) {
        List<CorrectionDto> correctionDtoList = new ArrayList<>();
        Call<List<CorrectionDto>> correctionDtoListCall = correctionApi.search(correctionUrl, query);

        try {
            correctionDtoList = correctionDtoListCall.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение списка CorrectionDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение списка CorrectionDto - ", e);
        }

        return correctionDtoList;
    }

    @Override
    public List<CorrectionDto> getAll() {
        Call<List<CorrectionDto>> invoiceDtoListCall = correctionApi.getAll(correctionUrl);
        return callExecuteService.callExecuteBodyList(invoiceDtoListCall, CorrectionDto.class);
    }

    @Override
    public CorrectionDto getById(Long id) {

        Call<CorrectionDto> correctionDtoCall = correctionApi.getById(correctionUrl, id);
        return callExecuteService.callExecuteBodyById(correctionDtoCall, CorrectionDto.class, id);
    }

    @Override
    public CorrectionDto create(CorrectionDto correctionDto) {
        return null;
    }

    @Override
    public void update(CorrectionDto correctionDto) {

    }

    @Override
    public void deleteById(Long id) {
        Call<Void> correctionDtoCall = correctionApi.deleteById(correctionUrl, id);
       callExecuteService.callExecuteBodyDelete(correctionDtoCall, CorrectionDto.class, id);
    }

    @Override
    public void moveToIsRecyclebin(Long id) {
        Call<Void> dtoCall = correctionApi.moveToIsRecyclebin(correctionUrl, id);
        callExecuteService.callExecuteBodyMoveToIsRecyclebin(dtoCall, CorrectionDto.class, id);
    }

    @Override
    public void restoreFromIsRecyclebin(Long id) {
        Call<Void> dtoCall = correctionApi.restoreFromIsRecyclebin(correctionUrl, id);
        callExecuteService.callExecuteBodyRestoreFromIsRecyclebin(dtoCall, CorrectionDto.class, id);

    }
}
