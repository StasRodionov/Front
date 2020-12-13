package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.TypeOfPriceDto;
import com.trade_accounting.services.interfaces.TypeOfPriceService;
import com.trade_accounting.services.interfaces.api.TypeOfPriceApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
@Slf4j
public class TypeOfPriceServiceImpl implements TypeOfPriceService {

    private final TypeOfPriceApi typeOfPriceApi;

    private final String typeOfPriceUrl;

    private List<TypeOfPriceDto> typeOfPriceDtoList;

    private TypeOfPriceDto typeOfPriceDto;

    @Autowired
    public TypeOfPriceServiceImpl(@Value("${typeOfPrice_url}") String typeOfPriceUrl, Retrofit retrofit) {

        this.typeOfPriceUrl = typeOfPriceUrl;

        typeOfPriceApi = retrofit.create(TypeOfPriceApi.class);

    }

//    @PostConstruct
//    public void init(){
//        getAll();
//        getById(1L);
//        create(new TypeOfPriceDto(null,"fff","ddd"));
//        update(new TypeOfPriceDto(2L,"fffddd","ddd"));
//        deleteById(2L);
//    }


    @Override
    public List<TypeOfPriceDto> getAll() {
        Call<List<TypeOfPriceDto>> typeOfPriceDtoListCall = typeOfPriceApi.getAll(typeOfPriceUrl);

       typeOfPriceDtoListCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<TypeOfPriceDto>> call, Response<List<TypeOfPriceDto>> response) {
                if (response.isSuccessful()) {
                    typeOfPriceDtoList = response.body();
                } else {
                    System.out.println("Response error " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<TypeOfPriceDto>> call, Throwable throwable) {
                log.debug("Произошла ошибка при получении списка TypeOfPriceDto");
            }
        });

        return typeOfPriceDtoList;
    }

    @Override
    public TypeOfPriceDto getById(Long id) {
        Call<TypeOfPriceDto> typeOfPriceDtoCall = typeOfPriceApi.getById(typeOfPriceUrl, id);

        typeOfPriceDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<TypeOfPriceDto> call, Response<TypeOfPriceDto> response) {
                if (response.isSuccessful()) {
                    typeOfPriceDto = response.body();
                } else {
                    System.out.println("Response error " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<TypeOfPriceDto> call, Throwable throwable) {
                log.debug("Произошла ошибка при получении TypeOfPriceDto c id = {}", id);
            }
        });

        return typeOfPriceDto;
    }

    @Override
    public void create(TypeOfPriceDto typeOfPriceDto) {
        Call<TypeOfPriceDto> typeOfPriceDtoCall = typeOfPriceApi.create(typeOfPriceUrl, typeOfPriceDto);

        typeOfPriceDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<TypeOfPriceDto> call, Response<TypeOfPriceDto> response) {
                if (!response.isSuccessful()) {
                    System.out.println("Response error " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<TypeOfPriceDto> call, Throwable throwable) {
                log.debug("Произошла ошибка при отправке запроса на создание нового экземпляра {}", typeOfPriceDto);
            }
        });
    }

    @Override
    public void update(TypeOfPriceDto typeOfPriceDto) {
        Call<TypeOfPriceDto> typeOfPriceDtoCall = typeOfPriceApi.update(typeOfPriceUrl, typeOfPriceDto);

        typeOfPriceDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<TypeOfPriceDto> call, Response<TypeOfPriceDto> response) {
                if (!response.isSuccessful()) {
                    System.out.println("Response error " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<TypeOfPriceDto> call, Throwable throwable) {
                log.debug("Произошла ошибка при отправке запроса на обновление экземпляра {}", typeOfPriceDto);
            }
        });
    }

    @Override
    public void deleteById(Long id) {
        Call<TypeOfPriceDto> typeOfPriceDtoCall = typeOfPriceApi.deleteById(typeOfPriceUrl, id);

        typeOfPriceDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<TypeOfPriceDto> call, Response<TypeOfPriceDto> response) {
                if (!response.isSuccessful()) {
                    System.out.println("Response error " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<TypeOfPriceDto> call, Throwable throwable) {
                log.debug("Произошла ошибка при отправке запроса на удаление экземпляра TypeOfPriceDto c id = {}", id);
            }
        });
    }
}
