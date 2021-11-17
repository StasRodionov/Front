package com.trade_accounting.components.sells;

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
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import java.util.ArrayList;
import java.util.List;

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
    private final SelectProductFromListWithQuantityModalWin selectProductFromListWithQuantityModalWin;
    private final Grid<ProductDto> grid = new Grid<>(ProductDto.class, false);
    private ProductDto productDto;
    private List<ProductDto> tempInvoiceProductDtoList = new ArrayList<>();
    private static Double summ = 0.00;

    public ReturnBuyersReturnModalView(ContractorService contractorService,
                                       WarehouseService warehouseService,
                                       CompanyService companyService,
                                       SelectProductFromListWithQuantityModalWin selectProductFromListWithQuantityModalWin) {
        this.contractorService = contractorService;
        this.warehouseService = warehouseService;
        this.companyService = companyService;
        this.selectProductFromListWithQuantityModalWin = selectProductFromListWithQuantityModalWin;
        selectProductFromListWithQuantityModalWin.addDetachListener(detachEvent -> {
            this.productDto = selectProductFromListWithQuantityModalWin.productSelect.getValue();
            productDto.setMinimumBalance(Integer.parseInt(selectProductFromListWithQuantityModalWin.productSelect.getLabel()));
            summ = Double.parseDouble(selectProductFromListWithQuantityModalWin.productSelect.getHelperText());
            configureGrid();
            if (productDto != null) {
                tempInvoiceProductDtoList.add(productDto);
                add(summConfigure());
            }
        });
        add(topButtons(), formToAddCommissionAgent(), grid);
    }

    private Grid<ProductDto> configureGrid() {
        grid.removeAllColumns();
        grid.setItems(tempInvoiceProductDtoList);
        grid.getColumns();
        grid.addColumn(inPrDto -> inPrDto.getDescription()).setHeader("Название");
        grid.addColumn(inPrDto -> inPrDto.getMinimumBalance()).setHeader("Количество");
        grid.addColumn(inPrDto -> inPrDto.getPurchasePrice()).setHeader("Цена");
        return grid;
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
            selectProductFromListWithQuantityModalWin.updateProductList();
            selectProductFromListWithQuantityModalWin.open();
        });
    }

    private VerticalLayout formToAddCommissionAgent() {
        VerticalLayout form = new VerticalLayout();
        form.add(horizontalLayout1(), horizontalLayout2(),
                horizontalLayout3(), horizontalLayout4(), commentConfigure());
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
        Label label2 = new Label("от");
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
        commentConfig.setWidth("400px");
        commentConfig.setPlaceholder("Комментарий");
        horizontal3.add(commentConfig);
        return horizontal3;
    }

    private HorizontalLayout summConfigure() {
        HorizontalLayout horizontal6 = new HorizontalLayout();
        summConfig.setValue(summ.toString());
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
