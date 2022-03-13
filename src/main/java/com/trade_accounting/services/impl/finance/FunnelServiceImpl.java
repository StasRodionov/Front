package com.trade_accounting.services.impl.finance;

import com.trade_accounting.models.dto.finance.FunnelDto;
import com.trade_accounting.models.dto.invoice.InvoiceDto;
import com.trade_accounting.services.api.finance.FunnelApi;
import com.trade_accounting.services.impl.CallExecuteService;
import com.trade_accounting.services.interfaces.finance.FunnelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class FunnelServiceImpl implements FunnelService {

    private final FunnelApi funnelApi;

    private final String funnelUrl;

    private final CallExecuteService<FunnelDto> dtoCallExecuteService;

    public FunnelServiceImpl(@Value("${funnel_url}") String funnelUrl, Retrofit retrofit, CallExecuteService<FunnelDto> dtoCallExecuteService) {
        funnelApi = retrofit.create(FunnelApi.class);
        this.funnelUrl = funnelUrl;
        this.dtoCallExecuteService = dtoCallExecuteService;
    }

    @Override
    public List<FunnelDto> getAll() {
        List<FunnelDto> funnelDtoList = new ArrayList<>();
        Call<List<FunnelDto>> funnelApiAll = funnelApi.getAll(funnelUrl);

        try {
            funnelDtoList.addAll(Objects.requireNonNull(funnelApiAll.execute().body()));
            log.info("Успешно выполнен запрос на получение списка FunnelDto");
        } catch (IOException | NullPointerException e) {
            log.error("Попытка перехода на страницу /purchases  не авторизованного пользователя  - {NullPointerException}", e);
            log.error("Произошла ошибка при выполнении запроса на получение списка InvoiceDto - {IOException}", e);
        }
        return funnelDtoList;
    }

    @Override
    public List<FunnelDto> searchByFilter(Map<String, String> query) {
        List<FunnelDto> funnelDtoList = new ArrayList<>();
        Call<List<FunnelDto>> funnelApiAll = funnelApi.searchByFilter(funnelUrl, query);
        try{
            funnelDtoList = funnelApiAll.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение списка FunnelDto по ФИЛЬТРУ -{}", query);
        }catch (IOException e){
            log.error("Произошла ошибка при выполнении запроса ФИЛЬТРА на поиск и получение списка FunnelDto - ", e);
        }

        return funnelDtoList;
    }

    @Override
    public List<FunnelDto> getAllByType(String type) {
        List<FunnelDto> list = getAll();
        list.removeIf(funnelDto -> !Objects.equals(funnelDto.getType(), type));
        return list;
    }

    @Override
    public FunnelDto getById(Long id) {
        Call<FunnelDto> funnelDtoCall = funnelApi.getById(funnelUrl, id);
        return dtoCallExecuteService.callExecuteBodyById(funnelDtoCall, FunnelDto.class, id);
    }
}


