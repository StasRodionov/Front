package com.trade_accounting.components.apps.impl.company;

import com.trade_accounting.models.dto.company.PriceListProductDto;
import com.trade_accounting.services.api.company.PriceListProductApi;
import com.trade_accounting.services.interfaces.company.PriceListProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class PriceListProductServiceImpl implements PriceListProductService {
    private final PriceListProductApi priceListProductApi;
    private final String priceListProductUrl;
    private PriceListProductDto priceListProductDto;

    public PriceListProductServiceImpl(@Value("${price_list_product_url}") String priceListProductUrl, Retrofit retrofit) {
        this.priceListProductUrl = priceListProductUrl;
        this.priceListProductApi = retrofit.create(PriceListProductApi.class);
    }

    @Override
    public List<PriceListProductDto> getAll() {
        Call<List<PriceListProductDto>> priceListProductDtoListCall = priceListProductApi.getAll(priceListProductUrl);
        List<PriceListProductDto> priceListProductDtoList = null;
        try {
            priceListProductDtoList = priceListProductDtoListCall.execute().body();
            log.info("Успешно выполнен запрос на получение списка экземпляров priceListProductDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на получение списка экземпляров priceListProductDto");
        }
        return priceListProductDtoList;
    }

    @Override
    public PriceListProductDto getById(Long id) {
        Call<PriceListProductDto> priceListDtoCall = priceListProductApi.getById(priceListProductUrl, id);
        try {
            priceListProductDto = priceListDtoCall.execute().body();
            log.info("Успешно выполнен запрос на получение экземпляра priceListProductDto по id= {}", id);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на получение экземпляра priceListProductDto по id= {} - {}",
                    id, e);
        }
        return priceListProductDto;
    }

    @Override
    public void create(PriceListProductDto priceListProductDto) {
        Call<Void> priceListProductDtoCall = priceListProductApi.create(priceListProductUrl, priceListProductDto);
        try {
            priceListProductDtoCall.execute().body();
            log.info("Успешно выполнен запрос на создание экземпляра PriceListProductDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на создание экземпляра PriceListProductDto - {}", priceListProductDto, e);
        }
    }

    @Override
    public void update(PriceListProductDto priceListProductDto) {
        Call<Void> priceListProductDtoCall = priceListProductApi.update(priceListProductUrl, priceListProductDto);
        try {
            priceListProductDtoCall.execute();
            log.info("Успешно выполнен запрос на обновление экземпляра PriceListProductDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на обновление экземпляра PriceListProductDto - {}", priceListProductDto, e);
        }
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> priceListProductDtoCall = priceListProductApi.deleteById(priceListProductUrl, id);
        try {
            priceListProductDtoCall.execute();
            log.info("Успешно выполнен запрос на удаление экземпляра PriceListProductDto с id= {}", id);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на удаление экземпляра PriceListProductDto с id= {} - {}", id, e);
        }
    }

    @Override
    public List<PriceListProductDto> getByPriceListId(Long id) {
        List<PriceListProductDto> priceListProductDtoList = null;
        Call<List<PriceListProductDto>> priceListProductDtoCall = priceListProductApi.getByPriceListId(priceListProductUrl + "/priceList", id);
        try {
            priceListProductDtoList = priceListProductDtoCall.execute().body();
            log.info("Успешно выполнен запрос на получение списка PriceListProductDto с PriceList.id = {}", id);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на получение списка PriceListProductDto по id= {} - {}", id, e);
        }
        return priceListProductDtoList;
    }

    @Override
    public List<PriceListProductDto> search(Map<String, String> query) {
        List<PriceListProductDto> priceListProductDtoList = null;
        Call<List<PriceListProductDto>> callDtoList = priceListProductApi.search(priceListProductUrl, query);
        try {
            priceListProductDtoList = callDtoList.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение списка PriceListProductDto по ФИЛЬТРУ -{}", query);
        } catch (Exception e) {
            log.error("Произошла ошибка при выполнении запроса ФИЛЬТРА на поиск и получение списка PriceListProductDto - ");
        }
        return priceListProductDtoList;
    }

    @Override
    public List<PriceListProductDto> getByProductId(Long id) {
        List<PriceListProductDto> priceListProductDtoList = null;
        Call<List<PriceListProductDto>> priceListProductDtoCall = priceListProductApi.getByProductId(priceListProductUrl + "/product", id);
        try {
            priceListProductDtoList = priceListProductDtoCall.execute().body();
            log.info("Успешно выполнен запрос на получение списка PriceListProductDto с productId = {}", id);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на получение списка PriceListProductDto по productId= {} - {}", id, e);
        }
        return priceListProductDtoList;
    }


    @Override
    public List<PriceListProductDto> quickSearch(String text) {
        List<PriceListProductDto> priceListProductDtos = new ArrayList<>();
        Call<List<PriceListProductDto>> priceListProductDtoCall = priceListProductApi.quickSearch(priceListProductUrl, text.toLowerCase());

        try {
            priceListProductDtos = priceListProductDtoCall.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение списка PriceListProductDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение списка PriceListProductDto: ", e);
        }
        return priceListProductDtos;
    }
}
