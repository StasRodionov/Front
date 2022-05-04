package com.trade_accounting.components.goods;

import com.trade_accounting.components.AppView;
import com.trade_accounting.models.dto.company.CompanyDto;
import com.trade_accounting.models.dto.company.PriceListDto;
import com.trade_accounting.services.interfaces.company.CompanyService;
import com.trade_accounting.services.interfaces.company.PriceListService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.trade_accounting.config.SecurityConstants.GOODS_GOODS_PRICE_VIEW;
import static com.trade_accounting.config.SecurityConstants.GOODS_GOODS__PRICE_LIST_EDIT;

@UIScope
@Route(value = GOODS_GOODS__PRICE_LIST_EDIT, layout = AppView.class)
@SpringComponent
public class PriceModalEditWindow extends Dialog {

    private final String fieldWidth = "400px";
    private final String labelWidth = "200px";

    private final CompanyService companyService;
    private final PriceListService priceListService;
    private PriceListDto priceListDto;

    private final ComboBox<CompanyDto> companyComboBox = new ComboBox<>();
    private final DateTimePicker dateTimePicker = new DateTimePicker();
    private final Checkbox checkboxIsSent = new Checkbox();
    private final Checkbox checkboxIsPrint = new Checkbox();
    private final TextField comment = new TextField();
    private final TextField number = new TextField();

    private final Binder<PriceListDto> priceListDtoBinder =
            new Binder<>(PriceListDto.class);
    private PriceListDto priceListDtoToEdit = new PriceListDto();
    private final String TEXT_FOR_REQUEST_FIELD = "Обязательное поле";
    private List<Long> listNumber = new ArrayList<>();

    private String parentLocation = GOODS_GOODS_PRICE_VIEW;
    public PriceModalEditWindow(PriceListService priceListService,
                                CompanyService companyService) {
        this.companyService = companyService;
        this.priceListService = priceListService;
        add(header());
        add(lowerLayout());
        setCloseOnOutsideClick(true);
        setCloseOnEsc(true);

    }
    public void setPriceListEdit(PriceListDto editDto) {
        this.priceListDto = editDto;
        number.setValue(editDto.getNumber());
        dateTimePicker.setValue(LocalDateTime.parse(editDto.getTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        companyComboBox.setValue(companyService.getById(editDto.getCompanyId()));
        checkboxIsSent.setValue(editDto.getSent());
        checkboxIsPrint.setValue(editDto.getPrinted());
        comment.setValue(editDto.getCommentary());
    }


    private HorizontalLayout header() {
        HorizontalLayout header = new HorizontalLayout();
        //System.out.println(priceListDto.getNumber());
        H2 title = new H2("Изменение прайс-листа");
        header.add(getSaveButton(), getCloseButton(), title);
        return header;
    }

    private HorizontalLayout lowerLayout() {
        HorizontalLayout layout = new HorizontalLayout();
        Div div = new Div();
        div.add(
                dateConfigure(),
                companyConfigure(),
                checkboxSentLayout(),
                checkboxPrintLayout(),
                comment()
        );
        layout.add(div);
        return layout;
    }

    private Button getSaveButton() {
        Button saveButton = new Button("Изменить", event -> {

                priceListDto.setCompanyId(companyComboBox.getValue().getId());
                priceListDto.setTime(String.valueOf(dateTimePicker.getValue()));
                priceListDto.setSent(checkboxIsSent.getValue());
                priceListDto.setPrinted(checkboxIsPrint.getValue());
                priceListDto.setCommentary(comment.getValue());
                priceListDto.setNumber(number.getValue());
                if (priceListDtoBinder.validate().isOk()) {
                    priceListService.update(priceListDto);
                    clearAll();
                    close();
                    UI.getCurrent().navigate(parentLocation);
                    priceListService.getAll();
                } else {
                    priceListDtoBinder.validate().notifyBindingValidationStatusHandlers();
                }
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        return saveButton;
    }

    private Button getCloseButton() {
        return new Button("Закрыть", event -> {
            clearAll();
            close();
        });
    }

    private String AddNumber() {
        priceListService.getAll().stream().forEach(i -> listNumber.add(Long.valueOf(i.getNumber())));
        Long number = listNumber.stream().max(Comparator.naturalOrder()).get()+1;
        return String.valueOf(number);
    }

    private Component dateConfigure() {
        Label label = new Label("От");
        label.setWidth(labelWidth);
        dateTimePicker.setWidth(fieldWidth);
        priceListDtoBinder.forField(dateTimePicker)
                .asRequired(TEXT_FOR_REQUEST_FIELD);
        return new HorizontalLayout(label, dateTimePicker);
    }

    private Component comment() {
        Label label = new Label("Комментарий");
        comment.setWidth(fieldWidth);
        label.setWidth(labelWidth);
        return new HorizontalLayout(label, comment);
    }

    private Component checkboxSentLayout() {
        Label label = new Label("Отправлено");
        checkboxIsSent.setWidth(fieldWidth);
        label.setWidth(labelWidth);
        return new HorizontalLayout(label, checkboxIsSent);
    }

    private Component checkboxPrintLayout() {
        Label label = new Label("Напечатано");
        checkboxIsPrint.setWidth(fieldWidth);
        label.setWidth(labelWidth);
        return new HorizontalLayout(label, checkboxIsPrint);
    }

    private HorizontalLayout companyConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        List<CompanyDto> list = companyService.getAll();
        if (list != null) {
            companyComboBox.setItems(list);
        }
        companyComboBox.setItemLabelGenerator(CompanyDto::getName);
        companyComboBox.setWidth(fieldWidth);
        Label label = new Label("Организация");
        companyComboBox.setWidth(fieldWidth);
        label.setWidth(labelWidth);
        horizontalLayout.add(label, companyComboBox);
        priceListDtoBinder.forField(companyComboBox)
                .asRequired(TEXT_FOR_REQUEST_FIELD);
        //
        return horizontalLayout;
    }
    public void setParentLocation(String parentLocation) {
        this.parentLocation = parentLocation;
    }

    void clearAll() {
        companyComboBox.clear();
        dateTimePicker.clear();
        checkboxIsSent.clear();
        checkboxIsPrint.clear();
        comment.clear();
    }
}
