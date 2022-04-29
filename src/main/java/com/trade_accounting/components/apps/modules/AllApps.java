package com.trade_accounting.components.apps.modules;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
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
public class AllApps extends VerticalLayout {
    private final VerticalLayout bodyCategoriesAppsVertical = new VerticalLayout();
    private int sumCategories = TypeAppsEnum.values().length - 1;
    String srcBase64LogoApp_56X56 = "data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMTQiIGhlaWdodD0iMTQiIHZpZXdCb3g9IjAgMCAxNCAxNCIgZmlsbD0ibm9uZSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KPHBhdGggZD0iTTEzLjUgOFYxMi41QzEzLjUgMTIuNzY1MiAxMy4zOTQ2IDEzLjAxOTYgMTMuMjA3MSAxMy4yMDcxQzEzLjAxOTYgMTMuMzk0NiAxMi43NjUyIDEzLjUgMTIuNSAxMy41SDEuNUMxLjIzNDc4IDEzLjUgMC45ODA0MyAxMy4zOTQ2IDAuNzkyODkzIDEzLjIwNzFDMC42MDUzNTcgMTMuMDE5NiAwLjUgMTIuNzY1MiAwLjUgMTIuNVYxLjVDMC41IDEuMjM0NzggMC42MDUzNTcgMC45ODA0MyAwLjc5Mjg5MyAwLjc5Mjg5M0MwLjk4MDQzIDAuNjA1MzU3IDEuMjM0NzggMC41IDEuNSAwLjVINiIgc3Ryb2tlPSIjMDAwMDAxIiBzdHJva2UtbGluZWNhcD0icm91bmQiIHN0cm9rZS1saW5lam9pbj0icm91bmQiLz4KPHBhdGggZD0iTTEwIDAuNUgxMy41VjQiIHN0cm9rZT0iIzAwMDAwMSIgc3Ryb2tlLWxpbmVjYXA9InJvdW5kIiBzdHJva2UtbGluZWpvaW49InJvdW5kIi8+CjxwYXRoIGQ9Ik0xMy41IDAuNUw3IDciIHN0cm9rZT0iIzAwMDAwMSIgc3Ryb2tlLWxpbmVjYXA9InJvdW5kIiBzdHJva2UtbGluZWpvaW49InJvdW5kIi8+Cjwvc3ZnPgo=";
    String srcBase64LogoApp_90X90 = "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD/4QBqRXhpZgAASUkqAAgAAAADABIBAwABAAAAAQAAADEBAgARAAAAMgAAAGmHBAABAAAARAAAAAAAAABTaG90d2VsbCAwLjMwLjEwAAACAAKgCQABAAAAhwAAAAOgCQABAAAAhwAAAAAAAAD/4Qn0aHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wLwA8P3hwYWNrZXQgYmVnaW49Iu+7vyIgaWQ9Ilc1TTBNcENlaGlIenJlU3pOVGN6a2M5ZCI/PiA8eDp4bXBtZXRhIHhtbG5zOng9ImFkb2JlOm5zOm1ldGEvIiB4OnhtcHRrPSJYTVAgQ29yZSA0LjQuMC1FeGl2MiI+IDxyZGY6UkRGIHhtbG5zOnJkZj0iaHR0cDovL3d3dy53My5vcmcvMTk5OS8wMi8yMi1yZGYtc3ludGF4LW5zIyI+IDxyZGY6RGVzY3JpcHRpb24gcmRmOmFib3V0PSIiIHhtbG5zOmV4aWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20vZXhpZi8xLjAvIiB4bWxuczp0aWZmPSJodHRwOi8vbnMuYWRvYmUuY29tL3RpZmYvMS4wLyIgZXhpZjpQaXhlbFhEaW1lbnNpb249IjEzNSIgZXhpZjpQaXhlbFlEaW1lbnNpb249IjEzNSIgdGlmZjpJbWFnZVdpZHRoPSIxMzUiIHRpZmY6SW1hZ2VIZWlnaHQ9IjEzNSIgdGlmZjpPcmllbnRhdGlvbj0iMSIvPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDw/eHBhY2tldCBlbmQ9InciPz7/2wBDAAMCAgMCAgMDAwMEAwMEBQgF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";
    String srcBase64PayBlack = "data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMjAiIGhlaWdodD0iMjAiIHZpZXdCb3g9IjAgMCAyMCAyMCIgZmlsbD0ibm9uZSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KPHJlY3Qgd2lkdGg9IjIwIiBoZWlnaHQ9IjIwIiByeD0iMTAiIGZpbGw9IiMzMDMzMzQiLz4KPHBhdGggZD0iTTcuMjc2IDkuNTg0SDguMjcyVjUuNkgxMC41MDRDMTEuMDQgNS42IDExLjQ5NiA1LjY3NiAxMS44NzIgNS44MjhDMTIuMjQ4IDUuOTcyIDEyLjU1MiA2LjE2OCAxMi43ODQgNi40MTZDMTMuMDI0IDYuNjU2IDEzLjE5NiA2Ljk0IDEzLjMgNy4yNjhDMTMuNDEyIDcuNTg4IDEzLjQ2OCA3LjkyIDEzLjQ2OCA4LjI2NEMxMy40NjggOC42MTYgMTMuNDEyIDguOTQ4IDEzLjMgOS4yNkMxMy4xOTYgOS41NzIgMTMuMDI0IDkuODQ4IDEyLjc4NCAxMC4wODhDMTIuNTUyIDEwLjMyOCAxMi4yNDggMTAuNTIgMTEuODcyIDEwLjY2NEMxMS40OTYgMTAuOCAxMS4wNCAxMC44NjggMTAuNTA0IDEwLjg2OEg5LjgzMlYxMS43OEgxMS45NDRWMTMuMDE2SDkuODMyVjE0SDguMjcyVjEzLjAxNkg3LjI3NlYxMS43OEg4LjI3MlYxMC44NjhINy4yNzZWOS41ODRaTTEwLjM0OCA5LjU4NEMxMC41NjQgOS41ODQgMTAuNzY0IDkuNTY4IDEwLjk0OCA5LjUzNkMxMS4xNCA5LjQ5NiAxMS4zMDggOS40MjggMTEuNDUyIDkuMzMyQzExLjU5NiA5LjIzNiAxMS43MDggOS4xMDQgMTEuNzg4IDguOTM2QzExLjg2OCA4Ljc2IDExLjkwOCA4LjUzNiAxMS45MDggOC4yNjRDMTEuOTA4IDcuOTkyIDExLjg2OCA3Ljc2NCAxMS43ODggNy41OEMxMS43MDggNy4zOTYgMTEuNTk2IDcuMjUyIDExLjQ1MiA3LjE0OEMxMS4zMDggNy4wMzYgMTEuMTQgNi45NiAxMC45NDggNi45MkMxMC43NjQgNi44NzIgMTAuNTY0IDYuODQ4IDEwLjM0OCA2Ljg0OEg5LjgzMlY5LjU4NEgxMC4zNDhaIiBmaWxsPSJ3aGl0ZSIvPgo8L3N2Zz4K";
    String srcBase64PayBlue = "data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIyMCIgaGVpZ2h0PSIyMCIgdmlld0JveD0iMCAwIDIwIDIwIj4KICAgIDxnIGZpbGw9Im5vbmUiIGZpbGwtcnVsZT0iZXZlbm9kZCI+CiAgICAgICAgPGNpcmNsZSBjeD0iMTAiIGN5PSIxMCIgcj0iOS41IiBmaWxsPSIjMTg2OTk5IiBzdHJva2U9IiMxODY5OTkiLz4KICAgICAgICA8cGF0aCBmaWxsPSIjRkZGIiBmaWxsLXJ1bGU9Im5vbnplcm8iIGQ9Ik0xMS42NzQgNS41NTZjLjg5NyAwIDEuNTc1LjIzIDIuMDM2LjY5LjQ2LjQ2MS42OS45OTQuNjkgMS44MjQgMCAuODMtLjIzIDEuNDA2LS42OSAxLjg3MS0uNDYuNDY1LTEuMTQuNjkzLTIuMDM2LjY4NUg4Ljk2djEuMjI2aDIuOTM3di45OTVIOC45NnYxLjU5N0g3Ljc3OHYtMS41OTdINi4zNDJ2LS45OTVoMS40MzZ2LTEuMjI2SDYuMzQyVjkuNjNoMS40MzZWNS41NTZ6bS0uMzk4Ljk5Nkg4Ljk2VjkuNjNoMi4zMTZjLjY3Mi4wMDggMS4xNjQtLjEyOSAxLjQ3NS0uNDExLjMxMi0uMjgyLjQ2Ny0uNjE4LjQ2Ny0xLjE0OXMtLjE1NS0uODIzLS40NjctMS4xMDFjLS4zMS0uMjc4LS44MDMtLjQxNy0xLjQ3NS0uNDE3eiIvPgogICAgPC9nPgo8L3N2Zz4K";
    String srcImageAvatar = "src/main/resources/static/images/";
    String avatar = "";
    String altAvatar = "";
    String titleApp = "";


    public AllApps() {

        //Настраиваем стили главного контейнера для всех категорий приложений
        bodyCategoriesAppsVertical.getStyle()
                .set("min-width", "840px")
                .set("max-width", "1240px");

        configurationAllApps();
        add(bodyCategoriesAppsVertical);

    }








    /**
     * Создаем страницу с всеми категориями приложений
     */
    private void configurationAllApps() {
        List<Div> divListCategories = configurationDiv(sumCategories);

        //Настраиваем div категорий
        for (int i = 0; i < divListCategories.size(); i++) {
            //Когда будет реализована БД заменить рандом на РЕАЛЬНЫЕ цифры!
            int sumCardApps = 1 + (int) (Math.random() * 10);

            Div divCategory = divListCategories.get(i);
            divCategory.getStyle()
                    .set("width", "100%");
            divCategory.setId("category-" + (i + 1));




            //Создаем и оформляем заголовок ктегории
            Text headerCategory = new Text(TypeAppsEnum.values()[i + 1].getTypeApp() + " (" + sumCardApps + ")");
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
                nameApp.setClassName("anchorApp");
                divNameApp.add(nameApp);

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
                Image base64IconPay = image(srcBase64PayBlack);
                Span spanTextPay = new Span("x 12");
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


                //Создаем диалоговое окно
                //После того, как будет реализована БД заменить на динамическое значение!
                Dialog dialog = new Dialog();
                dialog.getElement().setAttribute("aria-label", "Add note");

                VerticalLayout dialogLayout = createDialogLayout(dialog);
                dialogLayout.getStyle().set("width", "422px");
                dialog.add(dialogLayout);
                dialog.setModal(false);
                dialog.setDraggable(true);

                //Добавляем прослушку клика на карточку
                div.addClickListener(e -> dialog.open());


                //Добавляем готовую карточку в категорию
                divBodyForCardsInCategory.add(div);

            });




            //Формируем состав раздела категории
            divCategory.add(divHeaderCategory, divBodyForCardsInCategory);
            //Добавляем категорию
            bodyCategoriesAppsVertical.add(divCategory);

        }

    }

    /**
     * Создаем экземпляр диалогового окна в VerticalLayout
     * @param dialog
     * @return - созданный объект диалогового окна в VerticalLayout
     */
    private VerticalLayout createDialogLayout(Dialog dialog) {

        Div divBodyDialog = new Div();
        divBodyDialog.getStyle()
                .set("height", "450px")
                .set("width", "422px");

        Div divHeader = new Div();
        Div divLogo = new Div();
        Image base64LogoApp = image(srcBase64LogoApp_90X90);
        Span spanNameApp = new Span();
        Span spanNameVendor = new Span();
        Div divPayApp = new Div();
        Span spanPay = new Span();
        Image base64IconPay = image(srcBase64PayBlue);
        Span spanTextPay = new Span();
        Div divLabelFreePeriod = new Div();
        Div divInstallApp = new Div();
        Button installAppButton = new Button("Установить");
        Span spanPermission = new Span();
        Span spanLabelDescription = new Span();
        Span spanDescriptionApp = new Span();
        Span spanLabelInfo = new Span();
        Div divInfoApp = new Div();
        Span spanDeveloper = new Span();
        Span spanNameDeveloper = new Span();
        Span spanSiteDeveloper = new Span();
        Span spanSiteDeveloperUrl = new Span();
        Span spanEmailDeveloper = new Span();
        Span spanEmailDeveloperAddress = new Span();


        //Составляем заголовок
        divLogo.add(base64LogoApp);
        divHeader.add(divLogo);

        spanNameApp.add(new Text("Интеграция с Сбер Эквайринг"));
        spanNameApp.getStyle()
                .set("display", "inline-block")
                .set("vertical-align", "top")
                .set("margin-left", "110px")
                .set("margin-top", "-100px")
                .set("color", "#000")
                .set("font-size", "14px")
                .set("font-weight", "bold");
        divHeader.add(spanNameApp);

        spanNameVendor.add(new Text("ООО «АЭМГРУП АВТОМАТИЗАЦИЯ»"));
        spanNameVendor.getStyle()
                .set("display", "inline-block")
                .set("vertical-align", "top")
                .set("margin-left", "110px")
                .set("margin-top", "-105px")
                .set("color", "#222")
                .set("font-size", "12px")
                .set("opacity", ".6");
        divHeader.add(spanNameVendor);

        spanPay.add(base64IconPay);
        divPayApp.add(spanPay);

        spanTextPay.add(new Text("x 12"));
        spanTextPay.getStyle()
                .set("display", "inline-block")
                .set("vertical-align", "top")
                .set("margin-left", "5px")
                .set("margin-top", "-2px")
                .set("color", "#186999")
                .set("font-size", "15px");
        divPayApp.add(spanTextPay);

        divLabelFreePeriod.add(new Text("14 дней бесплатно"));
        divLabelFreePeriod.getStyle()
                .set("display", "inline-block")
                .set("vertical-align", "top")
                .set("margin-left", "10px")
                .set("color", "#222")
                .set("font", " 12px 'Helvetica Neue',Helvetica,Arial,'Lucida Grande',sans-serif")
                .set("padding", "3px 7px")
                .set("border", "solid 1px #165587")
                .set("border-radius", "3px");
        divPayApp.add(divLabelFreePeriod);

        installAppButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        installAppButton.getStyle()
                .set("width", "85px")
                .set("height", "26px")
                .set("font", "12px 'Helvetica Neue',Helvetica,Arial,'Lucida Grande',sans-serif")
                .set("cursor", "pointer");
        divInstallApp.add(installAppButton);
        divPayApp.add(divInstallApp);

        divPayApp.getStyle()
                .set("display", "inline-block")
                .set("vertical-align", "top")
                .set("margin-left", "110px")
                .set("margin-top", "-105px");

        divHeader.add(divPayApp);


        //Создаем предупреждение
        spanPermission.add(new Text("Устанавливая приложение, вы соглашаетесь предоставить приложению доступ с правами администратора к данным вашего аккаунта. Приложение сможет как получать данные, так и изменять их."));
        spanPermission.getStyle()
                .set("display", "block")
                .set("margin-top", "-50px")
                .set("border", "solid 1px #ccd008")
                .set("border-radius", "3px")
                .set("background-color", "#fdffc1")
                .set("padding", "10px")
                .set("font", "12px 'Helvetica Neue',Helvetica,Arial,'Lucida Grande',sans-serif");


        //Создаем описание
        spanLabelDescription.add(new Text("Описание"));
        spanLabelDescription.getStyle()
                .set("display", "block")
                .set("margin", "20px 0 10px 0")
                .set("color", "#000")
                .set("font-size", "12px")
                .set("font-weight", "bold")
                .set("line-height", "1.67");

        spanDescriptionApp.add(new Text("Интеграция с Сбер Эквайринг — Приложение создает ссылку на оплату и делает возврат платежа с помощью кнопок в счете покупателю. Обновляет статусы счета покупателя при успешной оплате/возврате."));
        spanDescriptionApp.getStyle()
                .set("display", "block")
                .set("color", "#1a1a1a")
                .set("font-size", "12px")
                .set("line-height", "1.33");


        //Создаем информацию о приложении
        spanLabelInfo.add(new Text("Информация о приложении"));
        spanLabelInfo.getStyle()
                .set("display", "block")
                .set("margin", "20px 0 10px 0")
                .set("color", "#000")
                .set("font-size", "12px")
                .set("font-weight", "bold")
                .set("line-height", "1.67");


        spanDeveloper.add(new Text("Разработчик: "));
        spanDeveloper.getStyle()
                .set("display", "inline-block")
                .set("vertical-align", "top")
                .set("font-size", "12px")
                .set("font", "12px 'Helvetica Neue',Helvetica,Arial,'Lucida Grande',sans-serif")
                .set("color", "#000")
                .set("line-height", "2");
        divInfoApp.add(spanDeveloper);
        spanNameDeveloper.add(new Text("ООО «Фрэшкуб»"));
        spanNameDeveloper.getStyle()
                .set("display", "inline-block")
                .set("vertical-align", "top")
                .set("font-size", "12px")
                .set("font", "12px 'Helvetica Neue',Helvetica,Arial,'Lucida Grande',sans-serif")
                .set("color", "#000")
                .set("line-height", "2")
                .set("margin-left", "150px");
        divInfoApp.add(spanNameDeveloper);

        spanSiteDeveloper.add(new Text("Сайт разработчика: "));
        spanSiteDeveloper.getStyle()
                .set("display", "inline-block")
                .set("vertical-align", "top")
                .set("font-size", "12px")
                .set("font", "12px 'Helvetica Neue',Helvetica,Arial,'Lucida Grande',sans-serif")
                .set("color", "#000")
                .set("line-height", "2");
        divInfoApp.add(spanSiteDeveloper);

        Anchor urlDeveloper = anchor("https://www.freshcube.ru/", "www.freshcube.ru");
        urlDeveloper.getElement().setAttribute("target", "_blank");
        spanSiteDeveloperUrl.add(urlDeveloper);
        spanSiteDeveloperUrl.getStyle()
                .set("display", "inline-block")
                .set("vertical-align", "top")
                .set("font-size", "12px")
                .set("font", "12px 'Helvetica Neue',Helvetica,Arial,'Lucida Grande',sans-serif")
                .set("color", "#000")
                .set("line-height", "2")
                .set("margin-left", "115px")
                .set("text-decoration", "underline");
        divInfoApp.add(spanSiteDeveloperUrl);

        spanEmailDeveloper.add(new Text("Email разработчика: "));
        spanEmailDeveloper.getStyle()
                .set("display", "inline-block")
                .set("vertical-align", "top")
                .set("font-size", "12px")
                .set("font", "12px 'Helvetica Neue',Helvetica,Arial,'Lucida Grande',sans-serif")
                .set("color", "#000")
                .set("line-height", "2");
        divInfoApp.add(spanEmailDeveloper);

        spanEmailDeveloperAddress.add(anchor("mailto:support@freshcube.ru", "support@freshcube.ru"));
        spanEmailDeveloperAddress.getStyle()
                .set("display", "inline-block")
                .set("vertical-align", "top")
                .set("font-size", "12px")
                .set("font", "12px 'Helvetica Neue',Helvetica,Arial,'Lucida Grande',sans-serif")
                .set("color", "#000")
                .set("line-height", "2")
                .set("margin-left", "112px")
                .set("text-decoration", "underline");
        divInfoApp.add(spanEmailDeveloperAddress);


        divBodyDialog.add(divHeader, spanPermission, spanLabelDescription, spanDescriptionApp, spanLabelInfo, divInfoApp);


        Button cancelButton = new Button("Закрыть", new Icon("lumo", "cross"), e -> dialog.close());
        HorizontalLayout buttonLayout = new HorizontalLayout(cancelButton);
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        VerticalLayout dialogLayout = new VerticalLayout(divBodyDialog, buttonLayout);
        dialogLayout.setPadding(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);

        return dialogLayout;

    }

    /**
     * Создаем экземпляр List<Div>
     * @param sumDiv
     * @return - созданный объект List<Div>
     */
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
