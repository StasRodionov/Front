package com.trade_accounting.components.apps.impl.warehouse;

import com.trade_accounting.models.dto.warehouse.AcceptanceDto;
import com.trade_accounting.components.apps.impl.CallExecuteService;
import com.trade_accounting.services.interfaces.warehouse.AcceptanceService;
import com.trade_accounting.services.api.warehouse.AcceptanceApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class AcceptanceServiceImpl implements AcceptanceService {

    private final AcceptanceApi acceptanceApi;
    private final String acceptanceUrl;
    private final CallExecuteService<AcceptanceDto> callExecuteService;

    public AcceptanceServiceImpl(Retrofit retrofit, @Value("${acceptance_url}") String acceptanceUrl,
                                 CallExecuteService<AcceptanceDto> callExecuteService) {
        this.acceptanceApi = retrofit.create(AcceptanceApi.class);
        this.acceptanceUrl = acceptanceUrl;
        this.callExecuteService = callExecuteService;
    }

    @Override
    public List<AcceptanceDto> getAll() {
        List<AcceptanceDto> acceptanceDtoList = new ArrayList<>();
        Call<List<AcceptanceDto>> callListAcceptances = acceptanceApi.getAll(acceptanceUrl);
        try {
            acceptanceDtoList = callListAcceptances.execute().body();
            log.info("Успешно выполнен запрос на получение списка AcceptanceDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на получение списка AcceptanceDto - {IOException}", e);
        }
        return acceptanceDtoList;
    }

//    @Override
//    public List<AcceptanceDto> getAll() {
//        Call<List<AcceptanceDto>> acceptanceDtoListCall = acceptanceApi.getAll(acceptanceUrl);
//        return callExecuteService.callExecuteBodyList(acceptanceDtoListCall, AcceptanceDto.class);
//    }

    @Override
    public AcceptanceDto getById(Long id) {
        Call<AcceptanceDto> acceptanceDtoCall = acceptanceApi.getById(acceptanceUrl, id);
        return callExecuteService.callExecuteBodyById(acceptanceDtoCall, AcceptanceDto.class, id);
    }

    @Override
    public List<AcceptanceDto> getByProjectId(Long id) {
        List<AcceptanceDto> acceptanceDtoList = new ArrayList<>();
        Call<List<AcceptanceDto>> acceptanceDtoListCall = acceptanceApi.getByProjectId(acceptanceUrl, id);

        try {
            acceptanceDtoList = acceptanceDtoListCall.execute().body();
            log.info("Успешно выполнен запрос на получение списка AcceptanceDto");
        } catch (IOException | NullPointerException e) {
            log.error("Попытка перехода на страницу /acceptance  не авторизованного пользователя - {NullPointerException}", e);
            log.error("Произошла ошибка при выполнении запроса на получение списка AcceptanceDto - {IOException}", e);
        }
        return acceptanceDtoList;
    }

    @Override
    public Response<AcceptanceDto> create(AcceptanceDto acceptDto) {
        Call<AcceptanceDto> acceptanceDtoCall = acceptanceApi.create(acceptanceUrl, acceptDto);
        Response<AcceptanceDto> response = Response.success(new AcceptanceDto());
        try {
            response = acceptanceDtoCall.execute();
            log.info("Успешно выполнен запрос на создание Приемки");
        } catch (IOException e) {
            log.error("Произошла ошибка при создании Приемки {}", e);
        }
        return response;
    }

    @Override
    public void update(AcceptanceDto acceptDto) {
        Call<Void> updateSupplierAccount = acceptanceApi.update(acceptanceUrl, acceptDto);
        try {
            updateSupplierAccount.execute().body();
            log.info("Успешно выполнен запрос на обновление экземпляра Приемки");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на обновление экземпляра Приемки - {}", e);
        }

    }

    @Override
    public void deleteById(Long id) {
        Call<Void> acceptanceDtoCall = acceptanceApi.deleteById(acceptanceUrl, id);
        callExecuteService.callExecuteBodyDelete(acceptanceDtoCall, AcceptanceDto.class, id);
    }

    @Override
    public List<AcceptanceDto> searchByFilter(Map<String, String> queryAcceptance) {
        List<AcceptanceDto> acceptanceDtoList = new ArrayList<>();
        Call<List<AcceptanceDto>> callListAcceptance = acceptanceApi.searchByFilter(acceptanceUrl, queryAcceptance);
        try {
            acceptanceDtoList = callListAcceptance.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение приемки по фильтру {}", queryAcceptance);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск иполучение приемок {IOException}", e);
        }
        return acceptanceDtoList;
    }

    @Override
    public List<AcceptanceDto> search(String search) {
        List<AcceptanceDto> acceptanceDtoList = new ArrayList<>();
        Call<List<AcceptanceDto>> callListAcceptance = acceptanceApi.search(acceptanceUrl, search);
        try {
            acceptanceDtoList = callListAcceptance.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение приемки по фильтру {}", search);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение приемки {IOException}", e);
        }
        return acceptanceDtoList;
    }

    @Override
    public void moveToIsRecyclebin(Long id) {
        Call<Void> acceptanceDtoCall = acceptanceApi.moveToIsRecyclebin(acceptanceUrl, id);
        callExecuteService.callExecuteBodyMoveToIsRecyclebin(acceptanceDtoCall, AcceptanceDto.class, id);
    }

    @Override
    public void restoreFromIsRecyclebin(Long id) {
        Call<Void> acceptanceDtoCall = acceptanceApi.restoreFromIsRecyclebin(acceptanceUrl, id);
        callExecuteService.callExecuteBodyRestoreFromIsRecyclebin(acceptanceDtoCall, AcceptanceDto.class, id);

    }
}
