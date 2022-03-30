package com.trade_accounting.services.impl.client;

import com.trade_accounting.models.dto.client.AccountDto;
import com.trade_accounting.models.dto.client.EmployeeDto;
import com.trade_accounting.services.api.client.AccountApi;
import com.trade_accounting.services.api.client.EmployeeApi;
import com.trade_accounting.services.impl.CallExecuteService;
import com.trade_accounting.services.interfaces.client.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

@Slf4j
@Service
public class AccountServiceImpl implements AccountService {

    private final AccountApi accountApi;
    private final EmployeeApi employeeApi;

    private final String accountUrl;
    private final String employeeUrl;

    private final CallExecuteService<AccountDto> dtoCallExecuteAccountService;
    private final CallExecuteService<EmployeeDto> dtoCallExecuteEmployeeService;

    public AccountServiceImpl(@Value("${account_url}") String accountUrl,
                              @Value("${account_url}") String employeeUrl,
                              Retrofit retrofit,
                              CallExecuteService<AccountDto> dtoCallExecuteAccountService,
                              CallExecuteService<EmployeeDto> dtoCallExecuteEmployeeService) {
        this.accountUrl = accountUrl;
        this.employeeUrl = employeeUrl;
        accountApi = retrofit.create(AccountApi.class);
        employeeApi = retrofit.create(EmployeeApi.class);
        this.dtoCallExecuteAccountService = dtoCallExecuteAccountService;
        this.dtoCallExecuteEmployeeService = dtoCallExecuteEmployeeService;
    }

    @Override
    public void create(AccountDto accountDto, EmployeeDto employeeDto) {
        Call<Void> accountDtoCall = accountApi.create(accountUrl, accountDto);
        dtoCallExecuteAccountService.callExecuteBodyCreate(accountDtoCall, AccountDto.class);
    }
}
