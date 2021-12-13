package com.trade_accounting.components.goods;

import org.springframework.stereotype.Component;

@Component
public class TitleForModal {
    private String title;

    public TitleForModal() {
    }

    public TitleForModal(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }
}
