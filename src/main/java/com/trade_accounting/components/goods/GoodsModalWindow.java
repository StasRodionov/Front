package com.trade_accounting.components.goods;

import com.trade_accounting.models.dto.*;
import com.trade_accounting.services.interfaces.*;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
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

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

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

    private final ComboBox<UnitDto> unitDtoComboBox = new ComboBox<>();
    private final ComboBox<ContractorDto> contractorDtoComboBox = new ComboBox<>();
    private final ComboBox<TaxSystemDto> taxSystemDtoComboBox = new ComboBox<>();
    private final ComboBox<ProductGroupDto> productGroupDtoComboBox = new ComboBox<>();
    private final ComboBox<AttributeOfCalculationObjectDto> attributeOfCalculationObjectComboBox = new ComboBox<>();

    private final HorizontalLayout typeOfPriceLayout = new HorizontalLayout();
    private Map<TypeOfPriceDto, NumberField> numberFields;

    private GoodsView goodsView;
    private final HorizontalLayout footer = new HorizontalLayout();


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

        unitDtoComboBox.setItemLabelGenerator(UnitDto::getFullName);
        add(getHorizontalLayout("Единицы измерения", unitDtoComboBox));

        contractorDtoComboBox.setItemLabelGenerator(ContractorDto::getName);
        add(getHorizontalLayout("Поставщик", contractorDtoComboBox));

        taxSystemDtoComboBox.setItemLabelGenerator(TaxSystemDto::getName);
        add(getHorizontalLayout("Система налогообложения", taxSystemDtoComboBox));

        productGroupDtoComboBox.setItemLabelGenerator(ProductGroupDto::getName);
        add(getHorizontalLayout("Группа продуктов", productGroupDtoComboBox));

        attributeOfCalculationObjectComboBox.setItemLabelGenerator(AttributeOfCalculationObjectDto::getName);
        add(getHorizontalLayout("Признак предмета расчета", attributeOfCalculationObjectComboBox));

        add(getHorizontalLayout("Типы цен", typeOfPriceLayout));
        footer.getStyle().set("padding-bottom", "30px");
        footer.getStyle().set("padding-top", "30px");
        add(footer);
    }

    @Override
    public void open() {
        init();
        footer.add(getFooterHorizontalLayout(getAddButton()));
        super.open();
    }

    @Override
    public void close() {
        goodsView.updateAfterModalWindowClose();
        super.close();
    }

    public void open(ProductDto productDto) {
        init();
        productDto = productService.getById(productDto.getId());
        nameTextField.setValue(productDto.getName());
        descriptionField.setValue(productDto.getDescription());
        weightNumberField.setValue(productDto.getWeight().doubleValue());
        volumeNumberField.setValue(productDto.getVolume().doubleValue());
        purchasePriceNumberField.setValue(productDto.getPurchasePrice().doubleValue());

        unitDtoComboBox.setValue(productDto.getUnitDto());
        contractorDtoComboBox.setValue(productDto.getContractorDto());
        taxSystemDtoComboBox.setValue(productDto.getTaxSystemDto());
        productGroupDtoComboBox.setValue(productDto.getProductGroupDto());
        attributeOfCalculationObjectComboBox.setValue(productDto.getAttributeOfCalculationObjectDto());
        initTypeOfPriceFrom(productDto.getProductPriceDtos());
        footer.add(getRemoveButton(productDto), getFooterHorizontalLayout(getUpdateButton(productDto)));
        super.open();
    }

    private void init() {
        nameTextField.clear();
        descriptionField.clear();
        weightNumberField.clear();
        volumeNumberField.clear();
        purchasePriceNumberField.clear();
        footer.removeAll();
        typeOfPriceLayout.removeAll();
        numberFields = new HashMap<>();
        unitDtoComboBox.setItems(unitService.getAll());
        contractorDtoComboBox.setItems(contractorService.getAll());//изменил
        taxSystemDtoComboBox.setItems(taxSystemService.getAll());
        productGroupDtoComboBox.setItems(productGroupService.getAll());
        attributeOfCalculationObjectComboBox.setItems(attributeOfCalculationObjectService.getAll());
        typeOfPriceLayout.add(getTypeOfPriceForm(typeOfPriceService.getAll()));
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

    private HorizontalLayout getFooterHorizontalLayout(Button addOrUpdateButton){
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        horizontalLayout.add(addOrUpdateButton);
        horizontalLayout.add(new Button("Закрыть", event -> close()));
        horizontalLayout.setMargin(false);
        horizontalLayout.setWidth("100%");
        return horizontalLayout;
    }

    private Component getTypeOfPriceForm(List<TypeOfPriceDto> typeOfPriceDtos) {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setPadding(false);
        verticalLayout.setSpacing(false);
        typeOfPriceDtos.forEach(typeOfPriceDto -> {
            HorizontalLayout horizontalLayout = new HorizontalLayout();
            Label label = new Label(typeOfPriceDto.getName());
            label.setWidth("150px");
            horizontalLayout.add(label);
            NumberField numberField = new NumberField();
            numberField.setWidth("200px");
            numberFields.put(typeOfPriceDto, numberField);
            horizontalLayout.add(numberField);
            verticalLayout.add(horizontalLayout);
        });
        return verticalLayout;
    }

    private void initTypeOfPriceFrom(List<ProductPriceDto> list){
        list.forEach(productPriceDto -> {
            numberFields.get(productPriceDto.getTypeOfPriceDto()).setValue(productPriceDto.getValue().doubleValue());
        });
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

    private Button getAddButton() {
        return new Button("Добавить", event -> {
            ProductDto productDto = new ProductDto();
            updateProductDto(productDto);
            productService.create(productDto);
            Notification.show(String.format("Товар %s добавлен", productDto.getName()));
            close();
        });
    }

    private Button getRemoveButton(ProductDto productDto) {
        Button deleteButton = new Button("Удалить", buttonClickEvent -> {
            productService.deleteById(productDto.getId());
            Notification.show(String.format("Товар %s удален", productDto.getName()));
            close();
        });
        deleteButton.setMinWidth("100px");
        return deleteButton;
    }

    private Button getUpdateButton(ProductDto productDto){
        return new Button("Изменить", event -> {
            updateProductDto(productDto);
            productService.update(productDto);
            Notification.show(String.format("Товар %s изменен", productDto.getName()));
            close();
        });
    }

    private void updateProductDto(ProductDto productDto){
        productDto.setName(nameTextField.getValue());
        productDto.setWeight(BigDecimal.valueOf(weightNumberField.getValue()));
        productDto.setVolume(BigDecimal.valueOf(volumeNumberField.getValue()));
        productDto.setPurchasePrice(BigDecimal.valueOf(purchasePriceNumberField.getValue()));
        productDto.setDescription(descriptionField.getValue());
        productDto.setUnitDto(unitDtoComboBox.getValue());
        productDto.setContractorDto(contractorDtoComboBox.getValue());
        productDto.setTaxSystemDto(taxSystemDtoComboBox.getValue());
        productDto.setProductGroupDto(productGroupDtoComboBox.getValue());
        productDto.setAttributeOfCalculationObjectDto((attributeOfCalculationObjectComboBox.getValue()));

        if (productDto.getProductPriceDtos() == null)  {
            productDto.setProductPriceDtos(new ArrayList<>());
        }

        numberFields.forEach((typeOfPriceDto, numberField) -> {
            AtomicBoolean b = new AtomicBoolean(true);
            productDto.getProductPriceDtos().forEach(productPriceDto -> {
                if (productPriceDto.getTypeOfPriceDto().equals(typeOfPriceDto)){
                    productPriceDto.setValue(BigDecimal.valueOf(numberField.getValue()));
                    b.set(false);
                }
            });
            if (b.get()){
                ProductPriceDto productPriceDto = new ProductPriceDto();
                productPriceDto.setTypeOfPriceDto(typeOfPriceDto);
                productPriceDto.setValue(BigDecimal.valueOf(numberField.getValue()));
                productDto.getProductPriceDtos().add(productPriceDto);
            }
        });

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

    public void setGoodsView(GoodsView goodsView) {
        this.goodsView = goodsView;
    }


}
