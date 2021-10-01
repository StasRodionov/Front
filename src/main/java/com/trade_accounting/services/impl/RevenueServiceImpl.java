package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.RevenueDto;
import com.trade_accounting.services.interfaces.RevenueService;
import com.trade_accounting.services.interfaces.api.RevenueApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.util.List;

@Slf4j
@Service
public class RevenueServiceImpl implements RevenueService {

    private final RevenueApi revenueApi;

    private final String revenueUrl;

    private RevenueDto revenueDto;

    private final CallExecuteService<RevenueDto> dtoCallExecuteService;

    public RevenueServiceImpl(Retrofit retrofit, @Value("${returns_url}") String retailReturnUrl, CallExecuteService<RevenueDto> dtoCallExecuteService) {
        this.revenueApi = retrofit.create(RevenueApi.class);
        this.revenueUrl = retailReturnUrl;
        this.dtoCallExecuteService = dtoCallExecuteService;
    }

    @Override
    public List<RevenueDto> getAll() {
        Call<List<RevenueDto>> revenueDtoListCall = revenueApi.getAll(revenueUrl);
        return dtoCallExecuteService.callExecuteBodyList(revenueDtoListCall, RevenueDto.class);
    }

    @Override
    public RevenueDto getById(Long id) {
        Call<RevenueDto> revenueDtoCall = revenueApi.getById(revenueUrl, id);
        return dtoCallExecuteService.callExecuteBodyById(revenueDtoCall, revenueDto, RevenueDto.class, id);
    }

    @Override
    public void create(RevenueDto revenueDto) {
        Call<Void> revenueDtoCall = revenueApi.create(revenueUrl, revenueDto);
        dtoCallExecuteService.callExecuteBodyCreate(revenueDtoCall, RevenueDto.class);
    }

    @Override
    public void update(RevenueDto revenueDto) {
        Call<Void> revenueDtoCall = revenueApi.update(revenueUrl, revenueDto);
        dtoCallExecuteService.callExecuteBodyUpdate(revenueDtoCall, RevenueDto.class);
    }


    @Override
    public void deleteById(Long id) {
        Call<Void> revenueDtoCall = revenueApi.deleteById(revenueUrl, id);
        dtoCallExecuteService.callExecuteBodyDelete(revenueDtoCall, RevenueDto.class, id);
    }
}