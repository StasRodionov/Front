package com.trade_accounting.components.purchases;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.models.dto.CompanyDto;
import com.trade_accounting.models.dto.ContractDto;
import com.trade_accounting.models.dto.ContractorDto;
import com.trade_accounting.models.dto.InvoiceDto;
import com.trade_accounting.models.dto.InvoiceProductDto;
import com.trade_accounting.models.dto.ProductDto;
import com.trade_accounting.models.dto.ProductPriceDto;
import com.trade_accounting.models.dto.SupplierAccountDto;
import com.trade_accounting.models.dto.SupplierAccountProductsListDto;
import com.trade_accounting.models.dto.WarehouseDto;
import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.ContractService;
import com.trade_accounting.services.interfaces.ContractorService;
import com.trade_accounting.services.interfaces.InvoiceProductService;
import com.trade_accounting.services.interfaces.InvoiceService;
import com.trade_accounting.services.interfaces.ProductService;
import com.trade_accounting.services.interfaces.SupplierAccountProductsListService;
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
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import retrofit2.Response;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Route(value = "purchases/add-new-invoices-to-suplier", layout = AppView.class)
@PageTitle("Новый счет поставщика")
@SpringComponent
@UIScope
public class SupplierAccountModalView extends Dialog {
    private final ProductService productService;
    private final SupplierAccountService supplierAccountService;
    private final CompanyService companyService;
    private final WarehouseService warehouseService;
    private final ContractorService contractorService;
    private final ContractService contractService;
    private final InvoiceService invoiceService;
    private final InvoiceProductService invoiceProductService;
    private final SupplierAccountProductsListService supplierAccountProductsListService;
    private SupplierAccountDto saveSupplier = new SupplierAccountDto();
    private final ComboBox<CompanyDto> companyDtoComboBox = new ComboBox<>();
    private final ComboBox<WarehouseDto> warehouseDtoComboBox = new ComboBox<>();
    private final ComboBox<ContractDto> contractDtoComboBox = new ComboBox<>();
    private final ComboBox<ContractorDto> contractorDtoComboBox = new ComboBox<>();
    private final DateTimePicker dateTimePicker = new DateTimePicker();
    private final DatePicker paymentDatePicker = new DatePicker();
    private final DatePicker dt1 = new DatePicker();
    private final TextField text = new TextField();
    private final Checkbox isSpend = new Checkbox("Проведено");
    private final TextField supplierNumber = new TextField();
    private final TextField commentConfig = new TextField();
    private final Notifications notifications;
    private final Binder<SupplierAccountDto> supplierAccountDtoBinder = new Binder<>(SupplierAccountDto.class);
    private final String TEXT_FOR_REQUEST_FIELD = "Обязательное поле";
    private final Dialog dialogOnCloseView = new Dialog();
    private Grid<SupplierAccountProductsListDto> grid = new Grid<>(SupplierAccountProductsListDto.class, false);
    private final GridPaginator<SupplierAccountProductsListDto> paginator;
    private final PurchasesChooseGoodsModalWin purchasesChooseGoodsModalWin;
    private List<SupplierAccountProductsListDto> tempSupplierAccountProductsListDtos = new ArrayList<>();
    private List<SupplierAccountProductsListDto> supplierAccountProductsListDtoList = new ArrayList<>();
    private final H4 totalPrice = new H4();
    private final ComboBox<InvoiceDto> invoiceSelectField = new ComboBox<>();

    public SupplierAccountModalView(SupplierAccountService supplierAccountService,
                                    CompanyService companyService,
                                    WarehouseService warehouseService,
                                    ContractorService contractorService,
                                    ContractService contractService,
                                    Notifications notifications,
                                    PurchasesChooseGoodsModalWin purchasesChooseGoodsModalWin,
                                    ProductService productService,
                                    InvoiceService invoiceService,
                                    InvoiceProductService invoiceProductService,
                                    SupplierAccountProductsListService supplierAccountProductsListService) {
        this.supplierAccountService = supplierAccountService;
        this.companyService = companyService;
        this.warehouseService = warehouseService;
        this.contractorService = contractorService;
        this.contractService = contractService;
        this.notifications = notifications;
        this.purchasesChooseGoodsModalWin = purchasesChooseGoodsModalWin;
        this.productService = productService;
        this.invoiceService = invoiceService;
        this.invoiceProductService = invoiceProductService;
        this.supplierAccountProductsListService = supplierAccountProductsListService;
        configureGrid();
        configureInvoiceSelectField();
        paginator = new GridPaginator<>(grid, tempSupplierAccountProductsListDtos, 50);
        configureCloseViewDialog();
        setSizeFull();
        configureDateTimePickerField();
        add(upperButtonInModalView(), formToAddSupplerAccount(), grid);
        purchasesChooseGoodsModalWin.addDetachListener(detachEvent -> {
            if (purchasesChooseGoodsModalWin.productSelect.getValue() != null
                    && purchasesChooseGoodsModalWin.priceSelect.getValue() != null
                    && purchasesChooseGoodsModalWin.amoSelect.getValue() != null) {

                addProduct(purchasesChooseGoodsModalWin.productSelect.getValue(),
                        purchasesChooseGoodsModalWin.priceSelect.getValue(),
                        purchasesChooseGoodsModalWin.amoSelect.getValue());
                purchasesChooseGoodsModalWin.productSelect.setValue(null);
                purchasesChooseGoodsModalWin.priceSelect.setValue(null);
                purchasesChooseGoodsModalWin.amoSelect.setValue(BigDecimal.valueOf(0.0));
            }
        });
    }

    //кнопка "заполнить по заказу"
    private Button buttonFillInOrder() {
        Button button = new Button("Заполнить по...");
        button.addClickListener(buttonClickEvent -> {
            dateTimePicker.setValue(LocalDateTime.parse(invoiceSelectField.getValue().getDate()));
            commentConfig.setValue(invoiceSelectField.getValue().getComment());
            companyDtoComboBox.setValue(companyService.getById(invoiceSelectField.getValue().getCompanyId()));
            contractorDtoComboBox.setValue(contractorService.getById(invoiceSelectField.getValue().getContractorId()));
            warehouseDtoComboBox.setValue(warehouseService.getById(invoiceSelectField.getValue().getWarehouseId()));
            isSpend.setValue(invoiceSelectField.getValue().getIsSpend());
            tempSupplierAccountProductsListDtos = convertInvoiceProdToSupplier(invoiceProductService.getByInvoiceId(invoiceSelectField.getValue().getId()));
            paginator.setData(tempSupplierAccountProductsListDtos);
            setTotalPrice();
        });
        return button;
    }

    //Выпадающий список с заказами
    private void configureInvoiceSelectField() {
        List<InvoiceDto> invoices = invoiceService.getAll("EXPENSE");
        if (invoices != null) {
            invoiceSelectField.setItems(invoices);
        }
        invoiceSelectField.setWidth("500px");
        invoiceSelectField.setPlaceholder("Заказ");
        invoiceSelectField.setItemLabelGenerator(e -> "Id: " + e.getId() +
                ", Контрагент: " + contractorService.getById(e.getContractorId()).getName() +
                ", Компания: " + companyService.getById(e.getCompanyId()).getName()  +
                ", на сумму: " + getTotalPriceForSelectFild(e));
    }

    public BigDecimal getTotalPriceForSelectFild(InvoiceDto invoiceDto) {
        BigDecimal totalPriceForSelectFild = BigDecimal.valueOf(0);
        for (InvoiceProductDto iDto : invoiceProductService.getByInvoiceId(invoiceDto.getId())) {
            totalPriceForSelectFild = totalPriceForSelectFild.add(iDto.getPrice().multiply(iDto.getAmount()));
        }
        return totalPriceForSelectFild;
    }

    public void addProduct(ProductDto productDto, ProductPriceDto productPriceDto, BigDecimal amount) {
        SupplierAccountProductsListDto supplierAccountProductsListDto = new SupplierAccountProductsListDto();
        supplierAccountProductsListDto.setProductId(productDto.getId());
        supplierAccountProductsListDto.setAmount(amount);
        supplierAccountProductsListDto.setPrice(productPriceDto.getValue());
        supplierAccountProductsListDto.setSum(supplierAccountProductsListDto.getAmount().multiply(supplierAccountProductsListDto.getPrice()).setScale(2, RoundingMode.DOWN));
        supplierAccountProductsListDto.setPercentNds("20");
        supplierAccountProductsListDto.setNds(supplierAccountProductsListDto.getSum().multiply(BigDecimal.valueOf(0.2)).setScale(2, RoundingMode.DOWN));
        supplierAccountProductsListDto.setTotal(supplierAccountProductsListDto.getSum().add(supplierAccountProductsListDto.getNds()));
        if (!isProductInList(productDto)) {
            tempSupplierAccountProductsListDtos.add(supplierAccountProductsListDto);
            paginator.setData(tempSupplierAccountProductsListDtos);
            setTotalPrice();
        }
    }

    private void configureGrid() {
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.removeAllColumns();
        grid.setItems(tempSupplierAccountProductsListDtos);
        grid.addColumn(inPrDto -> tempSupplierAccountProductsListDtos.indexOf(inPrDto) + 1).setHeader("№");
        grid.addColumn(inPrDto -> productService.getById(inPrDto.getProductId()).getName()).setHeader("Название").setSortable(true);
        grid.addColumn(inPrDto -> productService.getById(inPrDto.getProductId()).getDescription()).setHeader("Описание");
        grid.addColumn(SupplierAccountProductsListDto::getAmount).setHeader("Количество").setSortable(true).setKey("productAmount").setId("Количество");
        grid.addColumn(SupplierAccountProductsListDto::getPrice).setHeader("Цена").setSortable(true).setKey("productPrice").setId("Цена");
        grid.addColumn(SupplierAccountProductsListDto::getSum).setHeader("Сумма").setSortable(true).setKey("productSum").setId("Сумма");
        grid.addColumn(SupplierAccountProductsListDto::getPercentNds).setHeader("% НДС").setSortable(true).setKey("productPercentNds").setId("% НДС");
        grid.addColumn(SupplierAccountProductsListDto::getNds).setHeader("НДС").setSortable(true).setKey("productNds").setId("НДС");
        grid.addColumn(SupplierAccountProductsListDto::getTotal).setHeader("Всего").setSortable(true).setKey("productTotal").setId("Всего");

        grid.addComponentColumn(column -> {
            Button edit = new Button(new Icon(VaadinIcon.TRASH));
            edit.addClassName("delete");
            edit.addClickListener(e -> deleteProduct(column.getId()));
            return edit;
        });
    }

    public void clearField() {
        companyDtoComboBox.setValue(null);
        warehouseDtoComboBox.setValue(null);
        contractDtoComboBox.setValue(null);
        contractorDtoComboBox.setValue(null);
        invoiceSelectField.setValue(null);
        configureInvoiceSelectField();
        dateTimePicker.setValue(null);
        paymentDatePicker.setValue(null);
        dt1.setValue(null);
        text.setValue("");
        isSpend.setValue(false);
        supplierNumber.setValue("");
        commentConfig.setValue("");
        totalPrice.setText("");
        tempSupplierAccountProductsListDtos = new ArrayList<>();
        configureGrid();
    }

    public void setSupplierAccountsForEdit(SupplierAccountDto editSupplierAccounts) {
        this.saveSupplier = editSupplierAccounts;
        supplierNumber.setValue(saveSupplier.getId().toString());
        dateTimePicker.setValue(LocalDateTime.parse(saveSupplier.getDate()));
        paymentDatePicker.setValue(LocalDate.parse(saveSupplier.getPlannedDatePayment()));
        commentConfig.setValue(saveSupplier.getComment());
        companyDtoComboBox.setValue(companyService.getById(saveSupplier.getCompanyId()));
        warehouseDtoComboBox.setValue(warehouseService.getById(saveSupplier.getWarehouseId()));
        contractDtoComboBox.setValue(contractService.getById(saveSupplier.getContractId()));
        contractorDtoComboBox.setValue(contractorService.getById(saveSupplier.getContractorId()));
        isSpend.setValue(saveSupplier.getIsSpend());
        tempSupplierAccountProductsListDtos = supplierAccountProductsListService.getBySupplierId(saveSupplier.getId());
        supplierAccountProductsListDtoList = supplierAccountProductsListService.getBySupplierId(saveSupplier.getId());
        paginator.setData(tempSupplierAccountProductsListDtos);
        setTotalPrice();
    }

    private HorizontalLayout upperButtonInModalView() {
        HorizontalLayout upperButton = new HorizontalLayout();
        upperButton.add(title(), saveButton(), closeButton(), addProductButton(), invoiceSelectField, buttonFillInOrder());
        upperButton.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        return upperButton;
    }

    private H2 title() {
        final H2 label = new H2("Добавление счета");
        label.setHeight("2.2em");
        return label;
    }

    private Button saveButton() {
        return new Button("Сохранить", e -> {
            if (!supplierAccountDtoBinder.validate().isOk()) {
                supplierAccountDtoBinder.validate().notifyBindingValidationStatusHandlers();
            } else {
                SupplierAccountDto supplierAccountDto = updateSupplier();
                deleteRemovedProducts();
                saveProducts(supplierAccountDto);
                clearField();
                close();
                UI.getCurrent().navigate("purchases");
                notifications.infoNotification(String.format("Счет поставщика № %s сохранен", supplierAccountDto.getId()));
            }
        });
    }

    private SupplierAccountDto updateSupplier() {
        SupplierAccountDto supplierAccountDto = new SupplierAccountDto();
        if (!supplierNumber.getValue().equals("")) {
            supplierAccountDto.setId(Long.parseLong(supplierNumber.getValue()));
        }
        supplierAccountDto.setDate(dateTimePicker.getValue().toString());
        supplierAccountDto.setPlannedDatePayment(paymentDatePicker.getValue().toString());
        supplierAccountDto.setCompanyId(companyDtoComboBox.getValue().getId());
        supplierAccountDto.setWarehouseId(warehouseDtoComboBox.getValue().getId());
        supplierAccountDto.setContractId(contractDtoComboBox.getValue().getId());
        supplierAccountDto.setTypeOfInvoice("EXPENSE");
        supplierAccountDto.setContractorId(contractorDtoComboBox.getValue().getId());
        supplierAccountDto.setIsSpend(isSpend.getValue());
        supplierAccountDto.setIsRecyclebin(false);
        supplierAccountDto.setComment(commentConfig.getValue());
        Response<SupplierAccountDto> supplierAccountDtoResponse = supplierAccountService.create(supplierAccountDto);
        return supplierAccountDtoResponse.body();
    }

    private Button closeButton() {
        Button button = new Button("Закрыть", new Icon(VaadinIcon.CLOSE));
        button.addClickListener(buttonClickEvent -> dialogOnCloseView.open());
        return button;
    }

    private Button addProductButton() {
        Button button = new Button("Добавить из справочника", new Icon(VaadinIcon.PLUS_CIRCLE));
        button.addClickListener(buttonClickEvent -> {
            purchasesChooseGoodsModalWin.updateProductList();
            purchasesChooseGoodsModalWin.open();
        });
        return button;
    }

    private VerticalLayout formToAddSupplerAccount() {
        VerticalLayout form = new VerticalLayout();
        form.add(horizontalLayout1(), horizontalLayout2(),
                horizontalLayout3(), dataPlaneConfigure(), incomingConfigure(), commentConfigure(), totalPrice());
        return form;
    }

    private HorizontalLayout horizontalLayout1() {
        HorizontalLayout hLay1 = new HorizontalLayout();
        hLay1.add(dataConfigure(), isSpend);
        return hLay1;
    }

    private HorizontalLayout horizontalLayout2() {
        HorizontalLayout hLay2 = new HorizontalLayout();
        hLay2.add(companyConfigure(), warehouseConfigure());
        return hLay2;
    }

    private HorizontalLayout horizontalLayout3() {
        HorizontalLayout hLay3 = new HorizontalLayout();
        hLay3.add(contractorConfigure(), contractConfigure());
        return hLay3;
    }

    private HorizontalLayout dataConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Счет поставщика №");
        label.setWidth("150px");

//        supplierNumber.setAutofocus(true);
        supplierNumber.setWidth("50px");
        supplierNumber.setEnabled(false);
//        supplierNumber.setRequired(true);
//        supplierNumber.setRequiredIndicatorVisible(true);
//        supplierAccountDtoBinder.forField(supplierNumber)
//                .asRequired(TEXT_FOR_REQUEST_FIELD)
//                .bind(SupplierAccountDto::getIdValid, SupplierAccountDto::setIdValid);
        Label label2 = new Label("от");
        dateTimePicker.setWidth("350px");
        dateTimePicker.setRequiredIndicatorVisible(true);
        supplierAccountDtoBinder.forField(dateTimePicker)
                .asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(SupplierAccountDto::getDateValid, SupplierAccountDto::setDateValid);
        horizontalLayout.add(label, supplierNumber, label2, dateTimePicker);
        return horizontalLayout;
    }

    private HorizontalLayout contractorConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        List<ContractorDto> contractorDtos = contractorService.getAll();
        if (contractorDtos != null) {
            contractorDtoComboBox.setItems(contractorDtos);
        }
        contractorDtoComboBox.setItemLabelGenerator(ContractorDto::getName);
        contractorDtoComboBox.setWidth("350px");
        contractorDtoComboBox.setRequired(true);
        contractorDtoComboBox.setRequiredIndicatorVisible(true);
        supplierAccountDtoBinder.forField(contractorDtoComboBox)
                .asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(SupplierAccountDto::getContractorDtoValid, SupplierAccountDto::setContractorDtoValid);
        Label label = new Label("Контрагент");
        label.setWidth("100px");
        horizontalLayout.add(label, contractorDtoComboBox);
        return horizontalLayout;
    }

    private HorizontalLayout companyConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        List<CompanyDto> companyDtos = companyService.getAll();
        if (companyDtos != null) {
            companyDtoComboBox.setItems(companyDtos);
        }
        companyDtoComboBox.setItemLabelGenerator(CompanyDto::getName);
        companyDtoComboBox.setWidth("350px");
        companyDtoComboBox.setRequired(true);
        companyDtoComboBox.setRequiredIndicatorVisible(true);
        supplierAccountDtoBinder.forField(companyDtoComboBox)
                .asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(SupplierAccountDto::getCompanyDtoValid, SupplierAccountDto::setCompanyDtoValid);
        Label label = new Label("Компания");
        label.setWidth("100px");
        horizontalLayout.add(label, companyDtoComboBox);
        return horizontalLayout;
    }

    private HorizontalLayout warehouseConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        List<WarehouseDto> warehouseDtos = warehouseService.getAll();
        if (warehouseDtos != null) {
            warehouseDtoComboBox.setItems(warehouseDtos);
        }
        warehouseDtoComboBox.setItemLabelGenerator(WarehouseDto::getName);
        warehouseDtoComboBox.setWidth("350px");
        warehouseDtoComboBox.setRequired(true);
        warehouseDtoComboBox.setRequiredIndicatorVisible(true);
        supplierAccountDtoBinder.forField(warehouseDtoComboBox)
                .asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(SupplierAccountDto::getWarehouseDtoValid, SupplierAccountDto::setWarehouseDtoValid);
        Label label = new Label("Склад");
        label.setWidth("100px");
        horizontalLayout.add(label, warehouseDtoComboBox);
        return horizontalLayout;
    }

    private HorizontalLayout contractConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        List<ContractDto> contractDtos = contractService.getAll();
        if (contractDtos != null) {
            contractDtoComboBox.setItems(contractDtos);
        }
        contractDtoComboBox.setItemLabelGenerator(ContractDto::getNumber);
        contractDtoComboBox.setWidth("350px");
        contractDtoComboBox.setRequired(true);
        contractDtoComboBox.setRequiredIndicatorVisible(true);
        supplierAccountDtoBinder.forField(contractDtoComboBox)
                .asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(SupplierAccountDto::getContractDtoValid, SupplierAccountDto::setContractDtoValid);
        Label label = new Label("Договор");
        label.setWidth("100px");
        horizontalLayout.add(label, contractDtoComboBox);
        return horizontalLayout;
    }

    private H4 totalPrice() {
        totalPrice.setText(getTotalPrice().toString());
        totalPrice.setHeight("2.0em");
        return totalPrice;
    }

    private HorizontalLayout dataPlaneConfigure() {
        HorizontalLayout horizontal = new HorizontalLayout();

        Label label = new Label("План. дата оплаты");
        label.setWidth("150px");
        paymentDatePicker.setWidth("150px");
        supplierAccountDtoBinder.forField(paymentDatePicker)
                .asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(SupplierAccountDto::getPaymentDateValid, SupplierAccountDto::setPaymentDateValid);
        horizontal.add(label, paymentDatePicker);
        return horizontal;
    }

    private HorizontalLayout incomingConfigure() {
        HorizontalLayout horizontal1 = new HorizontalLayout();

        Label label = new Label("Входящий номер");
        label.setWidth("150px");

        text.setWidth("70px");
        Label label2 = new Label("от");
        dt1.setWidth("150px");
        horizontal1.add(label, text, label2, dt1);
        return horizontal1;
    }

    private HorizontalLayout commentConfigure() {
        HorizontalLayout horizontal3 = new HorizontalLayout();
        commentConfig.setPlaceholder("Комментарий");
        horizontal3.add(commentConfig);
        return horizontal3;
    }

    private void configureCloseViewDialog() {
        dialogOnCloseView.add(new Text("Вы уверены? Несохраненные данные будут потеряны!!!"));
        dialogOnCloseView.setCloseOnEsc(false);
        dialogOnCloseView.setCloseOnOutsideClick(false);
        Button confirmButton = new Button("Продолжить", event -> {
            closeView();
            dialogOnCloseView.close();
            close();
        });
        Button cancelButton = new Button("Отменить", event -> dialogOnCloseView.close());
        Shortcuts.addShortcutListener(dialogOnCloseView, dialogOnCloseView::close, Key.ESCAPE);
        dialogOnCloseView.add(new Div(confirmButton, new Div(), cancelButton));
    }

    private boolean isProductInList(ProductDto productDto) {
        boolean isExists = false;
        for (SupplierAccountProductsListDto supplierAccountProductsListDto : tempSupplierAccountProductsListDtos) {
            if (supplierAccountProductsListDto.getProductId().equals(productDto.getId())) {
                isExists = true;
                break;
            }
        }
        return isExists;
    }

    public BigDecimal getTotalPrice() {
        BigDecimal totalPrice = BigDecimal.valueOf(0.0);
        for (SupplierAccountProductsListDto supplierAccountProductsListDto : tempSupplierAccountProductsListDtos) {
            totalPrice = totalPrice.add(supplierAccountProductsListDto.getTotal());
        }
        return totalPrice;
    }

    private void setTotalPrice() {
        totalPrice.setText(
                String.format("%.2f", getTotalPrice())
        );
    }

    private void closeView() {
        clearField();
        UI.getCurrent().navigate("purchases");
    }

    private void configureDateTimePickerField() {
        dateTimePicker.setValue(LocalDateTime.now());
    }

    private void saveProducts(SupplierAccountDto supplierAccountDto) {
        for (SupplierAccountProductsListDto supplierAccountProductsListDto: tempSupplierAccountProductsListDtos) {
            supplierAccountProductsListDto.setSupplierAccountId(supplierAccountDto.getId());
            supplierAccountProductsListService.create(supplierAccountProductsListDto);
        }
    }

    private void deleteProduct(Long id) {
        SupplierAccountProductsListDto found = new SupplierAccountProductsListDto();
        for (SupplierAccountProductsListDto supplierAccountProductsListDto : tempSupplierAccountProductsListDtos) {
            if (supplierAccountProductsListDto.getId() == id) {
                found = supplierAccountProductsListDto;
                break;
            }
        }
        tempSupplierAccountProductsListDtos.remove(found);
        paginator.setData(tempSupplierAccountProductsListDtos);
        setTotalPrice();
    }

    private List<SupplierAccountProductsListDto> convertInvoiceProdToSupplier(List<InvoiceProductDto> invoiceProductDto) {
        List<SupplierAccountProductsListDto> supplierAccountProductsListDtos = new ArrayList<>();
        for (InvoiceProductDto iPDto: invoiceProductDto) {
            SupplierAccountProductsListDto supplierAccountProductsListDto = new SupplierAccountProductsListDto();
            supplierAccountProductsListDto.setProductId(iPDto.getProductId());
            supplierAccountProductsListDto.setAmount(iPDto.getAmount());
            supplierAccountProductsListDto.setPrice(iPDto.getPrice());
            supplierAccountProductsListDto.setSum(supplierAccountProductsListDto.getAmount().multiply(supplierAccountProductsListDto.getPrice()).setScale(2, RoundingMode.DOWN));
            supplierAccountProductsListDto.setPercentNds("20");
            supplierAccountProductsListDto.setNds(supplierAccountProductsListDto.getSum().multiply(BigDecimal.valueOf(0.2)).setScale(2, RoundingMode.DOWN));
            supplierAccountProductsListDto.setTotal(supplierAccountProductsListDto.getSum().add(supplierAccountProductsListDto.getNds()));
            supplierAccountProductsListDtos.add(supplierAccountProductsListDto);
        }
        return supplierAccountProductsListDtos;
    }
    private void deleteRemovedProducts() {
        for (SupplierAccountProductsListDto removed : supplierAccountProductsListDtoList) {
            if(!tempSupplierAccountProductsListDtos.contains(removed)) {
                supplierAccountProductsListService.deleteById(removed.getId());
            }
        }
    }
}
