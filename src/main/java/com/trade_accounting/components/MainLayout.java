package com.trade_accounting.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

@Route(value = "/")
@RouteAlias(value = "")
@PageTitle("Главная | CRM")
public class MainLayout extends VerticalLayout {

    public MainLayout() {

        Button warning = new Button("Это главная страница сайта, необходимо ее реализовать!");
        Button auth = new Button("Войти в личный кабинет");
        auth.addClickListener(e -> auth.getUI().ifPresent(ui -> ui.navigate("login")));
        add(warning, auth);


        //Что я бы решил дальше:
        //1. Рассортировал классы в таких папках как sells, profile, goods и т.д. Все что отвечает за front (имеет @Route) в идеологически понятную папку,
        // все остальные классы тоже сгруппировать по другим папкам

        //2. Если опираться на moysklad.ru, то его кабинет лежит на поддомене app, будет не верным решением оставлять личный кабинет и основную страницу на одном и томже домене
        //Для ЛК нужен поддомен, поэтому всем страницам нужно его добавить

        //3. В форме авторизации нет возможности закешить логин и пароль. Если это возможно, было бы неплохо сделать

        //4. Если ходить по сайту, остановиться, к примеру, на /app/purchases, перезагрузить сервер и потом перезагрузить данную страницу,
        // то место редиректа будет ошибка. Возможно дело не в коде авторизации а в создании бинов

        //5. Нет страницы 404, если перейти на несуществующий адрес, то будет выведет полный список страниц которые есть, а не страница 404
        //Об этом упоминалось тут: RedirectRouteNotFoundError.java


        //Что почитать:
        //https://vaadin.com/blog/securing-vaadin-apps-with-spring-security-best-practices
        //https://vaadin.com/docs/v14/flow/tutorial/login-and-authentication



    }
}
