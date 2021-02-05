package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.InvoiceProductDto;
import com.trade_accounting.services.interfaces.InvoiceProductService;
import com.trade_accounting.services.interfaces.api.InvoiceProductApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class InvoiceProductServiceImpl implements InvoiceProductService {

    private final InvoiceProductApi invoiceProductApi;
    private final String invoiceProductUrl;

    public InvoiceProductServiceImpl(@Value("{invoice_product_url}") String invoiceProductUrl, Retrofit retrofit){
        invoiceProductApi = retrofit.create(InvoiceProductApi.class);
        this.invoiceProductUrl = invoiceProductUrl;
    }

    @Override
    public List<InvoiceProductDto> getAll() {
        List<InvoiceProductDto> invoiceProductDtoList = new ArrayList<>();
        Call<List<InvoiceProductDto>> invoiceProductDtoListCall = invoiceProductApi.getAll(invoiceProductUrl);
        try {
            invoiceProductDtoList.addAll(invoiceProductDtoListCall.execute().body());
            log.info("Успешно выполнен запрос на получение списка invoiceProductDto");
        } catch (IOException e){
            log.error("Произошла ошибка при выполнении запроса на получение списка invoiceProductDto - {}", e);
        }
        return invoiceProductDtoList;
    }

    @Override
    public InvoiceProductDto getById(Long id) {
        InvoiceProductDto invoiceProductDto = null;
        Call<InvoiceProductDto> invoiceProductDtoCall = invoiceProductApi.getById(invoiceProductUrl, id);
        try{
            invoiceProductDto = invoiceProductDtoCall.execute().body();
            log.info("Успешно выполнен запрос на получение экземпляра InvoiceProductDto с id = {}", id);
        } catch (IOException e){
            log.error("Произошла ошибка при выполнении запроса на получение экземпляра InvoiceDto по id= {} - {}", id, e);
        }
        return invoiceProductDto;
    }

    @Override
    public void create(InvoiceProductDto invoiceProductDto) {
        Call<Void> invoiceProductDtoCall = invoiceProductApi.create(invoiceProductUrl, invoiceProductDto);
        try {
            invoiceProductDtoCall.execute();
            log.info("Успешно выполнен запрос на создание экземпляра InvoiceProductDto");
        } catch (IOException e){
            log.error("Произошла ошибка при выполнении запроса на создание экземпляра InvoiceProductDto - {}", e);
        }
    }

    @Override
    public void update(InvoiceProductDto invoiceProductDto) {
        Call<Void> invoiceProductDtoCall = invoiceProductApi.create(invoiceProductUrl, invoiceProductDto);
        try {
            invoiceProductDtoCall.execute();
            log.info("Успешно выполнен запрос на обновление экземпляра InvoiceProductDto");
        } catch (IOException e){
            log.error("Произошла ошибка при выполнении запроса на обновление экземпляра InvoiceProductDto - {}", e);
        }
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> invoiceProductDtoCall = invoiceProductApi.deleteById(invoiceProductUrl, id);

        try {
            invoiceProductDtoCall.execute();
            log.info("Успешно выполнен запрос на удаление экземпляра InvoiceProductDto с id = {}", id);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на удаление экземпляра InvoiceProductDto по id= {} - {}", id, e);
        }

    }
}
