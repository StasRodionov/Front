package com.trade_accounting.components.settings;

import com.trade_accounting.models.dto.client.DepartmentDto;
import com.trade_accounting.models.dto.client.EmployeeDto;
import com.trade_accounting.models.dto.units.UnitDto;
import com.trade_accounting.services.interfaces.client.DepartmentService;
import com.trade_accounting.services.interfaces.client.EmployeeService;
import com.trade_accounting.services.interfaces.units.UnitService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;


public class UnitsBulkEditModalWindow extends Dialog {
    private final List<UnitDto> unitDtoList;
    private ComboBox employeeOwner = new ComboBox();
    private ComboBox departmentOwner = new ComboBox();
    private ComboBox generalAccess = new ComboBox();

    private final EmployeeService employeeService;
    private final DepartmentService departmentService;
    private final UnitService unitService;

    public UnitsBulkEditModalWindow(List<UnitDto> unitDtoList, EmployeeService employeeService,
                                    DepartmentService departmentService, UnitService unitService) {
        this.employeeService = employeeService;
        this.unitDtoList = unitDtoList;
        this.departmentService = departmentService;
        this.unitService = unitService;
        setCloseOnOutsideClick(false);
        setCloseOnEsc(false);
        add(configureHeader(), configureBody());
    }

    private HorizontalLayout configureHeader() {
        HorizontalLayout header = new HorizontalLayout();
        header.add(getSaveButton(), getCancelButton(), getDeleteButton());
        return header;
    }

    private VerticalLayout configureBody() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(configEmployee(), configDepartment(), configGeneralAccess());
        return verticalLayout;
    }

    private ComboBox configEmployee() {
        employeeOwner.setWidth("500px");
        employeeOwner.setPlaceholder("Владелец-сотрудник");
        employeeOwner.setItems(employeeService.getAll().stream().map(EmployeeDto::getFirstName).collect(Collectors.toList()));
        return employeeOwner;
    }

    private ComboBox configDepartment() {
        departmentOwner.setWidth("500px");
        departmentOwner.setPlaceholder("Владелец-отдел");
        departmentOwner.setItems(departmentService.getAll().stream().map(DepartmentDto::getName).collect(Collectors.toList()));
        return departmentOwner;
    }

    private ComboBox configGeneralAccess() {
        generalAccess.setWidth("500px");
        generalAccess.setPlaceholder("Общий доступ");
        generalAccess.setItems(true, false);
        return generalAccess;
    }

    private Button getSaveButton() {
        return new Button("Сохранить", event -> {
            for (UnitDto newUnitDto : unitDtoList) {
                newUnitDto.setGeneralAccess((boolean) generalAccess.getValue());
                newUnitDto.setDepartmentOwner(getFieldValueNotNull(departmentOwner.getValue().toString()));
                newUnitDto.setEmployeeOwner(getFieldValueNotNull(employeeOwner.getValue().toString()));
                newUnitDto.setDateOfChange(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                unitService.update(newUnitDto);
                close();
            }
        });
    }

    private Button getCancelButton() {
        return new Button("Закрыть", event -> {
            close();
        });
    }

    private Button getDeleteButton() {
        return new Button("Удалить", event -> {
            for (UnitDto unitDto: unitDtoList)
            unitService.deleteById(unitDto.getId());
            close();
        });
    }

    private String getFieldValueNotNull(String value) {
        return value == null ? "" : value;
    }
}
