package com.trade_accounting.components.sells;


import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.models.dto.CompanyDto;
import com.trade_accounting.models.dto.ContractorDto;
import com.trade_accounting.models.dto.InvoiceDto;
import com.trade_accounting.models.dto.InvoiceProductDto;
import com.trade_accounting.models.dto.ProductDto;
import com.trade_accounting.models.dto.WarehouseDto;
import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.ContractorService;
import com.trade_accounting.services.interfaces.InvoiceProductService;
import com.trade_accounting.services.interfaces.InvoiceService;
import com.trade_accounting.services.interfaces.WarehouseService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.grid.Grid;
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
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToBigDecimalConverter;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import retrofit2.Response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.WeakHashMap;

@Slf4j
@Route(value = "sells/customer-order-edit", layout = AppView.class)
@PageTitle("Изменить заказ")
@SpringComponent
@UIScope
public class SalesEditCreateInvoiceView extends Div implements AfterNavigationObserver {

    private final ContractorService contractorService;
    private final CompanyService companyService;
    private final WarehouseService warehouseService;
    private final InvoiceService invoiceService;
    private final InvoiceProductService invoiceProductService;

    private final String labelWidth = "100px";
    private final String fieldWidth = "300px";
    private TextField invoiceIdField = new TextField();
    private DateTimePicker dateField = new DateTimePicker();
    private TextField typeOfInvoiceField = new TextField();
    private Checkbox isSpend = new Checkbox("Проведено");
    private final Select<CompanyDto> companySelect = new Select<>();
    private final Select<ContractorDto> contractorSelect = new Select<>();
    private final Select<WarehouseDto> warehouseSelect = new Select<>();

    TextField amountField = new TextField();
    private H4 totalPrice = new H4();
    public H2 title = new H2("Добавление заказа");

    private List<InvoiceProductDto> tempInvoiceProductDtoList = new ArrayList<>();

    private final Grid<InvoiceProductDto> grid = new Grid<>(InvoiceProductDto.class, false);
    private final GridPaginator<InvoiceProductDto> paginator;
    private final SalesChooseGoodsModalWin salesChooseGoodsModalWin;

    Editor<InvoiceProductDto> editor = grid.getEditor();
    Binder<InvoiceProductDto> binderInvoiceProductDto = new Binder<>(InvoiceProductDto.class);
    Binder<InvoiceDto> binderInvoiceDto = new Binder<>(InvoiceDto.class);

    @Autowired
    public SalesEditCreateInvoiceView(ContractorService contractorService,
                                      CompanyService companyService,
                                      WarehouseService warehouseService,
                                      InvoiceService invoiceService,
                                      InvoiceProductService invoiceProductService,
                                      SalesChooseGoodsModalWin salesChooseGoodsModalWin
    ) {
        this.contractorService = contractorService;
        this.companyService = companyService;
        this.warehouseService = warehouseService;
        this.invoiceService = invoiceService;
        this.invoiceProductService = invoiceProductService;
        this.salesChooseGoodsModalWin = salesChooseGoodsModalWin;

        configureGrid();
        paginator = new GridPaginator<>(grid, tempInvoiceProductDtoList, 100);

        add(upperButtonsLayout(), formLayout(), grid, paginator);
    }

    private void configureGrid() {
        grid.setItems(tempInvoiceProductDtoList);
        grid.addColumn(inPrDto -> inPrDto.getProductDto().getName()).setHeader("Название")
                .setKey("productDtoName").setId("Название");
        grid.addColumn(inPrDto -> inPrDto.getProductDto().getDescription()).setHeader("Описание")
                .setKey("productDtoDescr").setId("Описание");
        Grid.Column<InvoiceProductDto> firstNameColumn = grid.addColumn("amount").setHeader("Количество");
        grid.addColumn(inPrDto -> inPrDto.getProductDto().getUnitDto().getFullName()).setHeader("Единицы")
                .setKey("productDtoUnit").setId("Единицы");
        grid.addColumn("price").setHeader("Цена").setId("Цена");
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

        Grid.Column<InvoiceProductDto> deleteColumn = grid.addComponentColumn(column -> {
            Button edit = new Button(new Icon(VaadinIcon.TRASH));
            edit.addClassName("delete");
            edit.addClickListener(e -> {
                deleteProduct(column.getProductDto().getId());
            });
            edit.setEnabled(!editor.isOpen());
            editButtons.add(edit);
            return edit;
        });

        editor.addOpenListener(e -> editButtons.stream()
                .forEach(button -> button.setEnabled(!editor.isOpen())));
        editor.addCloseListener(e -> editButtons.stream()
                .forEach(button -> button.setEnabled(!editor.isOpen())));

        Button save = new Button("Save", e -> {
            editor.save();
            setTotalPrice();
            paginator.setData(tempInvoiceProductDtoList);
        });
        save.addClassName("save");

        Button cancel = new Button("Cancel", e -> editor.cancel());
        cancel.addClassName("cancel");

// Add a keypress listener that listens for an escape key up event.
        grid.getElement().addEventListener("keyup", event -> editor.cancel())
                .setFilter("event.key === 'Escape' || event.key === 'Esc'");

        grid.getElement().addEventListener("keyup", event -> {
            editor.save();
            setTotalPrice();
            paginator.setData(tempInvoiceProductDtoList);
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
        upper.add(buttonQuestion(), title(), buttonSave(), buttonClose(), buttonAddProduct());
        upper.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        return upper;
    }

    private VerticalLayout formLayout() {
        VerticalLayout upper = new VerticalLayout();
        upper.add(horizontalLayout1(),
                horizontalLayout2()
        );
        return upper;
    }

    private HorizontalLayout horizontalLayout1() {
        HorizontalLayout horizontalLayout1 = new HorizontalLayout();
        horizontalLayout1.add(configureDateField(),
                configureContractorSelect(),
                isSpend
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

    private HorizontalLayout configureDateField() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Дата");
        label.setWidth(labelWidth);
        dateField.setWidth(fieldWidth);
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
        companySelect.setWidth(fieldWidth);
        binderInvoiceDto.forField(companySelect)
                .withValidator(Objects::nonNull, "Не заполнено!")
                .bind("companyDto");
        Label label = new Label("Компания");
        label.setWidth(labelWidth);
        companyLayout.add(label, companySelect);
        return companyLayout;
    }

    private HorizontalLayout configureContractorSelect() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        List<ContractorDto> contractors = contractorService.getAll();
        if (contractors != null) {
            contractorSelect.setItems(contractors);
        }
        contractorSelect.setItemLabelGenerator(ContractorDto::getName);
        contractorSelect.setWidth(fieldWidth);
        binderInvoiceDto.forField(contractorSelect)
                .withValidator(Objects::nonNull, "Не заполнено!")
                .bind("contractorDto");
        Label label = new Label("Контрагент");
        label.setWidth(labelWidth);
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
        warehouseSelect.setWidth(fieldWidth);
        binderInvoiceDto.forField(warehouseSelect)
                .withValidator(Objects::nonNull, "Не заполнено!")
                .bind("warehouseDto");
        Label label = new Label("Склад");
        label.setWidth(labelWidth);
        horizontalLayout.add(label, warehouseSelect);
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

    private Button buttonSave() {
        Button buttonSave = new Button("Сохранить", buttonClickEvent -> {

            if (!binderInvoiceDto.validate().isOk()) {
                binderInvoiceDto.validate().notifyBindingValidationStatusHandlers();
            } else {
                if (dateField.getValue() == null) {
                    dateField.setValue(LocalDateTime.now());
                }
                InvoiceDto invoiceDto = saveInvoice();
                addInvoiceProductToInvoicedDto(invoiceDto);
                UI.getCurrent().navigate("sells");
            }
        });
        return buttonSave;
    }

    private Button buttonClose() {
        Button buttonUnit = new Button("Закрыть", new Icon(VaadinIcon.CLOSE));
        buttonUnit.addClickListener(event -> {
            resetView();
            buttonUnit.getUI().ifPresent(ui -> ui.navigate("sells"));
        });
        return buttonUnit;
    }

    private Button buttonAddProduct() {
        Button button = new Button("Добавить продукт", new Icon(VaadinIcon.PLUS_CIRCLE), buttonClickEvent -> {
            salesChooseGoodsModalWin.open();
        });
        return button;
    }

    public void addProduct(ProductDto productDto) {
        InvoiceProductDto invoiceProductDto = new InvoiceProductDto();
        invoiceProductDto.setProductDto(productDto);
        invoiceProductDto.setAmount(BigDecimal.ONE);
        invoiceProductDto.setPrice(productDto.getPurchasePrice());
        if (!isProductInList(productDto)) {
            tempInvoiceProductDtoList.add(invoiceProductDto);
            paginator.setData(tempInvoiceProductDtoList);
            setTotalPrice();
        }
    }

    private void deleteProduct(Long id) {
        InvoiceProductDto found = new InvoiceProductDto();
        for (InvoiceProductDto invoiceProductDto : tempInvoiceProductDtoList) {
            if (invoiceProductDto.getProductDto().getId().equals(id)) {
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
        }

        if (invoiceDto.getDate() != null) {
            dateField.setValue(LocalDateTime.parse(invoiceDto.getDate()));
        }

        if (invoiceDto.getTypeOfInvoice() != null) {
            typeOfInvoiceField.setValue(invoiceDto.getTypeOfInvoice());
        } else {
            typeOfInvoiceField.setValue("");
        }

        if (invoiceDto.getCompanyDto() != null) {
            companySelect.setValue(invoiceDto.getCompanyDto());
        }

        if (invoiceDto.getContractorDto() != null) {
            contractorSelect.setValue(invoiceDto.getContractorDto());
        }

        isSpend.setValue(invoiceDto.isSpend());

        if (invoiceDto.getWarehouseDto() != null) {
            warehouseSelect.setValue(invoiceDto.getWarehouseDto());
        }

    }

    private boolean isProductInList(ProductDto productDto) {
        boolean isExists = false;
        for (InvoiceProductDto invoiceProductDto : tempInvoiceProductDtoList) {
            if (invoiceProductDto.getProductDto().getId().equals(productDto.getId())) {
                isExists = true;
            }
        }
        return isExists;
    }

    private BigDecimal getTotalPrice() {
        System.out.println(companySelect.getValue());
        BigDecimal totalPrice = BigDecimal.valueOf(0.0);
        for (InvoiceProductDto invoiceProductDto : tempInvoiceProductDtoList) {
            totalPrice = totalPrice.add(invoiceProductDto.getProductDto().getPurchasePrice()
                    .multiply(invoiceProductDto.getAmount()));
        }
        return totalPrice;
    }

    private void setTotalPrice() {
        totalPrice.setText(getTotalPrice().toString());
    }

    public void showUpdateTitle(boolean isUpdate) {
        title.setText(isUpdate ? "Редактирование заказа" : "Добавление заказа");
    }

    public void resetView() {
        invoiceIdField.clear();
        dateField.clear();
        companySelect.setValue(null);
        contractorSelect.setValue(null);
        warehouseSelect.setValue(null);
        isSpend.clear();
        contractorSelect.setInvalid(false);
        companySelect.setInvalid(false);
        warehouseSelect.setInvalid(false);
        title.setText("Добавление аказа");
        tempInvoiceProductDtoList = new ArrayList<>();
        paginator.setData(tempInvoiceProductDtoList);
        setTotalPrice();
    }

    private InvoiceDto saveInvoice() {
        InvoiceDto invoiceDto = new InvoiceDto();
        if (!invoiceIdField.getValue().equals("")) {
            invoiceDto.setId(Long.parseLong(invoiceIdField.getValue()));
        }
        invoiceDto.setDate(dateField.getValue().toString());
        invoiceDto.setCompanyDto(companySelect.getValue());
        invoiceDto.setContractorDto(contractorSelect.getValue());
        invoiceDto.setWarehouseDto(warehouseSelect.getValue());
        invoiceDto.setTypeOfInvoice("RECEIPT");
        invoiceDto.setSpend(isSpend.getValue());

        Response<InvoiceDto> invoiceDtoResponse = invoiceService.create(invoiceDto);
        InvoiceDto invoiceDtoForProducts = invoiceDtoResponse.body();
        System.out.println(invoiceDtoForProducts);
        return invoiceDtoForProducts;
    }

    private void addInvoiceProductToInvoicedDto(InvoiceDto invoiceDto) {
        for (InvoiceProductDto invoiceProductDto:tempInvoiceProductDtoList){
        invoiceProductDto.setInvoiceDto(invoiceDto);
        invoiceProductDto.setProductDto(invoiceProductDto.getProductDto());
        invoiceProductDto.setPrice(invoiceProductDto.getPrice());
        invoiceProductDto.setAmount(invoiceProductDto.getAmount());
        invoiceProductService.create(invoiceProductDto);
        System.out.println(invoiceProductDto);
        }
    }

    private void  removeAllInvociceProductByInvoice(InvoiceDto invoiceDto){

    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
    }
}
