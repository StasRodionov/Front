package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.AcceptanceDto;
import com.trade_accounting.services.interfaces.AcceptanceService;
import com.trade_accounting.services.interfaces.api.AcceptanceApi;
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
    private AcceptanceDto acceptanceDto;

    public AcceptanceServiceImpl(Retrofit retrofit, @Value("${acceptance_url}") String acceptanceUrl,
                                 CallExecuteService<AcceptanceDto> callExecuteService) {
        acceptanceApi = retrofit.create(AcceptanceApi.class);
        this.acceptanceUrl = acceptanceUrl;
        this.callExecuteService = callExecuteService;
    }

    @Override
    public List<AcceptanceDto> getAll() {
        Call<List<AcceptanceDto>> acceptanceDtoListCall = acceptanceApi.getAll(acceptanceUrl);
        return callExecuteService.callExecuteBodyList(acceptanceDtoListCall, AcceptanceDto.class);
    }

    @Override
    public AcceptanceDto getById(Long id) {
        Call<AcceptanceDto> acceptanceDtoCall = acceptanceApi.getById(acceptanceUrl, id);
        return callExecuteService.callExecuteBodyById(acceptanceDtoCall, acceptanceDto, AcceptanceDto.class, id);
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
            System.out.println("Ответ сервера   ");
            log.info("Успешно выполнен запрос на обновление экземпляра Приемки");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на обновление экземпляра Приемки - {}", e);
        }

    }



//
//        Call<Void> acceptanceDtoCall = acceptanceApi.update(acceptanceUrl, acceptanceDto);
//        try {
//            acceptanceDtoCall.execute();
//            log.info("Успешно выполнен запрос на обновление экземпляра Приемки");
//        } catch (IOException e) {
//            log.error("Произошла ошибка при выполнении запроса на обновление экземпляра Приемки - {}", e);
//        }
//    }

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
    public List<AcceptanceDto> searchByString(String nameFilter) {
        List<AcceptanceDto> acceptanceDtoList = new ArrayList<>();
        Call<List<AcceptanceDto>> callListAcceptance = acceptanceApi.searchByString(acceptanceUrl, nameFilter);
        try {
            acceptanceDtoList = callListAcceptance.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение приемки по фильтру {}", nameFilter);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение приемки {IOException}", e);
        }
        return acceptanceDtoList;
    }
}
