package com.trade_accounting.components.goods;

import com.trade_accounting.components.AppView;
import com.trade_accounting.models.dto.RevenueDto;
import com.trade_accounting.services.interfaces.RevenueService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Route(value = "goods/revenue-edit", layout = AppView.class)
@PageTitle("Оборот")
@PreserveOnRefresh
@SpringComponent
@UIScope
public class RevenueModalWindow extends Dialog {
    private final String fieldWidth = "400px";
    private final String labelWidth = "200px";

    private static final TextField description = new TextField();
    private static final NumberField productId = new NumberField();
    private final NumberField unitShortName = new NumberField();
    private final NumberField startOfPeriodAmount = new NumberField();
    private final NumberField startOfPeriodSumOfPrice = new NumberField();
    private final NumberField comingAmount = new NumberField();
    private final NumberField comingSumOfPrice = new NumberField();
    private final NumberField spendingAmount = new NumberField();
    private final NumberField spendingSumOfPrice = new NumberField();
    private final NumberField endOfPeriodAmount = new NumberField();
    private final NumberField endOfPeriodSumOfPrice = new NumberField();
    private final RevenueService revenueService;
    private RevenueDto revenueDtoToEdit = new RevenueDto();
    private Binder<RevenueDto> revenueDtoBinder = new Binder<>(RevenueDto.class);

    public RevenueModalWindow(RevenueService revenueService) {
        this.revenueService = revenueService;
        add(header());
        add(lowerLayout());
        setCloseOnOutsideClick(true);
        setCloseOnEsc(true);
    }

    private HorizontalLayout header() {
        HorizontalLayout header = new HorizontalLayout();
        H2 title = new H2("Обороты");
        header.add(getSaveButton(), getCloseButton(), title);
        return header;
    }

    private HorizontalLayout lowerLayout() {
        HorizontalLayout layout = new HorizontalLayout();
        Div div = new Div();
        div.add(
                addName(),
                addProductId(),
                addUnitShortName(),
                addStartOfPeriodAmount(),
                addStartOfPeriodSumOfPrice(),
                addComingAmount(),
                addComingSumOfPrice(),
                addSpendingAmount(),
                addSpendingSumOfPrice(),
                addEndOfPeriodAmount() ,
                addEndOfPeriodSumOfPrice()
        );
        layout.add(div);
        return layout;
    }

    private Component addName() {
        Label label = new Label("Наименование");
        label.setWidth(labelWidth);
        return new HorizontalLayout(label, description);
    }

    private Component addProductId() {
        Label label = new Label("Код товара");
        label.setWidth(labelWidth);
        return new HorizontalLayout(label, productId);
    }

    private Component addUnitShortName() {
        Label label = new Label("Единица измерения");
        label.setWidth(labelWidth);
        return new HorizontalLayout(label, unitShortName);
    }

    private Component addStartOfPeriodAmount() {
        Label label = new Label("Количество на начало периода ");
        label.setWidth(labelWidth);
        return new HorizontalLayout(label, startOfPeriodAmount);
    }

    private Component addStartOfPeriodSumOfPrice() {
        Label label = new Label("Сумма на начало периода");
        label.setWidth(labelWidth);
        return new HorizontalLayout(label, startOfPeriodSumOfPrice);
    }

    private Component addComingAmount() {
        Label label = new Label("Количество(приход)");
        label.setWidth(labelWidth);
        return new HorizontalLayout(label, comingAmount);
    }

    private Component addComingSumOfPrice() {
        Label label = new Label("Сумма(приход)");
        label.setWidth(labelWidth);
        return new HorizontalLayout(label, comingSumOfPrice);
    }

    private Component addSpendingAmount() {
        Label label = new Label("Количество(расход)");
        label.setWidth(labelWidth);
        return new HorizontalLayout(label, spendingAmount);
    }

    private Component addSpendingSumOfPrice() {
        Label label = new Label("Сумма(расход)");
        label.setWidth(labelWidth);
        return new HorizontalLayout(label, spendingSumOfPrice);
    }

    private Component addEndOfPeriodAmount() {
        Label label = new Label("Кол-во на конец периода");
        label.setWidth(labelWidth);
        return new HorizontalLayout(label, endOfPeriodAmount);
    }

    private Component addEndOfPeriodSumOfPrice() {
        Label label = new Label("Кол-во на конец периода");
        label.setWidth(labelWidth);
        return new HorizontalLayout(label, endOfPeriodSumOfPrice);
    }

    private Button getSaveButton() {
        Button saveButton = new Button("Сохранить", event -> {
            if (revenueDtoToEdit.getId() != null) {
                revenueDtoToEdit.setDescription(description.getValue());
                revenueDtoToEdit.setItemNumber(productId.getValue().intValue());
                revenueDtoToEdit.setUnitShortName(unitShortName.getValue().toString());
                revenueDtoToEdit.setStartOfPeriodAmount(startOfPeriodAmount.getValue().intValue());
                revenueDtoToEdit.setStartOfPeriodSumOfPrice(startOfPeriodSumOfPrice.getValue().intValue());
                revenueDtoToEdit.setComingAmount(comingAmount.getValue().intValue());
                revenueDtoToEdit.setComingSumOfPrice(comingSumOfPrice.getValue().intValue());
                revenueDtoToEdit.setSpendingAmount(spendingAmount.getValue().intValue());
                revenueDtoToEdit.setSpendingSumOfPrice(spendingSumOfPrice.getValue().intValue());
                revenueDtoToEdit.setEndOfPeriodAmount(endOfPeriodAmount.getValue().intValue());
                revenueDtoToEdit.setEndOfPeriodSumOfPrice(endOfPeriodSumOfPrice.getValue().intValue());

                if (revenueDtoBinder.validate().isOk()) {
                    revenueService.update(revenueDtoToEdit);
                    clearAll();
                    close();
                } else {
                    revenueDtoBinder.validate().notifyBindingValidationStatusHandlers();
                }
            } else {
                RevenueDto revenueDto = new RevenueDto();
                revenueDtoToEdit.setDescription(description.getValue());
                revenueDtoToEdit.setItemNumber(productId.getValue().intValue());
                revenueDtoToEdit.setUnitShortName(unitShortName.getValue().toString());
                revenueDtoToEdit.setStartOfPeriodAmount(startOfPeriodAmount.getValue().intValue());
                revenueDtoToEdit.setStartOfPeriodSumOfPrice(startOfPeriodSumOfPrice.getValue().intValue());
                revenueDtoToEdit.setComingAmount(comingAmount.getValue().intValue());
                revenueDtoToEdit.setComingSumOfPrice(comingSumOfPrice.getValue().intValue());
                revenueDtoToEdit.setSpendingAmount(spendingAmount.getValue().intValue());
                revenueDtoToEdit.setSpendingSumOfPrice(spendingSumOfPrice.getValue().intValue());
                revenueDtoToEdit.setEndOfPeriodAmount(endOfPeriodAmount.getValue().intValue());
                revenueDtoToEdit.setEndOfPeriodSumOfPrice(endOfPeriodSumOfPrice.getValue().intValue());
                if (revenueDtoBinder.validate().isOk()) {
                    revenueService.create(revenueDto);
                    clearAll();
                    close();
                } else {
                    revenueDtoBinder.validate().notifyBindingValidationStatusHandlers();
                }
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

    public void clearAll() {
        description.clear();
        productId.clear();
        unitShortName.clear();
        startOfPeriodAmount.clear();
        startOfPeriodSumOfPrice.clear();
        comingAmount.clear();
        comingSumOfPrice.clear();
        spendingAmount.clear();
        spendingSumOfPrice.clear();
        endOfPeriodAmount.clear();
        endOfPeriodSumOfPrice.clear();
    }
}
