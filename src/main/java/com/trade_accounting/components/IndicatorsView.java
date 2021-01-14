package com.trade_accounting.components;


        import com.vaadin.flow.component.html.Div;
        import com.vaadin.flow.component.html.Label;
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
        Tab indicatorsLayout = new Tab(new Label("Показатели"));
        Tab docsLayout = new Tab(new Label("Документы"));
        Tab binLayout = new Tab(new Label("Корзина"));
        Tab auditLayout = new Tab(new Label("Аудит"));
        Tab filesLayout = new Tab(new Label("Файлы"));


        return new Tabs(
                indicatorsLayout,
                docsLayout,
                binLayout,
                auditLayout,
                filesLayout
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
