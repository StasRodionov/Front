package com.trade_accounting.services.interfaces.dadata;

import com.kuliginstepan.dadata.client.domain.Suggestion;
import com.kuliginstepan.dadata.client.domain.address.Address;

import java.util.List;

public interface DadataAddressService {

    List<Suggestion<Address>> getAddress(String s);
}
