package com.trade_accounting.components;


import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "indicators", layout = AppView.class)
@PageTitle("Показатели")
public class IndicatorsView extends Div implements AfterNavigationObserver {

    IndicatorsView() {
        add(configurationSubMenu());
    }

    private Tabs configurationSubMenu() {
        HorizontalLayout indicatorsLayout = new HorizontalLayout(new Label("Показатели"));
        HorizontalLayout docsLayout = new HorizontalLayout(new Label("Документы"));
        HorizontalLayout binLayout = new HorizontalLayout(new Label("Корзина"));
        HorizontalLayout auditLayout = new HorizontalLayout(new Label("Аудит"));
        HorizontalLayout filesLayout = new HorizontalLayout(new Label("Файлы"));


        return new Tabs(
                new Tab(indicatorsLayout),
                new Tab(docsLayout),
                new Tab(binLayout),
                new Tab(auditLayout),
                new Tab(filesLayout)
        );

    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        AppView appView = (AppView) afterNavigationEvent.getActiveChain().get(1);
        appView.getChildren().forEach(e -> {
            if (e.getClass() == Tabs.class) {
                ((Tabs) e).setSelectedIndex(0);
            }
        });
    }
}
