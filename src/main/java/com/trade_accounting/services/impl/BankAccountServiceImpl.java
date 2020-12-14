package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.BankAccountDto;
import com.trade_accounting.services.interfaces.BankAccountService;
import com.trade_accounting.services.interfaces.api.BankAccountApi;
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
public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountApi bankAccountApi;

    private final String bankAccountUrl;

    private List<BankAccountDto> bankAccountDtoList;

    private BankAccountDto bankAccountDto;

    @Autowired
    public BankAccountServiceImpl(@Value("${bankAccount_url}") String bankAccountUrl, Retrofit retrofit) {

        this.bankAccountUrl = bankAccountUrl;
        bankAccountApi = retrofit.create(BankAccountApi.class);
    }

    @Override
    public List<BankAccountDto> getAll() {

        Call<List<BankAccountDto>> bankAccountDtoListCall = bankAccountApi.getAll(bankAccountUrl);

        bankAccountDtoListCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<BankAccountDto>> call, Response<List<BankAccountDto>> response) {
                if (response.isSuccessful()) {
                    bankAccountDtoList = response.body();
                } else {
                    System.out.println("Response error " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<BankAccountDto>> call, Throwable throwable) {
                //TODO Logging
            }
        });

        return bankAccountDtoList;
    }

    @Override
    public BankAccountDto getById(Long id) {

        Call<BankAccountDto> bankAccountDtoCall = bankAccountApi.getById(bankAccountUrl, id);

        bankAccountDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<BankAccountDto> call, Response<BankAccountDto> response) {
                if (response.isSuccessful()) {
                    bankAccountDto = response.body();
                } else {
                    System.out.println("Response error " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<BankAccountDto> call, Throwable throwable) {
                //TODO Logging
            }
        });
        return bankAccountDto;
    }

    @Override
    public void create(BankAccountDto dto) {

        Call<BankAccountDto> bankAccountDtoCall = bankAccountApi.create(bankAccountUrl, dto);

        bankAccountDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<BankAccountDto> call, Response<BankAccountDto> response) {
                if (!response.isSuccessful()) {
                    System.out.println("Response error " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<BankAccountDto> call, Throwable throwable) {
                //TODO Logging
            }
        });
    }

    @Override
    public void update(BankAccountDto dto) {

        Call<BankAccountDto> bankAccountDtoCall = bankAccountApi.update(bankAccountUrl, dto);

        bankAccountDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<BankAccountDto> call, Response<BankAccountDto> response) {
                if (!response.isSuccessful()) {
                    System.out.println("Response error " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<BankAccountDto> call, Throwable throwable) {
                //TODO Logging
            }
        });
    }

    @Override
    public void deleteById(Long id) {

        Call<BankAccountDto> bankAccountDtoCall = bankAccountApi.deleteById(bankAccountUrl, id);

        bankAccountDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<BankAccountDto> call, Response<BankAccountDto> response) {
                if (!response.isSuccessful()) {
                    System.out.println("Response error " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<BankAccountDto> call, Throwable throwable) {
                //TODO Logging
            }
        });
    }

    @PostConstruct
    public void test() {
        //create(new BankAccountDto(null, "15661", "Sber", "hjkhj", "446565", "465456", true, "1"));
        //getAll();
        //getById(1L);
        //update(new BankAccountDto(1L, "15661", "Sber", "hjkhj", "446565", "465456", true, "1"));
        //deleteById(1L);
    }
}
