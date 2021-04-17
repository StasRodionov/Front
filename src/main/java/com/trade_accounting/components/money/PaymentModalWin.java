package com.trade_accounting.components.money;

import com.trade_accounting.models.dto.CompanyDto;
import com.trade_accounting.models.dto.ContractDto;
import com.trade_accounting.models.dto.ContractorDto;
import com.trade_accounting.models.dto.PaymentDto;
import com.trade_accounting.models.dto.ProjectDto;
import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.ContractService;
import com.trade_accounting.services.interfaces.ContractorService;
import com.trade_accounting.services.interfaces.PaymentService;
import com.trade_accounting.services.interfaces.ProjectService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringComponent
@UIScope
@Slf4j

public class PaymentModalWin extends Dialog {
    private final PaymentService paymentService;
    private final CompanyService companyService;
    private final ContractorService contractorService;
    private final ProjectService projectService;
    private final ContractService contractService;
    private final DateTimePicker dateField = new DateTimePicker();
    private final ComboBox <String> typeofPaymentBox = new ComboBox<>();
    private final ComboBox <CompanyDto> companyDtoComboBox = new ComboBox<>();
    private final ComboBox <ContractorDto> contractorDtoComboBox = new ComboBox<>();
    private final ComboBox <ContractDto> contractDtoComboBox = new ComboBox<>();
    private final ComboBox <ProjectDto> projectDtoComboBox = new ComboBox<>();
    private final TextField payNumber = new TextField();
    private final BigDecimalField sum = new BigDecimalField();
//    Div div = new Div();
//    private final PaymentDto paymentDto =getPaymentdto();

    @Autowired

    public PaymentModalWin(PaymentService paymentService,
                           CompanyService companyService,
                           ContractorService contractorService,
                           ProjectService projectService,
                           ContractService contractService) {
        this.paymentService = paymentService;
        this.companyService = companyService;
        this.contractorService = contractorService;
        this.projectService = projectService;
        this.contractService = contractService;


        typeofPaymentBox.setItems("Входящий","Исходящий");
        companyDtoComboBox.setItems(companyService.getAll());
        companyDtoComboBox.setItemLabelGenerator(CompanyDto::getName);
        contractDtoComboBox.setItems(contractService.getAll());
        contractDtoComboBox.setItemLabelGenerator(ContractDto::getNumber);
        contractorDtoComboBox.setItems(contractorService.getAll());
        contractorDtoComboBox.setItemLabelGenerator(ContractorDto::getName);
        projectDtoComboBox.setItems(projectService.getAll());
        projectDtoComboBox.setItemLabelGenerator(ProjectDto:: getName);
//        div.add(getHorizontalLayout("Дата", dateField),
//                getHorizontalLayout("Компания", companyDtoComboBox),
//                getHorizontalLayout("Номер платежа",payNumber),
//                getHorizontalLayout("Тип платежа", typeofPaymentBox),
//                getHorizontalLayout("Контрагент", contractorDtoComboBox),
//                getHorizontalLayout("Проект", projectDtoComboBox));
        setCloseOnOutsideClick(true);
        setCloseOnEsc(true);
        add(getHeader());
        add(getHorizontalLayout("Дата", dateField));
        add(getHorizontalLayout("Компания", companyDtoComboBox));
        add(getHorizontalLayout("Номер платежа",payNumber));
        add(getHorizontalLayout("Тип платежа", typeofPaymentBox));
        add(getHorizontalLayout("Контрагент", contractorDtoComboBox));
        add(getHorizontalLayout("Договор", contractDtoComboBox));
        add(getHorizontalLayout("Проект", projectDtoComboBox));
        add(getHorizontalLayout("Сумма", sum));
//        getPaymentdto();
        add(getFooter());




    }
    private Component getHeader() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        H2 title = new H2("Добавление платежа");
        title.setHeight("1.5em");
        title.setWidth("345px");
        horizontalLayout.add(title);
//        VerticalLayout verticalLayout = new VerticalLayout();
//        verticalLayout.setPadding(false);
////        verticalLayout.add(getImageButton(), imageHorizontalLayout);
//        horizontalLayout.add(title, verticalLayout);
        horizontalLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.END);
        return horizontalLayout;
    }
    private <T extends Component & HasSize> HorizontalLayout getHorizontalLayout(String labelText, T field) {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label(labelText);
        field.setWidth("400px");
        label.setWidth("200px");
        horizontalLayout.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER, label);
        horizontalLayout.add(label, field);
        return horizontalLayout;
    }
//    private PaymentDto getPaymentdto() {
//        PaymentDto paymentDto = new PaymentDto();
//        paymentDto.setTime(LocalDateTime.now().toString());
//        paymentDto.setCompanyDto(companyService.getById(1L));
//        paymentDto.setContractorDto(contractorService.getById(1L));
//        paymentDto.setContractDto(contractService.getById(1L));
//        paymentDto.setProjectDto(projectService.getById(1L));
//        paymentDto.setNumber("00010");

    //        paymentDto.setTypeOfPayment(typeofPaymentBox.getValue());
//        return paymentDto;
//    }
    private PaymentDto updatePaymentDto() {
        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setTime(dateField.getValue().toString());
        paymentDto.setCompanyDto(companyDtoComboBox.getValue());
        paymentDto.setContractorDto(contractorDtoComboBox.getValue());
        paymentDto.setContractDto(contractDtoComboBox.getValue());
        paymentDto.setProjectDto(projectDtoComboBox.getValue());
        paymentDto.setNumber(payNumber.getValue());
        paymentDto.setSum(sum.getValue());

        if (typeofPaymentBox.getValue().equals("Входящий")) {
            paymentDto.setTypeOfPayment("INCOMING");

        }
        else {
            paymentDto.setTypeOfPayment("OUTGOING");
        };
        System.out.println(typeofPaymentBox.getValue());
        System.out.println(paymentDto.getTypeOfPayment());
        return paymentDto;
    }
    private void reset() {
        dateField.clear();
        companyDtoComboBox.clear();
        contractorDtoComboBox.clear();
        contractDtoComboBox.clear();
        projectDtoComboBox.clear();
        payNumber.clear();
        typeofPaymentBox.clear();
    }
    private Button getSaveButton() {
        return new Button("Сохранить", new Icon(VaadinIcon.PLUS_CIRCLE), event ->{
//                updatePaymentDto();
            paymentService.create(updatePaymentDto());
//                div.removeAll();
            reset();
            UI.getCurrent().navigate("money");
            close();});
    }
    private Button getCancelButton() {
        return new Button("Отмена",new Icon(VaadinIcon.CLOSE), event ->{
            reset();
            close();
        });
    }

    private Component getFooter() {
        HorizontalLayout footer = new HorizontalLayout();
        VerticalLayout saveverticalLayout = new VerticalLayout();
        VerticalLayout cancelverticalLayout = new VerticalLayout();
        saveverticalLayout.add(getSaveButton());
        saveverticalLayout.setWidth("550px");
        saveverticalLayout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.END);
        cancelverticalLayout.add(getCancelButton());
        cancelverticalLayout.setWidth("100px");
        cancelverticalLayout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.END);
        footer.add(saveverticalLayout, cancelverticalLayout);
        footer.setHeight("100px");
        footer.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.END);
        return footer;
    }

}
