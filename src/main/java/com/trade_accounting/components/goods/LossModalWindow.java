package com.trade_accounting.components.goods;

import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.controllers.dto.CompanyDto;
import com.trade_accounting.controllers.dto.LossDto;
import com.trade_accounting.controllers.dto.LossProductDto;
import com.trade_accounting.controllers.dto.WarehouseDto;
import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.LossProductService;
import com.trade_accounting.services.interfaces.LossService;
import com.trade_accounting.services.interfaces.ProductService;
import com.trade_accounting.services.interfaces.WarehouseService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.vaadin.gatanaso.MultiselectComboBox;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@UIScope
@SpringComponent
public class LossModalWindow extends Dialog {
    LossDto lossDto;
    private final WarehouseService warehouseService;
    private final LossService lossService;
    private final CompanyService companyService;
    private final LossProductService lossProductService;
    private final ProductService productService;
    private final Notifications notifications;
    private final TitleForModal title;

    private final ComboBox<CompanyDto> companyComboBox = new ComboBox<>();
    private final ComboBox<WarehouseDto> warehouseComboBox = new ComboBox<>();
    private final DateTimePicker dateTimePicker = new DateTimePicker();
    private final MultiselectComboBox<Long> lossProductsIdComboBox= new MultiselectComboBox();
    private final com.vaadin.flow.component.checkbox.Checkbox checkboxIsSent = new Checkbox("Отправленно");
    private final com.vaadin.flow.component.checkbox.Checkbox checkboxIsPrint = new Checkbox("Напечатано");
    private final com.vaadin.flow.component.textfield.TextField returnNumber = new com.vaadin.flow.component.textfield.TextField();
    private final com.vaadin.flow.component.textfield.TextArea textArea = new TextArea();
    private final Binder<LossDto> lossDtoBinder = new Binder<>(LossDto.class);

    private final String TEXT_FOR_REQUEST_FIELD = "Обязательное поле";

    public LossModalWindow(WarehouseService warehouseService,
                           LossService lossService,
                           CompanyService companyService,
                           LossProductService lossProductService,
                           ProductService productService,
                           Notifications notifications,
                           TitleForModal title) {
        this.companyService = companyService;
        this.warehouseService = warehouseService;
        this.lossService = lossService;
        this.lossProductService = lossProductService;
        this.productService = productService;
        this.notifications = notifications;
        this.title = title;

        setSizeFull();
        add(headerLayout(), formLayout());
    }

    public void setLossEdit(LossDto editDto) {
        this.lossDto = editDto;
        returnNumber.setValue(editDto.getId().toString());
        dateTimePicker.setValue(LocalDateTime.parse(editDto.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        textArea.setValue(editDto.getComment());
        warehouseComboBox.setValue(warehouseService.getById(editDto.getWarehouseId()));
        companyComboBox.setValue(companyService.getById(editDto.getCompanyId()));
        Set<Long> idSet = new HashSet<>(lossDto.getLossProductsIds());
        lossProductsIdComboBox.setValue(idSet);
        checkboxIsPrint.setValue(lossDto.getIsPrint());
        checkboxIsSent.setValue(lossDto.getIsSent());
    }
    private HorizontalLayout headerLayout() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(title(), saveButton(), closeButton(), addGoodButton());
        return horizontalLayout;
    }
    private VerticalLayout formLayout() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(formLayout1(), formLayout2(), formLayout4());
        return verticalLayout;
    }

    private H2 title() {
        return new H2("Списание");
    }

    private com.vaadin.flow.component.button.Button saveButton() {
        return new com.vaadin.flow.component.button.Button("Сохранить", e -> {
            //save
            if (!lossDtoBinder.validate().isOk()) {
                lossDtoBinder.validate().notifyBindingValidationStatusHandlers();
            } else {
                LossDto dto = new LossDto();

                if(returnNumber.getValue() != "") {
                    dto.setId(Long.parseLong(returnNumber.getValue()));
//                    dto.setLossProductsIds(lossDto.getLossProductsIds());
                }
                dto.setCompanyId(companyComboBox.getValue().getId());
                dto.setWarehouseId(warehouseComboBox.getValue().getId());
                dto.setDate(dateTimePicker.getValue().toString());
                dto.setIsSent(checkboxIsSent.getValue());
                dto.setIsPrint(checkboxIsPrint.getValue());
                dto.setComment(textArea.getValue());
                List<Long> idList = new ArrayList<>(lossProductsIdComboBox.getValue());
                dto.setLossProductsIds(idList);
                lossService.create(dto);
                close();
                clearAllFieldsModalView();
                notifications.infoNotification("Списание сохранено");
                UI.getCurrent().navigate("lossView");
            }
        });
    }
    private com.vaadin.flow.component.button.Button closeButton() {
        com.vaadin.flow.component.button.Button button = new Button("Закрыть", new Icon(VaadinIcon.CLOSE));
        button.addClickListener(e -> {
            close();
            clearAllFieldsModalView();
        });
        return button;
    }

    private Button addGoodButton() {
        Button button = new Button("Добавить продукт", new Icon(VaadinIcon.PLUS));
        button.addClickListener(e -> {
            close();
            LossProductModalWindow modalView = new LossProductModalWindow(
                    productService,
                    companyService,
                    warehouseService,
                    lossService,
                    notifications,
                    lossProductService
            );
            modalView.open();
        });
        return button;
    }

    private HorizontalLayout formLayout1() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(numberConfigure(), dateConfigure(), checkboxLayout(), checkboxPrintLayout());
        return horizontalLayout;
    }

    private HorizontalLayout formLayout2() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(companyConfigure(), warehouseConfigure());
        return horizontalLayout;
    }

    private HorizontalLayout formLayout4() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(lossProductsConfigure(),commentConfig());
        return horizontalLayout;
    }

    private HorizontalLayout numberConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        com.vaadin.flow.component.html.Label label = new com.vaadin.flow.component.html.Label("Списание №");
        label.setWidth("150px");
        returnNumber.setWidth("50px");
        horizontalLayout.add(label, returnNumber);
        lossDtoBinder.forField(returnNumber)
                .asRequired(TEXT_FOR_REQUEST_FIELD);
        //
        return horizontalLayout;
    }

    private HorizontalLayout dateConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        com.vaadin.flow.component.html.Label label = new com.vaadin.flow.component.html.Label("От");
        dateTimePicker.setWidth("350px");
        horizontalLayout.add(label, dateTimePicker);
        lossDtoBinder.forField(dateTimePicker)
                .asRequired(TEXT_FOR_REQUEST_FIELD);
        //
        return horizontalLayout;
    }
    private VerticalLayout checkboxLayout() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(checkboxIsSent);
        return verticalLayout;
    }

    private VerticalLayout checkboxPrintLayout() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(checkboxIsPrint);
        return verticalLayout;
    }

    private HorizontalLayout companyConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        java.util.List<CompanyDto> list = companyService.getAll();
        if (list != null) {
            companyComboBox.setItems(list);
        }
        companyComboBox.setItemLabelGenerator(CompanyDto::getName);
        companyComboBox.setWidth("350px");
        com.vaadin.flow.component.html.Label label = new com.vaadin.flow.component.html.Label("Организация");
        label.setWidth("100");
        horizontalLayout.add(label, companyComboBox);
        lossDtoBinder.forField(companyComboBox)
                .asRequired(TEXT_FOR_REQUEST_FIELD);
        //
        return horizontalLayout;
    }

    private HorizontalLayout warehouseConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        List<WarehouseDto> list = warehouseService.getAll();
        if (list != null) {
            warehouseComboBox.setItems(list);
        }
        warehouseComboBox.setItemLabelGenerator(WarehouseDto::getName);
        warehouseComboBox.setWidth("350px");
        com.vaadin.flow.component.html.Label label = new com.vaadin.flow.component.html.Label("Склад");
        label.setWidth("50");
        horizontalLayout.add(label, warehouseComboBox);
        lossDtoBinder.forField(warehouseComboBox)
                .asRequired(TEXT_FOR_REQUEST_FIELD);
        //
        return horizontalLayout;
    }

    private HorizontalLayout commentConfig() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        com.vaadin.flow.component.html.Label label = new Label("Причина списания");
        label.setWidth("100px");
        horizontalLayout.setWidth("750px");
        horizontalLayout.setHeight("100px");
        horizontalLayout.add(label, textArea);
        lossDtoBinder.forField(textArea);
        return horizontalLayout;
    }
    private void clearAllFieldsModalView() {
        companyComboBox.setValue(null);
        warehouseComboBox.setValue(null);
        dateTimePicker.setValue(null);
        textArea.setValue("");
        returnNumber.setValue("");
        checkboxIsSent.setValue(false);
        checkboxIsPrint.setValue(false);
    }

    private HorizontalLayout  lossProductsConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();

        List<LossDto> iod = lossService.getAll();
        List<Long> checkInIod = new ArrayList<>();
        for(LossDto id : iod) {
            List<Long> list = id.getLossProductsIds();
            checkInIod.addAll(list);
        }

        List<LossProductDto> labels = lossProductService.getAll();
        List<Long> items = new ArrayList<>();
        for(LossProductDto id : labels) {
            Long check = id.getId();
            if(!(checkInIod.contains(check))) {
                items.add(id.getId());
            }
        }

        lossProductsIdComboBox.setItems(items);
        lossProductsIdComboBox.setItemLabelGenerator(item -> productService.getById(lossProductService
                .getById(item).getProductId()).getName());
        lossProductsIdComboBox.setWidth("350px");
        Label label = new Label("Список товаров");
        label.setWidth("100px");
        horizontalLayout.add(label, lossProductsIdComboBox);

        lossDtoBinder.forField(lossProductsIdComboBox)
                .asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(lossDto -> new HashSet<>(lossDto.getLossProductsIds()),
                        (lossDto, lossDto1) -> lossDto.setLossProductsIds(lossDto
                                .getLossProductsIds()));
//        UI.getCurrent().navigate("lossView");
        return horizontalLayout;
    }
}
