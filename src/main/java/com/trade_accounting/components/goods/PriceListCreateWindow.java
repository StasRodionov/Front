package com.trade_accounting.components.goods;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.Buttons;
import com.trade_accounting.models.dto.company.CompanyDto;
import com.trade_accounting.models.dto.company.PriceListDto;
import com.trade_accounting.models.dto.company.PriceListProductPercentsDto;
import com.trade_accounting.models.dto.company.TypeOfPriceDto;
import com.trade_accounting.services.interfaces.company.CompanyService;
import com.trade_accounting.services.interfaces.company.PriceListProductPercentsService;
import com.trade_accounting.services.interfaces.company.PriceListService;
import com.trade_accounting.services.interfaces.company.TypeOfPriceService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.trade_accounting.config.SecurityConstants.*;

@SpringComponent
@Route(value = GOODS_GOODS__PRICE_LIST_CREATE, layout = AppView.class)
@UIScope
@Slf4j
public class PriceListCreateWindow extends VerticalLayout implements AfterNavigationObserver {
    private final TypeOfPriceService typeOfPriceService;
    private final PriceListService priceListService;
    private final CompanyService companyService;
    private final Binder<PriceListDto> priceListDtoBinder = new Binder<>(PriceListDto.class);
    private final Binder<PriceListProductPercentsDto> priceListProductPercentsDtoBinder = new Binder<>(PriceListProductPercentsDto.class);

    private final ComboBox<TypeOfPriceDto> typeOfPriceComboBox = new ComboBox<>();
    private final ComboBox<CompanyDto> companyComboBox = new ComboBox<>();
    private final TextField name = new TextField();
    private TextField column = new TextField();
    private BigDecimalField percent = new BigDecimalField();

    private VerticalLayout firstLayout = new VerticalLayout();
    private final String TEXT_FOR_REQUEST_FIELD = "Обязательное поле";
    private String priceListNumber = "Прайс-лист от " + LocalDate.now();
    private Button resume = new Button("Продолжить");
    private Button cancel = new Button("Отмена");
    private AtomicInteger count = new AtomicInteger(0);
    private final PriceListProductPercentsService priceListProductPercentsService;
    private GoodsPriceLayoutPriceListView view;
    private List<TypeOfPriceDto> list;
    private Checkbox checkboxPercent = new Checkbox();
    private List<CompanyDto> listCompany;

    //    private List<PriceListProductPricesDto> priceListProductPricesDtos = new ArrayList<>();


    public PriceListCreateWindow(TypeOfPriceService typeOfPriceService,
                                 PriceListService priceListService,
                                 CompanyService companyService,
                                 PriceListProductPercentsService priceListProductPercentsService,
                                 GoodsPriceLayoutPriceListView view) {
        this.typeOfPriceService = typeOfPriceService;
        this.priceListService = priceListService;
        this.companyService = companyService;
        this.priceListProductPercentsService = priceListProductPercentsService;
        this.view = view;
        percent.setEnabled(false);
        add(lowerLayout());
        setHorizontalComponentAlignment(Alignment.CENTER);
    }

    private VerticalLayout lowerLayout() {
        firstLayout.setSpacing(true);
        firstLayout.add(buttonsLayout(),
                name(),
                typeOfPriceConfigure(),
                companyConfigure(),
//                createColumn(),
                column());
        return firstLayout;
    }

    private Component column() {
        HorizontalLayout layout = new HorizontalLayout();
        Label label = new Label("Колонка1");
        Button questionButton2 = Buttons.buttonQuestion("Если флажок снят, то цена в колонке установлена не будет. " +
                "Если Флажок установлен и в поле стоит 0, то будет взята цена из карточки. " +
                "Положительное значение в поле — наценка, отрицательное — скидка. " +
                "В дальнейшем все цены можно будет изменить вручную.");

//        Раскомментировать при добавлении функционала динамического добавления колонок
//        TextField column = new TextField();
//        BigDecimalField percent = new BigDecimalField();
//        Button delete = new Button(new Icon(VaadinIcon.TRASH));
//        delete.addClickListener(event -> layout.removeAll());
        priceListDtoBinder.forField(name).asRequired(TEXT_FOR_REQUEST_FIELD).bind("number");
        priceListDtoBinder.forField(companyComboBox).asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(PriceListDto::getCompanyDtoValid, PriceListDto::setCompanyDtoValid);
        priceListProductPercentsDtoBinder.forField(column).asRequired(TEXT_FOR_REQUEST_FIELD).bind("name");
        priceListProductPercentsDtoBinder.forField(typeOfPriceComboBox).asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(PriceListProductPercentsDto::getTypeOfPriceDtoValid, PriceListProductPercentsDto::setTypeOfPriceDtoValid);
        column.setWidth("300px");
        percent.setWidth("70px");
        label.setWidth("70px");
        layout.add(label, column, questionButton2, checkboxPercent(), percent/*, delete*/);
        layout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        return layout;
    }

    private Component checkboxPercent() {
        Label label = new Label("Скидка (-) или наценка (+), % от цены продажи");
        checkboxPercent.setWidth("50px");
        checkboxPercent.setAutofocus(true);
        label.setWidth("350px");
        checkboxPercent.addValueChangeListener(event -> percent.setEnabled(checkboxPercent.getValue().equals(true)));
        return new HorizontalLayout(checkboxPercent, label);
    }

    private HorizontalLayout buttonsLayout() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setSpacing(true);
        layout.add(resume(), cancel());
        return layout;
    }

    private Button resume() {
        resume.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        resume.addClickListener(event -> {
            priceListDtoBinder.setValidatorsDisabled(false);
            priceListProductPercentsDtoBinder.setValidatorsDisabled(false);
            if (!priceListDtoBinder.validate().isOk() || !priceListProductPercentsDtoBinder.validate().isOk()) {
                priceListDtoBinder.validate().notifyBindingValidationStatusHandlers();
                priceListProductPercentsDtoBinder.validate().notifyBindingValidationStatusHandlers();
            } else {
                PriceListDto priceListDto = new PriceListDto();
                priceListDto.setNumber(name.getValue());
                priceListDto.setTypeOfPriceId(typeOfPriceComboBox.getValue().getId());
                priceListDto.setCompanyId(companyComboBox.getValue().getId());
                priceListDto.setTime(LocalDateTime.now().toString().substring(0, 16));
                priceListDto.setIsSpend(true);
                priceListDto.setIsRecyclebin(false);
                priceListDto.setPrinted(false);
                priceListDto.setSent(false);
                priceListDto.setId(priceListService.create(priceListDto).getId());
                PriceListProductPercentsDto priceListProductPercentsDto = new PriceListProductPercentsDto();
                priceListProductPercentsDto.setName(column.getValue());
                if (percent.getValue() == null) {
                    priceListProductPercentsDto.setPercent(BigDecimal.ZERO);
                } else {
                    priceListProductPercentsDto.setPercent(percent.getValue());
                }
                priceListProductPercentsDto.setPriceListId(priceListDto.getId());
                priceListProductPercentsDto.setId(priceListProductPercentsService.create(priceListProductPercentsDto).getId());
                view.setPriceListForCreate(priceListDto, priceListProductPercentsDto);
                priceListDtoBinder.setValidatorsDisabled(true);
                priceListProductPercentsDtoBinder.setValidatorsDisabled(true);
                UI.getCurrent().navigate(GOODS_GOODS__PRICE_LIST_EDIT);
            }
        });

        return resume;
    }

    private Button cancel() {
        cancel.addClickListener(event -> {
            if (count.get() != 0) {
                count.decrementAndGet();
            }
            priceListDtoBinder.setValidatorsDisabled(true);
            priceListProductPercentsDtoBinder.setValidatorsDisabled(true);
            UI.getCurrent().navigate(GOODS_GOODS_PRICE_VIEW);
        });
        return cancel;
    }

    private Component name() {
        Label label = new Label("Название прайс-листа");
        label.setWidth("200px");
        name.setWidth("300px");
        if (checkNumber()) {
            name.setValue(priceListNumber + " (" + count.incrementAndGet() + ")");
        } else {
            name.setValue(priceListNumber);
            count.set(0);
        }
        return new HorizontalLayout(label, name);
    }

    private boolean checkNumber() {
        return priceListService.getAll().stream()
                .anyMatch(i -> i.getNumber().startsWith(priceListNumber));
    }

//    Раскомментировать при добавлении функционала динамического добавления колонок
//    private Button createColumn() {
//        Button edit = new Button(new Icon(VaadinIcon.FILE_TEXT_O));
//        edit.addClickListener(event -> firstLayout.add(column()));
//        return edit;
//    }

    private HorizontalLayout typeOfPriceConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Button questionButton1 = Buttons.buttonQuestion("На основании каких цен будет формироваться прайс-лист. " +
                "Цены берутся из карточки товара.");
        questionButton1.setWidth("100px");
        list = typeOfPriceService.getAll();
        if (list != null) {
            typeOfPriceComboBox.setItems(list);
            typeOfPriceComboBox.setValue(list.get(0));
        }
        typeOfPriceComboBox.setItemLabelGenerator(TypeOfPriceDto::getName);
        typeOfPriceComboBox.setWidth("300px");
        Label label = new Label("Тип Цены");
        label.setWidth("85px");
        horizontalLayout.add(questionButton1, label, typeOfPriceComboBox);
        priceListDtoBinder.forField(typeOfPriceComboBox)
                .asRequired(TEXT_FOR_REQUEST_FIELD);
        return horizontalLayout;
    }

    private HorizontalLayout companyConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        listCompany = companyService.getAll();
        if (listCompany != null) {
            companyComboBox.setItems(listCompany);
        }
        companyComboBox.setItemLabelGenerator(CompanyDto::getName);
        companyComboBox.setWidth("300px");
        Label label = new Label("Организация");
        label.setWidth("200px");
        horizontalLayout.add(label, companyComboBox);
        priceListDtoBinder.forField(companyComboBox)
                .asRequired(TEXT_FOR_REQUEST_FIELD);
        return horizontalLayout;
    }


    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        clearForm();
        if (checkNumber()) {
            name.setValue(priceListNumber + " (" + count.incrementAndGet() + ")");
        } else {
            name.setValue(priceListNumber);
        }
        if (list != null) {
            typeOfPriceComboBox.setItems(list);
            typeOfPriceComboBox.setValue(list.get(0));
        }
    }

    private void clearForm() {
        typeOfPriceComboBox.clear();
        companyComboBox.clear();
        name.clear();
        column.clear();
        percent.clear();
        checkboxPercent.clear();
    }
}
