package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.TypeOfContractorDto;
import com.trade_accounting.services.interfaces.TypeOfContractorService;
import com.trade_accounting.services.interfaces.api.TypeOfContractorApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class TypeOfContractorServiceImpl implements TypeOfContractorService {

    private final TypeOfContractorApi typeOfContractorApi;
    private final String typeOfContractorUrl;
    private List<TypeOfContractorDto> typeOfContractorDtoList;
    private TypeOfContractorDto typeOfContractorDto;

    public TypeOfContractorServiceImpl(@Value("${type_of_contractor_url}") String typeOfContractorUrl, Retrofit retrofit) {
        this.typeOfContractorUrl = typeOfContractorUrl;
        typeOfContractorApi = retrofit.create(TypeOfContractorApi.class);
    }

    @Override
    public List<TypeOfContractorDto> getAll() {
        Call<List<TypeOfContractorDto>> typeOfContractorDtoListCall = typeOfContractorApi.getAll(typeOfContractorUrl);
        try {
            typeOfContractorDtoList = typeOfContractorDtoListCall.execute().body();
            log.info("Успешно выполнен запрос на получение списка TypeOfContractorDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на получение списка TypeOfContractorDto");
        }
        return typeOfContractorDtoList;
    }

    @Override
    public TypeOfContractorDto getById(Long id) {
        Call<TypeOfContractorDto> typeOfContractorDtoCall = typeOfContractorApi.getById(typeOfContractorUrl, id);
        try {
            typeOfContractorDto = typeOfContractorDtoCall.execute().body();
            log.info("Успешно выполнен запрос на получение TypeOfContractorDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на получение TypeOfContractorDto - {}", e);
        }
        return typeOfContractorDto;
    }

    @Override
    public void create(TypeOfContractorDto typeOfContractorDto) {

        Call<Void> typeOfContractorDtoCall = typeOfContractorApi.create(typeOfContractorUrl, typeOfContractorDto);
        try {
            typeOfContractorDtoCall.execute();
            log.info("Успешно выполнен запрос создания typeOfContractorDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при создании typeOfContractorDto - {}", e);
        }
    }

    @Override
    public void update(TypeOfContractorDto typeOfContractorDto) {

        Call<Void> typeOfContractorDtoCall = typeOfContractorApi.update(typeOfContractorUrl, typeOfContractorDto);
        try {
            typeOfContractorDtoCall.execute();
            log.info("Успешно выполнен запрос на изменение typeOfContractorDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при обновлении typeOfContractorDto - {}", e);
        }
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> typeOfContractorDtoCall = typeOfContractorApi.deleteById(typeOfContractorUrl, id);
        try {
            typeOfContractorDtoCall.execute();
            log.info("Успешно выполнен запрос на удаление typeOfContractor");
        } catch (IOException e) {
            log.error("Произошла ошибка при удалении typeOfContractor - {}", e);
        }
    }
}