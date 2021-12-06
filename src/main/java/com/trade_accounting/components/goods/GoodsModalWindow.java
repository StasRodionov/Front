package com.trade_accounting.components.goods;

import com.trade_accounting.components.sells.InformationView;
import com.trade_accounting.models.dto.AttributeOfCalculationObjectDto;
import com.trade_accounting.models.dto.ContractorDto;
import com.trade_accounting.models.dto.ImageDto;
import com.trade_accounting.models.dto.ProductDto;
import com.trade_accounting.models.dto.ProductGroupDto;
import com.trade_accounting.models.dto.ProductPriceDto;
import com.trade_accounting.models.dto.TaxSystemDto;
import com.trade_accounting.models.dto.TypeOfPriceDto;
import com.trade_accounting.models.dto.UnitDto;
import com.trade_accounting.services.interfaces.AttributeOfCalculationObjectService;
import com.trade_accounting.services.interfaces.ContractorService;
import com.trade_accounting.services.interfaces.ImageService;
import com.trade_accounting.services.interfaces.ProductGroupService;
import com.trade_accounting.services.interfaces.ProductPriceService;
import com.trade_accounting.services.interfaces.ProductService;
import com.trade_accounting.services.interfaces.TaxSystemService;
import com.trade_accounting.services.interfaces.TypeOfPriceService;
import com.trade_accounting.services.interfaces.UnitService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ErrorLevel;
import com.vaadin.flow.data.validator.BigDecimalRangeValidator;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@SpringComponent
@UIScope
@Slf4j
public class GoodsModalWindow extends Dialog {

    private final ProductPriceService productPriceService;
    private final UnitService unitService;
    private final ContractorService contractorService;
    private final TaxSystemService taxSystemService;
    private final ProductService productService;
    private final ImageService imageService;
    private final ProductGroupService productGroupService;
    private final AttributeOfCalculationObjectService attributeOfCalculationObjectService;
    private final TypeOfPriceService typeOfPriceService;
    private final TextField nameTextField = new TextField();
    private final TextField descriptionField = new TextField();
    private final TextField countryOriginField = new TextField();
    private final TextField saleTax = new TextField();
    private final BigDecimalField itemNumber = new BigDecimalField();
    private final BigDecimalField minimumBalance = new BigDecimalField();
    private final BigDecimalField weightNumberField = new BigDecimalField();
    private final BigDecimalField volumeNumberField = new BigDecimalField();
    private final BigDecimalField purchasePriceNumberField = new BigDecimalField();
    private final ComboBox<UnitDto> unitDtoComboBox = new ComboBox<>();
    private final ComboBox<ContractorDto> contractorDtoComboBox = new ComboBox<>();
    private final ComboBox<TaxSystemDto> taxSystemDtoComboBox = new ComboBox<>();
    private final ComboBox<ProductGroupDto> productGroupDtoComboBox = new ComboBox<>();
    private final ComboBox<AttributeOfCalculationObjectDto> attributeOfCalculationObjectComboBox = new ComboBox<>();

    private final HorizontalLayout typeOfPriceLayout = new HorizontalLayout();
    private Map<TypeOfPriceDto, BigDecimalField> bigDecimalFields;
    private List<ImageDto> imageDtoList;
    private List<ImageDto> imageDtoListForRemove;
    private final HorizontalLayout imageHorizontalLayout = new HorizontalLayout();

    private final HorizontalLayout footer = new HorizontalLayout();
    private final Binder<ProductDto> productDtoBinder = new Binder<>(ProductDto.class);
    private final Binder<ProductPriceDto> priceDtoBinder = new Binder<>(ProductPriceDto.class);

    private ProductDto productDto;

    @Autowired
    public GoodsModalWindow(ProductPriceService productPriceService, UnitService unitService,
                            ContractorService contractorService,
                            TaxSystemService taxSystemService,
                            ProductService productService,
                            ImageService imageService,
                            ProductGroupService productGroupService,
                            AttributeOfCalculationObjectService attributeOfCalculationObjectService,
                            TypeOfPriceService typeOfPriceService) {
        this.productPriceService = productPriceService;
        this.unitService = unitService;
        this.contractorService = contractorService;
        this.taxSystemService = taxSystemService;
        this.productService = productService;
        this.imageService = imageService;
        this.productGroupService = productGroupService;
        this.attributeOfCalculationObjectService = attributeOfCalculationObjectService;
        this.typeOfPriceService = typeOfPriceService;


        setCloseOnOutsideClick(true);
        setCloseOnEsc(true);
        add(getHeader());

        nameTextField.setPlaceholder("Введите наименование");
        productDtoBinder.forField(nameTextField)
                .withValidator(text -> text.length() >= 3, "Не менее трёх символов", ErrorLevel.ERROR)
                .bind(ProductDto::getName, ProductDto::setName);
        nameTextField.setValueChangeMode(ValueChangeMode.EAGER);
        add(getHorizontalLayout("Наименование", nameTextField));

        itemNumber.setPlaceholder("Введите Артикул");
        productDtoBinder.forField(itemNumber)
                .withValidator(Objects::nonNull, "Введите артикул")
                .withValidator(new BigDecimalRangeValidator("Не верное значение", BigDecimal.ZERO, new BigDecimal("99999999999")))
                .bind(productDto -> new BigDecimal(productDto.getItemNumber()), (productDto1, itemNumber1) -> productDto1.setItemNumber(itemNumber1.intValue()));
        itemNumber.setValueChangeMode(ValueChangeMode.EAGER);
        add(getHorizontalLayout("Артикул", itemNumber));


        weightNumberField.setPlaceholder("Введите вес");
        productDtoBinder.forField(weightNumberField)
                .withValidator(Objects::nonNull, "Введите вес")
                .withValidator(new BigDecimalRangeValidator("Не верное значение", BigDecimal.ZERO, new BigDecimal("99999999999999999999")))//                .withValidator(value -> value < 0, "Не может быть меньше 0")
                .bind(ProductDto::getWeight, ProductDto::setWeight);
        weightNumberField.setValueChangeMode(ValueChangeMode.EAGER);
        add(getHorizontalLayout("Вес", weightNumberField));

        volumeNumberField.setPlaceholder("Введите объём");
        productDtoBinder.forField(volumeNumberField)
                .withValidator(Objects::nonNull, "Введите объём")
                .withValidator(new BigDecimalRangeValidator("Не верное значение", BigDecimal.ZERO, new BigDecimal("99999999999999999999")))//                .withValidator(value -> value < 0, "Не может быть меньше 0")
                .bind(ProductDto::getVolume, ProductDto::setVolume);
        volumeNumberField.setValueChangeMode(ValueChangeMode.EAGER);
        add(getHorizontalLayout("Объем", volumeNumberField));

        purchasePriceNumberField.setPlaceholder("Введите закупочную цену");
        productDtoBinder.forField(purchasePriceNumberField)
                .withValidator(Objects::nonNull, "Введите закупочную цену")
                .withValidator(new BigDecimalRangeValidator("Не верное значение", BigDecimal.ZERO, new BigDecimal("99999999999999999999")))//                .withValidator(value -> value < 0, "Не может быть меньше 0")
                .bind(ProductDto::getPurchasePrice, ProductDto::setPurchasePrice);
        purchasePriceNumberField.setValueChangeMode(ValueChangeMode.EAGER);
        add(getHorizontalLayout("Закупочная цена", purchasePriceNumberField));


        descriptionField.setPlaceholder("Введите описание");
        productDtoBinder.forField(descriptionField)
                .withValidator(text -> text.length() >= 3, "Не менее трёх символов", ErrorLevel.ERROR)
                .bind(ProductDto::getName, ProductDto::setName);
        descriptionField.setValueChangeMode(ValueChangeMode.EAGER);
        add(getHorizontalLayout("Описание", descriptionField));

        countryOriginField.setPlaceholder("Введите страну происхождения");
        productDtoBinder.forField(countryOriginField)
                .withValidator(text-> text.length()>=3,"Не менее 3 Символов", ErrorLevel.ERROR)
                .bind(ProductDto::getCountryOrigin, ProductDto::setCountryOrigin);
        countryOriginField.setValueChangeMode(ValueChangeMode.EAGER);
        add(getHorizontalLayout("Страна происхождения", countryOriginField));

        unitDtoComboBox.setPlaceholder("Выберите единицу измерения");
        unitDtoComboBox.setItems(unitService.getAll());
        unitDtoComboBox.setItemLabelGenerator(UnitDto::getFullName);
        add(getHorizontalLayout("Единицы измерения", unitDtoComboBox));

        contractorDtoComboBox.setPlaceholder("Выберите поставщика");
        contractorDtoComboBox.setItems(contractorService.getAll());
        contractorDtoComboBox.setItemLabelGenerator(ContractorDto::getName);
        add(getHorizontalLayout("Поставщик", contractorDtoComboBox));

        taxSystemDtoComboBox.setPlaceholder("Выберите систему налогообложения");
        taxSystemDtoComboBox.setItems(taxSystemService.getAll());
        taxSystemDtoComboBox.setItemLabelGenerator(TaxSystemDto::getName);
        add(getHorizontalLayout("Система налогообложения", taxSystemDtoComboBox));

        saleTax.setPlaceholder("Введите размер НДС");
        productDtoBinder.forField(saleTax)
                .withValidator(text -> text.length() >= 2, "Введите число сооствестующее облагаемой ставке НДС", ErrorLevel.ERROR)
                .bind(ProductDto::getName, ProductDto::setName);
        saleTax.setValueChangeMode(ValueChangeMode.EAGER);
        add(getHorizontalLayout("НДС", saleTax));

        productGroupDtoComboBox.setPlaceholder("Выберите группу продуктов");
        productGroupDtoComboBox.setItems(productGroupService.getAll());
        productGroupDtoComboBox.setItemLabelGenerator(ProductGroupDto::getName);
        add(getHorizontalLayout("Группа продуктов", productGroupDtoComboBox));

        minimumBalance.setPlaceholder("Введите минимальный баланс");
        productDtoBinder.forField(minimumBalance)
                .withValidator(Objects::nonNull, "Введите минимальный баланс")
                .withValidator(new BigDecimalRangeValidator("Не верное значение", BigDecimal.ZERO, new BigDecimal("99999999999")))
                .bind(productDto -> new BigDecimal(productDto.getMinimumBalance()), (productDto1, minimumBalance1) -> productDto1.setMinimumBalance(minimumBalance1.intValue()));
        minimumBalance.setValueChangeMode(ValueChangeMode.EAGER);
        add(getHorizontalLayout("Минимальный баланс", minimumBalance));

        attributeOfCalculationObjectComboBox.setPlaceholder("Выберите предмет расчета");
        attributeOfCalculationObjectComboBox.setItems(attributeOfCalculationObjectService.getAll());
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


    public void open(ProductDto editProductDto) {
        init();
        productDto = productService.getById(editProductDto.getId());
        nameTextField.setValue(productDto.getName());
        descriptionField.setValue(productDto.getDescription());
        weightNumberField.setValue(productDto.getWeight());
        volumeNumberField.setValue(productDto.getVolume());
        purchasePriceNumberField.setValue(productDto.getPurchasePrice());
        if(productDto.getCountryOrigin()==null){
            countryOriginField.setValue("нет данных");
        } else {
            countryOriginField.setValue(productDto.getCountryOrigin());
        }
        minimumBalance.setValue(BigDecimal.valueOf(productDto.getMinimumBalance()));
        if(productDto.getSaleTax()==null){
            saleTax.setValue("нет данных");
        } else {
            saleTax.setValue(productDto.getSaleTax());
        }
        itemNumber.setValue(BigDecimal.valueOf(productDto.getItemNumber()));
        unitDtoComboBox.setValue(unitService.getById(productDto.getUnitId()));
        contractorDtoComboBox.setValue(contractorService.getById(productDto.getContractorId()));
        taxSystemDtoComboBox.setValue(taxSystemService.getById(productDto.getTaxSystemId()));
        productGroupDtoComboBox.setValue(productGroupService.getById(productDto.getProductGroupId()));
        attributeOfCalculationObjectComboBox.setValue(attributeOfCalculationObjectService
                .getById(productDto.getAttributeOfCalculationObjectId()));
        imageDtoList = productDto.getImageDtos();
        for (ImageDto imageDto : imageDtoList) {
            StreamResource resource = new StreamResource("image", () -> new ByteArrayInputStream(imageDto.getContent()));
            Image image = new Image(resource, "image");
            image.setHeight("100px");
            imageHorizontalLayout.add(image, getRemoveImageButton(productDto, image, imageDto));
        }
        initTypeOfPriceFrom(productDto.getProductPriceIds());
        footer.add(getRemoveButton(productDto), getFooterHorizontalLayout(getUpdateButton(productDto)));

        super.open();
    }

    private void init() {
        nameTextField.clear();
        descriptionField.clear();
        weightNumberField.clear();
        volumeNumberField.clear();
        countryOriginField.clear();
        itemNumber.clear();
        minimumBalance.clear();
        saleTax.clear();
        purchasePriceNumberField.clear();
        footer.removeAll();
        imageHorizontalLayout.removeAll();
        typeOfPriceLayout.removeAll();
        bigDecimalFields = new HashMap<>();
        imageDtoList = new ArrayList<>();
        imageDtoListForRemove = new ArrayList<>();
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
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setPadding(false);
        verticalLayout.add(getImageButton(), imageHorizontalLayout);
        horizontalLayout.add(title, verticalLayout);
        return horizontalLayout;
    }

    private HorizontalLayout getFooterHorizontalLayout(Button addOrUpdateButton) {
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
            BigDecimalField field = new BigDecimalField();
            field.setWidth("200px");

            priceDtoBinder.forField(field)
                    .withValidator(Objects::nonNull, "Не заполнено!")
                    .withValidator(new BigDecimalRangeValidator("Не может быть меньше 0", BigDecimal.ZERO, new BigDecimal("99999999999999999999")))
                    .bind("value");
            field.setValueChangeMode(ValueChangeMode.EAGER);

            bigDecimalFields.put(typeOfPriceDto, field);
            horizontalLayout.add(field);
            verticalLayout.add(horizontalLayout);
        });
        return verticalLayout;
    }

    private void initTypeOfPriceFrom(List<Long> list) {
        list.forEach(productPriceId ->
                bigDecimalFields.get(typeOfPriceService
                                .getById(productPriceService.getById(productPriceId)
                                        .getTypeOfPriceId()))
                        .setValue(productPriceService.getById(productPriceId)
                                .getValue()));
    }

    private Component getImageButton() {
        Button imageButton = new Button("Добавить фото");
        Dialog dialog = new Dialog();
        MultiFileMemoryBuffer memoryBuffer = new MultiFileMemoryBuffer();
        Upload upload = new Upload(memoryBuffer);

        upload.addFinishedListener(event -> {
            try {
                ImageDto imageDto = new ImageDto();
                final String fileName = event.getFileName();
                String fileExtension = fileName.substring(fileName.indexOf("."));
                imageDto.setFileExtension(fileExtension);
                imageDto.setContent(memoryBuffer.getInputStream(event.getFileName()).readAllBytes());
                imageDtoList.add(imageDto);
                StreamResource resource = new StreamResource("image", () -> new ByteArrayInputStream(imageDto.getContent()));
                Image image = new Image(resource, "image");
                image.setHeight("100px");
                imageHorizontalLayout.add(image);
            } catch (IOException e) {
                e.printStackTrace();
            }

            dialog.close();
        });

        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidth("500px");
        layout.getStyle().set("overflow", "auto");
        dialog.add(upload);
        imageButton.addClickListener(x -> dialog.open());
        return imageButton;
    }

    private Button getAddButton() {
        return new Button("Добавить", event -> {
            ProductDto productDto = new ProductDto();
            if (!productDtoBinder.validate().isOk() || !priceDtoBinder.validate().isOk()) {
                productDtoBinder.validate().notifyBindingValidationStatusHandlers();
                priceDtoBinder.validate().notifyBindingValidationStatusHandlers();
            } else {
                updateProductDto(productDto);
                productService.create(productDto);
                Notification.show(String.format("Товар %s добавлен", productDto.getName()));
                close();
            }
        });
    }

    private Button getRemoveButton(ProductDto productDto) {
        Button deleteButton = new Button("Удалить", buttonClickEvent -> {
            productService.deleteById(productDto.getId());
            productDto.getImageDtos().forEach(el -> imageService.deleteById(el.getId()));
            Notification.show(String.format("Товар %s удален", productDto.getName()));
            close();
        });
        deleteButton.setMinWidth("100px");
        return deleteButton;
    }

    private Button getRemoveImageButton(ProductDto productDto, Image image, ImageDto imageDto) {
        Button deleteButton = new Button(new Icon(VaadinIcon.CLOSE_CIRCLE_O), buttonClickEvent -> {
            imageHorizontalLayout.remove(image);
            productDto.getImageDtos().remove(imageDto);
            imageDtoListForRemove.add(imageDto);
        });

        deleteButton.setMinWidth("10px");
        return deleteButton;
    }

    private Button getUpdateButton(ProductDto productDto) {
        return new Button("Изменить", event -> {
            if (checkAllFields()){

                updateProductDto(productDto);
                List<ProductPriceDto> list = new ArrayList<>();
                for (Long l : productDto.getProductPriceIds()){
                    list.add(productPriceService.getById(l));
                }
                productService.update(productDto);

                for (ProductPriceDto dto : list){
                    TypeOfPriceDto typeOfPriceDto = typeOfPriceService.getById(dto.getTypeOfPriceId());
                    productPriceService.update(dto);
                    typeOfPriceService.update(typeOfPriceDto);
                }

                imageDtoListForRemove.forEach(el -> imageService.deleteById(el.getId()));

                Notification.show(String.format("Товар %s изменен", productDto.getName()));
                close();
            } else {
                com.trade_accounting.components.sells.InformationView informationView =
                        new InformationView("Одно или несколько полей не заполнены");
                informationView.open();
                return;
            }

        });
    }

    private void updateProductDto(ProductDto productDto) {
        productDto.setName(nameTextField.getValue());
        productDto.setSaleTax(saleTax.getValue());
        productDto.setWeight(weightNumberField.getValue());
        productDto.setItemNumber(itemNumber.getValue().intValue());
        productDto.setVolume(volumeNumberField.getValue());
        productDto.setMinimumBalance(minimumBalance.getValue().intValue());
        productDto.setPurchasePrice(purchasePriceNumberField.getValue());
        productDto.setDescription(descriptionField.getValue());
        productDto.setUnitId(unitDtoComboBox.getValue().getId());
        productDto.setContractorId(contractorDtoComboBox.getValue().getId());
        productDto.setTaxSystemId(taxSystemDtoComboBox.getValue().getId());
        productDto.setProductGroupId(productGroupDtoComboBox.getValue().getId());
        productDto.setCountryOrigin(countryOriginField.getValue());
        productDto.setAttributeOfCalculationObjectId(attributeOfCalculationObjectComboBox.getValue().getId());
        productDto.setImageDtos(imageDtoList);

        if (productDto.getProductPriceIds() == null) {
            productDto.setProductPriceIds(new ArrayList<>());
        }
        productDto.getProductPriceIds().clear();
        bigDecimalFields.forEach((typeOfPriceDto, bigDecimalField) -> {

            List<ProductPriceDto> list = productPriceService.getAll().stream()
                    .filter(x -> x.getTypeOfPriceId().equals(typeOfPriceDto.getId()))
                    .filter(x -> x.getValue().compareTo(bigDecimalField.getValue()) == 0)
                    .collect(Collectors.toList());

            if (list.size() == 0){
                // создаем цену
                ProductPriceDto productPriceDto = new ProductPriceDto();
                productPriceDto.setTypeOfPriceId(typeOfPriceDto.getId());
                productPriceDto.setValue(bigDecimalField.getValue());
                productPriceService.create(productPriceDto);

                // получаем ID, так как заранее мы не можем его знать. затем ID присваиваем в лист TypeOfPrices
                Optional<ProductPriceDto> id = productPriceService.getAll().stream()
                        .filter(x -> x.getTypeOfPriceId().equals(typeOfPriceDto.getId()))
                        .filter(x -> x.getValue().compareTo(bigDecimalField.getValue()) == 0)
                        .findFirst();
                if (id.isPresent()){
                    productDto.getProductPriceIds().add(id.get().getId());
                }

            } else {
                productDto.getProductPriceIds().add(list.get(0).getId());
            }
        });

    }

    private boolean checkAllFields(){
        if ( minimumBalance.getValue().compareTo(BigDecimal.ZERO) < 0){
            return false;
        }

        AtomicBoolean flag = new AtomicBoolean(true);

        bigDecimalFields.forEach((typeOfPriceDto, bigDecimalField) -> {
            Optional<BigDecimal> bd = Optional.ofNullable(bigDecimalField.getValue());

            if (bd.isEmpty() || bd.get().compareTo(BigDecimal.ZERO) <= 0){
                flag.set(false);
            }
        });

        return flag.get();
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
}
