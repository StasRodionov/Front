package com.trade_accounting.components.settings;

import com.trade_accounting.models.dto.units.ExportDto;
import com.trade_accounting.models.dto.units.ImportDto;
import com.trade_accounting.services.interfaces.units.ExportService;
import com.trade_accounting.services.interfaces.units.ImportService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;

@UIScope
@Slf4j
public class ExportModalWindow extends Dialog {

    private final ExportService exportService;
    private Long id;
    private ComboBox<String> task = new ComboBox<>();
    private DateTimePicker start = new DateTimePicker();
    private DateTimePicker end = new DateTimePicker();
    private ComboBox<String> status = new ComboBox<>();

    private final String LABEL_WIDTH = "200px";
    private final String FIELD_WIDTH = "400px";

    public ExportModalWindow(ExportDto exportDto, ExportService exportService) {
        this.exportService = exportService;

        setCloseOnOutsideClick(true);
        setCloseOnEsc(true);

        add(
            header(),
            configureTaskField(),
            configureStartDate(),
            configureEndDate(),
            configureStatusField(),
            footer()
        );
    }

    @Override
    public void open() {
        if (id == null) {
            init();
        }
        super.open();
    }

    private void init() {
        task.clear();
        start.clear();
        end.clear();
        status.clear();
    }

    private Component header() {
        HorizontalLayout header = new HorizontalLayout();
        H2 title = new H2("Экспорт");
        title.setWidth("345px");
        header.add(title);
        return header;
    }

    private HorizontalLayout configureTaskField() {
        HorizontalLayout taskField = new HorizontalLayout();
        task.setItems("Товары и остатки", "Товары", "Контрагенты", "Банковская выписка", "Приемки");
        task.setValue("Задача");
        Label label = new Label("Задача");
        label.setWidth(LABEL_WIDTH);
        task.setWidth(FIELD_WIDTH);
        taskField.add(label, task);
        return taskField;
    }

    private HorizontalLayout configureStartDate() {
        HorizontalLayout startDate = new HorizontalLayout();
        Label label = new Label("Создана");
        start.setRequiredIndicatorVisible(true);
        label.setWidth(LABEL_WIDTH);
        start.setWidth(FIELD_WIDTH);
        startDate.add(label, start);
        return startDate;
    }

    private HorizontalLayout configureEndDate() {
        HorizontalLayout endDate = new HorizontalLayout();
        Label label = new Label("Завершена");
        end.setRequiredIndicatorVisible(true);
        label.setWidth(LABEL_WIDTH);
        end.setWidth(FIELD_WIDTH);
        endDate.add(label, end);
        return endDate;
    }

    private HorizontalLayout configureStatusField() {
        HorizontalLayout statusField = new HorizontalLayout();
        status.setItems("Задача завершена", "Ошибка выполнения");
        status.setValue("Статус");
        Label label = new Label("Статус");
        label.setWidth(LABEL_WIDTH);
        status.setWidth(FIELD_WIDTH);
        statusField.add(label, status);
        return statusField;
    }

    private HorizontalLayout footer() {
        HorizontalLayout footer = new HorizontalLayout();
        footer.add(getSaveButton(), getCancelButton());
        footer.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        footer.setPadding(true);
        return footer;
    }

    private Component getSaveButton() {
        return new Button("Сохранить", event -> {
            ExportDto exportDto = new ExportDto();
            exportDto.setId(id);
            exportDto.setTask(task.getValue());
            exportDto.setStart(String.valueOf(start.getValue()));
            exportDto.setStatus(status.getValue());

            if (exportDto.getId() == null) {
                exportService.create(exportDto);
            } else {
                exportService.update(exportDto);
            }

            close();
        });
    }

    private Button getCancelButton() {
        return new Button("Закрыть", event -> {
            close();
        });
    }

    private String getFieldValueNotNull(String value) {
        return value == null ? "" : value;
    }
}
