package com.trade_accounting.components.apps.views;

import com.trade_accounting.components.apps.modules.TypeAppsEnum;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


//https://vaden-pro.ru/blog/css/razmeshchenie-blokov-div-gorizontalno
//https://vaadin.com/vaadin-reference-card
@Slf4j
@SpringComponent
@UIScope
public class AllAppsView extends VerticalLayout {
    private final VerticalLayout bodyCategoriesAppsVertical = new VerticalLayout();
    private int sumCategories = TypeAppsEnum.values().length - 1;
    String srcBase64LogoApp_56X56 = "data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMTQiIGhlaWdodD0iMTQiIHZpZXdCb3g9IjAgMCAxNCAxNCIgZmlsbD0ibm9uZSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KPHBhdGggZD0iTTEzLjUgOFYxMi41QzEzLjUgMTIuNzY1MiAxMy4zOTQ2IDEzLjAxOTYgMTMuMjA3MSAxMy4yMDcxQzEzLjAxOTYgMTMuMzk0NiAxMi43NjUyIDEzLjUgMTIuNSAxMy41SDEuNUMxLjIzNDc4IDEzLjUgMC45ODA0MyAxMy4zOTQ2IDAuNzkyODkzIDEzLjIwNzFDMC42MDUzNTcgMTMuMDE5NiAwLjUgMTIuNzY1MiAwLjUgMTIuNVYxLjVDMC41IDEuMjM0NzggMC42MDUzNTcgMC45ODA0MyAwLjc5Mjg5MyAwLjc5Mjg5M0MwLjk4MDQzIDAuNjA1MzU3IDEuMjM0NzggMC41IDEuNSAwLjVINiIgc3Ryb2tlPSIjMDAwMDAxIiBzdHJva2UtbGluZWNhcD0icm91bmQiIHN0cm9rZS1saW5lam9pbj0icm91bmQiLz4KPHBhdGggZD0iTTEwIDAuNUgxMy41VjQiIHN0cm9rZT0iIzAwMDAwMSIgc3Ryb2tlLWxpbmVjYXA9InJvdW5kIiBzdHJva2UtbGluZWpvaW49InJvdW5kIi8+CjxwYXRoIGQ9Ik0xMy41IDAuNUw3IDciIHN0cm9rZT0iIzAwMDAwMSIgc3Ryb2tlLWxpbmVjYXA9InJvdW5kIiBzdHJva2UtbGluZWpvaW49InJvdW5kIi8+Cjwvc3ZnPgo=";
    String srcBase64Pay = "data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMjAiIGhlaWdodD0iMjAiIHZpZXdCb3g9IjAgMCAyMCAyMCIgZmlsbD0ibm9uZSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KPHJlY3Qgd2lkdGg9IjIwIiBoZWlnaHQ9IjIwIiByeD0iMTAiIGZpbGw9IiMzMDMzMzQiLz4KPHBhdGggZD0iTTcuMjc2IDkuNTg0SDguMjcyVjUuNkgxMC41MDRDMTEuMDQgNS42IDExLjQ5NiA1LjY3NiAxMS44NzIgNS44MjhDMTIuMjQ4IDUuOTcyIDEyLjU1MiA2LjE2OCAxMi43ODQgNi40MTZDMTMuMDI0IDYuNjU2IDEzLjE5NiA2Ljk0IDEzLjMgNy4yNjhDMTMuNDEyIDcuNTg4IDEzLjQ2OCA3LjkyIDEzLjQ2OCA4LjI2NEMxMy40NjggOC42MTYgMTMuNDEyIDguOTQ4IDEzLjMgOS4yNkMxMy4xOTYgOS41NzIgMTMuMDI0IDkuODQ4IDEyLjc4NCAxMC4wODhDMTIuNTUyIDEwLjMyOCAxMi4yNDggMTAuNTIgMTEuODcyIDEwLjY2NEMxMS40OTYgMTAuOCAxMS4wNCAxMC44NjggMTAuNTA0IDEwLjg2OEg5LjgzMlYxMS43OEgxMS45NDRWMTMuMDE2SDkuODMyVjE0SDguMjcyVjEzLjAxNkg3LjI3NlYxMS43OEg4LjI3MlYxMC44NjhINy4yNzZWOS41ODRaTTEwLjM0OCA5LjU4NEMxMC41NjQgOS41ODQgMTAuNzY0IDkuNTY4IDEwLjk0OCA5LjUzNkMxMS4xNCA5LjQ5NiAxMS4zMDggOS40MjggMTEuNDUyIDkuMzMyQzExLjU5NiA5LjIzNiAxMS43MDggOS4xMDQgMTEuNzg4IDguOTM2QzExLjg2OCA4Ljc2IDExLjkwOCA4LjUzNiAxMS45MDggOC4yNjRDMTEuOTA4IDcuOTkyIDExLjg2OCA3Ljc2NCAxMS43ODggNy41OEMxMS43MDggNy4zOTYgMTEuNTk2IDcuMjUyIDExLjQ1MiA3LjE0OEMxMS4zMDggNy4wMzYgMTEuMTQgNi45NiAxMC45NDggNi45MkMxMC43NjQgNi44NzIgMTAuNTY0IDYuODQ4IDEwLjM0OCA2Ljg0OEg5LjgzMlY5LjU4NEgxMC4zNDhaIiBmaWxsPSJ3aGl0ZSIvPgo8L3N2Zz4K";
    String srcImageAvatar = "src/main/resources/static/images/";
    String avatar = "";
    String altAvatar = "";
    String titleApp = "";


    public AllAppsView() {

        //Настраиваем стили главного контейнера для всех категорий приложений
        bodyCategoriesAppsVertical.getStyle()
                .set("min-width", "840px")
                .set("max-width", "1240px");

        configurationAllApps();
        add(bodyCategoriesAppsVertical);

    }








    private void configurationAllApps() {
        List<Div> divListCategories = configurationDiv(sumCategories);

        //Настраиваем div категорий
        for (int i = 0; i < divListCategories.size(); i++) {
            //Когда будет реализована БД заменить рандом на РЕАЛЬНЫЕ цифры!
            int sumCardApps = 1 + (int) (Math.random() * 10);

            Div divCategory = divListCategories.get(i);
            divCategory.getStyle()
                    .set("width", "100%");




            //Создаем и оформляем заголовок ктегории
            Text headerCategory = new Text(TypeAppsEnum.values()[i].getTypeApp() + " (" + sumCardApps + ")");
            Div divHeaderCategory = new Div();
            divHeaderCategory.getStyle()
                    .set("font-weight", "700")
                    .set("font-size", "20px")
                    .set("margin-bottom", "24px")
                    .set("color", "#323034");
            divHeaderCategory.add(headerCategory);




            //Создаем и оформляем карточки в категории
            Div divBodyForCardsInCategory = new Div();
            List<Div> divListCardApps = configurationDiv(sumCardApps);
            divBodyForCardsInCategory.getStyle().set("margin-bottom", "48px");

            divListCardApps.forEach(div -> {

                //Задаем стили для карточки
                div.getStyle()
                        .set("border", "1px solid #e7e9ec")
                        .set("border-radius", "8px")
                        .set("padding", "16px 16px 12px 16px")
                        .set("cursor", "pointer")

                        .set("margin", "10px")
                        .set("width", "346px")
                        .set("height", "118px")
                        .set("display", "inline-block");
                div.getElement()
                        .setAttribute("onmouseover", "this.style.boxShadow='5px 5px 10px #E7E9EC'")
                        .setAttribute("onmouseout", "this.style.boxShadow='none'");
                div.setClassName("cardApp");


                //Добавляем к карточке div с аватаркой
                //После того, как будет реализована БД заменить на динамические параметры!
                avatar = "avatar.jpg";
                altAvatar = "PayAnyWay";
                Image image = image();
                Div divIconForCard = new Div();
                divIconForCard.getStyle()
                        .set("display", "inline-block")
                        .set("margin-bottom", "50px");
                divIconForCard.add(image);
                div.add(divIconForCard);


                //Добавляем к карточке div c именем приложения
                //После того, как будет реализована БД заменить на динамические значения!
                //https://vaadin.com/directory/component/htmlattributehandler
                Anchor nameApp = anchor("https://www.amgbp.ru/tpost/rcji8krob1-integratsiya-s-sber-ekvairing", "Интеграция с Сбер Эквайринг");
                Image base64LogoApp = image(srcBase64LogoApp_56X56);
                Div divNameApp = new Div();
                divNameApp.getStyle()
                        .set("display", "inline-block")
                        .set("vertical-align", "top")
                        .set("width", "276px")
                        .set("height", "22px")
                        .set("margin-left", "14px")
                        .set("margin-top", "-5px")

                        .set("white-space", "nowrap") //????
                        .set("overflow", "hidden")
                        .set("text-overflow", "ellipsis");

                nameApp.getStyle()
                        .set("width", "276px")
                        .set("font-weight", "700")
                        .set("font-size", "16px")
                        .set("color", "#303334")
                        .set("text-decoration", "none");
                nameApp.getElement()
                        .setAttribute("onmouseover", "this.style.color='#107782'")
                        .setAttribute("onmouseout", "this.style.color='#303334'")
                        .setAttribute("target", "_blank");
                divNameApp.add(nameApp);

                //ПОКА ДЛИННЫЙ ТЕКСТ ДВИГАЕТ ЗНАЧЕК ВПРАВО
                base64LogoApp.getStyle()
                        .set("margin-left", "10px");
                divNameApp.add(base64LogoApp);
                div.add(divNameApp);


                //Добавляем к карточке div c описанием приложения
                //После того, как будет реализована БД заменить на динамическое значение!
                Div divTitleApp = new Div();
                titleApp = "Приложение создает ссылку на оплату и \nделает возврат платежа с помощью \nкнопок в счете покупателю. Обновляет статусы счета покупателя при успешной оплате/возврате.";
                Text textApp = new Text(titleApp);
                divTitleApp.setTitle(titleApp);
                divTitleApp.getStyle()
                        .set("display", "inline-block")
                        .set("vertical-align", "top")
                        .set("width", "276px")
                        .set("height", "50px")
                        .set("margin-left", "70px")
                        .set("margin-top", "-85px")
                        .set("font-size", "14px")
                        .set("line-height", "1.2")

                        .set("white-space", "pre")
                        .set("overflow", "hidden")
                        .set("text-overflow", "ellipsis");
                divTitleApp.add(textApp);
                div.add(divTitleApp);


                //Добавляем к карточке div c условиями его использования
                //После того, как будет реализована БД заменить на динамическое значение!
                Div divConditionsApp = new Div();
                Image base64IconPay = image(srcBase64Pay);
                Span spanTextPay = new Span("x 2");
                Div divLabelFreePeriod = new Div();
                Text textFreePeriod = new Text("14 дней бесплатно");
                divConditionsApp.getStyle()
                        .set("display", "inline-block")
                        .set("vertical-align", "top")
                        .set("width", "276px")
                        .set("height", "21px")
                        .set("margin-left", "70px")
                        .set("margin-top", "-45px");
                divConditionsApp.add(base64IconPay);

                spanTextPay.getStyle()
                        .set("color", "#303334")
                        .set("font-size", "14px")
                        .set("font-weight", "700")
                        .set("margin-left", "5px")
                        .set("vertical-align", "top")
                        .set("margin-right", "10px");
                divConditionsApp.add(spanTextPay);

                divLabelFreePeriod.getStyle()
                        .set("display", "inline-block")
                        .set("vertical-align", "top")
                        .set("border-radius", "16px")
                        .set("padding", "0 12px")
                        .set("height", "20px")
                        .set("color", "#1ea273")
                        .set("background", "#dff3df")
                        .set("width", "115px")
                        .set("height", "21px")
                        .set("font-size", "12px")
                        .set("text-align", "center")
                        .set("line-height", "20px");
                divLabelFreePeriod.add(textFreePeriod);
                divConditionsApp.add(divLabelFreePeriod);
                div.add(divConditionsApp);











                Dialog dialog = new Dialog();
                dialog.getElement().setAttribute("aria-label", "Add note");

                VerticalLayout dialogLayout = createDialogLayout(dialog);
                dialog.add(dialogLayout);
                dialog.setModal(false);
                dialog.setDraggable(true);



                div.addClickListener(e -> dialog.open());


                /*div.addClickListener(clickEvent -> {
                    Notification notification = Notification.show("Application submitted!");
                    notification.addThemeVariants(NotificationVariant.LUMO_CONTRAST);
                    notification.setPosition(Notification.Position.MIDDLE);
                    notification.setDuration(0);


                    //notification.addDetachListener(detachEvent -> button.setEnabled(true));
                });*/






                divBodyForCardsInCategory.add(div);

            });



















            //Формируем состав раздела категории
            divCategory.add(divHeaderCategory, divBodyForCardsInCategory);
            //Добавляем категорию
            bodyCategoriesAppsVertical.add(divCategory);

        }

    }







    private VerticalLayout createDialogLayout(Dialog dialog) {

        Div divBodyDialog = new Div();


        Div divHeader = new Div();
            Div divLogo = new Div();
            Span spanNameApp = new Span();
            Span spanNameVendor = new Span();
            Div divPayApp = new Div();
                Span spanPay = new Span();
                Span spanTextPay = new Span();
                Div divLabelFreePeriod = new Div();
                Text textFreePeriod = new Text("14 дней бесплатно");
















        H2 headline = new H2("Add note");
        headline.getStyle().set("margin", "0").set("font-size", "1.5em")
                .set("font-weight", "bold");
        HorizontalLayout header = new HorizontalLayout(headline);
        header.getElement().getClassList().add("draggable");
        header.setSpacing(false);
        header.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("cursor", "move");
        // Use negative margins to make draggable header stretch over full width,
        // covering the padding of the dialog
        header.getStyle()
                .set("padding", "var(--lumo-space-m) var(--lumo-space-l)")
                .set("margin",
                        "calc(var(--lumo-space-s) * -1) calc(var(--lumo-space-l) * -1) 0");

        TextField titleField = new TextField("Title");
        TextArea descriptionArea = new TextArea("Description");
        VerticalLayout fieldLayout = new VerticalLayout(titleField,
                descriptionArea);
        fieldLayout.setSpacing(false);
        fieldLayout.setPadding(false);
        fieldLayout.setAlignItems(FlexComponent.Alignment.STRETCH);

        Button cancelButton = new Button(new Icon("lumo", "cross"), e -> dialog.close());
        Button saveButton = new Button("Add note", e -> dialog.close());
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        HorizontalLayout buttonLayout = new HorizontalLayout(cancelButton,
                saveButton);
        buttonLayout
                .setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        VerticalLayout dialogLayout = new VerticalLayout(header, fieldLayout,
                buttonLayout);
        dialogLayout.setPadding(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "300px").set("max-width", "100%");

        return dialogLayout;
    }














    private List<Div> configurationDiv(int sumDiv) {
         List<Div> divList = new ArrayList<>();
        for (int i = 0; i < sumDiv; i++) {
            divList.add(new Div());
        }
        return divList;
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
        StreamResource resource = new StreamResource(avatar, () -> getImageInputStream(srcImageAvatar, avatar));
        Image logo = new Image(resource, altAvatar);
        logo.setClassName("card-avatar");
        logo.getStyle().set("height", "56px");
        logo.getStyle().set("width", "56px");
        return logo;
    }

    private Image image(String srcBase64) {
        Image logo = new Image();
        logo.setSrc(srcBase64);
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
