package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.CorrectionDto;
import com.trade_accounting.models.dto.LossDto;
import com.trade_accounting.services.interfaces.LossService;
import com.trade_accounting.services.interfaces.api.LossApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class LossServiceImpl implements LossService {

    private final LossApi lossApi;
    private final String lossUrl;
    private final CallExecuteService<LossDto> callExecuteService;
    private LossDto lossDto;

    public LossServiceImpl(Retrofit retrofit, @Value("${loss_url}") String lossUrl,
                           CallExecuteService<LossDto> callExecuteService) {
        this.lossApi = retrofit.create(LossApi.class);
        this.lossUrl = lossUrl;
        this.callExecuteService = callExecuteService;
    }

    @Override
    public List<LossDto> getAll() {
        Call<List<LossDto>> listCall = lossApi.getAll(lossUrl);
        return callExecuteService.callExecuteBodyList(listCall, LossDto.class);
    }

    @Override
    public LossDto getById(Long id) {
        Call<LossDto> lossDtoCall = lossApi.getById(lossUrl, id);
        return callExecuteService.callExecuteBodyById(lossDtoCall, LossDto.class, id);
    }

    @Override
    public LossDto create(LossDto lossDto) {
        Call<LossDto> lossDtoCall = lossApi.create(lossUrl, lossDto);

        try {
            lossDto = lossDtoCall.execute().body();
            log.info("Успешно выполнен запрос на создание Loss");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на получение LossDto - {}", e);
        }

        return lossDto;
    }

    @Override
    public void update(LossDto lossDto) {
        Call<Void> lossDtoCall = lossApi.update(lossUrl, lossDto);
        try{
            lossDtoCall.execute();
            log.info("Успешно выполнен запрос на обновление экземпляра Loss");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на обновление экземпляра LossDto - {}", e);
        }

    }

    @Override
    public void deleteById(Long id) {
        Call<Void> lossDtoCall = lossApi.deleteById(lossUrl, id);
        callExecuteService.callExecuteBodyDelete(lossDtoCall, LossDto.class, id);
    }

    @Override
    public void moveToIsRecyclebin(Long id) {
        Call<Void> dtoCall = lossApi.moveToIsRecyclebin(lossUrl, id);
        callExecuteService.callExecuteBodyMoveToIsRecyclebin(dtoCall, LossDto.class, id);
    }

    @Override
    public void restoreFromIsRecyclebin(Long id) {
        Call<Void> dtoCall = lossApi.restoreFromIsRecyclebin(lossUrl, id);
        callExecuteService.callExecuteBodyRestoreFromIsRecyclebin(dtoCall, LossDto.class, id);

    }

}
