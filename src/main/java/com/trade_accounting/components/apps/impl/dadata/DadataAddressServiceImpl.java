package com.trade_accounting.components.apps.impl.dadata;

import com.kuliginstepan.dadata.client.DadataClient;
import com.kuliginstepan.dadata.client.domain.Suggestion;
import com.kuliginstepan.dadata.client.domain.address.Address;
import com.kuliginstepan.dadata.client.domain.address.AddressRequestBuilder;
import com.trade_accounting.services.interfaces.dadata.DadataAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class DadataAddressServiceImpl implements DadataAddressService {

    private final DadataClient client;

    public List<Suggestion<Address>> getAddress(String s) {
        List<Suggestion<Address>> suggestions =
                client.suggestAddress(AddressRequestBuilder.create(s).build()).collectList().block();
        return suggestions;
    }
}
