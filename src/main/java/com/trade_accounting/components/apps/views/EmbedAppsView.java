package com.trade_accounting.components.apps.views;


import com.trade_accounting.components.AppView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.trade_accounting.config.SecurityConstants.BODY_URL;


//https://vaadin.com/docs/latest/flow/tutorial/login-and-authentication
//https://www.cyberforum.ru/java-j2ee/thread1881954.html
//https://vaadin.com/docs/v8/framework/layout/layout-orderedlayout
//https://vaadin.com/docs/v14/flow/routing/tutorial-routing-url-generation
//https://vaadin.com/docs/latest/flow/tutorial/navigation-and-layouts
@Slf4j
@Route(value = BODY_URL + "embed-apps", layout = AppView.class)
@PageTitle("Приложения")
@SpringComponent
@UIScope
public class EmbedAppsView extends VerticalLayout {
    private final String SRC_IMAGE_APPS_NOT_FOUND = "src/main/resources/static/images/";
    private final String APPS_NOT_FOUND = "apps-notfound.png";
    private final String NO_APPS_INSTALLED = "Ни одного приложения не установлено.";
    private final String TRY_INSTALL_APP_LINK = "Попробуйте установить приложение по ссылке ниже.";

    public EmbedAppsView() {
        setHeight("100%");
        add(configureAppsNotFound());
    }




    private VerticalLayout configureAppsNotFound() {
        VerticalLayout verticalBody = new VerticalLayout();
        verticalBody.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        verticalBody.setJustifyContentMode(JustifyContentMode.CENTER);
        verticalBody.setHeight("100%");

        Image image = image();
        Div div1 = div(text(NO_APPS_INSTALLED));
        Div div2 = div(text(TRY_INSTALL_APP_LINK));
        Anchor anchor = anchor("app/apps", "Выбрать приложение");

        image.getStyle()
                .set("margin-bottom", "40px");
        div1.getStyle()
                .set("opacity", ".6")
                .set("font-size", "18px")
                .set("line-height", "0")
                .set("color", "#282e30");
        div2.getStyle()
                .set("opacity", ".6")
                .set("font-size", "18px")
                .set("line-height", "1.33")
                .set("color", "#282e30");
        anchor.getStyle()
                .set("font-size", "18px")
                .set("color", "#3a74a3")
                .set("text-decoration", "none");

        verticalBody.add(image, div1, div2, anchor);
        return verticalBody;
    }


    /**
     * Создаем экземпляр Div
     * @param components
     * @return - созданный объект Div
     */
    private Div div(Component... components) {
        return new Div(components);
    }

    /**
     * Создаем экземпляр Anchor
     * @param url
     * @param text
     * @return - созданный объект Anchor
     */
    private Anchor anchor(String url, String text) {
        return new Anchor(url, text);
    }

    /**
     * Создаем экземпляр Text
     * @param text
     * @return - созданный объект Text
     */
    private Text text(String text) {
        return new Text(text);
    }

    /**
     * Создаем экземпляр Image
     * @return - созданный объект Image
     */
    private Image image() {
        StreamResource resource = new StreamResource(APPS_NOT_FOUND, () -> getImageInputStream(SRC_IMAGE_APPS_NOT_FOUND, APPS_NOT_FOUND));
        Image logo = new Image(resource, "apps-notfound");
        logo.setId("apps-notfound");
        logo.getStyle().set("height", "96px");
        logo.getStyle().set("width", "128px");
        return logo;
    }

    public static InputStream getImageInputStream(String src, String imageName) {
        InputStream imageInputStream = null;
        try {
            imageInputStream = new DataInputStream(new FileInputStream(src + imageName));
        } catch (IOException ex) {
            log.error("При чтении images {} произошла ошибка", imageName);
        }
        return imageInputStream;
    }

}
