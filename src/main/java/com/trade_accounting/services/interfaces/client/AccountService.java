package com.trade_accounting.services.interfaces.client;

import com.trade_accounting.models.dto.client.AccountDto;
import com.trade_accounting.models.dto.client.EmployeeDto;
import com.trade_accounting.services.interfaces.util.PageableService;

import java.util.List;
import java.util.Map;

public interface AccountService {

    void create(AccountDto accountDto, EmployeeDto employeeDto);
}
