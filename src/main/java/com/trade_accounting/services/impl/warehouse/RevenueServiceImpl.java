package com.trade_accounting.services.impl.warehouse;

import com.trade_accounting.models.dto.warehouse.RevenueDto;
import com.trade_accounting.services.impl.CallExecuteService;
import com.trade_accounting.services.interfaces.warehouse.RevenueService;
import com.trade_accounting.services.api.warehouse.RevenueApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class RevenueServiceImpl implements RevenueService {

    private final RevenueApi revenueApi;

    private final String revenueUrl;

    private final CallExecuteService<RevenueDto> dtoCallExecuteService;

    public RevenueServiceImpl(Retrofit retrofit, @Value("${revenue_url}") String retailReturnUrl, CallExecuteService<RevenueDto> dtoCallExecuteService) {
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
        return dtoCallExecuteService.callExecuteBodyById(revenueDtoCall, RevenueDto.class, id);
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

    public List<RevenueDto> search(Map<String, String> query) {
        List<RevenueDto> revenueDtoList = new ArrayList<>();
        Call<List<RevenueDto>> revenueDtoListCall = revenueApi.search(revenueUrl, query);
        try {
            revenueDtoList = revenueDtoListCall.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение списка счетов invoice -{}", query);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение списка InvoiceDto - ", e);
        }
        return revenueDtoList;
    }
}