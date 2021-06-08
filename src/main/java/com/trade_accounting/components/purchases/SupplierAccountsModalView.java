package com.trade_accounting.components.purchases;

import com.trade_accounting.components.sells.SalesChooseGoodsModalWin;
import com.trade_accounting.models.dto.CompanyDto;
import com.trade_accounting.models.dto.ContractDto;
import com.trade_accounting.models.dto.ContractorDto;
import com.trade_accounting.models.dto.SupplierAccountsDto;
import com.trade_accounting.models.dto.WarehouseDto;
import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.ContractorService;
import com.trade_accounting.services.interfaces.SupplierAccountService;
import com.trade_accounting.services.interfaces.WarehouseService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import java.util.List;


@SpringComponent
@UIScope
public class SupplierAccountsModalView extends Dialog {

    private final SupplierAccountService supplierAccountService;
    private final CompanyService companyService;
    private final WarehouseService warehouseService;
    private final ContractorService contractorService;
    private SupplierAccountsDto supplierAccountsDto;
    private final SalesChooseGoodsModalWin salesChooseGoodsModalWin;

    private final ComboBox<CompanyDto> companyDtoComboBox = new ComboBox<>();
    private final ComboBox<WarehouseDto> warehouseDtoComboBox = new ComboBox<>();
    private final ComboBox<ContractDto> contractDtoComboBox = new ComboBox<>();
    public final  ComboBox<ContractorDto> contractorSelect = new ComboBox<>();
    private final Binder<SupplierAccountsDto> binder = new Binder<>(SupplierAccountsDto.class);
    private final DateTimePicker dateTimePicker = new DateTimePicker();
    private final Checkbox isSpend = new Checkbox("Проведено");


    public SupplierAccountsModalView(SupplierAccountService supplierAccountService,
                                     CompanyService companyService,
                                     WarehouseService warehouseService,
                                     ContractorService contractorService, SalesChooseGoodsModalWin salesChooseGoodsModalWin) {
        this.supplierAccountService = supplierAccountService;
        this.companyService = companyService;
        this.warehouseService = warehouseService;
        this.contractorService = contractorService;
        this.salesChooseGoodsModalWin = salesChooseGoodsModalWin;
        setSizeFull();
        add(upperButtonInModalView(),formToAddSupplerAccount());
    }


    public void setSupplierAccountsForEdit(SupplierAccountsDto editSupplierAccounts) {
        this.supplierAccountsDto = editSupplierAccounts;
        companyDtoComboBox.setValue(supplierAccountsDto.getCompanyDto());
        warehouseDtoComboBox.setValue(supplierAccountsDto.getWarehouseDto());
        contractDtoComboBox.setValue(supplierAccountsDto.getContractDto());
    }

    private HorizontalLayout upperButtonInModalView() {
        HorizontalLayout upperButton = new HorizontalLayout();
        upperButton.add(title(),saveButton(),closeButton(),addProductButton());
        upperButton.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        return upperButton;
    }

    private H2 title() {
        final H2 label = new H2("Добавление счета");
        label.setHeight("2.2em");
        return label;
    }

    private Button saveButton() {
        return new Button("Сохранить");
    }

    private Button closeButton() {
        return new Button("Закрыть", new Icon(VaadinIcon.CLOSE));
    }

    private Button addProductButton() {
        return new Button("Добавить из справочника", new Icon(VaadinIcon.PLUS_CIRCLE), e -> {
            salesChooseGoodsModalWin.updateProductList();
            salesChooseGoodsModalWin.open();
        });
    }

    private VerticalLayout formToAddSupplerAccount() {
        VerticalLayout form = new VerticalLayout();
        form.add(horizontalLayout1(), horizontalLayout2());
        return form;
    }

    private HorizontalLayout horizontalLayout1() {
        HorizontalLayout hLay1 = new HorizontalLayout();
        hLay1.add(dataConfigure(), isSpend);
        return  hLay1;
    }

    private HorizontalLayout horizontalLayout2() {
        HorizontalLayout hLay2 = new HorizontalLayout();
        hLay2.add(companyConfigure(),contractorConfigure());
        return hLay2;
    }

    private HorizontalLayout dataConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Счет поставщика №");
        label.setWidth("150px");
        TextField text = new TextField();
        text.setWidth("50px");
        Label label2 = new Label("от");
        dateTimePicker.setWidth("350px");
        horizontalLayout.add(label,text,label2,dateTimePicker);
        return horizontalLayout;
    }

    private HorizontalLayout contractorConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        List<ContractorDto> contractorDtos = contractorService.getAll();
        contractorSelect.setItemLabelGenerator(ContractorDto::getName);
        contractorSelect.setWidth("350px");
        Label label = new Label("Контрагент");
        label.setWidth("100px");
        horizontalLayout.add(label,contractorSelect);
        return horizontalLayout;
    }

    private HorizontalLayout companyConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        List<CompanyDto> companyDtos = companyService.getAll();
        companyDtoComboBox.setItemLabelGenerator(CompanyDto::getName);
        companyDtoComboBox.setWidth("350px");
        Label label = new Label("Компания");
        label.setWidth("100px");
        horizontalLayout.add(label,companyDtoComboBox);
        return horizontalLayout;
    }

}
