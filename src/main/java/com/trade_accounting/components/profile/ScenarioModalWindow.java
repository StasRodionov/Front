package com.trade_accounting.components.profile;

import com.trade_accounting.components.util.ValidTextField;
import com.trade_accounting.models.dto.company.ContractorGroupDto;
import com.trade_accounting.models.dto.units.ScenarioDto;
import com.trade_accounting.services.interfaces.units.ScenarioService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.listbox.MultiSelectListBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import org.springframework.beans.factory.annotation.Autowired;

public class ScenarioModalWindow extends Dialog {

    private TextField nameField = new TextField();

    private final String TEXT_FOR_REQUEST_FIELD = "Обязательное поле";

    private final Checkbox activeStatus = new Checkbox("Действует");

    private final ComboBox<String> object = new ComboBox<>();

    private final ComboBox<String> event = new ComboBox<>();

    private final ComboBox<String> action = new ComboBox<>();

    private final Checkbox straightaway = new Checkbox("Сразу");

    private final Checkbox afterSometimes = new Checkbox("Через");

    private TextField time = new TextField();

    private TextArea commentField = new TextArea();

    private Long id;

    private final String labelWidth = "100px";

    private final String fieldWidth = "400px";

    private final ScenarioService scenarioService;


    public ScenarioModalWindow(ScenarioDto scenarioDto,
                               ScenarioService scenarioService){
        this.scenarioService = scenarioService;

        setCloseOnOutsideClick(false);
        setCloseOnEsc(false);
        id = scenarioDto.getId();
        nameField.setValue(getFieldValueNotNull(scenarioDto.getName()));
        commentField.setValue(getFieldValueNotNull(scenarioDto.getComment()));
        add(new Text("Наименование"), header(),
            new VerticalLayout(configureCommentField(), configureActionField(),
                    configureEventField(),configeruObjectField()));//,wheneventField();
    }
    private HorizontalLayout header(){
        HorizontalLayout header = new HorizontalLayout();
        nameField.setWidth("345px");
        header.add(nameField,getSaveButton(),getCancelButton(),getDeleteButton());
        return header;
    }
    private HorizontalLayout configureCommentField(){
        HorizontalLayout hzl = new HorizontalLayout();
        Label label = new Label("Комментарий");
        label.setWidth(labelWidth);
        commentField.setWidth(fieldWidth);
        commentField.getStyle().set("minHeight","120px");
        hzl.add(label,commentField);
        return hzl;
    }

    private HorizontalLayout configureActionField(){
        HorizontalLayout hzl2 = new HorizontalLayout();
        setSizeFull();
        hzl2.add(activeStatus);
        return hzl2;
    }

    private HorizontalLayout configureEventField(){
        HorizontalLayout hzl3 = new HorizontalLayout();
        object.setItems("Заказ покупателю","Счёт покупателю","Отгрузка","Заказ поставщику","Счёт поставщика","Приёмка");
        object.setValue("Объект");
        action.setItems("Создан","Изменён","Отсутсвует");
        action.setValue("Действие");
        Label label = new Label("Какое событие запускает сценарий?");
        label.setWidth("100px");
        hzl3.add(label, object, action);
        return hzl3;
    }

    private HorizontalLayout configeruObjectField(){
       HorizontalLayout hzl3 = new HorizontalLayout();
       event.setItems("Изменить заказ","Изменить резерв","Отправить уведомление","Отправить вебхук","Создать связанный документ");
       event.setValue("Добавить действие");
       Label label = new Label("Что нужно сделать?");
       hzl3.add(label,event);
       return hzl3;
    }

    /*private HorizontalLayout wheneventField(){
        HorizontalLayout hzl4 = new HorizontalLayout();
        setSizeFull();
        time.setWidth("50px");

    }*/

     private Button getSaveButton() {
         return new Button("Сохранить", event -> {
             ScenarioDto scenarioDto = new ScenarioDto();
             scenarioDto.setId(id);
             scenarioDto.setName(nameField.getValue());
             scenarioDto.setComment(commentField.getValue());
             scenarioDto.setActiveStatus(activeStatus.getValue());
             if (scenarioDto.getId() == null) {
                 scenarioService.create(scenarioDto);
             } else {
                 scenarioService.update(scenarioDto);
             }
             close();
         });
     }

     private  Button getCancelButton(){
        Button cancelButton = new Button("Закрыть",event -> {
            close();
        });
        return cancelButton;
     }

     private Button getDeleteButton(){
        Button deleteButton = new Button("Удалить",event -> {
            scenarioService.deleteById(id);
            close();
        });
        return deleteButton;
     }



    private String getFieldValueNotNull(String value) {
        return value == null ? "" : value;
    }
}
