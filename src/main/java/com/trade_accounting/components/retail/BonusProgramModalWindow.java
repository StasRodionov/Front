package com.trade_accounting.components.retail;

import com.trade_accounting.models.dto.BonusProgramDto;
import com.trade_accounting.models.dto.ContractorGroupDto;
import com.trade_accounting.services.interfaces.BonusProgramService;
import com.trade_accounting.services.interfaces.ContractorGroupService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.listbox.MultiSelectListBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ErrorLevel;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class BonusProgramModalWindow extends Dialog {

    private final String fieldWidth = "400px";
    private final String labelWidth = "200px";

    private final TextField name = new TextField();
    private final Checkbox isActive = new Checkbox();
    private final Checkbox allContractors = new Checkbox();
    private final MultiSelectListBox<ContractorGroupDto> contractorGroup = new MultiSelectListBox<>();
    private final MultiSelectListBox<String> contractorGroupName = new MultiSelectListBox<>();
    private final TextField accrualRule = new TextField();
    private final TextField writeOffRules = new TextField();
    private final TextField maxPaymentPercentage = new TextField();
    private final TextField numberOfDays = new TextField();
    private final Checkbox welcomePoints = new Checkbox();
    private final TextField numberOfPoints = new TextField();
    private final Checkbox registrationInBonusProgram = new Checkbox();
    private final Checkbox firstPurchase = new Checkbox();

    private final Select<String> defaultTaxationSystem = new Select<>();
    private final Select<String> orderTaxationSystem = new Select<>();
    //private final MultiSelectListBox<EmployeeDto> cashiers = new MultiSelectListBox<>();

    private final BonusProgramService bonusProgramService;
    private final ContractorGroupService contractorGroupService;

    private BonusProgramDto bonusProgramDtoToEdit = new BonusProgramDto();
    private Binder<BonusProgramDto> bonusProgramDtoBinder = new Binder<>(BonusProgramDto.class);

    public BonusProgramModalWindow(BonusProgramService bonusProgramService, ContractorGroupService contractorGroupService) {
        this.bonusProgramService = bonusProgramService;
        this.contractorGroupService = contractorGroupService;

        add(header());
        add(lowerLayout());
        setCloseOnOutsideClick(true);
        setCloseOnEsc(true);
    }

    private HorizontalLayout header() {
        HorizontalLayout header = new HorizontalLayout();
        H2 title = new H2("Бонусная программа");
        header.add(getSaveButton(), getCloseButton(), title);
        return header;
    }

    private HorizontalLayout lowerLayout() {
        HorizontalLayout layout = new HorizontalLayout();
        Div div = new Div();
        div.add(
                addName(),
                addStatus(),
                addAllContractors(),
                addContractorGroup(),
                addAccrualRule(),
                addWriteOffRules(),
                addMaxPaymentPercentage(),
                addNumberOfDays(),
                addWelcomePoints(),
                addNumberOfPoints(),
                addRegistrationInBonusProgram(),
                addFirstPurchase()
        );
        layout.add(div);
        return layout;
    }

    private Component addName() {
        Label label = new Label("Наименование");
        label.setWidth(labelWidth);
        bonusProgramDtoBinder.forField(name).
                withValidator(text -> text.length() >= 1, "Поле должно быть заполнено", ErrorLevel.ERROR)
                .bind("name");
        name.setWidth(fieldWidth);
        return new HorizontalLayout(label, name);
    }

    private Component addStatus() {
        Label label = new Label("Действует");
        label.setWidth(labelWidth);
        return new HorizontalLayout(label, isActive);
    }
    private Component addAllContractors() {
        Label label = new Label("Все контрагенты");
        label.setWidth(labelWidth);
        return new HorizontalLayout(label, allContractors);
    }

    private Component addAccrualRule() {
        Label label = new Label("Правило начилсения");
        label.setWidth(labelWidth);
        accrualRule.setWidth(fieldWidth);
        return new HorizontalLayout(label, accrualRule);
    }

    private Component addWriteOffRules() {
        Label label = new Label("Правило списания");
        label.setWidth(labelWidth);
        writeOffRules.setWidth(fieldWidth);
        return new HorizontalLayout(label, writeOffRules);
    }

    private Component addMaxPaymentPercentage() {
        Label label = new Label("Макс. процент оплаты");
        label.setWidth(labelWidth);
        maxPaymentPercentage.setWidth(fieldWidth);
        return new HorizontalLayout(label, maxPaymentPercentage);
    }

    private Component addNumberOfDays() {
        Label label = new Label("Баллы начисл. через");
        label.setWidth(labelWidth);
        numberOfDays.setWidth(fieldWidth);
        return new HorizontalLayout(label, numberOfDays);
    }

    private Component addWelcomePoints() {
        Label label = new Label("Приветственные баллы");
        label.setWidth(labelWidth);
        return new HorizontalLayout(label, welcomePoints);
    }

    private Component addNumberOfPoints() {
        Label label = new Label("Колл-во баллов");
        label.setWidth(labelWidth);
        numberOfPoints.setWidth(fieldWidth);
        return new HorizontalLayout(label, numberOfPoints);
    }
    private Component addRegistrationInBonusProgram() {
        Label label = new Label("При регистрации");
        label.setWidth(labelWidth);
        return new HorizontalLayout(label, registrationInBonusProgram);
    }
    private Component addFirstPurchase() {
        Label label = new Label("При первой покупке");
        label.setWidth(labelWidth);
        return new HorizontalLayout(label, firstPurchase);
    }

    private Component addContractorGroup() {
        Label label = new Label("Контрагенты из групп");
        label.setWidth(labelWidth);
        /*List<ContractorGroupDto> contractorGroupDto = contractorGroupService.getAll().stream()
                .collect(Collectors.toList());
        contractorGroup.setItems(contractorGroupDto);
        contractorGroup.setWidth(fieldWidth);*/

        List<String> contractorGroupNameDto = contractorGroupService.getAll().stream()
                .map(ContractorGroupDto::getName)
                .collect(Collectors.toList());

        contractorGroupName.setWidth(fieldWidth);
        contractorGroupName.setItems(contractorGroupNameDto);
        return new HorizontalLayout(label, contractorGroupName);
    }


    private Button getSaveButton() {
        Button saveButton = new Button("Сохранить", event -> {
            if (bonusProgramDtoToEdit.getId() != null) {
                bonusProgramDtoToEdit.setName(name.getValue());
                bonusProgramDtoToEdit.setActiveStatus(isActive.getValue());
                bonusProgramDtoToEdit.setAllContractors(allContractors.getValue());
                if (contractorGroup.isEmpty()) {
                    contractorGroup.setValue(bonusProgramDtoToEdit.getContractorGroupIds().stream().map(contractorGroupService::getById).collect(Collectors.toSet()));
                }
                //bonusProgramDtoToEdit.setContractorGroupIds(contractorGroup.getValue().stream().map(ContractorGroupDto::getId).collect(Collectors.toList()));
                bonusProgramDtoToEdit.setContractorGroupIds(contractorGroup.getValue().stream()
                                .filter(e1 -> contractorGroupName.getValue().stream().anyMatch(e2 -> e1.equals(e2)))
                                .map(ContractorGroupDto::getId)
                                .collect(Collectors.toList())
                                );
                bonusProgramDtoToEdit.setAccrualRule(new BigDecimal(accrualRule.getValue()));
                bonusProgramDtoToEdit.setWriteOffRules(new BigDecimal(writeOffRules.getValue()));
                bonusProgramDtoToEdit.setMaxPaymentPercentage(Short.valueOf(maxPaymentPercentage.getValue()));
                bonusProgramDtoToEdit.setNumberOfDays(Short.valueOf(numberOfDays.getValue()));
                bonusProgramDtoToEdit.setWelcomePoints(welcomePoints.getValue());
                bonusProgramDtoToEdit.setNumberOfPoints(Long.valueOf(numberOfPoints.getValue()));
                bonusProgramDtoToEdit.setRegistrationInBonusProgram(registrationInBonusProgram.getValue());
                bonusProgramDtoToEdit.setFirstPurchase(firstPurchase.getValue());

                if (bonusProgramDtoBinder.validate().isOk()) {
                    bonusProgramService.update(bonusProgramDtoToEdit);
                    clearAll();
                    close();
                } else {
                    bonusProgramDtoBinder.validate().notifyBindingValidationStatusHandlers();
                }
            } else {
                BonusProgramDto bonusProgramDto = new BonusProgramDto();
                bonusProgramDto.setName(name.getValue());
                bonusProgramDto.setActiveStatus(isActive.getValue());
                bonusProgramDto.setAllContractors(allContractors.getValue());
                bonusProgramDto.setContractorGroupIds(contractorGroup.getValue().stream().map(ContractorGroupDto::getId).collect(Collectors.toList()));
                bonusProgramDto.setAccrualRule(new BigDecimal(1));
                bonusProgramDto.setWriteOffRules(new BigDecimal(1));
                bonusProgramDto.setMaxPaymentPercentage(Short.parseShort(maxPaymentPercentage.getValue()));
                bonusProgramDto.setNumberOfDays(Short.parseShort(numberOfDays.getValue()));
                bonusProgramDto.setWelcomePoints(welcomePoints.getValue());
                bonusProgramDto.setNumberOfPoints(Long.valueOf(numberOfPoints.getValue()));
                bonusProgramDto.setRegistrationInBonusProgram(registrationInBonusProgram.getValue());
                bonusProgramDto.setFirstPurchase(firstPurchase.getValue());
                if (bonusProgramDtoBinder.validate().isOk()) {
                    bonusProgramService.create(bonusProgramDto);
                    clearAll();
                    close();
                } else {
                    bonusProgramDtoBinder.validate().notifyBindingValidationStatusHandlers();
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
        isActive.clear();
        name.clear();
        allContractors.clear();
        contractorGroup.clear();
        accrualRule.clear();
        writeOffRules.clear();
        maxPaymentPercentage.clear();
        numberOfDays.clear();
        welcomePoints.clear();
        numberOfPoints.clear();
        registrationInBonusProgram.clear();
        firstPurchase.clear();
    }
}
