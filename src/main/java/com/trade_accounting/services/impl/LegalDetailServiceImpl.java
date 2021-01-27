package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.LegalDetailDto;
import com.trade_accounting.services.interfaces.LegalDetailService;
import com.trade_accounting.services.interfaces.api.LegalDetailApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class LegalDetailServiceImpl implements LegalDetailService {

    private final LegalDetailApi legalDetailApi;
    private final String legalDetailUrl;
    private LegalDetailDto legalDetailDto;

    public LegalDetailServiceImpl(@Value("${legal_detail_url}") String legalDetailUrl, Retrofit retrofit) {
        this.legalDetailUrl = legalDetailUrl;
        legalDetailApi = retrofit.create(LegalDetailApi.class);
    }

    @Override
    public List<LegalDetailDto> getAll() {
        List<LegalDetailDto> legalDetailDtoList = new ArrayList<>();
        Call<List<LegalDetailDto>> legalDetailListCall = legalDetailApi.getAll(legalDetailUrl);

        try {
            legalDetailDtoList.addAll(legalDetailListCall.execute().body());
            log.info("Успешно выполнен запрос на получение списка LegalDetailDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на получение списка LegalDetailDto - {}", e);
        }

        return legalDetailDtoList;
    }

    /*@Override
    public List<LegalDetailDto> getAll() {
        Call<List<LegalDetailDto>> legalDetailListCall = legalDetailApi.getAll(legalDetailUrl);

        legalDetailListCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<LegalDetailDto>> call, Response<List<LegalDetailDto>> response) {
                if (response.isSuccessful()) {
                    legalDetailDtoList = response.body();
                    log.info("Успешно выполнен запрос на получение списка LegalDetailDto");
                } else {
                    log.error("Произошла ошибка при выполнении запроса на получение списка LegalDetailDto - {}",
                            response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<LegalDetailDto>> call, Throwable t) {
                log.error("Произошла ошибка при получении ответа на запрос списка LegalDetailDto", t);
            }
        });

        return legalDetailDtoList;
    }*/

    @Override
    public LegalDetailDto getById(Long id) {
        Call<LegalDetailDto> legalDetailDtoCall = legalDetailApi.getById(legalDetailUrl, id);

        try {
            legalDetailDto = legalDetailDtoCall.execute().body();
            log.info("Успешно выполнен запрос на получение экземпляра LegalDetailDto по id= {}", id);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на получение экземпляра LegalDetailDto по id= {} - {}",
                    id, e);
        }

        return legalDetailDto;
    }

    /*@Override
    public LegalDetailDto getById(Long id) {
        Call<LegalDetailDto> legalDetailDtoCall = legalDetailApi.getById(legalDetailUrl, id);

        legalDetailDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<LegalDetailDto> call, Response<LegalDetailDto> response) {
                if (response.isSuccessful()) {
                    legalDetailDto = response.body();
                    log.info("Успешно выполнен запрос на получение экземпляра LegalDetailDto по id= {}", id);
                } else {
                    log.error("Произошла ошибка при выполнении запроса на получение экземпляра LegalDetailDto по id= {} - {}",
                            id, response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<LegalDetailDto> call, Throwable t) {
                log.error("Произошла ошибка при получении ответа на запрос экземпляра LegalDetailDto по id", t);
            }
        });

        return legalDetailDto;
    }*/

    @Override
    public void create(LegalDetailDto legalDetailDto) {
        Call<Void> legalDetailDtoCall = legalDetailApi.create(legalDetailUrl, legalDetailDto);

        try {
            legalDetailDtoCall.execute();
            log.info("Успешно выполнен запрос на создание экземпляра LegalDetailDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на создание экземпляра LegalDetailDto - {}", e);
        }
    }


    /*@Override
    public void create(LegalDetailDto legalDetailDto) {
        Call<Void> call = legalDetailApi.create(legalDetailUrl, legalDetailDto);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    log.info("Успешно выполнен запрос на создание экземпляра LegalDetailDto");
                } else {
                    log.error("Произошла ошибка при выполнении запроса на создание экземпляра LegalDetailDto - {}",
                            response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                log.error("Произошла ошибка при получении ответа на запрос создания экземпляра LegalDetailDto", t);
            }
        });
    }*/

    @Override
    public void update(LegalDetailDto legalDetailDto) {
        Call<Void> legalDetailDtoCall = legalDetailApi.update(legalDetailUrl, legalDetailDto);

        try {
            legalDetailDtoCall.execute();
            log.info("Успешно выполнен запрос на обновление экземпляра LegalDetailDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на обновление экземпляра LegalDetailDto - {}", e);
        }
    }

    /*@Override
    public void update(LegalDetailDto legalDetailDto) {
        Call<Void> call = legalDetailApi.update(legalDetailUrl, legalDetailDto);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    log.info("Успешно выполнен запрос на обновление экземпляра LegalDetailDto");
                } else {
                    log.error("Произошла ошибка при выполнении запроса на обновление экземпляра LegalDetailDto - {}",
                            response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                log.error("Произошла ошибка при получении ответа на запрос обновления экземпляра LegalDetailDto", t);
            }
        });
    }*/

    @Override
    public void deleteById(Long id) {
        Call<Void> legalDetailDtoCall = legalDetailApi.deleteById(legalDetailUrl, id);

        try {
            legalDetailDtoCall.execute();
            log.info("Успешно выполнен запрос на удаление экземпляра LegalDetailDto с id= {}", id);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на удаление экземпляра LegalDetailDto с id= {} - {}",
                    id, e);
        }
    }

    /*@Override
    public void deleteById(Long id) {
        Call<Void> call = legalDetailApi.deleteById(legalDetailUrl, id);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    log.info("Успешно выполнен запрос на удаление экземпляра LegalDetailDto с id= {}", id);
                } else {
                    log.error("Произошла ошибка при выполнении запроса на удаление экземпляра LegalDetailDto с id= {} - {}",
                            id, response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                log.error("Произошла ошибка при получении ответа на запрос удаления экземпляра LegalDetailDto", t);
            }
        });
    }*/
}
