package com.trade_accounting.components.apps.impl.units;

import com.trade_accounting.components.apps.impl.CallExecuteService;
import com.trade_accounting.models.dto.units.CountryDto;
import com.trade_accounting.services.api.units.CountryApi;
import com.trade_accounting.services.interfaces.units.CountryService;
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
public class CountryServiceImpl implements CountryService {

    private final CountryApi countryApi;
    private final String countryUrl;
    private final CallExecuteService<CountryDto> countryDtoCallExecuteService;

    public CountryServiceImpl(@Value("${country_url}") String countryUrl, Retrofit retrofit, CallExecuteService<CountryDto> countryDtoCallExecuteService) {
        countryApi = retrofit.create(CountryApi.class);
        this.countryUrl = countryUrl;
        this.countryDtoCallExecuteService = countryDtoCallExecuteService;
    }

    @Override
    public List<CountryDto> getAll() {
        Call<List<CountryDto>> countryDtoGetAll = countryApi.getAll(countryUrl);
        return countryDtoCallExecuteService.callExecuteBodyList(countryDtoGetAll, CountryDto.class);
    }

    @Override
    public CountryDto getById(Long id) {
        Call<CountryDto> countryDtoCall = countryApi.getById(countryUrl, id);
        return countryDtoCallExecuteService.callExecuteBodyById(countryDtoCall, CountryDto.class, id);
    }

    @Override
    public void create(CountryDto countryDto) {
        Call<Void> countryDtoCall = countryApi.create(countryUrl, countryDto);
        countryDtoCallExecuteService.callExecuteBodyCreate(countryDtoCall, CountryDto.class);
    }

    @Override
    public void update(CountryDto countryDto) {
        Call<Void> countryDtoCall = countryApi.update(countryUrl, countryDto);
        countryDtoCallExecuteService.callExecuteBodyUpdate(countryDtoCall, CountryDto.class);
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> countryDtoCall = countryApi.deleteById(countryUrl, id);
        countryDtoCallExecuteService.callExecuteBodyDelete(countryDtoCall,CountryDto.class,id);
    }

    @Override
    public List<CountryDto> search(Map<String, String> query) {
        List<CountryDto> countryDtoList = new ArrayList<>();
        Call<List<CountryDto>> countryDtoListCall = countryApi.search(countryUrl, query);

        try{
            countryDtoList = countryDtoListCall.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение списка стран");
        } catch (IOException e){
            log.error("Произошла ошибка при выполнении запроса на поиск и получение списка стран - ", e);
        }
        return countryDtoList;
    }

    @Override
    public List<CountryDto> searchByString(String search) {
        List<CountryDto> countryDtoList = new ArrayList<>();
        Call<List<CountryDto>> countryDtoListCall = countryApi.searchByString(countryUrl, search.toLowerCase());

        try {
            countryDtoList = countryDtoListCall.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение списка стран");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение списка стран - ", e);
        }
        return countryDtoList;
    }
}
