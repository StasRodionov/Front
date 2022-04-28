package com.trade_accounting.components.apps.modules;

public enum TypeAppsEnum {

    ALL_APPS("Все приложения"),
    NEW_APPS("Новые"),
    CRM_APPS("CRM"),
    EMAIL_SMS_MESSENGERS_APPS("Email, SMS, мессенджеры"),
    AUTOMATION_APPS("Автоматизация"),
    ANALYTICS_APPS("Аналитика"),
    BANKS_APPS("Банки"),
    OTHER_APPS("Другое"),
    MARKETPLACES_APPS("Маркетплейсы"),
    MOBILE_APPS("Мобильные приложения"),
    ONLINE_CASH_APPS("Онлайн-кассы"),
    ONLINE_SALES_APPS("Онлайн-продажи"),
    PAYMENT_BY_QR_APPS("Оплата по QR-коду в рознице"),
    PAYMENT_SYSTEMS_APPS("Платежные системы"),
    LOYALTY_PROGRAMS_APPS("Программы лояльности"),
    MAILING_LIST_APPS("Рассылки"),
    DELIVERY_APPS("Службы доставки"),
    TELEPHONY_APPS("Телефония"),
    FINANCE_APPS("Финансы"),
    EDO_APPS("ЭДО");

    TypeAppsEnum(String category) {
        this.category = category;
    }

    private String category;
    public String getTypeApp() {
        return category;
    }

}
