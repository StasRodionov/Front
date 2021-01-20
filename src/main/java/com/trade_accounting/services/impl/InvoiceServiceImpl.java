package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.InvoiceDto;
import com.trade_accounting.services.interfaces.InvoiceService;
import com.trade_accounting.services.interfaces.api.InvoiceApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceApi invoiceApi;
    private final String invoiceUrl;

    public InvoiceServiceImpl(@Value("${invoice_url}") String invoiceUrl, Retrofit retrofit) {
        invoiceApi = retrofit.create(InvoiceApi.class);
        this.invoiceUrl = invoiceUrl;
    }

    @Override
    public List<InvoiceDto> getAll() {
        List<InvoiceDto> invoiceDtoList = new ArrayList<>();
        Call<List<InvoiceDto>> invoiceDtoListCall = invoiceApi.getAll(invoiceUrl);

        try {
            invoiceDtoList.addAll(invoiceDtoListCall.execute().body());
            log.info("Успешно выполнен запрос на получение списка invoiceDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на получение списка invoiceDto - {}", e);
        }
        return invoiceDtoList;
    }

    @Override
    public InvoiceDto getById(Long id) {
        InvoiceDto invoiceDto = null;
        Call<InvoiceDto> invoiceDtoCall = invoiceApi.getById(invoiceUrl, id);

        try {
            invoiceDto = invoiceDtoCall.execute().body();
            log.info("Успешно выполнен запрос на получение экземпляра InvoiceDto с id = {}", id);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на получение экземпляра InvoiceDto по id= {} - {}", id, e);
        }
        return invoiceDto;
    }

    @Override
    public void create(InvoiceDto invoiceDto) {

        Call<Void> invoiceDtoCall = invoiceApi.create(invoiceUrl, invoiceDto);

        try {
            invoiceDtoCall.execute();
            log.info("Успешно выполнен запрос на создание экземпляра InvoiceDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на создание экземпляра InvoiceDto - {}", e);
        }
    }

    @Override
    public void update(InvoiceDto invoiceDto) {

        Call<Void> invoiceDtoCall = invoiceApi.update(invoiceUrl, invoiceDto);

        try {
            invoiceDtoCall.execute();
            log.info("Успешно выполнен запрос на обновление экземпляра InvoiceDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на обновление экземпляра InvoiceDto - {}", e);
        }
    }

    @Override
    public void deleteById(Long id) {

        Call<Void> invoiceDtoCall = invoiceApi.deleteById(invoiceUrl, id);

        try {
            invoiceDtoCall.execute();
            log.info("Успешно выполнен запрос на удаление экземпляра InvoiceDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на удаление экземпляра InvoiceDto по id= {} - {}", id, e);
        }
    }
}
