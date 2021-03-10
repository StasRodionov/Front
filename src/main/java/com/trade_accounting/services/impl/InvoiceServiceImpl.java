package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.InvoiceDto;
import com.trade_accounting.services.interfaces.InvoiceService;
import com.trade_accounting.services.interfaces.api.InvoiceApi;
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

@Service
@Slf4j
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceApi invoiceApi;
    private final String invoiceUrl;
    private InvoiceDto invoiceDto;

    public InvoiceServiceImpl(@Value("${invoice_url}") String invoiceUrl, Retrofit retrofit) {
        invoiceApi = retrofit.create(InvoiceApi.class);
        this.invoiceUrl = invoiceUrl;
    }

    /*@Override
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
    }*/
    @Override
    public List<InvoiceDto> getAll() {

        List<InvoiceDto> invoiceDtoList = new ArrayList<>();
        Call<List<InvoiceDto>> invoiceDtoListCall = invoiceApi.getAll(invoiceUrl);

        try {
            invoiceDtoList.addAll(Objects.requireNonNull(invoiceDtoListCall.execute().body()));
            log.info("Успешно выполнен запрос на получение списка InvoiceDto");
        } catch (IOException | NullPointerException e) {
            log.error("Попытка перехода на страницу /purchases  не авторизованного пользователя  - {NullPointerException}", e);
            log.error("Произошла ошибка при выполнении запроса на получение списка InvoiceDto - {IOException}", e);
        }

//        invoiceDtoListCall.enqueue(new Callback<>() {
//
//            @Override
//            public void onResponse(Call<List<InvoiceDto>> call, Response<List<InvoiceDto>> response) {
//                System.out.println("********************************************");
//                System.out.println(response.code());
//                if (response.isSuccessful()) {
//                    invoiceDtoList.addAll(response.body());
//                    log.info("Успешно выполнен запрос на получение списка InvoiceDto");
//                } else {
//                    log.error("Произошла ошибка при выполнении запроса на получение списка InvoiceDto - {}", response.errorBody());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<InvoiceDto>> call, Throwable throwable) {
//                log.error("Произошла ошибка при получении ответа на запрос списка InvoiceDto", throwable);
//            }
//        });
        return invoiceDtoList;
    }

    /*@Override
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
    }*/

    @Override
    public InvoiceDto getById(Long id) {
        Call<InvoiceDto> invoiceDtoCall = invoiceApi.getById(invoiceUrl, id);

        invoiceDtoCall.enqueue(new Callback<>() {

            @Override
            public void onResponse(Call<InvoiceDto> call, Response<InvoiceDto> response) {
                if (response.isSuccessful()) {
                    invoiceDto = response.body();
                    log.info("Успешно выполнен запрос на получение экзаепляра InvoiceDto с id = {}", id);
                } else {
                    log.error("Произошла ошибка при выполнении запроса на получение экземпляра InvoiceDto - {}", response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<InvoiceDto> call, Throwable throwable) {
                log.error("Произошла ошибка при получении ответа на запрос экземпляра InvoiceDto", throwable);
            }
        });
        return invoiceDto;
    }

    @Override
    public List<InvoiceDto> search(Map<String, String> query) {
        List<InvoiceDto> invoiceDtoList = new ArrayList<>();
        Call<List<InvoiceDto>> invoiceDtoListCall = invoiceApi.search(invoiceUrl, query);

        try {
            invoiceDtoList = invoiceDtoListCall.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение списка счетов invoice");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение списка InvoiceDto - ", e);
        }
        return invoiceDtoList;
    }


    /*@Override
    public void create(InvoiceDto invoiceDto) {

        Call<Void> invoiceDtoCall = invoiceApi.create(invoiceUrl, invoiceDto);

        try {
            invoiceDtoCall.execute();
            log.info("Успешно выполнен запрос на создание экземпляра InvoiceDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на создание экземпляра InvoiceDto - {}", e);
        }
    }*/


    @Override
    public void create(InvoiceDto invoiceDto) {

        Call<Void> invoiceDtoCall = invoiceApi.create(invoiceUrl, invoiceDto);

        invoiceDtoCall.enqueue(new Callback<>() {

            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    log.info("Успешно выполнен запрос на создание экземпляра InvoiceDto");
                } else {
                    log.error("Произошла ошибка при выполнении запроса на создание экземпляра InvoiceDto - {}", response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                log.error("Произошла ошибка при получении ответа на запрос создания экземпляра InvoiceDto", throwable);
            }
        });
    }

    /*@Override
    public void update(InvoiceDto invoiceDto) {

        Call<Void> invoiceDtoCall = invoiceApi.update(invoiceUrl, invoiceDto);

        try {
            invoiceDtoCall.execute();
            log.info("Успешно выполнен запрос на обновление экземпляра InvoiceDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на обновление экземпляра InvoiceDto - {}", e);
        }
    }*/

    @Override
    public void update(InvoiceDto invoiceDto) {

        Call<Void> invoiceDtoCall = invoiceApi.update(invoiceUrl, invoiceDto);

        invoiceDtoCall.enqueue(new Callback<>() {

            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    log.info("Успешно выполнен запрос на обновление экземпляра InvoiceDto");
                } else {
                    log.error("Произошла ошибка при выполнении запроса на обновление экземпляра InvoiceDto - {}", response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                log.error("Произошла ошибка при получении ответа на запрос обновления экземпляра InvoiceDto", throwable);
            }
        });
    }

    /*@Override
    public void deleteById(Long id) {

        Call<Void> invoiceDtoCall = invoiceApi.deleteById(invoiceUrl, id);

        try {
            invoiceDtoCall.execute();
            log.info("Успешно выполнен запрос на удаление экземпляра InvoiceDto с id = {}", id);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на удаление экземпляра InvoiceDto по id= {} - {}", id, e);
        }
    }*/

    @Override
    public void deleteById(Long id) {

        Call<Void> invoiceDtoCall = invoiceApi.deleteById(invoiceUrl, id);

        invoiceDtoCall.enqueue(new Callback<>() {

            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    log.info("Успешно выполнен запрос на удаление экземпляра InvoiceDto");
                } else {
                    log.error("Произошла ошибка при выполнении запроса на удаление экземпляра InvoiceDto - {}", response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                log.error("Произошла ошибка при получении ответа на запрос удаления экземпляра InvoiceDto", throwable);
            }
        });
    }
}
