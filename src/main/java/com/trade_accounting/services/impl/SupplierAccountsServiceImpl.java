package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.SupplierAccountsDto;
import com.trade_accounting.services.interfaces.SupplierAccountService;
import com.trade_accounting.services.interfaces.api.SupplierAccountsApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class SupplierAccountsServiceImpl implements SupplierAccountService {

    private final SupplierAccountsApi supplier;
    private final String supplierUrl;


    public SupplierAccountsServiceImpl(@Value("${supplier_accounts_url}") String supplierUrl, Retrofit retrofit) {
        supplier = retrofit.create(SupplierAccountsApi.class);
        this.supplierUrl = supplierUrl;
    }

    @Override
    public List<SupplierAccountsDto> getAll() {
        List<SupplierAccountsDto> getAllSupplierAccounts = new ArrayList<>();
        Call<List<SupplierAccountsDto>> getAllSupplierAccountsCall = supplier.getAll(supplierUrl);

        try {
            getAllSupplierAccounts = getAllSupplierAccountsCall.execute().body();
            log.info("Успешно выполнен запрос на получение списка SupplierAccountsDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на получение списка SupplierAccountsDto - {IOException}", e);
        }
        return getAllSupplierAccounts;
    }

    @Override
    public SupplierAccountsDto getById(Long id) {
        Call<SupplierAccountsDto> getSupplierAccountByIdCall = supplier.getById(supplierUrl, id);
        SupplierAccountsDto supplierAccountsDto = new SupplierAccountsDto();
        try {
            supplierAccountsDto = getSupplierAccountByIdCall.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение счета поставщика supplierAccount по его id {}", id);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение счета поставщика SupplierAccount" +
                    " по его id {} - {IOException}", id, e);
        }
        return supplierAccountsDto;
    }

    @Override
    public Response<SupplierAccountsDto> create(SupplierAccountsDto supplierAccountsDto) {
        Call<SupplierAccountsDto> createSupplierAccount = supplier.create(supplierUrl, supplierAccountsDto);
        Response<SupplierAccountsDto> response = Response.success(new SupplierAccountsDto());
        try {
            response = createSupplierAccount.execute();
            log.info("Успешно выполнен запрос на создание supplierAccount");
        } catch (IOException e) {
            log.error("Произошла ошибка при создании счета поставщика {}", e);
        }
        return response;
    }

    @Override
    public void update(SupplierAccountsDto supplierAccountsDto) {
        Call<Void> updateSupplierAccount = supplier.update(supplierUrl, supplierAccountsDto);
        try {
            updateSupplierAccount.execute().body();
            log.info("Успешно выполнен запрос на обновление счета поставщика {}",supplierAccountsDto );
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на обновление счета поставщика {}", supplierAccountsDto);
        }
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> deleteSupplierAccountById = supplier.deleteById(supplierUrl,id);
        try {
            deleteSupplierAccountById.execute();
            log.info("Успешно выполнен запрос на удаление SupplierAccount  {}", id);
        } catch (IOException e) {
            log.error("Произошла ошибка при отправке запроса на удаление SupplierAccount {} - {}",id, e);
        }

    }
}
