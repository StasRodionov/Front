package com.trade_accounting.components.sells;


import com.trade_accounting.components.general.ProductSelectModal;
import com.trade_accounting.components.profile.SalesChannelModalWindow;
import com.trade_accounting.components.purchases.PurchasesSubMenuView;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.models.dto.company.CompanyDto;
import com.trade_accounting.models.dto.company.ContractorDto;
import com.trade_accounting.models.dto.invoice.InvoiceDto;
import com.trade_accounting.models.dto.invoice.InvoiceProductDto;
import com.trade_accounting.models.dto.invoice.InvoicesStatusDto;
import com.trade_accounting.models.dto.units.SalesChannelDto;
import com.trade_accounting.models.dto.util.ProjectDto;
import com.trade_accounting.models.dto.warehouse.ProductPriceDto;
import com.trade_accounting.models.dto.warehouse.WarehouseDto;
import com.trade_accounting.services.interfaces.company.CompanyService;
import com.trade_accounting.services.interfaces.company.ContractorService;
import com.trade_accounting.services.interfaces.invoice.InvoiceProductService;
import com.trade_accounting.services.interfaces.invoice.InvoiceService;
import com.trade_accounting.services.interfaces.invoice.InvoicesStatusService;
import com.trade_accounting.services.interfaces.units.SalesChannelService;
import com.trade_accounting.services.interfaces.util.ProjectService;
import com.trade_accounting.services.interfaces.warehouse.ProductPriceService;
import com.trade_accounting.services.interfaces.warehouse.ProductService;
import com.trade_accounting.services.interfaces.company.TypeOfPriceService;
import com.trade_accounting.services.interfaces.units.UnitService;
import com.trade_accounting.services.interfaces.warehouse.WarehouseService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Shortcuts;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.editor.Editor;
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
import com.vaadin.flow.data.converter.StringToBigDecimalConverter;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.BeforeLeaveObserver;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import retrofit2.Response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.WeakHashMap;
import java.util.stream.Collectors;

import static com.trade_accounting.config.SecurityConstants.*;

@Slf4j
@PreserveOnRefresh
@SpringComponent
@UIScope
public class SalesEditCreateInvoiceView extends VerticalLayout implements BeforeLeaveObserver {

    private final PurchasesSubMenuView purchasesSubMenuView;
    private final SalesSubMenuView salesSubMenuView;

    private final ProductService productService;
    private final ContractorService contractorService;
    private final CompanyService companyService;
    private final WarehouseService warehouseService;
    private final SalesChannelService salesChannelService;
    private final InvoiceService invoiceService;
    private final InvoicesStatusService invoicesStatusService;
    private final InvoiceProductService invoiceProductService;
    private final Notifications notifications;
    private final TypeOfPriceService typeOfPriceService;
    private final UnitService unitService;
    private final ProductPriceService productPriceService;
    private final ProjectService projectService;

    private static final String LABEL_WIDTH = "100px";
    private static final String FIELD_WIDTH = "350px";
    private static final String SALES_CHANNEL_FIELD_WIDTH = "300px";
    private final TextField invoiceIdField = new TextField();
    private final DateTimePicker dateField = new DateTimePicker();
    private final TextField typeOfInvoiceField = new TextField();
    private final Checkbox isSpend = new Checkbox("Проведено");
    private final Checkbox isSent = new Checkbox("Отправлено");
    private final Checkbox isPrint = new Checkbox("Напечатано");
    private final TextField commentField = new TextField();
    private final ComboBox<CompanyDto> companySelect = new ComboBox<>();
    private final ComboBox<ContractorDto> contractorSelect = new ComboBox<>();
    private final ComboBox<WarehouseDto> warehouseSelect = new ComboBox<>();
    private final ComboBox<SalesChannelDto> salesChannelSelect = new ComboBox<>();
    private final ComboBox<InvoicesStatusDto> invoicesStatusSelect = new ComboBox<>();
    private final ComboBox<ProjectDto> projectSelect = new ComboBox<>();

    private final TextField amountField = new TextField();

    private final Button buttonDelete = new Button("Удалить", new Icon(VaadinIcon.TRASH));

    private final H4 totalPrice = new H4();
    private final H2 title = new H2("Добавление заказа");

    private List<InvoiceProductDto> tempInvoiceProductDtoList = new ArrayList<>();

    private final Dialog dialogOnChangeContractor = new Dialog();
    private final Dialog dialogOnCloseView = new Dialog();

    private final Grid<InvoiceProductDto> grid = new Grid<>(InvoiceProductDto.class, false);
    private final GridPaginator<InvoiceProductDto> paginator;
    private final ProductSelectModal productSelectModal;

    private final Editor<InvoiceProductDto> editor = grid.getEditor();
    private final Binder<InvoiceProductDto> binderInvoiceProductDto = new Binder<>(InvoiceProductDto.class);
    private final Binder<InvoiceDto> binderInvoiceDto = new Binder<>(InvoiceDto.class);
    private final Binder<InvoiceDto> binderInvoiceDtoContractorValueChangeListener = new Binder<>(InvoiceDto.class);
    private String type = null;
    private String location = null;

    @Autowired
    public SalesEditCreateInvoiceView(ProductService productService, ContractorService contractorService,
                                      CompanyService companyService,
                                      WarehouseService warehouseService,
                                      SalesChannelService salesChannelService,
                                      InvoiceService invoiceService,
                                      InvoicesStatusService invoicesStatusService, InvoiceProductService invoiceProductService,
                                      Notifications notifications,
                                      ProductSelectModal productSelectModal,
                                      TypeOfPriceService typeOfPriceService, ProjectService projectService,
                                      UnitService unitService, ProductPriceService productPriceService,
                                      @Lazy PurchasesSubMenuView purchasesSubMenuView,
                                      @Lazy SalesSubMenuView salesSubMenuView) {
        this.productService = productService;
        this.contractorService = contractorService;
        this.companyService = companyService;
        this.warehouseService = warehouseService;
        this.salesChannelService = salesChannelService;
        this.invoiceService = invoiceService;
        this.invoicesStatusService = invoicesStatusService;
        this.invoiceProductService = invoiceProductService;
        this.typeOfPriceService = typeOfPriceService;
        this.unitService = unitService;
        this.productPriceService = productPriceService;
        this.projectService = projectService;
        this.notifications = notifications;
        this.productSelectModal = productSelectModal;
        this.purchasesSubMenuView = purchasesSubMenuView;
        this.salesSubMenuView = salesSubMenuView;

        configureRecalculateDialog();
        configureCloseViewDialog();

        productSelectModal.addDetachListener(detachEvent -> {
            if (productSelectModal.isFormValid()) {
                addProduct(productSelectModal.getInvoiceProductDto());
            }
            productSelectModal.clearForm();
        });

        binderInvoiceDtoContractorValueChangeListener.forField(contractorSelect)
                .withValidator(Objects::nonNull, "Не заполнено!")
                .bind("contractorDto");
        binderInvoiceDtoContractorValueChangeListener.addValueChangeListener(valueChangeEvent -> {
            if (
                    valueChangeEvent.isFromClient()
                            && valueChangeEvent.getOldValue() != null
                            && !tempInvoiceProductDtoList.isEmpty()
            ) {
                dialogOnChangeContractor.open();
            }
        });


        configureGrid();
        paginator = new GridPaginator<>(grid, tempInvoiceProductDtoList, 50);
        setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, paginator);

        add(upperButtonsLayout(), formLayout(), grid, paginator);
    }

    private void configureCloseViewDialog() {
        dialogOnCloseView.setCloseOnEsc(false);
        dialogOnCloseView.setCloseOnOutsideClick(false);
        Shortcuts.addShortcutListener(dialogOnCloseView, dialogOnCloseView::close, Key.ESCAPE);
    }


    private void configureGrid() {
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.setItems(tempInvoiceProductDtoList);
        grid.addColumn(inPrDto -> tempInvoiceProductDtoList.indexOf(inPrDto) + 1).setHeader("№").setId("№");
        grid.addColumn(inPrDto -> productService.getById(inPrDto.getProductId()).getName()).setHeader("Название")
                .setKey("productDtoName").setId("Название");
        grid.addColumn(inPrDto -> productService.getById(inPrDto.getProductId()).getDescription()).setHeader("Описание")
                .setKey("productDtoDescr").setId("Описание");
        Grid.Column<InvoiceProductDto> firstNameColumn = grid.addColumn("amount").setHeader("Количество");
        grid.addColumn(inPrDto -> unitService.getById(productService.getById(inPrDto.getProductId()).getUnitId()).getFullName()).setHeader("Единицы")
                .setKey("productDtoUnit").setId("Единицы");
        grid.addColumn("price").setHeader("Цена").setSortable(true).setId("Цена");
        /*grid.addColumn(inPrDto -> invoiceService.getById(inPrDto.getInvoiceId())).setHeader("Заказ №")
                .setKey("invoice").setId("Заказ №");*/
        grid.setHeight("36vh");
        grid.setColumnReorderingAllowed(true);
//        grid.setSelectionMode(Grid.SelectionMode.MULTI);

        editor.setBinder(binderInvoiceProductDto);
        editor.setBuffered(true);
        Div validationStatus = new Div();
        validationStatus.setId("validation");
        add(validationStatus);


        amountField.setPattern("^[1-9][0-9]*$");
        amountField.setErrorMessage("Требуется целое число");
        binderInvoiceProductDto.forField(amountField)
                .withConverter(new StringToBigDecimalConverter("must be a number"))
                .withStatusLabel(validationStatus).bind("amount");
        firstNameColumn.setEditorComponent(amountField);
        Collection<Button> editButtons = Collections
                .newSetFromMap(new WeakHashMap<>());

        Grid.Column<InvoiceProductDto> editorColumn = grid.addComponentColumn(column -> {
            Button edit = new Button(new Icon(VaadinIcon.EDIT));
            edit.addClassName("edit");
            edit.addClickListener(e -> {
                editor.editItem(column);
                amountField.focus();
            });
            edit.setEnabled(!editor.isOpen());
            editButtons.add(edit);
            return edit;
        });

        grid.addComponentColumn(column -> {
            Button edit = new Button(new Icon(VaadinIcon.TRASH));
            edit.addClassName("delete");
            edit.addClickListener(e -> deleteProduct(column.getProductId()));
            edit.setEnabled(!editor.isOpen());
            editButtons.add(edit);
            return edit;
        });

        editor.addOpenListener(e -> editButtons
                .forEach(button -> button.setEnabled(!editor.isOpen())));
        editor.addCloseListener(e -> editButtons
                .forEach(button -> button.setEnabled(!editor.isOpen())));

        Button save = new Button("Save", e -> {
            if (binderInvoiceProductDto.validate().isOk()) {
                editor.save();
                setTotalPrice();
                paginator.setData(tempInvoiceProductDtoList);
            } else {
                binderInvoiceProductDto.validate().notifyBindingValidationStatusHandlers();
                editor.cancel();
            }
        });
        save.addClassName("save");

        Button cancel = new Button("Cancel", e -> editor.cancel());
        cancel.addClassName("cancel");

        // Add a keypress listener that listens for an escape key up event.
        grid.getElement().addEventListener("keyup", event -> editor.cancel())
                .setFilter("event.key === 'Escape' || event.key === 'Esc'");

        grid.getElement().addEventListener("keyup", event -> {
            if (binderInvoiceProductDto.validate().isOk()) {
                editor.save();
                setTotalPrice();
                paginator.setData(tempInvoiceProductDtoList);
            } else {
                binderInvoiceProductDto.validate().notifyBindingValidationStatusHandlers();
                editor.cancel();
            }
            buttonAddProduct().focus();
        }).setFilter("event.key === 'Enter'");

        Div buttons = new Div(save, cancel);
        editorColumn.setEditorComponent(buttons);

//        editor.addSaveListener(
//                event -> System.out.println("save listener")
//        );
    }

    private HorizontalLayout upperButtonsLayout() {
        HorizontalLayout upper = new HorizontalLayout();
        upper.add(buttonQuestion(), title(), buttonSave(), configureDeleteButton(), buttonClose(), buttonAddProduct());
        upper.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        return upper;
    }

    private VerticalLayout formLayout() {
        VerticalLayout upper = new VerticalLayout();
        upper.add(horizontalLayout1(),
                horizontalLayout2(),
                horizontalLayout3(),
                horizontalLayout4()
        );
        return upper;
    }

    private HorizontalLayout horizontalLayout1() {
        HorizontalLayout horizontalLayout1 = new HorizontalLayout();
        horizontalLayout1.add(configureDateField(),
                configureContractorSelect(),
                configureInvoicesStatusSelect()
        );
        return horizontalLayout1;
    }

    private HorizontalLayout horizontalLayout2() {
        HorizontalLayout horizontalLayout2 = new HorizontalLayout();
        horizontalLayout2.add(configureCompanySelect(),
                configureWarehouseSelect(),
                configureTotalPrice()
        );
        return horizontalLayout2;
    }

    private HorizontalLayout horizontalLayout3() {
        HorizontalLayout horizontalLayout3 = new HorizontalLayout();
        horizontalLayout3.add(configureProjectSelect(),
                isSpend, isSent, isPrint, commentField
        );
        return horizontalLayout3;
    }

    private HorizontalLayout horizontalLayout4() {
        HorizontalLayout horizontalLayout4 = new HorizontalLayout();
        horizontalLayout4.add(
                configureSalesChannelSelect());
        return horizontalLayout4;
    }

    private HorizontalLayout configureDateField() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Дата");
        label.setWidth(LABEL_WIDTH);
        dateField.setWidth(FIELD_WIDTH);
        dateField.setHelperText("По умолчанию текущая дата/время");
        horizontalLayout.add(label, dateField);
        return horizontalLayout;
    }

    private HorizontalLayout configureCompanySelect() {
        HorizontalLayout companyLayout = new HorizontalLayout();
        List<CompanyDto> companies = companyService.getAll();
        if (companies != null) {
            companySelect.setItems(companies);
        }
        companySelect.setItemLabelGenerator(CompanyDto::getName);
        companySelect.setWidth(FIELD_WIDTH);
        binderInvoiceDto.forField(companySelect)
                .withValidator(Objects::nonNull, "Не заполнено!")
                .bind("companyDto");
        Label label = new Label("Компания");
        label.setWidth(LABEL_WIDTH);
        companyLayout.add(label, companySelect);
        return companyLayout;
    }

    private HorizontalLayout configureProjectSelect() {
        HorizontalLayout projectLayout = new HorizontalLayout();
        List<ProjectDto> projects = projectService.getAll();
        if (projects != null) {
            projectSelect.setItems(projects);
        }
        projectSelect.setItemLabelGenerator(ProjectDto::getName);
        projectSelect.setWidth(FIELD_WIDTH);
        Label label = new Label("Проект");
        label.setWidth(LABEL_WIDTH);
        projectLayout.add(label, projectSelect);
        return projectLayout;
    }

    private HorizontalLayout configureInvoicesStatusSelect() {
        HorizontalLayout invoicesStatusLayout = new HorizontalLayout();
        List<InvoicesStatusDto> invoicesStatusDtoList = invoicesStatusService.getAll();
        if (invoicesStatusDtoList != null) {
            invoicesStatusSelect.setItems(invoicesStatusDtoList);
        }
        invoicesStatusSelect.setWidth(FIELD_WIDTH);
        Label label = new Label("Статус");
        label.setWidth(LABEL_WIDTH);

        invoicesStatusSelect.setItemLabelGenerator(InvoicesStatusDto::getStatusName);
        invoicesStatusSelect.setHelperText("По умолчанию установится статус \"Новый\"");
        invoicesStatusLayout.add(label, invoicesStatusSelect);
        return invoicesStatusLayout;
    }

    private HorizontalLayout configureContractorSelect() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        List<ContractorDto> contractors = contractorService.getAll();
        if (contractors != null) {
            contractorSelect.setItems(contractors);
        }
        contractorSelect.setItemLabelGenerator(ContractorDto::getName);
        contractorSelect.setWidth(FIELD_WIDTH);
        binderInvoiceDto.forField(contractorSelect)
                .withValidator(Objects::nonNull, "Не заполнено!")
                .bind("contractorDto");
        Label label = new Label("Контрагент");
        label.setWidth(LABEL_WIDTH);
        horizontalLayout.add(label, contractorSelect);
        return horizontalLayout;
    }

    private HorizontalLayout configureWarehouseSelect() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        List<WarehouseDto> warehouses = warehouseService.getAll();
        if (warehouses != null) {
            warehouseSelect.setItems(warehouses);
        }
        warehouseSelect.setItemLabelGenerator(WarehouseDto::getName);
        warehouseSelect.setWidth(FIELD_WIDTH);
        binderInvoiceDto.forField(warehouseSelect)
                .withValidator(Objects::nonNull, "Не заполнено!")
                .bind("warehouseDto");
        Label label = new Label("Склад");
        label.setWidth(LABEL_WIDTH);
        horizontalLayout.add(label, warehouseSelect);
        return horizontalLayout;
    }
    private HorizontalLayout configureSalesChannelSelect() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        List<SalesChannelDto> salesChannels = salesChannelService.getAll();
        if (salesChannels != null) {
            salesChannelSelect.setItems(salesChannels);
        }
        salesChannelSelect.setItemLabelGenerator(SalesChannelDto::getName);
        salesChannelSelect.setWidth(SALES_CHANNEL_FIELD_WIDTH);
        Label label = new Label("Канал продаж");
        label.setWidth(LABEL_WIDTH);
        horizontalLayout.add(label, salesChannelSelect, buttonSalesChannel());
        return horizontalLayout;
    }

    private HorizontalLayout configureTotalPrice() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(totalPriceTitle(), totalPrice());
        return horizontalLayout;
    }

    private H4 totalPriceTitle() {
        H4 totalPriceTitle = new H4("Итого:");
        totalPriceTitle.setHeight("2.0em");
        return totalPriceTitle;
    }

    private H4 totalPrice() {
        totalPrice.setText(getTotalPrice().toString());
        totalPrice.setHeight("2.0em");
        return totalPrice;
    }

    private H2 title() {
        title.setHeight("2.0em");
        return title;
    }

    private Button buttonQuestion() {
        Button buttonQuestion = new Button(new Icon(VaadinIcon.QUESTION_CIRCLE_O));
        buttonQuestion.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        return buttonQuestion;
    }

    private void resetSourceTab(boolean isProtected) {
        switch (location) {
            case PURCHASES_SUPPLIERS_ORDERS_VIEW:
                purchasesSubMenuView.resetTabSelection(location, isProtected);
                break;
            case SELLS_CUSTOMERS_ORDERS_VIEW:
                salesSubMenuView.resetTabSelection(location, isProtected);
        }
    }

    private void changeTabSwitchType(boolean isProtected) {
        switch (location) {
            case PURCHASES_SUPPLIERS_ORDERS_VIEW:
                if (isProtected) {
                    purchasesSubMenuView.setProtectedTabSwitch();
                } else {
                    purchasesSubMenuView.releaseProtectedTabSwitch();
                }
                break;
            case SELLS_CUSTOMERS_ORDERS_VIEW:
                if (isProtected) {
                    salesSubMenuView.setProtectedTabSwitch();
                } else {
                    salesSubMenuView.releaseProtectedTabSwitch();
                }
        }
    }

    private Button buttonSave() {
        return new Button("Сохранить", buttonClickEvent -> {

            if (tempInvoiceProductDtoList.isEmpty()) {
                InformationView informationView = new InformationView("Вы не добавили ни одного продукта");
                informationView.open();
                return;
            }

            if (!binderInvoiceDto.validate().isOk()) {
                binderInvoiceDto.validate().notifyBindingValidationStatusHandlers();
            } else {

                if (dateField.getValue() == null) {
                    dateField.setValue(LocalDateTime.now());
                }
                InvoiceDto invoiceDto = saveInvoice(type);

                deleteAllInvoiceProductByInvoice(
                        getListOfInvoiceProductByInvoice(invoiceDto)
                );

                addInvoiceProductToInvoicedDto(invoiceDto);

                // purchasesSubMenuView.resetTabSelection(location, true);
                resetSourceTab(false);
                notifications.infoNotification(String.format("Заказ № %s сохранен", invoiceDto.getId()));
            }
        });
    }

    public Button buttonClose() {
        return new Button("Закрыть",
                new Icon(VaadinIcon.CLOSE),
                event -> {
//                    purchasesSubMenuView.resetTabSelection(location, true);
                    resetSourceTab(true);
                });
    }

    private Button buttonSalesChannel() {
        Button salesChannelButton = new Button(new Icon(VaadinIcon.PLUS_CIRCLE));
        salesChannelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        SalesChannelModalWindow salesChannelModalWindow = new SalesChannelModalWindow(new SalesChannelDto(), salesChannelService);
        salesChannelButton.addClickListener(event -> salesChannelModalWindow.open());
//        salesChannelModalWindow.addDetachListener(event -> updateList());
        return salesChannelButton;
    }

    private Button configureDeleteButton() {
        buttonDelete.addClickListener(event -> {
            deleteInvoiceById(Long.parseLong(invoiceIdField.getValue()));
            resetView();
//            buttonDelete.getUI().ifPresent(ui -> ui.getPage().setLocation(location));
//            purchasesSubMenuView.resetTabSelection(location);
            resetSourceTab(false);
        });

        return buttonDelete;
    }

    private Button buttonAddProduct() {
        Button buttonAddSale = new Button("Добавить продукт", new Icon(VaadinIcon.PLUS_CIRCLE));
        buttonAddSale.addClickListener(event -> {
            productSelectModal.updateProductList();
            productSelectModal.open();
        });
        return buttonAddSale;
    }

    public void addProduct(InvoiceProductDto invoiceProductDto) {
        if (!isProductInList(invoiceProductDto)) {
            tempInvoiceProductDtoList.add(invoiceProductDto);
            paginator.setData(tempInvoiceProductDtoList);
            setTotalPrice();
        }
    }

    private BigDecimal getPriceFromProductPriceByTypeOfPriceId(List<ProductPriceDto> productPriceDtoList, Long id) {

        // Раскомментить, когда в базе будут notNullable TypeOfPrice в таблице product_prises и закомментить
        /*Optional<ProductPriceDto> productPrice = productPriceDtoList.stream().filter(productPriceDto ->
                productPriceDto.getTypeOfPriceId().equals(id)).findFirst();*/
        Optional<ProductPriceDto> productPrice = Optional.ofNullable(productPriceDtoList.get(0));

        //TODO
        // Когда переделают инициализвцию продуктов (у которых есть список ProductPrice) на бэке
        // использовать return который закоментирован
        // return productPrice.get().getValue();

        return productPrice.isPresent() ? productPrice.get().getValue() : BigDecimal.ZERO;
    }

    private void deleteProduct(Long id) {
        InvoiceProductDto found = new InvoiceProductDto();
        for (InvoiceProductDto invoiceProductDto : tempInvoiceProductDtoList) {
            if (invoiceProductDto.getProductId().equals(id)) {
                found = invoiceProductDto;
                break;
            }
        }
        tempInvoiceProductDtoList.remove(found);
        paginator.setData(tempInvoiceProductDtoList);
        setTotalPrice();
    }

    public void setInvoiceDataForEdit(InvoiceDto invoiceDto) {

        if (invoiceDto.getId() != null) {
            invoiceIdField.setValue(invoiceDto.getId().toString());
            setInvoiceProductDtoListForEdit(invoiceDto);
        }

        if (invoiceDto.getDate() != null) {
            dateField.setValue(LocalDateTime.parse(invoiceDto.getDate()));
        }

        if (invoiceDto.getTypeOfInvoice() != null) {
            typeOfInvoiceField.setValue(invoiceDto.getTypeOfInvoice());
        } else {
            typeOfInvoiceField.setValue("");
        }

        if (invoiceDto.getComment() != null) {
            commentField.setValue(invoiceDto.getComment());
        } else {
            commentField.setValue("");
        }

        if (invoiceDto.getCompanyId() != null) {
            companySelect.setValue(companyService.getById(invoiceDto.getCompanyId()));
        }

        if (invoiceDto.getContractorId() != null) {
            contractorSelect.setValue(contractorService.getById(invoiceDto.getContractorId()));
        }

        if (invoiceDto.getWarehouseId() != null) {
            warehouseSelect.setValue(warehouseService.getById(invoiceDto.getWarehouseId()));
        }

        if (invoiceDto.getInvoicesStatusId() != null) {
            invoicesStatusSelect.setValue(invoicesStatusService.getById(invoiceDto.getInvoicesStatusId()));
        }

        if (invoiceDto.getProjectId() != null) {
            projectSelect.setValue(projectService.getById(invoiceDto.getProjectId()));
        }

        isSpend.setValue(invoiceDto.getIsSpend());
        isSent.setValue(invoiceDto.getIsSent());
        isPrint.setValue(invoiceDto.getIsPrint());
        commentField.setPlaceholder("Комментарий к заказу");
        commentField.setWidth("300px");

    }

    private boolean isProductInList(InvoiceProductDto invoiceProductDto) {
        return tempInvoiceProductDtoList.stream()
                .anyMatch(invoiceProductDtoElem -> invoiceProductDtoElem.getProductId().equals(invoiceProductDto.getProductId()));
    }

    public BigDecimal getTotalPrice() {
        BigDecimal totalPrice = BigDecimal.valueOf(0.0);
        for (InvoiceProductDto invoiceProductDto : tempInvoiceProductDtoList) {
            totalPrice = totalPrice.add(invoiceProductDto.getPrice()
                    .multiply(invoiceProductDto.getAmount()));
        }
        return totalPrice;
    }

    private void setTotalPrice() {
        totalPrice.setText(
                String.format("%.2f", getTotalPrice())
        );
    }

    public void setUpdateState(boolean isUpdate) {
        title.setText(isUpdate ? "Редактирование заказа" : "Добавление заказа");
        buttonDelete.setVisible(isUpdate);
    }

    public void resetView() {
        invoiceIdField.clear();
        dateField.clear();
        companySelect.setValue(null);
        contractorSelect.setValue(null);
        warehouseSelect.setValue(null);
        projectSelect.setValue(null);

        List<InvoicesStatusDto> invoicesStatusDtoList = invoicesStatusService.getAll();
        Optional<InvoicesStatusDto> dto = invoicesStatusDtoList.stream().filter(x -> x.getStatusName().toUpperCase(Locale.ROOT).contains("НОВЫЙ")).findFirst();
        invoicesStatusSelect.setValue(dto.get());

        isSpend.clear();
        isSent.clear();
        isPrint.clear();
        commentField.setValue("");
        commentField.setPlaceholder("Комментарий");
        contractorSelect.setInvalid(false);
        companySelect.setInvalid(false);
        warehouseSelect.setInvalid(false);
        title.setText("Добавление заказа");
        paginator.setData(tempInvoiceProductDtoList = new ArrayList<>());
        setTotalPrice();
    }

    private InvoiceDto saveInvoice(String type) {
        InvoiceDto invoiceDto = new InvoiceDto();
        if (!invoiceIdField.getValue().equals("")) {
            invoiceDto.setId(Long.parseLong(invoiceIdField.getValue()));
        }
        invoiceDto.setDate(dateField.getValue().toString());
        invoiceDto.setCompanyId(companySelect.getValue().getId());
        invoiceDto.setContractorId(contractorSelect.getValue().getId());
        invoiceDto.setWarehouseId(warehouseSelect.getValue().getId());
        invoiceDto.setSalesChannelId(salesChannelSelect.getValue().getId());
        invoiceDto.setInvoicesStatusId(invoicesStatusSelect.getValue().getId());
        invoiceDto.setProjectId(projectSelect.getValue() == null ?
                null : projectSelect.getValue().getId());
        invoiceDto.setTypeOfInvoice(type);
        invoiceDto.setIsSpend(isSpend.getValue());
        invoiceDto.setIsSent(isSent.getValue());
        invoiceDto.setIsPrint(isPrint.getValue());
        invoiceDto.setComment(commentField.getValue());

        Response<InvoiceDto> invoiceDtoResponse = invoiceService.create(invoiceDto);
        return invoiceDtoResponse.body();
    }

    private void addInvoiceProductToInvoicedDto(InvoiceDto invoiceDto) {
        for (InvoiceProductDto invoiceProductDto : tempInvoiceProductDtoList) {
            invoiceProductDto.setInvoiceId(invoiceDto.getId());
            invoiceProductDto.setProductId(invoiceProductDto.getProductId());
            invoiceProductDto.setPrice(invoiceProductDto.getPrice());
            invoiceProductDto.setAmount(invoiceProductDto.getAmount());
            invoiceProductService.create(invoiceProductDto);
        }
    }

    public List<InvoiceProductDto> getListOfInvoiceProductByInvoice(InvoiceDto invoiceDto) {
        return invoiceProductService.getByInvoiceId(invoiceDto.getId());
    }

    private void deleteAllInvoiceProductByInvoice(List<InvoiceProductDto> invoiceProductDtoList) {
        for (InvoiceProductDto invoiceProductDto : invoiceProductDtoList) {
            invoiceProductService.deleteById(invoiceProductDto.getId());
        }
    }

    public void deleteInvoiceById(Long invoiceDtoId) {
        invoiceService.deleteById(invoiceDtoId);
        notifications.infoNotification(String.format("Заказ № %s успешно удален", invoiceDtoId));
    }

    private void setInvoiceProductDtoListForEdit(InvoiceDto invoiceDto) {
        tempInvoiceProductDtoList = getListOfInvoiceProductByInvoice(invoiceDto);
        setTotalPrice();
        grid.setItems(tempInvoiceProductDtoList);
    }

    private void recalculateProductPrices() {
        for (InvoiceProductDto invoiceProductDto : tempInvoiceProductDtoList) {
            invoiceProductDto.setPrice(
                    getPriceFromProductPriceByTypeOfPriceId(
                            productService.getById(invoiceProductDto.getProductId()).getProductPriceIds()
                                    .stream().map(productPriceService::getById)
                                    .collect(Collectors.toList()),
                            typeOfPriceService.getById(contractorSelect.getValue().getTypeOfPriceId()).getId()
                            //contractorSelect.getValue().getTypeOfPriceDto().getId()
                    )
            );
            grid.setItems(tempInvoiceProductDtoList);
            setTotalPrice();
        }
    }

    private void configureRecalculateDialog() {
        dialogOnChangeContractor.add(new Text("Вы меняете покупателя!! Пересчитать цены на продукты?"));
        dialogOnChangeContractor.setCloseOnEsc(false);
        dialogOnChangeContractor.setCloseOnOutsideClick(false);

        Button confirmButton = new Button("Пересчитать", event -> {
            recalculateProductPrices();
            dialogOnChangeContractor.close();
        });
        Button cancelButton = new Button("Оставить как есть", event -> {
            dialogOnChangeContractor.close();
        });

        Shortcuts.addShortcutListener(dialogOnChangeContractor,
                dialogOnChangeContractor::close, Key.ESCAPE);

        dialogOnChangeContractor.add(new Div(confirmButton, new Div(), cancelButton));
    }

    // Поменять реализацию (выполняется избыточное "пересобирание" диалога при каждом его вызове)
    private void terminateCloseDialog(BeforeLeaveEvent beforeLeaveEvent) {
        BeforeLeaveEvent.ContinueNavigationAction action = beforeLeaveEvent.postpone();
        dialogOnCloseView.removeAll();

        Button confirmButton = new Button("Продолжить", event -> {
            dialogOnCloseView.close();
            changeTabSwitchType(false);
//            purchasesSubMenuView.releaseProtectedTabSwitch();
            action.proceed();
        });
        Button cancelButton = new Button("Отменить", event -> {
            dialogOnCloseView.close();
        });

        dialogOnCloseView.add(new VerticalLayout(
                new Text("Вы уверены? Несохраненные данные будут утеряны!"),
                new HorizontalLayout(cancelButton, confirmButton))
        );

        dialogOnCloseView.open();
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setProtectedTabSwitch() {
        changeTabSwitchType(true);
//        this.purchasesSubMenuView.setProtectedTabSwitch();
    }

    @Override
    public void beforeLeave(BeforeLeaveEvent beforeLeaveEvent) {
        terminateCloseDialog(beforeLeaveEvent);
    }
}
