package com.trade_accounting.components.money;

import com.trade_accounting.components.contractors.PrintContractorsXls;
import com.trade_accounting.models.dto.finance.MoneySubProfitLossDto;
import com.trade_accounting.services.interfaces.client.EmployeeService;
import com.trade_accounting.services.interfaces.finance.MoneySubProfitLossService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.StreamResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public class ProfitLossPrintModal extends Dialog {

    private static final String LABEL_WIDTH = "500px";
    private final MoneySubProfitLossDto moneySubProfitLossDto;
    private final EmployeeService employeeService;

    private final String pathForSaveXlsTemplate = "src/main/resources/xls_templates/profitLoss_templates/";

    public ProfitLossPrintModal(MoneySubProfitLossDto moneySubProfitLossDto, EmployeeService employeeService) {
        this.moneySubProfitLossDto = moneySubProfitLossDto;
        this.employeeService = employeeService;
        add(header(), configurePrintSelect(), valueSelectPrint(), footer());
    }

    private HorizontalLayout header() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        H3 title = new H3("Создание печатной формы");
        title.setHeight("1.2em");
        horizontalLayout.add(title);
        horizontalLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.END);
        return horizontalLayout;
    }

    private HorizontalLayout configurePrintSelect() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Создать печатную форму " +
                "по шаблону 'Прибыли и убытки'?");
        label.setWidth(LABEL_WIDTH);
        horizontalLayout.add(label);
        horizontalLayout.setSpacing(true);
        return horizontalLayout;
    }

    private ComboBox<String> valueSelectPrint() {
        ComboBox<String> print = new ComboBox<>();
        print.setWidth("300px");
        print.setPlaceholder("Печатная форма");
        print.setItems("Открыть в браузере", "Скачать в формате Excel", "Скачать в формате PDF");
        downloadXls(print);
        return print;
    }

    private List<File> getXlsFile() {
        File dir = new File(pathForSaveXlsTemplate);
        return Arrays.stream(Objects.requireNonNull(dir.listFiles()))
                .filter(File::isFile).filter(x -> x.getName().contains(".xls"))
                .collect(Collectors.toList());
    }

    private Anchor getLinkToProfitLossXls(File file) {
        String profitLossTemplate = file.getName();
        PrintProfitLossXls printProfitLossXls = new PrintProfitLossXls(
                file.getPath(), List.of(moneySubProfitLossDto), employeeService);
        return new Anchor(new StreamResource(profitLossTemplate, printProfitLossXls::createReport),
                "Скачать в формате Excel");
    }

    private Anchor getLinkToProfitLossPdf(File file) {
        String profitLossTemplate = file.getName().substring(0, file.getName().lastIndexOf(".")) + ".pdf";
        PrintProfitLossXls printProfitLossXls = new PrintProfitLossXls(
                file.getPath(), List.of(moneySubProfitLossDto), employeeService);
        return new Anchor(new StreamResource(profitLossTemplate, printProfitLossXls::createReportPDF), "Скачать в формате PDF");
    }

    private void downloadXls(ComboBox<String> print) {
        print.addValueChangeListener(x -> {
            if (x.getValue().equals("Скачать в формате Excel")) {
                getXlsFile().forEach(i -> add(getLinkToProfitLossXls(i)));
            } else if (x.getValue().equals("Скачать в формате PDF")) {
                getXlsFile().forEach(i -> add(getLinkToProfitLossPdf(i)));
            } else {
                close();
            }
        });
    }

    private HorizontalLayout footer() {
        HorizontalLayout footer = new HorizontalLayout();
        VerticalLayout cancelverticalLayout = new VerticalLayout();
        cancelverticalLayout.add(getCancelButton());
        cancelverticalLayout.setWidth("100px");
        cancelverticalLayout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.BASELINE);
        footer.add(cancelverticalLayout);
        footer.setHeight("100px");
        footer.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);
        return footer;
    }

    private Button getCancelButton() {
        return new Button("Отмена", new Icon(VaadinIcon.CLOSE), event -> {
            close();
        });
    }
}
