package com.trade_accounting.components.settings;

import com.trade_accounting.models.dto.finance.PaymentDto;
import com.trade_accounting.models.dto.invoice.InvoiceDto;
import com.trade_accounting.models.dto.util.ProjectDto;
import com.trade_accounting.services.interfaces.finance.PaymentService;
import com.trade_accounting.services.interfaces.invoice.InvoiceService;
import com.trade_accounting.services.interfaces.util.ProjectService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Shortcuts;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@UIScope
@Slf4j
public class AddEditProjectModal extends Dialog {

    private final ProjectService projectService;
    private final InvoiceService invoiceService;
    private final PaymentService paymentService;
    private TextField nameField = new TextField();
    private TextArea codeField = new TextArea();
    private TextArea descriptionField = new TextArea();
    private Long id;
    private final Dialog dialogOnCloseView = new Dialog();
    private Dialog associationWarning = new Dialog();

    public AddEditProjectModal(ProjectDto projectDto,
                               ProjectService projectService,
                               InvoiceService invoiceService,
                               PaymentService paymentService) {

        this.projectService = projectService;
        this.invoiceService = invoiceService;
        this.paymentService = paymentService;

        setCloseOnOutsideClick(false);
        setCloseOnEsc(false);

        id = projectDto.getId();
        nameField.setValue(getFieldValueNotNull(projectDto.getName()));
        codeField.setValue(getFieldValueNotNull(projectDto.getCode()));
        descriptionField.setValue(getFieldValueNotNull(projectDto.getDescription()));

        nameField.setPlaceholder("Введите наименование");
        nameField.setValueChangeMode(ValueChangeMode.EAGER);
        codeField.setPlaceholder("Введите код");
        codeField.setValueChangeMode(ValueChangeMode.EAGER);
        descriptionField.setPlaceholder("Введите описание");
        descriptionField.setValueChangeMode(ValueChangeMode.EAGER);

        add(
                header(),
                getHorizontalLayout("Наименование", nameField),
                getHorizontalLayout("Код", codeField),
                getHorizontalLayout("Описание", descriptionField),
                footer()
        );

        configureCloseViewDialog();
        configureAssociationWarningDialog();
    }

    private void configureCloseViewDialog() {
        dialogOnCloseView.setCloseOnEsc(false);
        dialogOnCloseView.setCloseOnOutsideClick(false);
        Shortcuts.addShortcutListener(dialogOnCloseView, dialogOnCloseView::close, Key.ESCAPE);
    }

    private void configureAssociationWarningDialog() {
        associationWarning.setCloseOnEsc(true);
        associationWarning.setCloseOnOutsideClick(true);
        Shortcuts.addShortcutListener(associationWarning, associationWarning::close, Key.ESCAPE);

    }

    private void buildAssociationWarningDialog(String projectName, String usageList) {
        associationWarning.removeAll();

        Button cancelButton = new Button("Закрыть", event -> {
            associationWarning.close();
        });

        associationWarning.add(new VerticalLayout(
                new Text("Требуется удалить связанные объекты, проект " + projectName + " используется в:\n" + usageList),
                new HorizontalLayout(cancelButton))
        );

        associationWarning.open();

    }

    private void terminateCloseDialog() {
        dialogOnCloseView.removeAll();

        Button confirmButton = new Button("Продолжить", event -> {
            dialogOnCloseView.close();
            close();
        });
        Button cancelButton = new Button("Отменить", event -> {
            dialogOnCloseView.close();
        });

        dialogOnCloseView.add(new VerticalLayout(
                new Text("Вы уверены? Несохраненные данные будут утеряны!"),
                new HorizontalLayout(cancelButton, confirmButton))
        );

        dialogOnCloseView.open();
    }

    @Override
    public void open() {
        if (id == null) {
            init();
        }
        super.open();
    }

    private void init() {
        nameField.clear();
        codeField.clear();
        descriptionField.clear();
    }

    private Component header() {
        HorizontalLayout header = new HorizontalLayout();
        H2 title = new H2("Проект");
        title.setWidth("345px");
        header.add(title);
        return header;
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

    private HorizontalLayout footer() {
        HorizontalLayout footer = new HorizontalLayout();
        footer.add(getSaveButton(), getCancelButton(), getDeleteButton());
        footer.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        footer.setPadding(true);
        return footer;
    }

    private Button getSaveButton() {
        return new Button("Сохранить", event -> {
            ProjectDto projectDto = new ProjectDto();
            projectDto.setId(id);
            projectDto.setName(nameField.getValue());
            projectDto.setCode(codeField.getValue());
            projectDto.setDescription(descriptionField.getValue());
            if (projectDto.getId() == null) {
                projectService.create(projectDto);
            } else {
                projectService.update(projectDto);
            }

            close();
        });
    }

    private Button getCancelButton() {
        return new Button("Закрыть", event -> {
            terminateCloseDialog();
        });
    }

    private Button getDeleteButton() {
        return new Button("Удалить", event -> {
            List<InvoiceDto> boundedInvoices = invoiceService.getByProjectId(id);
            List<PaymentDto> boundedPayments = paymentService.getByProjectId(id);

            if (boundedInvoices.isEmpty() & boundedPayments.isEmpty()) {
                projectService.deleteById(id);
                close();
            } else {
//                StringBuilder stringBuilder = new StringBuilder();


                buildAssociationWarningDialog(projectService.getById(id).getName(), "");
            }

//            for (InvoiceDto invoice : boundedInvoices) {
//                log.info(invoice.toString());
//                invoice.setProjectId(null);
//                invoiceService.update(invoice);
//            }
//
//            log.warn("СЧЕТА с ЗАДАННЫМ id после удаления: " + invoiceService.getByProjectId(id));
//
//            for (PaymentDto payment : boundedPayments) {
//                log.info(payment.toString());
//                payment.setProjectId(null);
//                paymentService.update(payment);
//            }

//            close();
        });
    }

    private String getFieldValueNotNull(String value) {
        return value == null ? "" : value;
    }

}
