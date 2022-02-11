package com.trade_accounting.components.sells;

import com.trade_accounting.models.dto.company.CompanyDto;
import com.trade_accounting.models.dto.company.ContractDto;
import com.trade_accounting.models.dto.company.ContractorDto;
import com.trade_accounting.models.dto.invoice.InvoiceDto;
import com.trade_accounting.services.interfaces.company.CompanyService;
import com.trade_accounting.services.interfaces.company.ContractService;
import com.trade_accounting.services.interfaces.company.ContractorService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import java.time.LocalDateTime;
import java.util.List;

@UIScope
@SpringComponent
public class CommissionAgentReportModalView extends Dialog {

    private final ContractorService contractorService;
    private final ContractService contractService;
    private final CompanyService companyService;
//    private InvoiceDto invoiceDto;
//    private final InvoiceService invoiceService;
//    private final Notifications notifications;
    private final ComboBox<ContractDto> contractDtoComboBox = new ComboBox<>();
    public  final  ComboBox<ContractorDto> contractorDtoComboBox = new ComboBox<>();
    private final ComboBox<CompanyDto> companyDtoComboBox = new ComboBox<>();
    private final Checkbox isSpend = new Checkbox("Проведено");
    private final Checkbox chooseCommissionAgentReport = new Checkbox("Выданный отчет коммисионера");
    private final TextField textReport = new TextField();
    private final TextField commentConfig = new TextField();
    private final DateTimePicker dateTimePicker = new DateTimePicker();
    private final ComboBox<String> chooseReward = new ComboBox<>();
    private       String datePeriod;
    private final DatePicker dateOn = new DatePicker();
    private final DatePicker dateTo = new DatePicker();


    public CommissionAgentReportModalView(ContractorService contractorService,
                                          ContractService contractService,
                                          CompanyService companyService) {
        this.contractorService = contractorService;
        this.contractService = contractService;
        this.companyService = companyService;

        setSizeFull();
        add(topButtons(), formToAddCommissionAgent());
    }

    private HorizontalLayout topButtons() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        setSizeFull();
        horizontalLayout.add(save(), closeButton(), addProductButton(), chooseCommissionAgentReport);
        return horizontalLayout;
    }



    private Button save() {
        Button button = new Button("Сохранить");
        button.addClickListener(e -> {
            close();
//            updateSupplier();
//            notifications.infoNotification(String.format(" № %s сохранен", invoiceDto.getId()));

        });
        return button;
    }

    public void setReturnEdit(InvoiceDto editDto) {
        if (editDto.getId() != null) {
            textReport.setValue(editDto.getId().toString());
        }

        if (editDto.getContractorId() != null) {
            contractorDtoComboBox.setValue(contractorService.getById(editDto.getContractorId()));
        }
        if (editDto.getComment() != null) {
            commentConfig.setValue(editDto.getComment());
        }
        if (editDto.getDate() != null) {
            dateTimePicker.setValue(LocalDateTime.parse(editDto.getDate()));
        }
        if (editDto.getCompanyId() != null) {
            companyDtoComboBox.setValue(companyService.getById(editDto.getCompanyId()));
        }
    }
    private Button closeButton() {
        Button button = new Button("Закрыть", new Icon(VaadinIcon.CLOSE));
        button.addClickListener(e -> {
            close();
        });
        return button;

    }

    private Button addProductButton() {
        return new Button("Добавить из справочника", new Icon(VaadinIcon.PLUS_CIRCLE), e -> {

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
        hLayout.add(dataConfigure(), isSpend);
        return hLayout;
    }

    private HorizontalLayout dataConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Полученный отчет комиссионера №");
        label.setWidth("150px");
        textReport.setWidth("50px");
        Label label2 = new Label("от");
        dateTimePicker.setWidth("350px");
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
        Label label = new Label("Компания");
        label.setWidth("100px");
        horizontalLayout.add(label,companyDtoComboBox);
        return horizontalLayout;
    }

    private HorizontalLayout horizontalLayout3() {
        HorizontalLayout hLay3 = new HorizontalLayout();
        hLay3.add(contractorConfigure(), contractConfigure());
        return hLay3;
    }
//    private HorizontalLayout horizontalLayout5() {
//        HorizontalLayout hLay5 = new HorizontalLayout();
//        hLay5.add(invoicesReceivedConfigure());
//        return hLay5;
//    }

    private HorizontalLayout contractorConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        List<ContractorDto> contractorDtos = contractorService.getAll();
        if(contractorDtos != null) {
            contractorDtoComboBox.setItems(contractorDtos);
        }
        contractorDtoComboBox.setItemLabelGenerator(ContractorDto::getName);
        contractorDtoComboBox.setWidth("350px");
        Label label = new Label("Контрагент");
        label.setWidth("100px");
        horizontalLayout.add(label, contractorDtoComboBox);
        return horizontalLayout;
    }
//    private HorizontalLayout invoicesReceivedConfigure() {
//        HorizontalLayout horizontalLayout = new HorizontalLayout();
//        List<ContractorDto> contractorDtos = contractorService.getAll();
//        if(contractorDtos != null) {
//            contractorDtoComboBox.setItems(contractorDtos);
//        }
//        contractorDtoComboBox.setItemLabelGenerator(ContractorDto::getName);
//        contractorDtoComboBox.setWidth("350px");
//        Label label = new Label("Счет-фактура");
//        label.setWidth("100px");
//        horizontalLayout.add(label, contractorDtoComboBox);
//        return horizontalLayout;
//    }

    private HorizontalLayout contractConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        List<ContractDto> contractDtos = contractService.getAll();
        if(contractDtos !=null) {
            contractDtoComboBox.setItems(contractDtos);
        }
        contractDtoComboBox.setItemLabelGenerator(ContractDto::getNumber);
        contractDtoComboBox.setWidth("350px");
        Label label = new Label("Договор");
        label.setWidth("100px");
        horizontalLayout.add(label,contractDtoComboBox);
        return horizontalLayout;
    }

    private HorizontalLayout horizontalLayout4() {
        HorizontalLayout hLayout4 = new HorizontalLayout();
        hLayout4.add(incomingConfigure(), configureAward());
        return hLayout4;
    }

    private HorizontalLayout commentConfigure() {
        HorizontalLayout horizontal3 = new HorizontalLayout();
        commentConfig.setWidth("500px");
        commentConfig.setHeight("300px");
        commentConfig.setPlaceholder("Комментарий");
        horizontal3.add(commentConfig);
        return horizontal3;
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
        chooseReward.setItems("Не рассчитывать","Процент от суммы продажи");
        chooseReward.setValue("Не рассчитывать");
        Label label = new Label("Вознаграждение");
        horizontalLayout.add(label,chooseReward);
        return horizontalLayout;
    }
//    private void updateSupplier() {
//        invoiceDto.setId(Long.parseLong(textReport.getValue()));
//        invoiceDto.setDate(dateTimePicker.getValue().toString());
////        buyersReturnDto.setWarehouseId(warehouseDtoComboBox.getValue().getId());
//       invoiceDto.setCompanyId(companyDtoComboBox.getValue().getId());
//       invoiceDto.setContractorId(contractorDtoComboBox.getValue().getId());
//        invoiceDto.setComment(commentConfig.getValue());
//
////        buyersReturnDto.setSum(new BigDecimal(summConfig.getValue()));
//        invoiceDto.setIsSent(isSpend.getValue());
////        buyersReturnDto.setIsPrint(checkboxIsPrint.getValue());
//        if (Boolean.TRUE.equals(invoiceDto.getIsSpend())){
//            invoiceService.create(invoiceDto);
//        } else {
//            invoiceService.update(invoiceDto);
//        }
////        UI.getCurrent().navigate("sells");
//
//        close();
//    }
}
