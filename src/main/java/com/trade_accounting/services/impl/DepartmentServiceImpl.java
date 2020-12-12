package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.DepartmentDto;
import com.trade_accounting.services.interfaces.DepartmentService;
import com.trade_accounting.services.interfaces.api.DepartmentApi;
import lombok.extern.slf4j.Slf4j;
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
public class DepartmentServiceImpl implements DepartmentService {

    DepartmentApi departmentApi;

    String departmentUrl;

    List<DepartmentDto> departmentDtoList;

    DepartmentDto departmentDto;

    public DepartmentServiceImpl(@Value("${department_url}") String departmentUrl, Retrofit retrofit) {
        this.departmentUrl = departmentUrl;
        this.departmentApi = retrofit.create(DepartmentApi.class);
    }

//    @PostConstruct
//    public void init() {
//        getAll();
//        getById(1L);
//        create(new DepartmentDto());
//        deleteById(1L);
//    }

    @Override
    public List<DepartmentDto> getAll() {
        Call<List<DepartmentDto>> departmentDtoListCall = departmentApi.getAll(departmentUrl);

        departmentDtoListCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<DepartmentDto>> call, Response<List<DepartmentDto>> response) {
                if (response.isSuccessful()) {
                    departmentDtoList = response.body();
                } else {
                    System.out.println("Response error " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<DepartmentDto>> call, Throwable t) {
                // TODO logging
            }
        });

        return departmentDtoList;
    }

    @Override
    public DepartmentDto getById(Long id) {
        Call<DepartmentDto> departmentDtoCall = departmentApi.getById(departmentUrl, id);

        departmentDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<DepartmentDto> call, Response<DepartmentDto> response) {
                if (response.isSuccessful()) {
                    departmentDto = response.body();
                } else {
                    System.out.println("Response error " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<DepartmentDto> call, Throwable t) {
                // TODO logging
            }
        });

        return departmentDto;
    }

    @Override
    public void create(DepartmentDto departmentDto) {
        Call<DepartmentDto> departmentDtoCall = departmentApi.create(departmentUrl, departmentDto);

        departmentDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<DepartmentDto> call, Response<DepartmentDto> response) {
                if (!response.isSuccessful()) {
                    System.out.println("Response error " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<DepartmentDto> call, Throwable t) {
                // TODO logging
            }
        });
    }

    @Override
    public void update(DepartmentDto departmentDto) {
        Call<DepartmentDto> departmentDtoCall = departmentApi.update(departmentUrl, departmentDto);

        departmentDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<DepartmentDto> call, Response<DepartmentDto> response) {
                if (!response.isSuccessful()) {
                    System.out.println("Response error " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<DepartmentDto> call, Throwable t) {
                // TODO logging
            }
        });
    }

    @Override
    public void deleteById(Long id) {
        Call<DepartmentDto> departmentDtoCall = departmentApi.deleteById(departmentUrl, id);

        departmentDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<DepartmentDto> call, Response<DepartmentDto> response) {
                if (!response.isSuccessful()) {
                    System.out.println("Response error " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<DepartmentDto> call, Throwable t) {
                // TODO logging
            }
        });
    }
}

