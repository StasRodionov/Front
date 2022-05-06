package com.trade_accounting.components.errors;

import com.trade_accounting.components.AppView;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import static com.trade_accounting.config.SecurityConstants.*;

@Slf4j
@Route(value = ERROR_404)
@PageTitle("ИмяПроекта - HTTP Status 404")
@SpringComponent
@UIScope
public class NotFoundErrorView extends VerticalLayout {
    private final String SRC_IMAGE_PAGE_NOT_FOUND = "src/main/resources/static/images/";
    private final String PAGE_NOT_FOUND = "page-notfound.svg";

    public NotFoundErrorView() {
        setHeight("100%");
        add(configurePageNotFoundError());
    }




    private HorizontalLayout configurePageNotFoundError() {
        String path = "";
        try {
            path = getLocation().getPath();
        } catch (URISyntaxException e) {
            log.error("При получении URL открытой страницы произошла ошибка", e);
        }
        HorizontalLayout horizontalBody = new HorizontalLayout();
        horizontalBody.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        horizontalBody.setJustifyContentMode(JustifyContentMode.CENTER);
        horizontalBody.setHeight("100%");
        horizontalBody.setWidth("100%");
        Image image = image();
        Span spanTextURL = new Span(new Text(path));
        Div bodyErrorPage = new Div();
        Div errorPageTextContent = new Div();
        Div headerErrorPageTextContent = new Div();
        Div descriptionErrorPageTextContent = new Div();
        Div anchorErrorPageTextContent = new Div();
        Anchor anchor = anchor("", "Вернуться на главную");
        Div errorPageImageContent = new Div();


        //Добавляем в div с текстовой информацией необходимые элементы
        errorPageTextContent.getStyle()
                .set("display", "inline-block")
                .set("vertical-align", "top")
                .set("max-width", "464px")
                .set("font-family", "'Helvetica Neue', Helvetica, Arial, 'Lucida Grande', sans-serif")
                .set("font-size", "1em")
                .set("line-height", "1.33")
                .set("text-align", "left")
                .set("color", "#282e30")
                .set("white-space", "pre-wrap");
        spanTextURL.getStyle().set("font-weight", "bold");

        //Добавляем заголовок
        headerErrorPageTextContent.add(new Text("Страница не найдена"));
        headerErrorPageTextContent.getStyle()
                .set("font-size", "2em")
                .set("font-weight", "bold")
                .set("line-height", "1.2")
                .set("text-align", "left")
                .set("color", "#282e30")
                .set("white-space", "nowrap")
                .set("margin-bottom", "1em");

        //Добавляем текст с описанием
        descriptionErrorPageTextContent.add(new Text("Запрашиваемая страница "),
                spanTextURL,
                new Text(" не найдена.\nВозможно данной страницы никогда не было, либо она была перемещена."));
        descriptionErrorPageTextContent.getStyle().set("margin-bottom", "1em");

        //Добавляем ссылку на главную страницу
        anchorErrorPageTextContent.add(anchor);

        //Собираем все элементы в div с текстовой информацией
        errorPageTextContent.add(headerErrorPageTextContent,
                descriptionErrorPageTextContent,
                anchorErrorPageTextContent);


        //Добавляем в div с изображением необходимые элементы
        errorPageImageContent.getStyle()
                .set("display", "inline-block")
                .set("vertical-align", "top");
        errorPageImageContent.add(image);


        //Добавляем весь контент в основной div
        bodyErrorPage.add(errorPageTextContent, errorPageImageContent);
        horizontalBody.add(bodyErrorPage);

        return horizontalBody;

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
     * Создаем экземпляр Image
     * @return - созданный объект Image
     */
    private Image image() {
        StreamResource resource = new StreamResource(PAGE_NOT_FOUND, () -> getImageInputStream(SRC_IMAGE_PAGE_NOT_FOUND, PAGE_NOT_FOUND));
        Image logo = new Image(resource, "page-notfound");
        logo.setId("page-notfound");
        logo.getStyle().set("height", "450px");
        logo.getStyle().set("width", "573px");
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

    //https://github.com/vaadin/flow/issues/1897
    public static URI getLocation() throws URISyntaxException {
        VaadinServletRequest request = (VaadinServletRequest) VaadinService.getCurrentRequest();
        StringBuffer uriString = request.getRequestURL();
        return new URI(uriString.toString());
    }

}
