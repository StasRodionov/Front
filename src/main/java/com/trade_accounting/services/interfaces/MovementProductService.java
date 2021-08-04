package com.trade_accounting.services.interfaces;

import java.util.List;

public interface MovementProductService {
    List<MovementProductService> getAll();

    MovementProductService getById(Long id);

    MovementProductService create(MovementProductService movementProductService);

    void update(MovementProductService movementProductService);

    void deleteById(Long id);
}
