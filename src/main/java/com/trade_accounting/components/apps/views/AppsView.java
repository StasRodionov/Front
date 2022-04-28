package com.trade_accounting.components.apps.views;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.apps.modules.TypeAppsEnum;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.trade_accounting.config.SecurityConstants.BODY_URL;

//https://vaadin.com/docs/v14/ds/components/basic-layouts
//https://vaadin.com/docs/latest/ds/components/tabs
@Slf4j
@Route(value = BODY_URL + "apps", layout = AppView.class)
@PageTitle("Приложения")
@SpringComponent
@UIScope
public class AppsView  extends Div implements AfterNavigationObserver {
    private final HorizontalLayout bodyHorizontal = new HorizontalLayout();
    private final Div div = new Div();
    private final AllAppsView allAppsView;


    @Autowired
    public AppsView(AllAppsView allAppsView) {
        this.allAppsView = allAppsView;

        //https://vaadin.com/forum/thread/17800495/vaadin-14-javascript
        UI.getCurrent().getPage().addJavaScript("js/AppsView.js");
        //Растягиваем контейнер на всю ширину
        div.getStyle().set("width", "100%");
        //Добавляем ранее созданный пустой объект Div контейнер, нужен для того,
        //чтобы в последующем после загрузки страницы мы имели контейнер в который могли бы что-то положить в ответ на действия юзера
        configurationHeader().forEach(this::add);
        bodyHorizontal.add(configurationMenu(), div);
        add(bodyHorizontal);

    }




    private List<Component> configurationHeader() {

        Text header = new Text("Приложения");
        Div divHeader = new Div();
        divHeader.add(header);
        divHeader.getStyle()
                .set("font-weight", "700")
                .set("font-size", "30px")
                .set("color", "#222222")
                .set("margin-left", "16px")
                .set("margin-top", "22px")
                .set("margin-bottom", "12px");

        Div divDelimiter = new Div();
        divDelimiter.getStyle()
                .set("border-bottom", "1px solid #e3e3e3")
                .set("position", "relative");

        List<Component> listComponents = new ArrayList<>();
        listComponents.add(divHeader);
        listComponents.add(divDelimiter);

        return listComponents;

    }

    private Tabs configurationMenu() {

        Tabs tabsCategoryApps = new Tabs();
        TypeAppsEnum[] typeAppsEnum = TypeAppsEnum.values();
        Arrays.stream(typeAppsEnum).forEach(category -> tabsCategoryApps.add(new Tab(category.getTypeApp())));

        tabsCategoryApps.setOrientation(Tabs.Orientation.VERTICAL);
        tabsCategoryApps.getStyle()
                .set("height", "100%")
                .set("width", "230px")
                .set("box-shadow", "none")
                .set("margin-top", "22px");

        tabsCategoryApps.setSelectedIndex(0);
        tabsCategoryApps.setId("menuApps");

        tabsCategoryApps.addSelectedChangeListener(event -> {
            Tab tab = event.getSelectedTab();
            if (tab.getLabel().equals(TypeAppsEnum.ALL_APPS.getTypeApp())) {
                div.removeAll();
                div.add(allAppsView);
            }
        });

        return tabsCategoryApps;

    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        //Получаем активную цепочку, которая у нас есть после навигации.
        //Выбираем из нее AppView
        AppView appView = (AppView) afterNavigationEvent.getActiveChain().get(1);

        //Перебираем дочерние объекты выбранной страницы и ищем верхнее меню
        appView.getChildren().forEach(e -> {
            if (e.getClass() == Tabs.class) {
                //Переводим фокус с первого таба меню на таб меню "Приложения"
                //Если этого не делать, то каждый раз будет курсор переводиться на первый таб
                ((Tabs) e).setSelectedIndex(9);
            }
        });
        //Указываем, что будет отображаться на странице при ее загрузке
        getUI().ifPresent(ui -> {
            div.removeAll();
            div.add(allAppsView);
        });

    }
}
