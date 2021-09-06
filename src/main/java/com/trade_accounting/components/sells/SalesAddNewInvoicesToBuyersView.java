package com.trade_accounting.components.sells;

import com.trade_accounting.components.AppView;
import com.trade_accounting.models.dto.CompanyDto;
import com.trade_accounting.models.dto.ContractDto;
import com.trade_accounting.models.dto.ContractorDto;
import com.trade_accounting.models.dto.InvoiceProductDto;
import com.trade_accounting.models.dto.InvoiceToBuyerListProductsDto;
import com.trade_accounting.models.dto.WarehouseDto;
import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.ContractService;
import com.trade_accounting.services.interfaces.ContractorService;
import com.trade_accounting.services.interfaces.WarehouseService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Route(value = "sells/add-new-invoices-to-buyers", layout = AppView.class)
@PageTitle("Новый счет")
@PreserveOnRefresh
@SpringComponent
@UIScope
public class SalesAddNewInvoicesToBuyersView extends VerticalLayout {

    private final TextField invoiceBuyerField;
    private final DateTimePicker dateTimePickerField;
    private final Checkbox isSpend;
    private final ComboBox<CompanyDto> companySelectField;
    private final ComboBox<WarehouseDto> warehouseSelectField;
    private final ComboBox<ContractorDto> contractorSelectField;
    private final ComboBox<ContractDto> contractSelectField;
    private final DatePicker plannedDatePaymentField;
    private final Grid<InvoiceToBuyerListProductsDto> grid;
    private final TextField commentTextField;
    private final H4 totalPriceField;
    private final H4 ndsPriceField;

    private final CompanyService companyService;
    private final WarehouseService warehouseService;
    private final ContractService contractService;
    private final ContractorService contractorService;

    public SalesAddNewInvoicesToBuyersView(CompanyService companyService,
                                           WarehouseService warehouseService,
                                           ContractService contractService,
                                           ContractorService contractorService) {
        this.companyService = companyService;
        this.warehouseService = warehouseService;
        this.contractService = contractService;
        this.contractorService = contractorService;

        invoiceBuyerField = new TextField();
        configureInvoiceBuyerField();
        dateTimePickerField = new DateTimePicker();
        configureDateTimePickerField();
        isSpend = new Checkbox("Проведено");
        companySelectField = new ComboBox<>();
        configureCompanySelectField();
        warehouseSelectField = new ComboBox<>();
        configureWarehouseSelectField();
        contractorSelectField = new ComboBox<>();
        configureContractorSelectField();
        contractSelectField = new ComboBox<>();
        configureContractSelectField();
        plannedDatePaymentField = new DatePicker();
        grid = new Grid<>(InvoiceToBuyerListProductsDto.class, false);
        configureGrid();
        commentTextField = new TextField();
        configureCommentTextField();
        totalPriceField = new H4();
        configureTotalPriceField();
        ndsPriceField = new H4();
        configureNdsPriceField();


        add(upperMenu(),
                formLayout(),
                grid,
                horizontalLayoutComment());
    }



    //Заголовок
    private H2 title() {
        H2 title = new H2("Добавление счета");
        title.setHeight("2.0em");
        return title;
    }

    //Кнопка "Сохранить"
    private Button buttonSave() {
        Button button = new Button("Сохранить");
        button.addClickListener(buttonClickEvent -> plugButton().open());
        return button;
    }

    //Кнопка "Закрыть"
    private Button buttonClose() {
        Button button = new Button("Закрыть", new Icon(VaadinIcon.CLOSE));
        button.addClickListener(buttonClickEvent -> dialogCloseView().open());
        return button;
    }

    //Диалоговое окно закрытия
    //реализовать правильный выход
    private Dialog dialogCloseView() {
        Dialog dialog = new Dialog();
        dialog.setCloseOnEsc(false);
        dialog.setCloseOnOutsideClick(false);
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(new Text("Вы уверены? Несохраненные данные будут потеряны!!!"));

        HorizontalLayout horizontalLayout = new HorizontalLayout();

        Button confirmButton = new Button("Продолжить", buttonClickEvent -> {
            UI.getCurrent().navigate("sells");
            dialog.close();
        });
        horizontalLayout.add(confirmButton);

        Button closeButton = new Button("Отменить", buttonClickEvent -> {
            dialog.close();
        });
        horizontalLayout.add(closeButton);
        verticalLayout.add(horizontalLayout);
        verticalLayout.setDefaultHorizontalComponentAlignment(Alignment.END);
        dialog.add(verticalLayout);
        return dialog;
    }

    //Кнопка "Добавить из справочника"
    private Button buttonAddFromDirectory() {
        Button button = new Button("Добавить из справочника", new Icon(VaadinIcon.PLUS_CIRCLE));
        button.addClickListener(buttonClickEvent -> plugButton().open());
        return button;
    }

    //Верхнее меню (title, buttonSave, buttonClose, buttonAddFromDirectory)
    private HorizontalLayout upperMenu() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(title(), buttonSave(), buttonClose(), buttonAddFromDirectory());
        horizontalLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        return horizontalLayout;
    }

    //Поля формы (invoiceBuyerField, dateTimePickerField, isSpend)
    private HorizontalLayout horizontalLayoutInvoiceBuyerAndDateAndConducted() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label labelInvoiceBuyerField = new Label("Счет покупателю №");
        Label labelDateField = new Label("от");

        horizontalLayout.add(labelInvoiceBuyerField,
                invoiceBuyerField,
                labelDateField,
                dateTimePickerField,
                isSpend);
        return horizontalLayout;
    }

    //Поля формы (companySelectField, warehouseSelectField)
    private HorizontalLayout horizontalLayoutCompanyAndWarehouse() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label labelCompany = new Label("Компания");
        labelCompany.setWidth("5em");
        Label labelWarehouse = new Label("Склад");
        labelWarehouse.setWidth("5em");

        horizontalLayout.add(labelCompany,
                companySelectField,
                labelWarehouse,
                warehouseSelectField);
        return horizontalLayout;
    }

    //Поля формы (contractorSelectField, contractSelectField)
    private HorizontalLayout horizontalLayoutContractorAndContract() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label labelContractor = new Label("Контрагент   ");
        labelContractor.setWidth("5em");
        Label labelContract = new Label("Договор");
        labelContract.setWidth("5em");

        horizontalLayout.add(labelContractor,
                contractorSelectField,
                labelContract,
                contractSelectField);
        return horizontalLayout;
    }

    //Поля формы (plannedDatePaymentField)
    private HorizontalLayout horizontalLayoutPlannedDatePaymentField() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label labelPlannedDatePayment = new Label("План. дата оплаты");

        horizontalLayout.add(labelPlannedDatePayment,
                plannedDatePaymentField);
        return horizontalLayout;
    }

    private HorizontalLayout horizontalLayoutComment() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        H4 totalPriceTitle = new H4("Итого:");
        H4 ndsPriceTitle = new H4("НДС:");
        horizontalLayout.add(commentTextField, totalPriceTitle, totalPriceField, ndsPriceTitle, ndsPriceField);
        return horizontalLayout;
    }

    private VerticalLayout formLayout() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(horizontalLayoutInvoiceBuyerAndDateAndConducted(),
                horizontalLayoutCompanyAndWarehouse(),
                horizontalLayoutContractorAndContract(),
                horizontalLayoutPlannedDatePaymentField());
        return verticalLayout;
    }

    //заглушка на кнопки
    private Dialog plugButton() {
        Dialog dialog = new Dialog();
        dialog.setCloseOnEsc(false);
        dialog.setCloseOnOutsideClick(false);
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add("ОЙ! Функционал данной кнопки еще не реализован!");
        Button closeButton = new Button("OК", buttonClickEvent -> {
            dialog.close();
        });
        verticalLayout.add(closeButton);
        verticalLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        dialog.add(verticalLayout);
        return dialog;
    }

    private void configureInvoiceBuyerField() {
        invoiceBuyerField.setWidth("3em");
        invoiceBuyerField.setEnabled(false);
    }

    private void configureDateTimePickerField() {
        dateTimePickerField.setValue(LocalDateTime.now());
    }

    private void configureCompanySelectField() {
        companySelectField.setWidth("25em");
        List<CompanyDto> listCompany = companyService.getAll();
        if (listCompany != null) {
            companySelectField.setItems(listCompany);
            companySelectField.setItemLabelGenerator(CompanyDto::getName);
        }
    }

    private void configureWarehouseSelectField() {
        warehouseSelectField.setWidth("25em");
        List<WarehouseDto> listWarehouse = warehouseService.getAll();
        if (listWarehouse != null) {
            warehouseSelectField.setItems(listWarehouse);
            warehouseSelectField.setItemLabelGenerator(WarehouseDto::getName);
        }
    }

    private void configureContractorSelectField() {
        contractorSelectField.setWidth("25em");
        List<ContractorDto> listContractor = contractorService.getAll();
        if (listContractor != null) {
            contractorSelectField.setItems(listContractor);
            contractorSelectField.setItemLabelGenerator(ContractorDto::getName);
        }
    }

    private void configureContractSelectField() {
        contractSelectField.setWidth("25em");
        List<ContractDto> listContract = contractService.getAll();
        if (listContract != null) {
            contractSelectField.setItems(listContract);
            contractSelectField.setItemLabelGenerator(ContractDto::getNumber);
        }
    }

    private void configureGrid() {
        grid.addColumn(InvoiceToBuyerListProductsDto::getProductDto).setHeader("Наименование").setSortable(true).setKey("productName").setId("Наименование");
        grid.addColumn(InvoiceToBuyerListProductsDto::getAmount).setHeader("Количество").setSortable(true).setKey("productAmount").setId("Количество");
        grid.addColumn(InvoiceToBuyerListProductsDto::getPrice).setHeader("Цена").setSortable(true).setKey("productPrice").setId("Цена");
        grid.addColumn(InvoiceToBuyerListProductsDto::getSum).setHeader("Сумма").setSortable(true).setKey("productSum").setId("Сумма");
        grid.addColumn(InvoiceToBuyerListProductsDto::getPercentNds).setHeader("% НДС").setSortable(true).setKey("productPercentNds").setId("% НДС");
        grid.addColumn(InvoiceToBuyerListProductsDto::getNds).setHeader("НДС").setSortable(true).setKey("productNds").setId("НДС");
        grid.addColumn(InvoiceToBuyerListProductsDto::getTotal).setHeader("Всего").setSortable(true).setKey("productTotal").setId("Всего");
    }

    private void configureCommentTextField() {
        commentTextField.setPlaceholder("Комментарий");
        commentTextField.setWidth("35em");
    }

    private void configureTotalPriceField() {
        totalPriceField.setText("0.00");
    }

    private void configureNdsPriceField() {
        ndsPriceField.setText("0.00");
    }
}

