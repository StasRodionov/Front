package com.trade_accounting.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.WrappedSession;
import lombok.extern.slf4j.Slf4j;


import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Route(value = "/")
@RouteAlias(value = "")
@PageTitle("Главная | CRM")
public class MainLayout extends VerticalLayout {

    public MainLayout() {

        // Картинка на фоне
        StreamResource bodyPicture = new StreamResource("german.png",
                () -> getImageInputStream("german.png"));
        Image bodyImage = new Image(bodyPicture, "");
        bodyImage.setId("german.png");
        bodyImage.getStyle()
                .set("margin", "auto")
                .set("margin-top", "10%")
                .set("position", "relative");

        // Кнопка возврата на домашнюю страницу
        StreamResource resource = new StreamResource("pictureForButtonWhichReturnsToHomePage.png",
                () -> getImageInputStream("pictureForButtonWhichReturnsToHomePage.png"));
        Image image = new Image(resource, "");
        image.setId("pictureForButtonWhichReturnsToHomePage");
        image.setHeight("80px");
        image.setWidth("80px");
        Button home = new Button("", e -> getUI().ifPresent(ui -> ui.navigate("")));
        home.setHeight("80px");
        home.setWidth("80px");
        home.setIcon(image);

        // Кнопка авторизация
        Button auth = new Button("Вход", e -> getUI().ifPresent(ui -> ui.navigate("login")));

        // Кнопка регистрация
        Button registration = new Button("Регистрация", e -> getUI().ifPresent(ui -> ui.navigate("registration")));
        registration.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        // Кнопка Начать работу
        Button startWork = new Button("Начать работу");
        startWork.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        startWork.setHeight("60px");
        startWork.setWidth("300px");
        WrappedSession wrappedSession = VaadinSession.getCurrent().getSession();

        // Оформуха для кнопок авторизации и регистрации
        HorizontalLayout horizontalLayout = new HorizontalLayout(auth, registration);
        horizontalLayout.getStyle()
                .set("right", "var(--lumo-space-l)")
                .set("position", "fixed")
                .set("top", "0");
        horizontalLayout.setSizeFull();
        horizontalLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        // Оформуха для кнопки Начать работу
        VerticalLayout verticalLayout = new VerticalLayout(startWork);
        verticalLayout.getStyle()
                .set("position", "fixed")
                .set("top", "300px")
                .set("left", "200px");

        // Бар
        H1 title = new H1(home);
        title.getStyle()
                .set("font-size", "var(--lumo-font-size-l)")
                .set("left", "var(--lumo-space-xl)")
                .set("margin", "0")
                .set("position", "absolute");
        Tabs tabs = getTabs();

        // Заполнение страницы
        add(horizontalLayout, verticalLayout, title, tabs, bodyImage);

    }

    // Добавление вкладок
    private Tabs getTabs (){
        Tabs tabs = new Tabs();
        tabs.getStyle().set("margin", "auto");
        tabs.add(
                createTab("Возможности"),
                createTab("Тарифы"),
                createTab("Поддержка"),
                createTab("Маркетплейсы"),
                createTab("Блог")
        );
        return tabs;
    }

    // Создание вкладок
    public Tab createTab(String viewName) {
        RouterLink link = new RouterLink();
        link.add(viewName);
        link.setTabIndex(0);
        return new Tab(link);
    }

    // Метод, достающий картинку
    private static InputStream getImageInputStream(String svgIconName) {
        InputStream imageInputStream = null;
        try {
            imageInputStream = new DataInputStream(new FileInputStream("src/main/resources/static/icons/" + svgIconName));
        } catch (IOException ex) {
            log.error("При чтении icon {} произошла ошибка", svgIconName);
        }
        return imageInputStream;
    }
}
