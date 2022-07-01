package com.trade_accounting.components.apps.impl.company;

import com.trade_accounting.models.dto.company.PriceListProductPercentsDto;
import com.trade_accounting.services.api.company.PriceListProductPercentsApi;
import com.trade_accounting.services.interfaces.company.PriceListProductPercentsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class PriceListProductPercentsServiceImpl implements PriceListProductPercentsService {

    private final PriceListProductPercentsApi priceListProductPercentsApi;
    private final String priceListProductPricesUrl;
    private PriceListProductPercentsDto priceListProductPercentsDto;

    public PriceListProductPercentsServiceImpl(@Value("${price_list_product_percent_url}") String priceListProductPricesUrl, Retrofit retrofit) {
        this.priceListProductPricesUrl = priceListProductPricesUrl;
        this.priceListProductPercentsApi = retrofit.create(PriceListProductPercentsApi.class);
    }

    @Override
    public List<PriceListProductPercentsDto> getAll() {
        Call<List<PriceListProductPercentsDto>> priceListProductPriceDtoListCall = priceListProductPercentsApi.getAll(priceListProductPricesUrl);
        List<PriceListProductPercentsDto> priceListProductDtoList = null;
        try {
            priceListProductDtoList = priceListProductPriceDtoListCall.execute().body();
            log.info("Успешно выполнен запрос на получение списка экземпляров priceListProductPricesDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на получение списка экземпляров priceListProductPricesDto");
        }
        return priceListProductDtoList;
    }

    @Override
    public PriceListProductPercentsDto getById(Long id) {
        Call<PriceListProductPercentsDto> priceListProductPricesDtoCall = priceListProductPercentsApi.getById(priceListProductPricesUrl, id);
        try {
            priceListProductPercentsDto = priceListProductPricesDtoCall.execute().body();
            log.info("Успешно выполнен запрос на получение экземпляра priceListProductPricesDto по id= {}", id);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на получение экземпляра priceListProductPricesDto по id= {} - {}",
                    id, e);
        }
        return priceListProductPercentsDto;
    }

    @Override
    public PriceListProductPercentsDto create(PriceListProductPercentsDto priceListProductPercentsDto) {
        Call<PriceListProductPercentsDto> priceListProductPricesDtoCall = priceListProductPercentsApi.create(priceListProductPricesUrl, priceListProductPercentsDto);
        PriceListProductPercentsDto priceListProductDtoList = null;
        try {
            priceListProductDtoList = priceListProductPricesDtoCall.execute().body();
            log.info("Успешно выполнен запрос на создание экземпляра priceListProductPricesDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на создание экземпляра priceListProductPricesDto - {}", priceListProductPercentsDto, e);
        }
        return priceListProductDtoList;
    }

    @Override
    public void update(PriceListProductPercentsDto priceListProductPercentsDto) {
        Call<Void> priceListProductPricesDtoCall = priceListProductPercentsApi.update(priceListProductPricesUrl, priceListProductPercentsDto);
        try {
            priceListProductPricesDtoCall.execute();
            log.info("Успешно выполнен запрос на обновление экземпляра priceListProductPricesDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на обновление экземпляра priceListProductPricesDto - {}", priceListProductPercentsDto, e);
        }
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> priceListProductPricesDtoCall = priceListProductPercentsApi.deleteById(priceListProductPricesUrl, id);
        try {
            priceListProductPricesDtoCall.execute();
            log.info("Успешно выполнен запрос на удаление экземпляра priceListProductPricesDto с id= {}", id);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на удаление экземпляра priceListProductPricesDto с id= {} - {}", id, e);
        }
    }

    @Override
    public List<PriceListProductPercentsDto> getByPriceListId(Long id) {
        List<PriceListProductPercentsDto> priceListProductPricesDtoList = null;
        Call<List<PriceListProductPercentsDto>> priceListProductPricesDtoCall = priceListProductPercentsApi.getByPriceListId(priceListProductPricesUrl + "/priceList", id);
        try {
            priceListProductPricesDtoList = priceListProductPricesDtoCall.execute().body();
            log.info("Успешно выполнен запрос на получение списка PriceListProductDto с PriceListProduct.id = {}", id);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на получение списка PriceListProductPricesDto по id= {} - {}", id, e);
        }
        return priceListProductPricesDtoList;
    }

    @Override
    public List<PriceListProductPercentsDto> search(Map<String, String> query) {
        List<PriceListProductPercentsDto> priceListProductPricesDtoList = null;
        Call<List<PriceListProductPercentsDto>> callDtoList = priceListProductPercentsApi.search(priceListProductPricesUrl, query);
        try {
            priceListProductPricesDtoList = callDtoList.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение списка PriceListProductDto по ФИЛЬТРУ -{}", query);
        } catch (Exception e) {
            log.error("Произошла ошибка при выполнении запроса ФИЛЬТРА на поиск и получение списка PriceListProductDto - ");
        }
        return priceListProductPricesDtoList;
    }
}
