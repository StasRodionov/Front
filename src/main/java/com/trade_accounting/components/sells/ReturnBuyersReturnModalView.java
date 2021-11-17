package com.trade_accounting.components.sells;

import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.models.dto.*;
import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.ContractorService;
import com.trade_accounting.services.interfaces.WarehouseService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.converter.StringToBigDecimalConverter;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.WeakHashMap;
import java.util.stream.Collectors;

@UIScope
@SpringComponent
public class ReturnBuyersReturnModalView extends Dialog {

    private final ContractorService contractorService;
    private final WarehouseService warehouseService;
    private final CompanyService companyService;

    private final ComboBox<WarehouseDto> warehouseDtoComboBox = new ComboBox<>();
    public  final  ComboBox<ContractorDto> contractorDtoComboBox = new ComboBox<>();
    private final ComboBox<CompanyDto> companyDtoComboBox = new ComboBox<>();
    private final Checkbox chooseReturnBuyersReturn = new Checkbox("Проведено");
    private final TextField textReport = new TextField();
    private final TextField commentConfig = new TextField();
    private final TextField summConfig = new TextField();

    private final DateTimePicker dateTimePicker = new DateTimePicker();
    private final ComboBox<String> choosePromoCode = new ComboBox<>();
    private       String datePeriod;
    private final DatePicker dateOn = new DatePicker();
    private final DatePicker dateTo = new DatePicker();
    private BuyersReturnDto buyersReturnDto;
    private final ReturnBuyersGoodModalWin returnBuyersGoodModalWin;
    private final Grid<ProductDto> grid = new Grid<>(ProductDto.class, false);
    private InvoiceProductDto invoiceProductDto = new InvoiceProductDto();
    private ProductDto productDto;
//    private final GridPaginator<ProductDto> paginator;
    private List<ProductDto> tempInvoiceProductDtoList = new ArrayList<>();
    private static long number = 0;
    private static Double summ = 0.00;

    public ReturnBuyersReturnModalView(ContractorService contractorService,
                                       WarehouseService warehouseService,
                                          CompanyService companyService,
                                       ReturnBuyersGoodModalWin returnBuyersGoodModalWin) {
        this.contractorService = contractorService;
        this.warehouseService = warehouseService;
        this.companyService = companyService;
        this.returnBuyersGoodModalWin = returnBuyersGoodModalWin;
        grid.addColumn(inPrDto -> invoiceProductDto.getId()).setHeader("№").setId("№");
        grid.addColumn(inPrDto -> invoiceProductDto.getId()).setHeader("Название").setId("Название");
        grid.addColumn(inPrDto -> invoiceProductDto.getAmount()).setHeader("Количество").setId("Количество");
        grid.addColumn(inPrDto -> invoiceProductDto.getPrice()).setHeader("Цена").setId("Цена");
        grid.addColumn(inPrDto -> summ).setHeader("Сумма").setId("Summa");
        returnBuyersGoodModalWin.addDetachListener(detachEvent -> {
            this.productDto = returnBuyersGoodModalWin.productSelect.getValue();
            productDto.setMinimumBalance(Integer.parseInt(returnBuyersGoodModalWin.productSelect.getLabel()));
            summ = Double.parseDouble(returnBuyersGoodModalWin.productSelect.getHelperText());
            System.out.println("11111111111222222222222222222  " + returnBuyersGoodModalWin.productSelect.getHelperText());
            if (productDto != null) {
                tempInvoiceProductDtoList.add(productDto);
                configureGrid();
                setSizeFull();
                add(summConfigure());
            }
        });
        setSizeFull();
        add(topButtons(), formToAddCommissionAgent(), grid);
    }


    private Grid<ProductDto> configureGrid() {
        grid.removeAllColumns();
        grid.setItems(tempInvoiceProductDtoList);
        for(ProductDto list: tempInvoiceProductDtoList) {
            System.out.println("LLLLLLLLLLLLLLLLLLList   " + list);
        }
        grid.addColumn(inPrDto -> inPrDto.getDescription()).setHeader("Название").setId("Название");
        grid.addColumn(inPrDto -> inPrDto.getMinimumBalance()).setHeader("Количество").setId("Количество"); //Это не верно
        grid.addColumn(inPrDto -> inPrDto.getPurchasePrice()).setHeader("Цена").setId("Цена");
//        grid.addColumn(inPrDto -> inPrDto.getMinimumBalance() * inPrDto.getPurchasePrice()).setHeader("Сумма").setId("Сумма");
        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        return grid;
    }

    public void addProduct(ProductDto productDto) {
        invoiceProductDto.setProductId(productDto.getId());
        invoiceProductDto.setAmount(BigDecimal.ONE);
        invoiceProductDto.setPrice(productDto.getPurchasePrice());
    }

    private HorizontalLayout topButtons() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        setSizeFull();
        horizontalLayout.add(save(), closeButton(), addProductButton(), chooseReturnBuyersReturn);
        return horizontalLayout;
    }

    public void setReturnEdit(BuyersReturnDto editDto) {
        this.buyersReturnDto = editDto;
        warehouseDtoComboBox.setValue(warehouseService.getById(editDto.getWarehouseId()));
        contractorDtoComboBox.setValue(contractorService.getById(editDto.getContractorId()));
        commentConfig.setValue(editDto.getComment());

//        dateTimePicker.setValue(LocalDateTime.parse(editDto.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        companyDtoComboBox.setValue(companyService.getById(editDto.getCompanyId()));
    }



    private Button save() {
        Button button = new Button("Сохранить");
        button.addClickListener(e -> {
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

    private Button addProductButton() {
        return new Button("Добавить продукт из списка", new Icon(VaadinIcon.PLUS_CIRCLE), buttonClickEvent -> {
            returnBuyersGoodModalWin.updateProductList();
            returnBuyersGoodModalWin.open();
        });
    }

    private VerticalLayout formToAddCommissionAgent() {
        VerticalLayout form = new VerticalLayout();
        form.add(horizontalLayout1(), horizontalLayout2(),
                horizontalLayout3(), horizontalLayout4(), commentConfigure(), summConfigure());
        return form;
    }

    private HorizontalLayout horizontalLayout1() {
        HorizontalLayout hLayout = new HorizontalLayout();
        hLayout.add(dataConfigure());
        return hLayout;
    }

    private HorizontalLayout dataConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Возврат покупателя №");
//        label.setWidth("150px");
//        textReport.setWidth("50px");
        Label label2 = new Label("от");
//        dateTimePicker.setWidth("350px");
        horizontalLayout.add(label,textReport,label2,dateTimePicker);
        return horizontalLayout;
    }

    private HorizontalLayout horizontalLayout2() {
        HorizontalLayout hLay2 = new HorizontalLayout();
        Label label = new Label("Период");
        hLay2.add(companyConfigure(), label,dateOn,dateTo);
        return hLay2;
    }

    private HorizontalLayout companyConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        List<CompanyDto> companyDtos = companyService.getAll();
        if(companyDtos != null) {
            companyDtoComboBox.setItems(companyDtos);
        }
        companyDtoComboBox.setItemLabelGenerator(CompanyDto::getName);
        companyDtoComboBox.setWidth("350px");
        Label label = new Label("Компания  ");
//        label.setWidth("100px");
        horizontalLayout.add(label,companyDtoComboBox);
        return horizontalLayout;
    }

    private HorizontalLayout horizontalLayout3() {
        HorizontalLayout hLay3 = new HorizontalLayout();
        hLay3.add(contractorConfigure(), warehouseConfigure());
        return hLay3;
    }

    private HorizontalLayout contractorConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        List<ContractorDto> contractorDtos = contractorService.getAll();
        if(contractorDtos != null) {
            contractorDtoComboBox.setItems(contractorDtos);
        }
        contractorDtoComboBox.setItemLabelGenerator(ContractorDto::getName);
        contractorDtoComboBox.setWidth("350px");
        Label label = new Label("Контрагент");
//        label.setWidth("100px");
        horizontalLayout.add(label, contractorDtoComboBox);
        return horizontalLayout;
    }

    private HorizontalLayout warehouseConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        List<WarehouseDto> wareDtos = warehouseService.getAll();
        if(wareDtos !=null) {
            warehouseDtoComboBox.setItems(wareDtos);
        }
        warehouseDtoComboBox.setWidth("150px");
        Label label = new Label("Договор");
        label.setWidth("100px");
        horizontalLayout.add(label,warehouseDtoComboBox);
        return horizontalLayout;
    }

    private HorizontalLayout horizontalLayout4() {
        HorizontalLayout hLayout4 = new HorizontalLayout();
        hLayout4.add(incomingConfigure(), configureAward());
        return hLayout4;
    }

    private HorizontalLayout commentConfigure() {
        HorizontalLayout horizontal3 = new HorizontalLayout();
//        commentConfig.setWidth("500px");
//        commentConfig.setHeight("300px");
        commentConfig.setPlaceholder("Комментарий");
        horizontal3.add(commentConfig);
        return horizontal3;
    }

    private HorizontalLayout summConfigure() {
        HorizontalLayout horizontal6 = new HorizontalLayout();
//        commentConfig.setWidth("500px");
//        commentConfig.setHeight("300px");
        summConfig.setValue(summ.toString());
        summConfig.setPlaceholder("Сумма");
        horizontal6.add(summConfig);
        return horizontal6;
    }

    private HorizontalLayout incomingConfigure() {
        HorizontalLayout horizontal1 = new HorizontalLayout();
        DatePicker dt1 = new DatePicker();
        Label label = new Label("Входящий номер");
        label.setWidth("150px");
        TextField text = new TextField();
        text.setWidth("70px");
        Label label2 = new Label("от");
        dt1.setWidth("150px");
        horizontal1.add(label,text,label2,dt1);
        return horizontal1;
    }

    private HorizontalLayout configureAward() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        choosePromoCode.setItems("Выдать купон","Не выдавать купон");
        choosePromoCode.setValue("Выдать купон");
        Label label = new Label("Компенсация");
        horizontalLayout.add(label, choosePromoCode);
        return horizontalLayout;
    }
}
