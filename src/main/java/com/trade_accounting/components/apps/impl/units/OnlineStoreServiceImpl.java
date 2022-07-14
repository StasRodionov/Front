package com.trade_accounting.components.apps.impl.units;

import com.trade_accounting.components.apps.impl.CallExecuteService;
import com.trade_accounting.models.dto.units.OnlineStoreDto;
import com.trade_accounting.services.api.units.OnlineStoreApi;
import com.trade_accounting.services.interfaces.units.OnlineStoreService;
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
public class OnlineStoreServiceImpl implements OnlineStoreService {

    private final OnlineStoreApi onlineStoreApi;
    private final String onlineStoreUrl;
    private final CallExecuteService<OnlineStoreDto> onlineStoreDtoCallExecuteService;

    public OnlineStoreServiceImpl(@Value("${online_store_url}") String onlineStoreUrl, Retrofit retrofit, CallExecuteService<OnlineStoreDto> onlineStoreDtoCallExecuteService) {
        onlineStoreApi = retrofit.create(OnlineStoreApi.class);
        this.onlineStoreUrl = onlineStoreUrl;
        this.onlineStoreDtoCallExecuteService = onlineStoreDtoCallExecuteService;
    }

    @Override
    public List<OnlineStoreDto> getAll() {
        Call<List<OnlineStoreDto>> dtos = onlineStoreApi.getAll(onlineStoreUrl);
        return onlineStoreDtoCallExecuteService.callExecuteBodyList(dtos, OnlineStoreDto.class);
    }

    @Override
    public OnlineStoreDto getById(Long id) {
        Call<OnlineStoreDto> dto = onlineStoreApi.getById(onlineStoreUrl, id);
        return onlineStoreDtoCallExecuteService.callExecuteBodyById(dto, OnlineStoreDto.class, id);
    }

    @Override
    public void create(OnlineStoreDto dto) {
        Call<Void> dtoCall = onlineStoreApi.create(onlineStoreUrl, dto);
        onlineStoreDtoCallExecuteService.callExecuteBodyCreate(dtoCall, OnlineStoreDto.class);
    }

    @Override
    public void update(OnlineStoreDto dto) {
        Call<Void> dtoCall = onlineStoreApi.update(onlineStoreUrl, dto);
        onlineStoreDtoCallExecuteService.callExecuteBodyUpdate(dtoCall, OnlineStoreDto.class);
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> dto = onlineStoreApi.deleteById(onlineStoreUrl, id);
        onlineStoreDtoCallExecuteService.callExecuteBodyDelete(dto,OnlineStoreDto.class, id);
    }

    @Override
    public List<OnlineStoreDto> search(Map<String, String> query) {
        List<OnlineStoreDto> dtos = new ArrayList<>();
        Call<List<OnlineStoreDto>> dtosCall = onlineStoreApi.search(onlineStoreUrl, query);

        try{
            dtos = dtosCall.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение списка магазинов");
        } catch (IOException e){
            log.error("Произошла ошибка при выполнении запроса на поиск и получение списка магазинов - ", e);
        }
        return dtos;
    }

    @Override
    public List<OnlineStoreDto> searchByString(String search) {
        List<OnlineStoreDto> dtos = new ArrayList<>();
        Call<List<OnlineStoreDto>> dtosCall = onlineStoreApi.searchByString(onlineStoreUrl, search.toLowerCase());

        try {
            dtos = dtosCall.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение списка магазинов");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение списка магазинов - ", e);
        }
        return dtos;
    }
}
