package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.UnitDto;
import com.trade_accounting.services.interfaces.UnitService;
import com.trade_accounting.services.interfaces.api.UnitApi;
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
public class UnitServiceImpl implements UnitService {

    private final UnitApi unitApi;

    private final String unitUrl;

    private List<UnitDto> unitDtoList;

    private UnitDto unitDto;


    @Autowired
    public UnitServiceImpl(@Value("${unit_url}") String unitUrl, Retrofit retrofit) {

        this.unitUrl = unitUrl;

        unitApi = retrofit.create(UnitApi.class);

    }

//    @PostConstruct
//    public void init(){
//        getAll();
//        getById(1L);
//        create(new UnitDto(null,"fff","ddd","aaa"));
//        update(new UnitDto(3L,"fff","ddd","aaa"));
//        deleteById(4L);
//    }

    @Override
    public List<UnitDto> getAll() {
        Call<List<UnitDto>> unitDtoListCall = unitApi.getAll(unitUrl);

        unitDtoListCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<UnitDto>> call, Response<List<UnitDto>> response) {
                if (response.isSuccessful()) {
                    unitDtoList = response.body();
                } else {
                    System.out.println("Response error " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<UnitDto>> call, Throwable throwable) {
                //TODO Logging
            }
        });

        return unitDtoList;
    }

    @Override
    public UnitDto getById(Long id) {
        Call<UnitDto> unitDtoCall = unitApi.getById(unitUrl, id);

        unitDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<UnitDto> call, Response<UnitDto> response) {
                if (response.isSuccessful()) {
                    unitDto = response.body();
                } else {
                    System.out.println("Response error " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<UnitDto> call, Throwable throwable) {
                //TODO Logging
            }
        });

        return unitDto;
    }

    @Override
    public void create(UnitDto unitDto) {
        Call<UnitDto> unitDtoCall = unitApi.create(unitUrl, unitDto);

        unitDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<UnitDto> call, Response<UnitDto> response) {
                if (!response.isSuccessful()) {
                    System.out.println("Response error " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<UnitDto> call, Throwable throwable) {
                //TODO Logging
            }
        });
    }

    @Override
    public void update(UnitDto unitDto) {
        Call<UnitDto> unitDtoCall = unitApi.update(unitUrl, unitDto);

        unitDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<UnitDto> call, Response<UnitDto> response) {
                if (!response.isSuccessful()) {
                    System.out.println("Response error " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<UnitDto> call, Throwable throwable) {
                //TODO Logging
            }
        });
    }

    @Override
    public void deleteById(Long id) {
        Call<UnitDto> unitDtoCall = unitApi.deleteById(unitUrl, id);

        unitDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<UnitDto> call, Response<UnitDto> response) {
                if (!response.isSuccessful()) {
                    System.out.println("Response error " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<UnitDto> call, Throwable throwable) {
                //TODO Logging
            }
        });
    }
}
