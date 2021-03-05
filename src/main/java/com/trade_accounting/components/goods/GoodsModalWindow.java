package com.trade_accounting.components.goods;

import com.trade_accounting.models.dto.*;
import com.trade_accounting.services.interfaces.*;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.gatanaso.MultiselectComboBox;

import java.math.BigDecimal;
import java.util.*;

@SpringComponent
@UIScope
@Slf4j
public class GoodsModalWindow extends Dialog {

    private final UnitService unitService;
    private final ContractorService contractorService;
    private final TaxSystemService taxSystemService;
    private final ImageService imageService;
    private final ProductService productService;
    private final ProductGroupService productGroupService;
    private final AttributeOfCalculationObjectService attributeOfCalculationObjectService;
    private final TypeOfPriceService typeOfPriceService;

    private final TextField nameTextField = new TextField();
    private final TextField descriptionField = new TextField();

    private final NumberField weightNumberField = new NumberField();
    private final NumberField volumeNumberField = new NumberField();
    private final NumberField purchasePriceNumberField = new NumberField();

    private final Select<UnitDto> unitDtoSelect = new Select<>();
    private final Select<ContractorDto> contractorDtoSelect = new Select<>();
    private final Select<TaxSystemDto> taxSystemDtoSelect = new Select<>();
    private final Select<ProductGroupDto> productGroupDtoSelect = new Select<>();
    private final Select<AttributeOfCalculationObjectDto> attributeOfCalculationObjectSelect = new Select<>();

    private final MultiselectComboBox<TypeOfPriceDto> multiSelectTypeOfPrice = new MultiselectComboBox<>();
    private final VerticalLayout verticalLayoutForPrice = new VerticalLayout();
    private Map<Long, NumberField> numberFields = new HashMap<>();

    private GoodsView goodsView;


    @Autowired
    public GoodsModalWindow(UnitService unitService,
                            ContractorService contractorService,
                            TaxSystemService taxSystemService,
                            ImageService imageService,
                            ProductService productService,
                            ProductGroupService productGroupService,
                            AttributeOfCalculationObjectService attributeOfCalculationObjectService,
                            TypeOfPriceService typeOfPriceService) {
        this.unitService = unitService;
        this.contractorService = contractorService;
        this.taxSystemService = taxSystemService;
        this.imageService = imageService;
        this.productService = productService;
        this.productGroupService = productGroupService;
        this.attributeOfCalculationObjectService = attributeOfCalculationObjectService;
        this.typeOfPriceService = typeOfPriceService;


        setCloseOnOutsideClick(false);
        setCloseOnEsc(false);
        add(getHeader());

        add(getHorizontalLayout("Наиминование", nameTextField));
        add(getHorizontalLayout("Вес", weightNumberField));
        add(getHorizontalLayout("Объем", volumeNumberField));
        add(getHorizontalLayout("Закупочная цена", purchasePriceNumberField));
        add(getHorizontalLayout("Описание", descriptionField));

        unitDtoSelect.setItemLabelGenerator(UnitDto::getFullName);
        add(getHorizontalLayout("Единицы измерения", unitDtoSelect));

        contractorDtoSelect.setItemLabelGenerator(ContractorDto::getName);
        add(getHorizontalLayout("Поставщик", contractorDtoSelect));

        taxSystemDtoSelect.setItemLabelGenerator(TaxSystemDto::getName);
        add(getHorizontalLayout("Система налогообложения", taxSystemDtoSelect));

        productGroupDtoSelect.setItemLabelGenerator(ProductGroupDto::getName);
        add(getHorizontalLayout("Группа продуктов", productGroupDtoSelect));

        attributeOfCalculationObjectSelect.setItemLabelGenerator(AttributeOfCalculationObjectDto::getName);
        add(getHorizontalLayout("Признак предмета расчета", attributeOfCalculationObjectSelect));


        multiSelectTypeOfPrice.setItemLabelGenerator(TypeOfPriceDto::getName);
        multiSelectTypeOfPrice.setWidth("600px");
        multiSelectTypeOfPrice.setLabel("Типы цен");
        multiSelectTypeOfPrice.addValueChangeListener(e -> {
            verticalLayoutForPrice.removeAll();
            Map<Long, NumberField> newMap = new HashMap<>();
            Set<TypeOfPriceDto> set = multiSelectTypeOfPrice.getValue();
            set.forEach(typeOfPriceDto -> {
                NumberField numberField;
                if (numberFields.containsKey(typeOfPriceDto.getId())){
                    numberField = numberFields.get(typeOfPriceDto.getId());
                }else {
                    numberField = new NumberField();
                }
                newMap.put(typeOfPriceDto.getId(), numberField);
                verticalLayoutForPrice.add(getHorizontalLayout(typeOfPriceDto.getName(), numberField));
            });
                    numberFields = newMap;
        });
        add(multiSelectTypeOfPrice);
        verticalLayoutForPrice.setSpacing(false);
        add(verticalLayoutForPrice);
        add(getFooter());
    }

    @Override
    public void open() {
        nameTextField.clear();
        descriptionField.clear();
        weightNumberField.clear();
        volumeNumberField.clear();
        purchasePriceNumberField.clear();
        unitDtoSelect.setItems(unitService.getAll());
        contractorDtoSelect.setItems(contractorService.getAll());
        taxSystemDtoSelect.setItems(taxSystemService.getAll());
        productGroupDtoSelect.setItems(productGroupService.getAll());
        attributeOfCalculationObjectSelect.setItems(attributeOfCalculationObjectService.getAll());
        multiSelectTypeOfPrice.setItems(typeOfPriceService.getAll());
        super.open();
    }

    private Component getHeader() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        H2 title = new H2("Добавление товара");
        title.setHeight("2.2em");
        title.setWidth("345px");
        horizontalLayout.add(title);
        horizontalLayout.add(getImageButton());
        return horizontalLayout;
    }
    private Component getFooter() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        horizontalLayout.add(getAddButton());
        horizontalLayout.add(new Button("Закрыть", event -> close()));
        horizontalLayout.setPadding(true);
        return horizontalLayout;
    }

    private Component getImageButton() {
        Button imageButton = new Button("Добавить фото");
        Dialog dialog = new Dialog();
        MemoryBuffer memoryBuffer = new MemoryBuffer();
        Upload upload = new Upload(memoryBuffer);

        upload.addFinishedListener(event -> {
                log.error("Не реализовано!");
            dialog.close();
        });
        dialog.add(upload);
        imageButton.addClickListener(x -> dialog.open());
        return imageButton;
    }

    private Button getAddButton(){
        return new Button("Добавить", event -> {
            ProductDto productDto = new ProductDto();
            productDto.setName(nameTextField.getValue());
            productDto.setWeight(BigDecimal.valueOf(weightNumberField.getValue()));
            productDto.setVolume(BigDecimal.valueOf(volumeNumberField.getValue()));
            productDto.setPurchasePrice(BigDecimal.valueOf(purchasePriceNumberField.getValue()));
            productDto.setDescription(descriptionField.getValue());
            productDto.setUnitDto(unitDtoSelect.getValue());
            productDto.setContractorDto(contractorDtoSelect.getValue());
            productDto.setTaxSystemDto(taxSystemDtoSelect.getValue());
            productDto.setProductGroupDto(productGroupDtoSelect.getValue());
            productDto.setAttributeOfCalculationObjectDto((attributeOfCalculationObjectSelect.getValue()));

            Set<TypeOfPriceDto> typeOfPriceDtos = multiSelectTypeOfPrice.getValue();
            List<PriceDto> priceDtos = new ArrayList<>();
            typeOfPriceDtos.forEach(typeOfPriceDto -> {
                PriceDto price = new PriceDto();
                price.setTypeOfPriceDto(typeOfPriceDto);
                price.setValue(BigDecimal.valueOf(numberFields.get(typeOfPriceDto.getId()).getValue()));
                priceDtos.add(price);
            });
            productDto.setPriceDtos(priceDtos);
            productService.create(productDto);
            Notification.show(String.format("Товар %s добавлен", productDto.getName()));
            goodsView.updateAfterModalWindowClose();
            close();
        });
    }


    private <T extends Component & HasSize> HorizontalLayout getHorizontalLayout(String labelText, T field) {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label(labelText);
        field.setWidth("400px");
        label.setWidth("200px");
        horizontalLayout.add(label, field);
        return horizontalLayout;
    }

    public void setGoodsView(GoodsView goodsView) {
        this.goodsView = goodsView;
    }
}
