package com.trade_accounting.components.apps.impl.units;

import com.trade_accounting.components.apps.impl.CallExecuteService;
import com.trade_accounting.models.dto.units.ImportDto;
import com.trade_accounting.services.api.units.ImportApi;
import com.trade_accounting.services.interfaces.units.ImportService;
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
public class ImportServiceImpl implements ImportService {

    private final ImportApi importApi;
    private final String importUrl;
    private final CallExecuteService<ImportDto> importDtoCallExecuteService;

    public ImportServiceImpl(@Value("${import_url}") String importUrl, Retrofit retrofit, CallExecuteService<ImportDto> importDtoCallExecuteService) {
        importApi = retrofit.create(ImportApi.class);
        this.importUrl = importUrl;
        this.importDtoCallExecuteService = importDtoCallExecuteService;
    }

    @Override
    public List<ImportDto> getAll() {
        Call<List<ImportDto>> importDtoGetAll = importApi.getAll(importUrl);
        return importDtoCallExecuteService.callExecuteBodyList(importDtoGetAll, ImportDto.class);
    }

    @Override
    public ImportDto getById(Long id) {
        Call<ImportDto> importDtoCall = importApi.getById(importUrl, id);
        return importDtoCallExecuteService.callExecuteBodyById(importDtoCall, ImportDto.class, id);
    }

    @Override
    public void create(ImportDto importDto) {
        Call<Void> importDtoCall = importApi.create(importUrl, importDto);
        importDtoCallExecuteService.callExecuteBodyCreate(importDtoCall, ImportDto.class);
    }

    @Override
    public void update(ImportDto importDto) {
        Call<Void> importDtoCall = importApi.update(importUrl, importDto);
        importDtoCallExecuteService.callExecuteBodyUpdate(importDtoCall, ImportDto.class);
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> importDtoCall = importApi.deleteById(importUrl, id);
        importDtoCallExecuteService.callExecuteBodyDelete(importDtoCall,ImportDto.class,id);
    }

    @Override
    public List<ImportDto> search(Map<String, String> query) {
        List<ImportDto> importDtoList = new ArrayList<>();
        Call<List<ImportDto>> importDtoListCall = importApi.search(importUrl, query);

        try{
            importDtoList = importDtoListCall.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение списка ImportDto");
        } catch (IOException e){
            log.error("Произошла ошибка при выполнении запроса на поиск и получение списка ImportDto - ", e);
        }
        return importDtoList;
    }

    @Override
    public List<ImportDto> searchByString(String search) {
        List<ImportDto> importDtoList = new ArrayList<>();
        Call<List<ImportDto>> importDtoListCall = importApi.searchByString(importUrl, search.toLowerCase());

        try {
            importDtoList = importDtoListCall.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение списка импортов");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение списка импортов - ", e);
        }
        return importDtoList;
    }


}
