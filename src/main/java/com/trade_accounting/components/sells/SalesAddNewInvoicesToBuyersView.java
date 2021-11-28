package com.trade_accounting.components.sells;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.models.dto.CompanyDto;
import com.trade_accounting.models.dto.ContractDto;
import com.trade_accounting.models.dto.ContractorDto;
import com.trade_accounting.models.dto.InvoiceToBuyerListProductsDto;
import com.trade_accounting.models.dto.SupplierAccountDto;
import com.trade_accounting.models.dto.WarehouseDto;
import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.ContractService;
import com.trade_accounting.services.interfaces.ContractorService;
import com.trade_accounting.services.interfaces.SupplierAccountService;
import com.trade_accounting.services.interfaces.WarehouseService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Shortcuts;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import retrofit2.Response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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
    private final ComboBox<CompanyDto> companySelectField = new ComboBox<>();
    private final ComboBox<WarehouseDto> warehouseSelectField = new ComboBox<>();
    private final ComboBox<ContractorDto> contractorSelectField = new ComboBox<>();
    private final ComboBox<ContractDto> contractSelectField = new ComboBox<>();
    private final DatePicker plannedDatePaymentField;
    private final Grid<InvoiceToBuyerListProductsDto> grid;
    private final TextField commentTextField;
    private final H4 totalPriceField;
    private final H4 ndsPriceField;
    private final H2 title = new H2("Добавление счета");
    private static final String LABEL_WIDTH = "100px";
    private static final String FIELD_WIDTH = "350px";
    private String typeOfInvoice = "RECEIPT";
    private final Dialog dialogOnCloseView = new Dialog();

    private final SalesChooseGoodsModalWin salesChooseGoodsModalWin;

    private final CompanyService companyService;
    private final WarehouseService warehouseService;
    private final ContractService contractService;
    private final ContractorService contractorService;
    private final Notifications notifications;
    private final SupplierAccountService supplierAccountService;

    private final Binder<SupplierAccountDto> supplierAccountDtoBinder = new Binder<>(SupplierAccountDto.class);
    private final Button buttonDelete = new Button("Удалить", new Icon(VaadinIcon.TRASH));

    private String location = null;

    @Autowired
    public SalesAddNewInvoicesToBuyersView(CompanyService companyService,
                                           WarehouseService warehouseService,
                                           ContractService contractService,
                                           ContractorService contractorService,
                                           SupplierAccountService supplierAccountService,
                                           SalesChooseGoodsModalWin salesChooseGoodsModalWin,
                                           @Lazy Notifications notifications) {
        this.companyService = companyService;
        this.warehouseService = warehouseService;
        this.contractService = contractService;
        this.contractorService = contractorService;
        this.supplierAccountService = supplierAccountService;
        this.notifications = notifications;
        this.salesChooseGoodsModalWin = salesChooseGoodsModalWin;

        invoiceBuyerField = new TextField();
        configureInvoiceBuyerField();
        dateTimePickerField = new DateTimePicker();
        configureDateTimePickerField();
        isSpend = new Checkbox("Проведено");

        configureCompanySelectField();
        configureWarehouseSelectField();
        configureContractorSelectField();
        configureContractSelectField();
        configureCloseViewDialog();

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
                horizontalLayoutCommentAndTotalPrice());
    }

    //Кнопка "Сохранить"
    private Button buttonSave() {
        Button button = new Button("Сохранить");
        button.addClickListener(buttonClickEvent -> {

            if (!supplierAccountDtoBinder.validate().isOk()) {
                supplierAccountDtoBinder.validate().notifyBindingValidationStatusHandlers();
            } else {

                if (dateTimePickerField.getValue() == null) {
                    dateTimePickerField.setValue(LocalDateTime.now());
                }
                SupplierAccountDto supplierAccountDto = saveInvoice(typeOfInvoice);

                /*deleteAllInvoiceProductByInvoice(
                        getListOfInvoiceProductByInvoice(invoiceDto)
                );

                addInvoiceProductToInvoicedDto(invoiceDto);*/ //добавление продуктов в счет
                UI.getCurrent().navigate(location);
                notifications.infoNotification(String.format("Счет № %s сохранен", supplierAccountDto.getId()));
            }
        });
        return button;
    }

    //Кнопка "Закрыть"
    private Button buttonClose() {
        Button button = new Button("Закрыть", new Icon(VaadinIcon.CLOSE));
        button.addClickListener(buttonClickEvent -> dialogOnCloseView.open());
        return button;
    }

    //Кнопка "Удалить"
    private Button configureDeleteButton() {
        buttonDelete.addClickListener(event -> {
            deleteInvoiceById(Long.parseLong(invoiceBuyerField.getValue()));
            resetView();
            buttonDelete.getUI().ifPresent(ui -> ui.navigate(location));
        });
        return buttonDelete;
    }

    //Диалоговое окно закрытия
        private void configureCloseViewDialog() {
        dialogOnCloseView.add(new Text("Вы уверены? Несохраненные данные будут потеряны!!!"));
        dialogOnCloseView.setCloseOnEsc(false);
        dialogOnCloseView.setCloseOnOutsideClick(false);
        Span message = new Span();

        Button confirmButton = new Button("Продолжить", event -> {
            closeView();
            dialogOnCloseView.close();
        });
        Button cancelButton = new Button("Отменить", event -> {
            dialogOnCloseView.close();
        });
// Cancel action on ESC press
        Shortcuts.addShortcutListener(dialogOnCloseView, () -> {
            dialogOnCloseView.close();
        }, Key.ESCAPE);

        dialogOnCloseView.add(new Div(confirmButton, new Div(), cancelButton));
    }

    private void closeView() {
        resetView();
        UI.getCurrent().navigate(location);
    }

    //Кнопка "Добавить из справочника"
    private Button buttonAddFromDirectory() {
        Button button = new Button("Добавить из справочника", new Icon(VaadinIcon.PLUS_CIRCLE));
        button.addClickListener(buttonClickEvent -> {

            salesChooseGoodsModalWin.updateProductList();
            salesChooseGoodsModalWin.open();
        });
        return button;
    }

    //Верхнее меню (buttonSave, buttonClose, buttonAddFromDirectory)
    private HorizontalLayout upperMenu() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();

        title.setHeight("2.0em");
        horizontalLayout.add(title, buttonSave(), configureDeleteButton(), buttonClose(), buttonAddFromDirectory());
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

    //Поля формы (commentTextField, totalPriceField, ndsPriceField)
    private HorizontalLayout horizontalLayoutCommentAndTotalPrice() {
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

    private void configureInvoiceBuyerField() {
        invoiceBuyerField.setWidth("3em");
        invoiceBuyerField.setEnabled(false);
    }

    private void configureDateTimePickerField() {
        dateTimePickerField.setValue(LocalDateTime.now());
    }

    private void configureCompanySelectField() {
        List<CompanyDto> companies = companyService.getAll();
        if (companies != null) {
            companySelectField.setItems(companies);
        }
        companySelectField.setItemLabelGenerator(CompanyDto::getName);
        companySelectField.setWidth(FIELD_WIDTH);
        supplierAccountDtoBinder.forField(companySelectField)
                .withValidator(Objects::nonNull, "Не заполнено!")
                .bind("companyDto");
    }

    private void configureWarehouseSelectField() {
        List<WarehouseDto> listWarehouse = warehouseService.getAll();
        if (listWarehouse != null) {
            warehouseSelectField.setItems(listWarehouse);
        }
        warehouseSelectField.setItemLabelGenerator(WarehouseDto::getName);
        warehouseSelectField.setWidth(FIELD_WIDTH);
        supplierAccountDtoBinder.forField(warehouseSelectField)
                .withValidator(Objects::nonNull, "Не заполнено!")
                .bind("warehouseDto");
    }

    private void configureContractorSelectField() {
        List<ContractorDto> listContractor = contractorService.getAll();
        if (listContractor != null) {
            contractorSelectField.setItems(listContractor);
        }
        contractorSelectField.setItemLabelGenerator(ContractorDto::getName);
        contractorSelectField.setWidth(FIELD_WIDTH);
        supplierAccountDtoBinder.forField(contractorSelectField)
                .withValidator(Objects::nonNull, "Не заполнено!")
                .bind("contractorDto");
    }

    private void configureContractSelectField() {
        List<ContractDto> listContract = contractService.getAll();
        if (listContract != null) {
            contractSelectField.setItems(listContract);
        }
        contractSelectField.setItemLabelGenerator(ContractDto::getNumber);
        contractSelectField.setWidth(FIELD_WIDTH);
        supplierAccountDtoBinder.forField(contractSelectField)
                .withValidator(Objects::nonNull, "Не заполнено!")
                .bind("contractDto");
    }

    private void configureGrid() {
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
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

    public void setUpdateState(boolean isUpdate) {
        title.setText(isUpdate ? "Редактирование счета" : "Добавление счета");
        buttonDelete.setVisible(isUpdate);

    }

    public void resetView() {
        dateTimePickerField.clear();
        companySelectField.setValue(null);
        contractorSelectField.setValue(null);
        warehouseSelectField.setValue(null);
        contractSelectField.setValue(null);
        isSpend.clear();
        contractorSelectField.setInvalid(false);
        companySelectField.setInvalid(false);
        warehouseSelectField.setInvalid(false);
        title.setText("Добавление счета");

    }

    public void setSupplierDataForEdit(SupplierAccountDto supplierAccountDto) {

        invoiceBuyerField.setValue(supplierAccountDto.getId().toString());
        dateTimePickerField.setValue(LocalDateTime.parse(supplierAccountDto.getDate()));
        companySelectField.setValue(companyService.getById(supplierAccountDto.getCompanyId()));
        contractorSelectField.setValue(contractorService.getById(supplierAccountDto.getContractorId()));
        isSpend.setValue(supplierAccountDto.getIsSpend());
        warehouseSelectField.setValue(warehouseService.getById(supplierAccountDto.getWarehouseId()));
        contractSelectField.setValue(contractService.getById(supplierAccountDto.getContractId()));
        plannedDatePaymentField.setValue(LocalDate.parse(supplierAccountDto.getPlannedDatePayment()));

    }

    private SupplierAccountDto saveInvoice(String typeOfInvoice) {
        SupplierAccountDto supplierAccountDto = new SupplierAccountDto();
        if (!invoiceBuyerField.getValue().equals("")) {
            supplierAccountDto.setId(Long.parseLong(invoiceBuyerField.getValue()));
        }
        supplierAccountDto.setDate(dateTimePickerField.getValue().toString());
        supplierAccountDto.setCompanyId(companySelectField.getValue().getId());
        supplierAccountDto.setContractorId(contractorSelectField.getValue().getId());
        supplierAccountDto.setWarehouseId(warehouseSelectField.getValue().getId());
        supplierAccountDto.setContractId(contractSelectField.getValue().getId());
        supplierAccountDto.setTypeOfInvoice(typeOfInvoice);
        supplierAccountDto.setIsSpend(isSpend.getValue());
        supplierAccountDto.setComment("");
        supplierAccountDto.setPlannedDatePayment(plannedDatePaymentField.getValue().toString());
        Response<SupplierAccountDto> supplierAccountDtoResponse = supplierAccountService.create(supplierAccountDto);
        return supplierAccountDtoResponse.body();
    }

    public void deleteInvoiceById(Long invoiceDtoId) {
        supplierAccountService.deleteById(invoiceDtoId);
        notifications.infoNotification(String.format("Заказ № %s успешно удален", invoiceDtoId));
    }

    public void setLocation(String location) {
        this.location = location;
    }
}

