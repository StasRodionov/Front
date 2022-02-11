package com.trade_accounting.components.production;

import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.models.dto.client.DepartmentDto;
import com.trade_accounting.models.dto.client.EmployeeDto;
import com.trade_accounting.models.dto.production.StagesProductionDto;
import com.trade_accounting.models.dto.production.TechnicalProcessDto;
import com.trade_accounting.services.interfaces.client.DepartmentService;
import com.trade_accounting.services.interfaces.client.EmployeeService;
import com.trade_accounting.services.interfaces.production.StagesProductionService;
import com.trade_accounting.services.interfaces.production.TechnicalProcessService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@UIScope
@SpringComponent
public class TechnicalProcessModalView extends Dialog {

    private final TechnicalProcessService technicalProcessService;
    private final EmployeeService employeeService;
    private final DepartmentService departmentService;
    private final Notifications notifications;
    private final StagesProductionService stagesProductionService;
    private Long id;
    private final TextArea textName = new TextArea();
    private final TextArea textDescription = new TextArea();
    private Set<Long> stagesProductionIds = new HashSet<>();
    private final ComboBox<EmployeeDto> employeeDtoComboBox = new ComboBox<>();
    private final ComboBox<DepartmentDto> departmentDtoComboBox = new ComboBox<>();
    private final Checkbox isSharedCheckBox = new Checkbox("В общем доступе", true);
    private final Checkbox isArchived = new Checkbox("В архив");

    private final Binder<TechnicalProcessDto> technicalProcessDtoBinder =
            new Binder<>(TechnicalProcessDto.class);
    private final String TEXT_FOR_REQUEST_FIELD = "Обязательное поле";
    private final VerticalLayout stagesProductionComboBoxes = new VerticalLayout();

    public TechnicalProcessModalView(TechnicalProcessService technicalProcessService,
                                     EmployeeService employeeService,
                                     DepartmentService departmentService,
                                     Notifications notifications,
                                     StagesProductionService stagesProductionService) {
        this.technicalProcessService = technicalProcessService;
        this.employeeService = employeeService;
        this.departmentService = departmentService;
        this.notifications = notifications;
        this.stagesProductionService = stagesProductionService;
        setSizeFull();
        add(mainVerticalLayout());
    }

    public void setTechnicalProcessEdit(TechnicalProcessDto editDto) {
        id = editDto.getId();
        textName.setValue(editDto.getName());
        textDescription.setValue(editDto.getDescription());
        employeeDtoComboBox.setValue(employeeService.getById(editDto.getEmployeeOwnerId()));
        departmentDtoComboBox.setValue(departmentService.getById(editDto.getDepartmentOwnerId()));
        if (editDto.getStagesProductionIds() != null) {
            stagesProductionIds.addAll(editDto.getStagesProductionIds());
        }
        isSharedCheckBox.setValue(editDto.getIsShared());
        isArchived.setValue(editDto.getIsArchived());
    }

    private H2 title() {
        return new H2("Тех процесс");
    }

    private VerticalLayout mainVerticalLayout() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(headerLayout(), bodyLayout(), getSelector());
        return verticalLayout;
    }

    private HorizontalLayout headerLayout() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(title(), saveButton(), closeButton());
        return horizontalLayout;
    }

    private HorizontalLayout bodyLayout() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(nameAndDescriptionLayout(), formLayoutDepartmentAndEmployee());
        return horizontalLayout;
    }

    private void clearAllFieldsModalView() {
        id = null;
        textName.setValue("");
        textDescription.setValue("");
        employeeDtoComboBox.setValue(null);
        departmentDtoComboBox.setValue(null);
        isArchived.setValue(false);
        isSharedCheckBox.setValue(true);
        stagesProductionIds.clear();
        stagesProductionComboBoxes.removeAll();
    }

    private Button closeButton() {
        Button button = new Button("Закрыть", new Icon(VaadinIcon.CLOSE));
        button.addClickListener(e -> {
            close();
            clearAllFieldsModalView();
        });
        return button;
    }

    private HorizontalLayout departmentConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        List<DepartmentDto> list = departmentService.getAll();
        if (list != null) {
            departmentDtoComboBox.setItems(list);
        }
        departmentDtoComboBox.setItemLabelGenerator(DepartmentDto::getName);
        departmentDtoComboBox.setWidth("350px");
        Label label = new Label("Отдел");
        label.setWidth("150px");
        horizontalLayout.add(label, departmentDtoComboBox);
        return horizontalLayout;
    }

    private HorizontalLayout employeeConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        List<EmployeeDto> list = employeeService.getAll();
        if (list != null) {
            employeeDtoComboBox.setItems(list);
        }
        employeeDtoComboBox.setItemLabelGenerator(EmployeeDto::getFirstName);
        employeeDtoComboBox.setWidth("350px");
        Label label = new Label("Сотрудник");
        label.setWidth("150px");
        horizontalLayout.add(label, employeeDtoComboBox);
        return horizontalLayout;
    }

    private VerticalLayout formLayoutDepartmentAndEmployee() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(departmentConfigure(), employeeConfigure(), isSharedCheckBox, isArchived);
        return verticalLayout;
    }

    private HorizontalLayout nameConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Наименование");
        label.setWidth("100px");
        horizontalLayout.setWidth("750px");
        horizontalLayout.setHeight("75px");
        horizontalLayout.add(label, textName);
        technicalProcessDtoBinder.forField(textName);
        return horizontalLayout;
    }

    private HorizontalLayout descriptionConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Описание");
        label.setWidth("100px");
        horizontalLayout.setWidth("750px");
        horizontalLayout.setHeight("200px");
        horizontalLayout.add(label, textDescription);
        technicalProcessDtoBinder.forField(textName);
        return horizontalLayout;
    }

    private VerticalLayout nameAndDescriptionLayout() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(nameConfigure(), descriptionConfigure());
        return verticalLayout;
    }

    private Button saveButton() {
        return new Button("Сохранить", e -> {
            if (!technicalProcessDtoBinder.validate().isOk()) {
                technicalProcessDtoBinder.validate().notifyBindingValidationStatusHandlers();
            } else {
                TechnicalProcessDto dto = new TechnicalProcessDto();
                dto.setId(id);
                dto.setName(textName.getValue());
                dto.setDescription(textDescription.getValue());
                dto.setEmployeeOwnerId(employeeDtoComboBox.getValue().getId());
                dto.setDepartmentOwnerId(departmentDtoComboBox.getValue().getId());
                dto.setStagesProductionIds(stagesProductionIds);
                dto.setIsArchived(isArchived.getValue());
                dto.setIsShared(isSharedCheckBox.getValue());
                dto.setDateOfChanged(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(LocalDateTime.now()));
                dto.setEmployeeWhoLastChangedId(employeeService.getPrincipal().getId());
                if (id != null) {
                    technicalProcessService.update(dto);
                } else {
                    technicalProcessService.create(dto);
                }
                UI.getCurrent().navigate("technical_process");
                close();
                clearAllFieldsModalView();
                notifications.infoNotification(String.format("Тех процесс %s сохранен", dto.getName()));
            }
        });
    }

    private Details getSelector() {
        Details stagesDetails = new Details("Этапы", new Text(" "));
        stagesDetails.addThemeVariants(DetailsVariant.REVERSE, DetailsVariant.FILLED);
        stagesDetails.setOpened(true);
        stagesDetails.addContent(stagesSelect());
        return stagesDetails;
    }

    private VerticalLayout stagesSelect() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setWidth("516px");
        stagesProductionComboBoxes.setWidth("516px");
        verticalLayout.add(getAddTechnicalProcessStagesButton(stagesProductionComboBoxes), stagesProductionComboBoxes);
        return verticalLayout;
    }

    private Button getAddTechnicalProcessStagesButton(VerticalLayout verticalLayout) {
        Button addStagesButton = new Button("Этап", new Icon(VaadinIcon.PLUS));
        addStagesButton.addClickListener(e ->
                verticalLayout.add(showStagesProduction()));
        return addStagesButton;
    }

    private HorizontalLayout showStagesProduction() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        List<StagesProductionDto> listStagesProduction = stagesProductionService.getALl();
        ComboBox<StagesProductionDto> stagesProductionDtoComboBox = new ComboBox<>();
        stagesProductionDtoComboBox.setItemLabelGenerator(StagesProductionDto::getName);
        stagesProductionDtoComboBox.setItems(listStagesProduction);
        Label label = new Label("Этап");
        label.setWidth("150px");
        Button deleteStage = new Button();
        deleteStage.setIcon(new Icon(VaadinIcon.CLOSE));
        horizontalLayout.add(label, stagesProductionDtoComboBox, deleteStage);
        stagesProductionDtoComboBox.addValueChangeListener(e -> stagesProductionIds.add(stagesProductionDtoComboBox.getValue().getId()));
        deleteStage.addClickListener(e -> {
            horizontalLayout.setVisible(false);
            if (stagesProductionDtoComboBox.getValue().getId() != null) {
                stagesProductionIds.remove(stagesProductionDtoComboBox.getValue().getId());
            }
        });
        return horizontalLayout;
    }
}
