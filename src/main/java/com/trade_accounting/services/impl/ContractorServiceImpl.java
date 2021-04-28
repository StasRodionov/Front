package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.ContractorDto;
import com.trade_accounting.models.dto.FiasModelDto;
import com.trade_accounting.services.interfaces.ContractorService;
import com.trade_accounting.services.interfaces.api.ContractorApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class ContractorServiceImpl implements ContractorService {

    private final String contractorUrl;

    private final ContractorApi contractorApi;

    private ContractorDto contractorDto;

    public ContractorServiceImpl(@Value("${contractor_url}") String contractorUrl, Retrofit retrofit) {
        this.contractorUrl = contractorUrl;
        this.contractorApi = retrofit.create(ContractorApi.class);
    }

    @Override
    public List<ContractorDto> getAll() {
        List<ContractorDto> contractorDtoList = new ArrayList<>();
        Call<List<ContractorDto>> contractorDtoListCall = contractorApi.getAll(contractorUrl);
        try {
            contractorDtoList = contractorDtoListCall.execute().body();
            Objects.requireNonNull(contractorDtoList).forEach(contr -> contr.getLegalDetailDto().setDate(
                    contr.getLegalDetailDto().getDateOfTheCertificate()));
            log.info("Успешно выполнен запрос на получение списка ContractorDto через getAll ");
        } catch (IOException e) {
            log.error("Произошла ошибка при отправке запроса на получение списка ContractorDto: {IOException}", e);
        }
        return contractorDtoList;
    }

    @Override
    public List<ContractorDto> getAll(String searchTerm) {
        List<ContractorDto> contractorDtoList = new ArrayList<>();
        Call<List<ContractorDto>> contractorDtoListCall = contractorApi.getAll(contractorUrl, searchTerm);
        try {
            contractorDtoList = contractorDtoListCall.execute().body();
            log.info("Успешно выполнен запрос на получение списка ContractorDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при отправке запроса на получение списка ContractorDto: {IOException}", e);
        }
        return contractorDtoList;
    }

    @Override
    public List<FiasModelDto> getAllAddressByLevel(String level) {
        List<FiasModelDto> regionsList = new ArrayList<>();
        Call<List<FiasModelDto>> listCall = contractorApi.searchAddressByLevel(contractorUrl, level);
        try {
            regionsList = listCall.execute().body();
            log.info("Успешно выполнен запрос на получение списка из БД Адресов");
        } catch (IOException e) {
            log.error("Произошла ошибка при отправке запроса на получение списка из БД Адресов: {IOException}", e);
        }
        return regionsList;
    }

    @Override
    public List<FiasModelDto> getAddressesByGuid(String aoguid) {
        List<FiasModelDto> citiesList = new ArrayList<>();
        Call<List<FiasModelDto>> listCall = contractorApi.searchAddressByAoguid(contractorUrl, aoguid);
        try {
            citiesList = listCall.execute().body();
            log.info("Успешно выполнен запрос на получение списка из БД Адресов");
        } catch (IOException e) {
            log.error("Произошла ошибка при отправке запроса на получение списка из БД Адресов: {IOException}", e);
        }
        return citiesList;
    }

    @Override
    public List<ContractorDto> searchContractor(Map<String, String> queryContractor) {
        List<ContractorDto> contractorDtoList = new ArrayList<>();
        Call<List<ContractorDto>> contractorDtoListCall = contractorApi.searchContractor(contractorUrl, queryContractor);
        try {
            contractorDtoList = contractorDtoListCall.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение списка контрактов contractor");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение контрактов contractorDto: {IOException}", e);
        }
        return contractorDtoList;
    }

    @Override
    public List<ContractorDto> getAllLite() {
        List<ContractorDto> contractorDtoList = new ArrayList<>();
        Call<List<ContractorDto>> call = contractorApi.getAll(contractorUrl);
        try {
            contractorDtoList = call.execute().body();
            log.info("Успешно выполнен запрос на получение списка ContractorDto (Лёгкое ДТО)");
        } catch (IOException e) {
            log.error("Произошла ошибка при получении списка ContractorDto (Легкое ДТО): {}", e.getMessage());
        }
        return contractorDtoList;
    }

    @Override
    public ContractorDto getById(Long id) {
        Call<ContractorDto> contractorDtoCall = contractorApi.getById(contractorUrl, id);
        try {
            contractorDto = contractorDtoCall.execute().body();
            log.info("Успешно выполнен запрос на получение ContractorDto по id = " + id);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на получение ContractorDto : {IOException}", e);
        }
        return contractorDto;
    }

    @Override
    public void create(ContractorDto contractorDto) {
        Call<Void> contractorDtoCall = contractorApi.create(contractorUrl, contractorDto);
        try {
            contractorDtoCall.execute();
            log.info("Успешно выполнен запрос на добавление экземпляра ContractorDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на добавление экземпляра ContractorDto : {IOException}", e);
        }
    }

    @Override
    public void update(ContractorDto contractorDto) {
        Call<Void> contractorDtoCall = contractorApi.update(contractorUrl, contractorDto);
        try {
            contractorDtoCall.execute();
            log.info("Успешно выполнен запрос на изменение экземпляра ContractorDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на изменение экземпляра ContractorDto: {IOException}", e);
        }
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> contractorDtoCall = contractorApi.deleteById(contractorUrl, id);
        try {
            contractorDtoCall.execute();
            log.info("Успешно выполнен запрос на удаление экземпляра ContractorDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на удаление экземпляра ContractorDto : {IOException}", e);
        }
    }
}
