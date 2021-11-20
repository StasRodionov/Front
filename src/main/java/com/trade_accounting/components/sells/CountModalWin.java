package com.trade_accounting.components.sells;


import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.models.dto.AcceptanceDto;
import com.trade_accounting.models.dto.AcceptanceProductionDto;
import com.trade_accounting.models.dto.InvoiceDto;
import com.trade_accounting.models.dto.ProductDto;
import com.trade_accounting.services.interfaces.AcceptanceService;
import com.trade_accounting.services.interfaces.ProductService;
import com.trade_accounting.services.interfaces.RemainService;
import com.trade_accounting.services.interfaces.UnitService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Shortcuts;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@SpringComponent
@PageTitle("Выбор товара из списка")
@UIScope

public class CountModalWin extends Dialog {
    static AcceptanceProductionDto acceptanceProductionDto = new AcceptanceProductionDto();
    private final TextField countTextField = new TextField();

    public CountModalWin() {
        add(configureActions(),configureButtons());
    }

    public void setReturnEdit(ProductDto productDto) {
        acceptanceProductionDto.setProductId(productDto.getId());
        acceptanceProductionDto.setPrice(productDto.getPurchasePrice());

    }

    private HorizontalLayout configureActions() {
        HorizontalLayout upper = new HorizontalLayout();
        upper.add(countConfig());
        return upper;
    }

    private HorizontalLayout configureButtons() {
        HorizontalLayout upper = new HorizontalLayout();
        upper.add(saveButton(), closeButton());
        return upper;
    }

    private H2 getTextOrder() {
        final H2 textOrder = new H2("Введите количество");
        textOrder.setHeight("2.2em");
        return textOrder;
    }

    private TextField countConfig() {
        countTextField.setWidth("300px");
        countTextField.setPlaceholder("Введите количество");
        countTextField.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        countTextField.setClearButtonVisible(true);
        countTextField.setValueChangeMode(ValueChangeMode.EAGER);
        return countTextField;
    }

    private Button saveButton() {
        Button button = new Button("Сохранить");
        button.addClickListener(e -> {
            if (countTextField!=null) {
                acceptanceProductionDto.setAmount(new BigDecimal(countTextField.getValue()));
            }else {
                acceptanceProductionDto.setAmount(new BigDecimal(0));
            }
            close();
        });
        return button;
    }

    private Button closeButton() {
        Button button = new Button("Закрыть", new Icon(VaadinIcon.CLOSE));
        button.addClickListener(e -> {
            close();
        });
        return button;

    }
    private Button buttonSettings() {
        return new Button(new Icon(VaadinIcon.COG_O));
    }

}
