package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.RoleDto;
import com.trade_accounting.services.interfaces.RoleService;
import com.trade_accounting.services.interfaces.api.RoleApi;
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
public class RoleServiceImpl implements RoleService {

    private final RoleApi roleApi;

    private final String roleUrl;

    private List<RoleDto> roleDtoList;

    private RoleDto roleDto;

    @Autowired
    public RoleServiceImpl(@Value("${role_url}") String roleUrl, Retrofit retrofit) {

        this.roleUrl = roleUrl;

        roleApi = retrofit.create(RoleApi.class);

    }

//    @PostConstruct
//    public void init(){
//        getAll();
//        getById(1L);
//        create(new RoleDto(null,"fff","ddd"));
//        update(new RoleDto(2L,"fff","ddd"));
//        deleteById(2L);
//    }

    @Override
    public List<RoleDto> getAll() {
        Call<List<RoleDto>> roleDtoListCall = roleApi.getAll(roleUrl);

        roleDtoListCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<RoleDto>> call, Response<List<RoleDto>> response) {
                if (response.isSuccessful()) {
                    roleDtoList = response.body();
                } else {
                    System.out.println("Response error " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<RoleDto>> call, Throwable throwable) {
                log.debug("Произошла ошибка при получении списка RoleDto");
            }
        });

        return roleDtoList;
    }

    @Override
    public RoleDto getById(Long id) {
        Call<RoleDto> roleDtoCall = roleApi.getById(roleUrl, id);

        roleDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<RoleDto> call, Response<RoleDto> response) {
                if (response.isSuccessful()) {
                    roleDto = response.body();
                } else {
                    System.out.println("Response error " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<RoleDto> call, Throwable throwable) {
                log.debug("Произошла ошибка при получении RoleDto c id = {}", id);
            }
        });

        return roleDto;
    }

    @Override
    public void create(RoleDto roleDto) {
        Call<RoleDto> roleDtoCall = roleApi.create(roleUrl, roleDto);

        roleDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<RoleDto> call, Response<RoleDto> response) {
                if (!response.isSuccessful()) {
                    System.out.println("Response error " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<RoleDto> call, Throwable throwable) {
                log.debug("Произошла ошибка при отправке запроса на создание нового экземпляра {}", roleDto);
            }
        });
    }

    @Override
    public void update(RoleDto roleDto) {
        Call<RoleDto> companyDtoCall = roleApi.update(roleUrl, roleDto);

        companyDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<RoleDto> call, Response<RoleDto> response) {
                if (!response.isSuccessful()) {
                    System.out.println("Response error " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<RoleDto> call, Throwable throwable) {
                log.debug("Произошла ошибка при отправке запроса на обновление экземпляра {}", roleDto);
            }
        });
    }

    @Override
    public void deleteById(Long id) {
        Call<RoleDto> companyDtoCall = roleApi.deleteById(roleUrl, id);

        companyDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<RoleDto> call, Response<RoleDto> response) {
                if (!response.isSuccessful()) {
                    System.out.println("Response error " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<RoleDto> call, Throwable throwable) {
                log.debug("Произошла ошибка при отправке запроса на удаление экземпляра RoleDto c id = {}", id);
            }
        });
    }
}
