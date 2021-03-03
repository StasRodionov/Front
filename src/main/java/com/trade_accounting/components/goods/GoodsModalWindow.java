package com.trade_accounting.components.goods;

import com.trade_accounting.models.dto.*;
import com.trade_accounting.services.interfaces.*;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;

@SpringComponent
@UIScope
@Slf4j
public class GoodsModalWindow extends Dialog {
    private final String PATH_IMAGE = "src/main/resources/images/employee/";
    private final String LABEL_WIDTH = "200px";
    private final String FIELD_WIDTH = "400px";


    private final UnitService unitService;
    private final ContractorService contractorService;
    private final TaxSystemService taxSystemService;
    private final ImageService imageService;
    private final ProductService productService;
    private final ProductGroupService productGroupService;
    private final AttributeOfCalculationObjectService attributeOfCalculationObjectService;

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


    @Autowired
    public GoodsModalWindow(UnitService unitService,
                            ContractorService contractorService,
                            TaxSystemService taxSystemService,
                            ImageService imageService,
                            ProductService productService, ProductGroupService productGroupService, AttributeOfCalculationObjectService attributeOfCalculationObjectService) {
        this.unitService = unitService;
        this.contractorService = contractorService;
        this.taxSystemService = taxSystemService;
        this.imageService = imageService;
        this.productService = productService;
        this.productGroupService = productGroupService;
        this.attributeOfCalculationObjectService = attributeOfCalculationObjectService;


        setCloseOnOutsideClick(false);
        setCloseOnEsc(false);
        add(getHeader());


        add(getHorizontalLayout("Наиминование", nameTextField));
        add(getImageButton());
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
    }

    @Override
    public void open() {
        unitDtoSelect.setItems(unitService.getAll());
        contractorDtoSelect.setItems(contractorService.getAll());
        taxSystemDtoSelect.setItems(taxSystemService.getAll());
        productGroupDtoSelect.setItems(productGroupService.getAll());
        attributeOfCalculationObjectSelect.setItems(attributeOfCalculationObjectService.getAll());
        super.open();
    }

    private HorizontalLayout getHeader() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        H2 title = new H2("Добавление товара");
        title.setHeight("2.2em");
        title.setWidth("345px");
        horizontalLayout.add(title);
        horizontalLayout.add(new Button("Добавить", event -> {
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
            System.out.println(productDto);
        }));
        horizontalLayout.add(new Button("Закрыть", event -> close()));
        return horizontalLayout;
    }

    private Component getImageButton() {
        Button imageButton = new Button("Добавить фото");
        Dialog dialog = new Dialog();
        MemoryBuffer memoryBuffer = new MemoryBuffer();
        Upload upload = new Upload(memoryBuffer);

        upload.addFinishedListener(event -> {
            String pathImageResult = PATH_IMAGE + event.getFileName();
            File newFile = new File(pathImageResult);
            ImageDto imageResult = new ImageDto();
            imageResult.setImageUrl(pathImageResult);

            try (InputStream inputStream = memoryBuffer.getInputStream();
                 FileOutputStream fos = new FileOutputStream(newFile)) {
                newFile.createNewFile();
                fos.write(inputStream.readAllBytes());
                fos.flush();
                imageService.create(imageResult);
                log.info("Фото профиля успешно загружено");
                System.out.println("Фото профиля успешно загружено");
            } catch (IOException e) {
                log.error("При загрузке фото профиля произошла ошибка");
            }
            dialog.close();
        });

        dialog.add(upload);
        imageButton.addClickListener(x -> dialog.open());
        return imageButton;
    }

    private <T extends Component & HasSize> HorizontalLayout getHorizontalLayout(String labelText, T field) {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        field.setWidth(FIELD_WIDTH);
        Label label = new Label(labelText);
        label.setWidth(LABEL_WIDTH);
        horizontalLayout.add(label, field);
        return horizontalLayout;
    }



}
