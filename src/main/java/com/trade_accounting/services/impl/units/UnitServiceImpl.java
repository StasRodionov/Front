package com.trade_accounting.services.impl.units;

import com.trade_accounting.models.dto.units.UnitDto;
import com.trade_accounting.services.impl.CallExecuteService;
import com.trade_accounting.services.interfaces.units.UnitService;
import com.trade_accounting.services.api.units.UnitApi;
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
public class UnitServiceImpl implements UnitService {

    private final UnitApi unitApi;

    private final String unitUrl;

    private final CallExecuteService<UnitDto> dtoCallExecuteService;

    public UnitServiceImpl(@Value("${unit_url}") String unitUrl, Retrofit retrofit, CallExecuteService<UnitDto> dtoCallExecuteService) {
        this.unitUrl = unitUrl;
        unitApi = retrofit.create(UnitApi.class);
        this.dtoCallExecuteService = dtoCallExecuteService;
    }

    @Override
    public List<UnitDto> getAll() {
        Call<List<UnitDto>> unitDtoListCall = unitApi.getAll(unitUrl);
        return dtoCallExecuteService.callExecuteBodyList(unitDtoListCall, UnitDto.class);
    }

    @Override
    public UnitDto getById(Long id) {
        Call<UnitDto> unitDtoCall = unitApi.getById(unitUrl, id);
        return dtoCallExecuteService.callExecuteBodyById(unitDtoCall, UnitDto.class, id);
    }

    @Override
    public void create(UnitDto unitDto) {
        Call<Void> unitDtoCall = unitApi.create(unitUrl, unitDto);
        dtoCallExecuteService.callExecuteBodyCreate(unitDtoCall, UnitDto.class);
    }

    @Override
    public void update(UnitDto unitDto) {
        Call<Void> unitDtoCall = unitApi.update(unitUrl, unitDto);
        dtoCallExecuteService.callExecuteBodyUpdate(unitDtoCall, UnitDto.class);
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> unitDtoCall = unitApi.deleteById(unitUrl, id);
        dtoCallExecuteService.callExecuteBodyDelete(unitDtoCall, UnitDto.class, id);
    }

    @Override
    public List<UnitDto> search(Map<String, String> query) {
        List<UnitDto> uitDtoList = new ArrayList<>();
        Call<List<UnitDto>> uitDtoListListCall = unitApi.search(unitUrl, query);
        try {
            uitDtoList = uitDtoListListCall.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение списка единиц измерения");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение списка eдиниц измерения - ", e);
        }
        return uitDtoList;
    }

    @Override
    public List<UnitDto> findBySearch(String search) {
        List<UnitDto> unitDtoList = new ArrayList<>();
        Call<List<UnitDto>> unitDtoListCall = unitApi
                .searchByString(unitUrl, search.toLowerCase());

        try {
            unitDtoList = unitDtoListCall.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение списка единиц измерения");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение списка eдиниц измерения - ", e);
        }
        return unitDtoList;
    }
}
