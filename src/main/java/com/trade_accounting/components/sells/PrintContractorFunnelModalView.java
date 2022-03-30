package com.trade_accounting.components.sells;

import com.trade_accounting.services.interfaces.finance.FunnelService;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@UIScope
@SpringComponent
public class PrintContractorFunnelModalView extends Dialog {
    private final FunnelService funnelService;
    private final String pathForSaveXlsTemplate = "src/main/resources/xls_templates/sales_templates/funnel/contractor";

    public PrintContractorFunnelModalView(FunnelService funnelService) {
        this.funnelService = funnelService;
        add(headerLayout());
    }

    private VerticalLayout headerLayout() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(title(), valuePrint());
        return verticalLayout;
    }


    private H3 title() {
        return new H3("Создание печатной формы");
    }

    private Select<String> valuePrint() {
        Select<String> print = new Select<>();
        print.setPlaceholder("Выберите формат");
        getXlsFiles().forEach(x -> print.add(getLinkToXlsContractorTemplate(x)));
        getXlsFiles().forEach(x -> print.add(getLinkToPdfContractorTemplate(x)));
        getXlsFiles().forEach(x -> print.add(getLinkToOdsContractorTemplate(x)));
        return print;
    }

    private List<File> getXlsFiles() {
        File dir = new File(pathForSaveXlsTemplate);
        return Arrays.stream(Objects.requireNonNull(dir.listFiles())).filter(File::isFile).filter(x -> x.getName()
                .contains(".xls")).collect(Collectors.toList());
    }



    private Anchor getLinkToXlsContractorTemplate(File file) {
        String templateName = file.getName();
        PrintFunnelXls printFunnelXls = new PrintFunnelXls(file.getPath(), funnelService.getAllByType("contractor"));
        return new Anchor(new StreamResource(templateName, printFunnelXls::createReport), "Скачать в формате Excel");
    }

    private Anchor getLinkToPdfContractorTemplate(File file) {
        String templateName = file.getName().substring(0, file.getName().lastIndexOf(".")) + ".pdf";
        PrintFunnelXls printFunnelXls = new PrintFunnelXls(file.getPath(), funnelService.getAllByType("contractor"));
        return new Anchor(new StreamResource(templateName, printFunnelXls::createReportPDF), "Скачать в формате PDF");
    }

    private Anchor getLinkToOdsContractorTemplate(File file) {
        String templateName = file.getName().substring(0, file.getName().lastIndexOf(".")) + ".ods";
        PrintFunnelXls printFunnelXls = new PrintFunnelXls(file.getPath(), funnelService.getAllByType("contractor"));
        return new Anchor(new StreamResource(templateName, printFunnelXls::createReportODS), "Скачать в формате Office Calc");
    }

}
