package com.trade_accounting.components.purchases;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.models.dto.company.CompanyDto;
import com.trade_accounting.models.dto.company.ContractorDto;
import com.trade_accounting.models.dto.warehouse.InventarizationDto;
import com.trade_accounting.models.dto.warehouse.WarehouseDto;
import com.trade_accounting.services.interfaces.company.CompanyService;
import com.trade_accounting.services.interfaces.company.ContractorService;
import com.trade_accounting.services.interfaces.warehouse.InventarizationService;
import com.trade_accounting.services.interfaces.warehouse.WarehouseService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.atmosphere.util.IOUtils.close;


@UIScope
@SpringComponent
@Route(value = "purchases/new-order-purchases", layout = AppView.class)
@PageTitle("Создание заказа")
@PreserveOnRefresh
@Slf4j
public class PurchasesSubPurchasingManagementModalWindow extends VerticalLayout {

    private final InventarizationService inventarizationService;
    private InventarizationDto inventarizationDto = new InventarizationDto();
    private final WarehouseService warehouseService;
    private final CompanyService companyService;
    private final ContractorService contractorService;
    private final Notifications notifications;

    private final ComboBox<CompanyDto> companyComboBox = new ComboBox<>();
    private final ComboBox<ContractorDto> contractorDtoComboBox = new ComboBox<>();
    private final ComboBox<WarehouseDto> warehouseComboBox = new ComboBox<>();
    private final DateTimePicker dateTimePicker = new DateTimePicker();
    private final Checkbox checkboxIsSent = new Checkbox("Проведена");
    private final TextField returnNumber = new TextField();
    private final TextField codeNumber = new TextField();
    private final TextField articleNumber = new TextField();
    private final TextField productMeasure = new TextField();
    private final TextField productQuantity = new TextField();
    private final TextField reservedProducts = new TextField();
    private final TextField reservedDays = new TextField();
    private final TextField daysStoreOnTheWarehouse = new TextField();
    private final TextField productsAvailableForOrder = new TextField();
    private final TextField productsAwaiting = new TextField();
    private final TextField productsReserve = new TextField();
    private final TextField restOfTheWarehouse = new TextField();
    private final TextField productSalesPerDay = new TextField();
    private final TextField productProfitMargin = new TextField();
    private final TextField productMargin = new TextField();
    private final TextField sumOfProducts = new TextField();
    private final TextArea textArea = new TextArea();
    private final H2 title = new H2("Добавление заказа");
    private final Button buttonDelete = new Button("Удалить", new Icon(VaadinIcon.TRASH));
    private final Grid<InventarizationDto> grid = new Grid<>(InventarizationDto.class, false);

    private final Binder<InventarizationDto> inventarizationBinder =
            new Binder<>(InventarizationDto.class);
    private final String TEXT_FOR_REQUEST_FIELD = "Обязательное поле";
    private String location;
    private String type;

    public PurchasesSubPurchasingManagementModalWindow(InventarizationService inventarizationService,
                                                       WarehouseService warehouseService,
                                                       CompanyService companyService,
                                                       ContractorService contractorService, Notifications notifications) {
        this.inventarizationService = inventarizationService;
        this.warehouseService = warehouseService;
        this.companyService = companyService;
        this.contractorService = contractorService;
        this.notifications = notifications;
        setSizeFull();
        add(headerLayout(), formLayout());

    }

    //TODO Разобраться с бизнесс логикой, поля которые отмеченные как заглушка.
    // 1. Или их должен вписывать пользователь
    // 2. Данные должны браться из БД
    public void setInventarizationEdit(InventarizationDto editDto) {
        this.inventarizationDto = editDto;
        returnNumber.setValue(editDto.getCompanyId().toString());
        codeNumber.setValue(editDto.getCompanyId().toString());// Заглушка
        articleNumber.setValue(editDto.getCompanyId().toString());// -/-
        productMeasure.setValue(editDto.getCompanyId().toString());// -/-
        productQuantity.setValue(editDto.getCompanyId().toString());// -/-
        reservedProducts.setValue(editDto.getCompanyId().toString());// -/-
        reservedDays.setValue(editDto.getCompanyId().toString());// -/-
        daysStoreOnTheWarehouse.setValue(editDto.getCompanyId().toString());// -/-
        productsAvailableForOrder.setValue(editDto.getCompanyId().toString());// -/-
        productsAwaiting.setValue(editDto.getCompanyId().toString());// -/-
        productsReserve.setValue(editDto.getCompanyId().toString());// -/-
        restOfTheWarehouse.setValue(editDto.getCompanyId().toString());// -/-
        productSalesPerDay.setValue(editDto.getCompanyId().toString());// -/-
        productProfitMargin.setValue(editDto.getCompanyId().toString());// -/-
        productMargin.setValue(editDto.getCompanyId().toString());// -/-
        sumOfProducts.setValue(editDto.getCompanyId().toString());// -/-
        dateTimePicker.setValue(LocalDateTime.parse(editDto.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        textArea.setValue(editDto.getComment());
        warehouseComboBox.setValue(warehouseService.getById(editDto.getWarehouseId()));
        companyComboBox.setValue(companyService.getById(editDto.getCompanyId()));

    }

    private HorizontalLayout headerLayout() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(title(), saveButton(), closeButton());
        return horizontalLayout;
    }

    private VerticalLayout formLayout() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(formLayout1(), formLayout2(), formLayout3(), formLayout4(), formLayout5(), formLayout6(), formLayout7());
        return verticalLayout;
    }

    private H2 title() {
        return new H2("Добавление нового заказа поставщикам");
    }


    private Button saveButton() {
        return new Button("Сохранить", e -> {
            //save
            if (!inventarizationBinder.validate().isOk()) {
                inventarizationBinder.validate().notifyBindingValidationStatusHandlers();
            } else {
            }
        });
    }

    private Button closeButton() {
        Button button = new Button("Закрыть", new Icon(VaadinIcon.CLOSE));
        button.addClickListener(e -> {
            close();
        });
        return button;
    }

    private HorizontalLayout formLayout1() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(productConfigure(), dateConfigure(), checkboxLayout());
        return horizontalLayout;
    }

    private HorizontalLayout formLayout2() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(productCodeConfigure(), companyConfigure(), warehouseConfigure());
        return horizontalLayout;
    }

    private HorizontalLayout formLayout3() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(articleNumberConfigure(), productMeasureConfigure(), productQuantityConfigure());
        return horizontalLayout;
    }

    private HorizontalLayout formLayout4() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(contractorConfigure(), sumOfProductsConfigure(), productMarginConfigure());
        return horizontalLayout;
    }

    private HorizontalLayout formLayout5() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(productProfitMarginConfigure(), productSalesPerDayConfigure(), restOfTheWarehouseConfigure());
        return horizontalLayout;
    }

    private HorizontalLayout formLayout6() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(productsReserveConfigure(), productsAwaitingConfigure(), productsAvailableForOrderConfigure());
        return horizontalLayout;
    }

    private HorizontalLayout formLayout7() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(daysStoreOnTheWarehouseConfigure(), reservedDaysConfigure(), reservedProductsConfigure());
        return horizontalLayout;
    }

    private HorizontalLayout sumOfProductsConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Сумма продукта");
        label.setWidth("150px");
        sumOfProducts.setWidth("300px");
        horizontalLayout.add(label, sumOfProducts);
        inventarizationBinder.forField(sumOfProducts)
                .asRequired(TEXT_FOR_REQUEST_FIELD);
        return horizontalLayout;
    }

    private HorizontalLayout productMarginConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Маржа продукта");
        label.setWidth("150px");
        productMargin.setWidth("300px");
        horizontalLayout.add(label, productMargin);
        inventarizationBinder.forField(productMargin)
                .asRequired(TEXT_FOR_REQUEST_FIELD);
        return horizontalLayout;
    }

    private HorizontalLayout productProfitMarginConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Рентабельность");
        label.setWidth("150px");
        productProfitMargin.setWidth("300px");
        horizontalLayout.add(label, productProfitMargin);
        inventarizationBinder.forField(productProfitMargin)
                .asRequired(TEXT_FOR_REQUEST_FIELD);
        return horizontalLayout;
    }

    private HorizontalLayout productSalesPerDayConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Продажи в день");
        label.setWidth("150px");
        productSalesPerDay.setWidth("300px");
        horizontalLayout.add(label, productSalesPerDay);
        inventarizationBinder.forField(productSalesPerDay)
                .asRequired(TEXT_FOR_REQUEST_FIELD);
        return horizontalLayout;
    }

    private HorizontalLayout restOfTheWarehouseConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Остаток на складе");
        label.setWidth("150px");
        restOfTheWarehouse.setWidth("300px");
        horizontalLayout.add(label, restOfTheWarehouse);
        inventarizationBinder.forField(restOfTheWarehouse)
                .asRequired(TEXT_FOR_REQUEST_FIELD);
        return horizontalLayout;
    }

    private HorizontalLayout productsReserveConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Продукты в резерве");
        label.setWidth("150px");
        productsReserve.setWidth("300px");
        horizontalLayout.add(label, productsReserve);
        inventarizationBinder.forField(productsReserve)
                .asRequired(TEXT_FOR_REQUEST_FIELD);
        return horizontalLayout;
    }

    private HorizontalLayout productsAwaitingConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Продукты в ожидании");
        label.setWidth("150px");
        productsAwaiting.setWidth("300px");
        horizontalLayout.add(label, productsAwaiting);
        inventarizationBinder.forField(productsAwaiting)
                .asRequired(TEXT_FOR_REQUEST_FIELD);
        return horizontalLayout;
    }

    private HorizontalLayout productsAvailableForOrderConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Доступны для заказа");
        label.setWidth("150px");
        productsAvailableForOrder.setWidth("300px");
        horizontalLayout.add(label, productsAvailableForOrder);
        inventarizationBinder.forField(productsAvailableForOrder)
                .asRequired(TEXT_FOR_REQUEST_FIELD);
        return horizontalLayout;
    }

    private HorizontalLayout daysStoreOnTheWarehouseConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Дней на складе");
        label.setWidth("150px");
        daysStoreOnTheWarehouse.setWidth("300px");
        horizontalLayout.add(label, daysStoreOnTheWarehouse);
        inventarizationBinder.forField(daysStoreOnTheWarehouse)
                .asRequired(TEXT_FOR_REQUEST_FIELD);
        return horizontalLayout;
    }

    private HorizontalLayout reservedDaysConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Дней в резерве");
        label.setWidth("150px");
        reservedDays.setWidth("300px");
        horizontalLayout.add(label, reservedDays);
        inventarizationBinder.forField(reservedDays)
                .asRequired(TEXT_FOR_REQUEST_FIELD);
        return horizontalLayout;
    }

    private HorizontalLayout reservedProductsConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Продукты в резерве");
        label.setWidth("150px");
        reservedProducts.setWidth("300px");
        horizontalLayout.add(label, reservedProducts);
        inventarizationBinder.forField(reservedProducts)
                .asRequired(TEXT_FOR_REQUEST_FIELD);
        return horizontalLayout;
    }

    private HorizontalLayout productConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Продукт");
        label.setWidth("150px");
        returnNumber.setWidth("300px");
        horizontalLayout.add(label, returnNumber);
        inventarizationBinder.forField(returnNumber)
                .asRequired(TEXT_FOR_REQUEST_FIELD);
        return horizontalLayout;
    }

    private HorizontalLayout articleNumberConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Артикль");
        label.setWidth("150px");
        articleNumber.setWidth("300px");
        horizontalLayout.add(label, articleNumber);
        inventarizationBinder.forField(articleNumber)
                .asRequired(TEXT_FOR_REQUEST_FIELD);
        return horizontalLayout;
    }

    private HorizontalLayout productMeasureConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Измерение продукта");
        label.setWidth("150px");
        productMeasure.setWidth("300px");
        horizontalLayout.add(label, productMeasure);
        inventarizationBinder.forField(productMeasure)
                .asRequired(TEXT_FOR_REQUEST_FIELD);
        return horizontalLayout;
    }

    private HorizontalLayout productQuantityConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Кол-во продукта");
        label.setWidth("150px");
        productQuantity.setWidth("300px");
        horizontalLayout.add(label, productQuantity);
        inventarizationBinder.forField(productQuantity)
                .asRequired(TEXT_FOR_REQUEST_FIELD);
        return horizontalLayout;
    }

    private HorizontalLayout productCodeConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Код продукта");
        label.setWidth("150px");
        codeNumber.setWidth("100px");
        horizontalLayout.add(label, codeNumber);
        inventarizationBinder.forField(codeNumber)
                .asRequired(TEXT_FOR_REQUEST_FIELD);
        return horizontalLayout;
    }

    private VerticalLayout checkboxLayout() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(checkboxIsSent);
        return verticalLayout;
    }

    private HorizontalLayout dateConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("От");
        dateTimePicker.setWidth("350px");
        horizontalLayout.add(label, dateTimePicker);
        inventarizationBinder.forField(dateTimePicker)
                .asRequired(TEXT_FOR_REQUEST_FIELD);
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
        Label label = new Label("Склад");
        label.setWidth("50");
        horizontalLayout.add(label, warehouseComboBox);
        inventarizationBinder.forField(warehouseComboBox)
                .asRequired(TEXT_FOR_REQUEST_FIELD);
        return horizontalLayout;
    }

    private HorizontalLayout companyConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        List<CompanyDto> list = companyService.getAll();
        if (list != null) {
            companyComboBox.setItems(list);
        }
        companyComboBox.setItemLabelGenerator(CompanyDto::getName);
        companyComboBox.setWidth("350px");
        Label label = new Label("Организация");
        label.setWidth("100");
        horizontalLayout.add(label, companyComboBox);
        inventarizationBinder.forField(companyComboBox)
                .asRequired(TEXT_FOR_REQUEST_FIELD);
        return horizontalLayout;
    }

    private HorizontalLayout contractorConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        List<ContractorDto> list = contractorService.getAll();
        if (list != null) {
            contractorDtoComboBox.setItems(list);
        }
        contractorDtoComboBox.setItemLabelGenerator(ContractorDto::getName);
        contractorDtoComboBox.setWidth("350px");
        Label label = new Label("Поставщик");
        label.setWidth("100");
        horizontalLayout.add(label, contractorDtoComboBox);
        inventarizationBinder.forField(contractorDtoComboBox)
                .asRequired(TEXT_FOR_REQUEST_FIELD);
        return horizontalLayout;
    }

    private void clearAllFieldsModalView() {
        companyComboBox.setValue(null);
        contractorDtoComboBox.setValue(null);
        warehouseComboBox.setValue(null);
        dateTimePicker.setValue(null);
        textArea.setValue("");
        returnNumber.setValue("");
        checkboxIsSent.setValue(false);
        articleNumber.setValue("");
        productMeasure.setValue("");
        productQuantity.setValue("");
    }

    //    private HorizontalLayout commentConfig() {
//        HorizontalLayout horizontalLayout = new HorizontalLayout();
//        Label label = new Label("Комментарий");
//        label.setWidth("100px");
//        horizontalLayout.setWidth("750px");
//        horizontalLayout.setHeight("100px");
//        horizontalLayout.add(label, textArea);
//        inventarizationBinder.forField(textArea);
//        return horizontalLayout;
//    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUpdateState(boolean isUpdate) {
        title.setText(isUpdate ? "Редактирование заказа" : "Добавление заказа");
        buttonDelete.setVisible(isUpdate);
    }

    public void resetView() {

    }
}
