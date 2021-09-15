package com.trade_accounting.components.production;

import com.trade_accounting.models.dto.ContractorDto;
import com.trade_accounting.models.dto.EmployeeDto;
import com.trade_accounting.models.dto.ProductDto;
import com.trade_accounting.models.dto.TechnicalCardDto;
import com.trade_accounting.models.dto.TechnicalCardGroupDto;
import com.trade_accounting.models.dto.TechnicalCardProductionDto;
import com.trade_accounting.services.interfaces.TechnicalCardGroupService;
import com.trade_accounting.services.interfaces.TechnicalCardProductionService;
import com.trade_accounting.services.interfaces.TechnicalCardService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.dom.Style;
import org.apache.poi.ss.formula.functions.T;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class TechnicalCardModalWindow extends Dialog {

    private final TextField idField = new TextField();
    private final TextField nameField = new TextField();
    private final TextField productionCostField = new TextField();
    private final TextArea commentField = new TextArea();
    private final ComboBox<TechnicalCardGroupDto> technicalCardGroupDtoSelect = new ComboBox<>();

    private final TextField lastNameField = new TextField();
    private final TextField prodCostField = new TextField();
    private final TextArea comField = new TextArea();

    private static final String LABEL_WIDTH = "100px";
    private static final String FIELD_WIDTH = "400px";
    private static final String MODAL_WINDOW_WIDTH = "650px";

    private final TechnicalCardService technicalCardService;
    private final TechnicalCardGroupService technicalCardGroupService;
    private final TechnicalCardProductionService technicalCardProductionService;

    private List<TechnicalCardGroupDto> technicalCardGroupDtoList;
    private List<ProductDto> productDtoList;
    private TechnicalCardDto technicalCardDto;
    private final Binder<TechnicalCardDto> technicalCardDtoBinder = new Binder<>(TechnicalCardDto.class);
    private List<ComboBox<ProductDto>> materialProductComboBoxList = new ArrayList<>();
    private List<ComboBox<ProductDto>> finalProductionProductComboBoxList = new ArrayList<>();
    private List<TextField> materialsAmountList = new ArrayList<>();
    private List<TextField> finalProductionAmountList = new ArrayList<>();
  //  private final List<TechnicalCardProductionDto> technicalCardProductionDto = new ArrayList<>();


    public TechnicalCardModalWindow(TechnicalCardDto technicalCardDto, TechnicalCardService technicalCardService,
                                    TechnicalCardGroupService technicalCardGroupService, TechnicalCardProductionService technicalCardProductionService, List<ProductDto> productDtoList
    ) {
        this.technicalCardDto = technicalCardDto;
        this.technicalCardService = technicalCardService;
        this.technicalCardGroupService = technicalCardGroupService;
        this.technicalCardProductionService = technicalCardProductionService;
        this.productDtoList = productDtoList;

        setCloseOnOutsideClick(true);
        setCloseOnEsc(true);

        idField.setValue(getFieldValueNotNull(String.valueOf(technicalCardDto.getId())));
        nameField.setValue(getFieldValueNotNull(technicalCardDto.getName()));
        productionCostField.setValue(getFieldValueNotNull(technicalCardDto.getProductionCost()));
        commentField.setValue(getFieldValueNotNull(technicalCardDto.getComment()));

        add(new Text("Техническая карта"), header(), technicalCardAccordion());
        setWidth(MODAL_WINDOW_WIDTH);
    }

    private HorizontalLayout header() {
        HorizontalLayout header = new HorizontalLayout();
        nameField.setWidth("445px");
        header.add(getSaveButton(), getCancelButton());
        return header;
    }

    private Details technicalCardAccordion() {
        Details allComponents = new Details();
        allComponents.addThemeVariants(DetailsVariant.REVERSE, DetailsVariant.FILLED);
        allComponents.setOpened(true);

        Details technicalCardGroupDetails = new Details("О тех. карте", new Text(" "));
        technicalCardGroupDetails.addThemeVariants(DetailsVariant.REVERSE, DetailsVariant.FILLED);
        technicalCardGroupDetails.setOpened(true);
        technicalCardGroupDetails.addContent(
                configureNameField(),
                configureCommentField(),
                configureProductionCostField(),
                technicalCardGroupSelect()
        );
        add(technicalCardGroupDetails);

        Details materialsDetails = new Details("Материалы", new Text(" "));
        materialsDetails.addThemeVariants(DetailsVariant.REVERSE, DetailsVariant.FILLED);
        materialsDetails.setOpened(true);
        materialsDetails.addContent(
                materialsSelect(true)
        );

        Details finalProductionDetails = new Details("Готовая продукция", new Text(" "));
        finalProductionDetails.addThemeVariants(DetailsVariant.REVERSE, DetailsVariant.FILLED);
        finalProductionDetails.setOpened(true);
        finalProductionDetails.addContent(
                materialsSelect(false)
        );

        allComponents.addContent(technicalCardGroupDetails, materialsDetails, finalProductionDetails);
        return allComponents;
    }

    private VerticalLayout materialsSelect(boolean material) {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(getAddTechnicalCardProductionButton(verticalLayout, material));
        if (technicalCardDto.getId() != null) {
            List<Long> technicalCardProductionDtoList = technicalCardDto.getMaterialsId();
            if (technicalCardProductionDtoList != null) {
                technicalCardProductionDtoList.forEach(tcp -> showTechnicalCardProduction(technicalCardProductionService.getById(tcp), verticalLayout, material));
            }
        }
        return verticalLayout;
    }

    private void showTechnicalCardProduction(TechnicalCardProductionDto technicalCardProductionDto, VerticalLayout verticalLayout,
                                             boolean material) {
        FormLayout technicalCardProductionForm = new FormLayout();
        Style tcpFormStyle = technicalCardProductionForm.getStyle();
        tcpFormStyle.set("width", "385px");
        TextField tcpAmount = new TextField();
        if (material){
            materialsAmountList.add(tcpAmount);
        } else {
            finalProductionAmountList.add(tcpAmount);
        }
        tcpAmount.setPlaceholder("Количество");
        tcpAmount.setRequired(true);
        tcpAmount.setRequiredIndicatorVisible(true);

        technicalCardProductionForm.addFormItem(productSelect(material), "Товар");
        technicalCardProductionForm.addFormItem(tcpAmount, "Количество");

        verticalLayout.addComponentAtIndex(1, technicalCardProductionForm);

    }

    private Button getAddTechnicalCardProductionButton(VerticalLayout verticalLayout, boolean material) {
        TechnicalCardProductionDto newTechnicalCardProductionDto = new TechnicalCardProductionDto();
        Button addMaterialButton;
        if (material) {
            addMaterialButton = new Button("Материал", event -> {
                showTechnicalCardProduction(newTechnicalCardProductionDto, verticalLayout, material);
            });
            addMaterialButton.setIcon(new Icon(VaadinIcon.PLUS_CIRCLE_O));
        } else {
            addMaterialButton = new Button("Готовая продукция", event -> {
                showTechnicalCardProduction(newTechnicalCardProductionDto, verticalLayout, material);
            });
            addMaterialButton.setIcon(new Icon(VaadinIcon.PLUS_CIRCLE_O));
        }
        return addMaterialButton;
    }

    private HorizontalLayout productSelect(boolean material) {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        ComboBox<ProductDto> productDtoSelect = new ComboBox<>();
        if(material){
            materialProductComboBoxList.add(productDtoSelect);
        } else {
            finalProductionProductComboBoxList.add(productDtoSelect);
        }
        if (productDtoList != null) {
            List<ProductDto> list = productDtoList;
            productDtoSelect.setItems(list);
        }
        productDtoSelect.setItemLabelGenerator(ProductDto::getName);
        productDtoSelect.setWidth(FIELD_WIDTH);
        horizontalLayout.add(productDtoSelect);
        return horizontalLayout;
    }

    private HorizontalLayout configureNameField() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        nameField.setWidth(FIELD_WIDTH);
        technicalCardDtoBinder.forField(nameField)
                .asRequired("Не заполнено!")
                .bind("name");
        Label label = new Label("Наименование");
        label.setWidth(LABEL_WIDTH);
        horizontalLayout.add(label, nameField);
        return horizontalLayout;
    }

    private HorizontalLayout configureCommentField() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        commentField.setWidth("345px");
        technicalCardDtoBinder.forField(commentField)
                .asRequired("Не заполнено!")
                .bind("comment");
        Label label = new Label("Комментарий");
        label.setWidth(LABEL_WIDTH);
        horizontalLayout.add(label, commentField);
        return horizontalLayout;
    }

    private HorizontalLayout configureProductionCostField() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        productionCostField.setWidth("345px");
        technicalCardDtoBinder.forField(productionCostField)
                .asRequired("Не заполнено!")
                .bind("comment");
        Label label = new Label("Затраты на производство");
        label.setWidth(LABEL_WIDTH);
        horizontalLayout.add(label, productionCostField);
        return horizontalLayout;
    }

    private HorizontalLayout technicalCardGroupSelect() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();

        technicalCardGroupDtoList = technicalCardGroupService.getAll();
        if (technicalCardGroupDtoList != null) {
            List<TechnicalCardGroupDto> list = technicalCardGroupDtoList;
            technicalCardGroupDtoSelect.setItems(list);
        }
        technicalCardGroupDtoSelect.setItemLabelGenerator(TechnicalCardGroupDto::getName);
        technicalCardGroupDtoSelect.setWidth(FIELD_WIDTH);
        technicalCardDtoBinder.forField(technicalCardGroupDtoSelect)
                .withValidator(Objects::nonNull, "Не заполнено!")
                .bind("technicalCardGroupDto");
        Label label = new Label("Группа");
        label.setWidth(LABEL_WIDTH);
        horizontalLayout.add(label, technicalCardGroupDtoSelect);
        return horizontalLayout;
    }

    private Button getSaveButton() {
        if (nameField.isEmpty()) {
            return new Button("Добавить", event -> {
                saveFields(technicalCardDto);
                technicalCardService.create(technicalCardDto);
                close();
            });
        } else {
            return new Button("Изменить", event -> {
                saveFields(technicalCardDto);
                technicalCardService.update(technicalCardDto);
                close();
            });
        }
    }
//========================================================//================
//    private void saveFields(TechnicalCardDto technicalCardDto) {
//        technicalCardDto.setName(nameField.getValue());
//        technicalCardDto.setComment(commentField.getValue());
//        technicalCardDto.setProductionCost(productionCostField.getValue());
//        technicalCardDto.setTechnicalCardGroupId(technicalCardDto.getTechnicalCardGroupId());
//        List<Long> materialsList = new ArrayList<>();
//        List<Long> finalProductionList = new ArrayList<>();
//        for (int i = 0; i < materialProductComboBoxList.size(); i++) {
//            materialsList.add(Long.valueOf(materialsAmountList.get(i).getValue()));
//            materialsList.add(Long.valueOf(materialProductComboBoxList.get(i).getValue().getId()));
//        }
//        technicalCardDto.setMaterialsId(materialsList);
//        for (int i = 0; i < finalProductionProductComboBoxList.size(); i++) {
//            finalProductionList.add(Long.valueOf(finalProductionAmountList.get(i).getValue()));
//            finalProductionList.add(Long.valueOf(finalProductionProductComboBoxList.get(i).getValue().getId()));
////                    .amount(Long.valueOf(finalProductionAmountList.get(i).getValue()))
////                    .productId(finalProductionProductComboBoxList.get(i).getValue().getId()).build());
//        }
//        technicalCardDto.setFinalProductionId(finalProductionList);
//    }
//========================================================//================
//private void saveFields(TechnicalCardDto technicalCardDto) {
//    technicalCardDto.setName(nameField.getValue());
//        technicalCardDto.setComment(commentField.getValue());
//        technicalCardDto.setProductionCost(productionCostField.getValue());
//        technicalCardDto.setTechnicalCardGroupId(technicalCardGroupDtoSelect.getValue().getId());
//        List<TechnicalCardProductionDto> materialsList = new ArrayList<>();
//        List<TechnicalCardProductionDto> finalProductionList = new ArrayList<>();
//        for (int i = 0; i < materialProductComboBoxList.size(); i++) {
//            materialsList.add(TechnicalCardProductionDto.builder()
//                    .amount(Long.valueOf(finalProductionAmountList.get(i).getValue()))
//                    .productId(finalProductionProductComboBoxList.get(i).getValue().getId()).build());
//        }
//    technicalCardDto.setMaterialsId(Collections.singletonList(Long.valueOf(String.valueOf(materialsList))));
//        for (int i = 0; i < finalProductionProductComboBoxList.size(); i++) {
//            finalProductionList.add(TechnicalCardProductionDto.builder()
//                    .amount(Long.valueOf(finalProductionAmountList.get(i).getValue()))
//                    .productId(finalProductionProductComboBoxList.get(i).getValue().getId()).build());
//        }
//        technicalCardDto.setFinalProductionId(Collections.singletonList(Long.valueOf(String.valueOf(finalProductionList))));
//
//}
//=============================================================//===========================

    private void saveFields(TechnicalCardDto technicalCardDto) {
        technicalCardDto.setName(nameField.getValue());
        technicalCardDto.setComment(commentField.getValue());
        technicalCardDto.setProductionCost(productionCostField.getValue());
        technicalCardDto.setTechnicalCardGroupId(technicalCardGroupDtoSelect.getValue().getId());
        List<TechnicalCardDto> materialsList = new ArrayList<>();
        List<Long> finalProductionList = new ArrayList<>();

        if (technicalCardDto.getId() != null) {

        }
    }

    private Button getCancelButton() {
        return new Button("Закрыть", event -> close());
    }

    private String getFieldValueNotNull(String value) {
        return value == null ? "" : value;
    }

    public void setTechnicalCarDataForEdit(TechnicalCardDto technicalCardDto) {
        if(technicalCardGroupService.getById(technicalCardDto.getTechnicalCardGroupId()).getName() !=null){
            technicalCardGroupDtoSelect.setValue(technicalCardGroupService.getById(technicalCardDto.getTechnicalCardGroupId()));
            lastNameField.setValue(technicalCardDto.getName());
            prodCostField.setValue(technicalCardDto.getProductionCost());
            comField.setValue(technicalCardDto.getComment());
        }

    }


//    public void setContractorDataForEdit(ContractorDto contractorDto) {
//        if (contractorGroupService.getById(contractorDto.getContractorGroupId()).getName() != null) {
//            contractorGroupDtoSelect.setValue(contractorGroupService.getById(contractorDto.getContractorGroupId()));
//        }
//
//        if (typeOfPriceService.getById(contractorDto.getTypeOfPriceId()).getName() != null) {
//            typeOfPriceDtoSelect.setValue(typeOfPriceService.getById(contractorDto.getTypeOfPriceId()));
//        }
//
//        if (legalDetailService.getById(contractorDto.getLegalDetailId()).getInn() != null) {
//            typeOfContractorDtoSelect.setValue(typeOfContractorService.getById(legalDetailDto.getTypeOfContractorDtoId()));
//            lastNameLegalDetailField.setValue(legalDetailDto.getLastName());
//            firstNameLegalDetailField.setValue(legalDetailDto.getFirstName());
//            middleNameLegalDetailField.setValue(legalDetailDto.getMiddleName());
//            addressLegalDetailField.setValue(getFieldValueNotNull(getAddressFromLegalDetail(legalDetailDto)));
//            commentToAddressLegalDetailField.setValue(legalDetailDto.getCommentToAddress());
//            innLegalDetailField.setValue(legalDetailDto.getInn());
//            kppLegalDetailField.setValue(legalDetailDto.getKpp());
//            okpoLegalDetailField.setValue(legalDetailDto.getOkpo());
//            ogrnLegalDetailField.setValue(legalDetailDto.getOgrn());
//            numberOfTheCertificateLegalDetailField.setValue(legalDetailDto.getNumberOfTheCertificate());
//            dateOfTheCertificateLegalDetailField.setValue(legalDetailDto.getDate());
//        }
//    }
}
