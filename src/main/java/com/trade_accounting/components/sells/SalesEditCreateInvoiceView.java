package com.trade_accounting.components.sells;


import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.models.dto.CompanyDto;
import com.trade_accounting.models.dto.ContractorDto;
import com.trade_accounting.models.dto.InvoiceProductDto;
import com.trade_accounting.models.dto.ProductDto;
import com.trade_accounting.models.dto.WarehouseDto;
import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.ContractorService;
import com.trade_accounting.services.interfaces.WarehouseService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Route(value = "sells/customer-order-edit", layout = AppView.class)
@PageTitle("Изменить заказ")
@SpringComponent
@UIScope
public class SalesEditCreateInvoiceView extends Div {

    private final ContractorService contractorService;
    private final CompanyService companyService;
    private final WarehouseService warehouseService;

    private String labelWidth = "100px";
    private String fieldWidth = "300px";
    private DateTimePicker dateField = new DateTimePicker();
    private Checkbox isSpend = new Checkbox("Проведено");
    private Select<CompanyDto> companySelect = new Select<>();
    private Select<ContractorDto> contractorSelect = new Select<>();
    private Select<WarehouseDto> warehouseSelect = new Select<>();

    private List<InvoiceProductDto> tempInvoiceProductDtoList = new ArrayList<>();
    private final Grid<InvoiceProductDto> grid = new Grid<>(InvoiceProductDto.class, false);
    private final GridPaginator<InvoiceProductDto> paginator;
    private final SalesChooseGoodsModalWin salesChooseGoodsModalWin;

    @Autowired
    public SalesEditCreateInvoiceView(ContractorService contractorService,
                                      CompanyService companyService,
                                      WarehouseService warehouseService,
                                      SalesChooseGoodsModalWin salesChooseGoodsModalWin

    ) {
        this.contractorService = contractorService;
        this.companyService = companyService;
        this.warehouseService = warehouseService;
        this.salesChooseGoodsModalWin = salesChooseGoodsModalWin;

        configureGrid();
        paginator = new GridPaginator<>(grid, tempInvoiceProductDtoList, 100);

        add(upperButtonsLayout(), formLayout(), grid, paginator);
    }

    private void configureGrid() {
        grid.setItems(tempInvoiceProductDtoList);
//        grid.addColumn("id").setHeader("ID").setId("ID");
        grid.addColumn(inPrDto -> inPrDto.getProductDto().getName()).setHeader("Название").setKey("productDtoName").setId("Название");
        grid.addColumn(inPrDto -> inPrDto.getProductDto().getDescription()).setHeader("Описание").setKey("productDtoDescr").setId("Описание");
        grid.addColumn("amount").setHeader("Количество").setId("Количество");
        grid.addColumn("price").setHeader("Цена").setId("Цена");
        grid.setHeight("36vh");
        grid.setColumnReorderingAllowed(true);
//        grid.setSelectionMode(Grid.SelectionMode.MULTI);
    }

    private HorizontalLayout upperButtonsLayout() {
        HorizontalLayout upper = new HorizontalLayout();
        upper.add(buttonQuestion(), title(), buttonSave(), buttonClose(), buttonAddProduct());
        upper.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
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
                configureWarehouseSelect()
        );
        return horizontalLayout2;
    }

    private VerticalLayout formLayout() {
        VerticalLayout upper = new VerticalLayout();
        upper.add(horizontalLayout1(),
                horizontalLayout2()
        );
        return upper;
    }

    private HorizontalLayout configureDateField() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Дата");
        label.setWidth(labelWidth);
        dateField.setWidth(fieldWidth);
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
        Label label = new Label("Склад");
        label.setWidth(labelWidth);
        horizontalLayout.add(label, warehouseSelect);
        return horizontalLayout;
    }


    private H2 title() {
        H2 title = new H2("Добавление/редактирование заказа");
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
            System.out.println("**************************************************************");
            System.out.println(dateField.getValue());
            System.out.println(companySelect.getValue());
            System.out.println(contractorSelect.getValue());
            System.out.println(warehouseSelect.getValue());
            System.out.println(isSpend.getValue());
        });
        return buttonSave;
    }

    private Button buttonClose() {
        Button buttonUnit = new Button("Закрыть", new Icon(VaadinIcon.CLOSE));
        buttonUnit.addClickListener(event -> {
            buttonUnit.getUI().ifPresent(ui -> ui.navigate("sells"));
        });
        return buttonUnit;
    }

    private Button buttonAddProduct() {
        return new Button("Добавить продукт", new Icon(VaadinIcon.PLUS_CIRCLE), buttonClickEvent -> {
            salesChooseGoodsModalWin.open();
        }
        );
    }

    public void addProduct(ProductDto productDto) {
        InvoiceProductDto invoiceProductDto = new InvoiceProductDto();
        invoiceProductDto.setProductDto(productDto);
        invoiceProductDto.setAmount(BigDecimal.ONE);
        invoiceProductDto.setPrice(productDto.getPurchasePrice());
        if (!isProductInList(productDto)) {
            tempInvoiceProductDtoList.add(invoiceProductDto);
        }
        paginator.setData(tempInvoiceProductDtoList);
    }

    private boolean isProductInList(ProductDto productDto) {
        boolean isExists = false;
        for (InvoiceProductDto invoiceProductDto : tempInvoiceProductDtoList) {
            if (invoiceProductDto.getProductDto().getId() == productDto.getId()) {
                isExists = true;
            }
        }
        return isExists;
    }
}
