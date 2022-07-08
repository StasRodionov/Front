package com.trade_accounting.components.settings;

import com.trade_accounting.models.dto.finance.PaymentDto;
import com.trade_accounting.models.dto.finance.ReturnToSupplierDto;
import com.trade_accounting.models.dto.invoice.InvoiceDto;
import com.trade_accounting.models.dto.util.ProjectDto;
import com.trade_accounting.models.dto.warehouse.AcceptanceDto;
import com.trade_accounting.services.interfaces.finance.PaymentService;
import com.trade_accounting.services.interfaces.finance.ReturnToSupplierService;
import com.trade_accounting.services.interfaces.invoice.InvoiceService;
import com.trade_accounting.services.interfaces.util.ProjectService;
import com.trade_accounting.services.interfaces.warehouse.AcceptanceService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Shortcuts;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Anchor;
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
    private final AcceptanceService acceptanceService;
    private final ReturnToSupplierService returnToSupplierService;

    private TextField nameField = new TextField();
    private TextArea codeField = new TextArea();
    private TextArea descriptionField = new TextArea();
    private Long id;
    private final Dialog dialogOnCloseView = new Dialog();
    private Dialog associationWarning = new Dialog();

    public AddEditProjectModal(ProjectDto projectDto,
                               ProjectService projectService,
                               InvoiceService invoiceService,
                               PaymentService paymentService,
                               AcceptanceService acceptanceService,
                               ReturnToSupplierService returnToSupplierService) {

        this.projectService = projectService;
        this.invoiceService = invoiceService;
        this.paymentService = paymentService;
        this.acceptanceService = acceptanceService;
        this.returnToSupplierService = returnToSupplierService;

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

    private void buildAssociationWarningDialog(String projectName, VerticalLayout usageListContainer) {
        associationWarning.removeAll();

        Button cancelButton = new Button("Закрыть", event -> {
            associationWarning.close();
        });

        associationWarning.add(new VerticalLayout(
                new Label("Требуется удалить связанные объекты, проект " + projectName + " используется в:"),
                usageListContainer,
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
            List<AcceptanceDto> boundedAcceptances = acceptanceService.getByProjectId(id);
            List<ReturnToSupplierDto> boundedReturnToSupplier = returnToSupplierService.getByProjectId(id);

            if (boundedInvoices.isEmpty() & boundedPayments.isEmpty() & boundedAcceptances.isEmpty()
                    & boundedReturnToSupplier.isEmpty()) {
                projectService.deleteById(id);
                close();
            } else {
                VerticalLayout usageListContainer = new VerticalLayout();

                for (InvoiceDto invoice : boundedInvoices) {
                    usageListContainer.add(new HorizontalLayout(
                            new Text("Invoice " + invoice.getTypeOfInvoice() + " with id "),
                            new Anchor("#", invoice.getId().toString())));
                }

                for (PaymentDto payment : boundedPayments) {
                    usageListContainer.add(new HorizontalLayout(
                            new Text("Payment " + payment.getTypeOfDocument() + " with id "),
                            new Anchor("#", payment.getId().toString())));
                }

                for (AcceptanceDto acceptance : boundedAcceptances) {
                    usageListContainer.add(new HorizontalLayout(
                            new Text("Приёмка with id "),
                            new Anchor("#", acceptance.getId().toString())));
                }

                for (ReturnToSupplierDto returns : boundedReturnToSupplier) {
                    usageListContainer.add(new HorizontalLayout(
                            new Text("Возврат поставщику with id "),
                            new Anchor("#", returns.getId().toString())));
                }

                buildAssociationWarningDialog(projectService.getById(id).getName(), usageListContainer);
            }
        });
    }

    private String getFieldValueNotNull(String value) {
        return value == null ? "" : value;
    }

}
