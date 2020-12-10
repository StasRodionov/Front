package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.ContractorDto;
import com.trade_accounting.services.interfaces.ContractorService;
import com.trade_accounting.services.interfaces.api.ContractorApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class ContractorServiceImpl implements ContractorService {

    private final String contractorUrl;

    private final ContractorApi contractorApi;

    private List<ContractorDto> contractorDtoList;

    private ContractorDto contractorDto;

    public ContractorServiceImpl(@Value("${contractor_url}") String contractorUrl, Retrofit retrofit) {
        this.contractorUrl = contractorUrl;
        this.contractorApi = retrofit.create(ContractorApi.class);
    }

//    @PostConstruct
//    public void test() {
//        create(new ContractorDto(
//                1L,
//                "name",
//                "inn",
//                "sortNumber",
//                "phone",
//                "fax",
//                "email",
//                "address",
//                "commentToAddress",
//                "comment",
//                null,
//                null,
//                null,
//                null,
//                null
//        ));
//        getAll();
//        update(new ContractorDto(
//                1L,
//                "name1",
//                "inn1",
//                "sortNumber1",
//                "phone1",
//                "fax1",
//                "email1",
//                "address1",
//                "commentToAddress1",
//                "comment1",
//                null,
//                null,
//                null,
//                null,
//                null
//        ));
//        getById(1L);
//        deleteById(1L);
//    }

    @Override
    public List<ContractorDto> getAll() {
        Call<List<ContractorDto>> contractorDtoListCall = contractorApi.getAll(contractorUrl);

        contractorDtoListCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<ContractorDto>> call, Response<List<ContractorDto>> response) {
                if (response.isSuccessful()) {
                    contractorDtoList = response.body();
                } else {
                    System.out.println("Response error " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<ContractorDto>> call, Throwable throwable) {
                // TODO logging
            }
        });

        return contractorDtoList;
    }

    @Override
    public ContractorDto getById(Long id) {
        Call<ContractorDto> contractorDtoCall = contractorApi.getById(contractorUrl, id);

        contractorDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ContractorDto> call, Response<ContractorDto> response) {
                if (response.isSuccessful()) {
                    contractorDto = response.body();
                } else {
                    System.out.println("Response error " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ContractorDto> call, Throwable throwable) {
                // TODO logging
            }
        });

        return contractorDto;
    }

    @Override
    public ContractorDto getByEmail(String email) {
        Call<ContractorDto> contractorDtoCall = contractorApi.getByEmail(contractorUrl, email);

        contractorDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ContractorDto> call, Response<ContractorDto> response) {
                if (response.isSuccessful()) {
                    contractorDto = response.body();
                } else {
                    System.out.println("Response error " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ContractorDto> call, Throwable throwable) {
                // TODO logging
            }
        });

        return contractorDto;
    }

    @Override
    public void create(ContractorDto contractorDto) {
        Call<ContractorDto> contractorDtoCall = contractorApi.create(contractorUrl, contractorDto);

        contractorDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ContractorDto> call, Response<ContractorDto> response) {
                if (!response.isSuccessful()) {
                    System.out.println("Response error " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ContractorDto> call, Throwable throwable) {
                // TODO logging
            }
        });
    }

    @Override
    public void update(ContractorDto contractorDto) {
        Call<ContractorDto> contractorDtoCall = contractorApi.update(contractorUrl, contractorDto);

        contractorDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ContractorDto> call, Response<ContractorDto> response) {
                if (!response.isSuccessful()) {
                    System.out.println("Response error " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ContractorDto> call, Throwable throwable) {
                // TODO logging
            }
        });
    }

    @Override
    public void deleteById(Long id) {
        Call<ContractorDto> contractorDtoCall = contractorApi.deleteById(contractorUrl, id);

        contractorDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ContractorDto> call, Response<ContractorDto> response) {
                if (!response.isSuccessful()) {
                    System.out.println("Response error " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ContractorDto> call, Throwable throwable) {
                // TODO logging
            }
        });
    }
}
