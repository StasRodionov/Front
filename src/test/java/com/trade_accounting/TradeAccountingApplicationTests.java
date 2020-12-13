package com.trade_accounting;

import com.trade_accounting.models.dto.TaxSystemDto;
import com.trade_accounting.services.interfaces.TaxSystemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.concurrent.TimeUnit;

@SpringBootTest
class TradeAccountingApplicationTests {
    TaxSystemDto dto;

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void mainTest() throws InterruptedException {
        TaxSystemService service = applicationContext.getBean(TaxSystemService.class);

        System.out.println(service.getAll());

        service.deleteById(4L);

        dto = service.getById(6L);
        TimeUnit.SECONDS.sleep(10);

        if (dto != null) {
            dto.setName("edited Tax");
            service.update(dto);
        }
    }

    @Test
    void contextLoads() {
    }

}
