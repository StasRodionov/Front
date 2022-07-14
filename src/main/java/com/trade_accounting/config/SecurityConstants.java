package com.trade_accounting.config;

import org.apache.poi.ss.formula.functions.T;

public class SecurityConstants {
    public static final String SECRET = "SECRET_KEY";
    public static final long EXPIRATION_TIME = 86400000;    // 24hrs
    // public static final long EXPIRATION_TIME = 900000;   // 15 min
    // public static final long EXPIRATION_TIME = 60000;    // 1 min
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String AUTHENTICATION_URL = "/login";
    public static final String SIGN_UP_URL = "/";
    public static final String BODY_URL = "app";


    // КОНСТАНТЫ ДЛЯ ИДЕНТИФИКАЦИИ ТАБЛИЦ (grid) ПРИ ОБМЕНЕ С БД МАСКАМИ ОТОБРАЖЕНИЯ КОЛОНОК
    public static final int GRID_PURCHASES_MAIN_SUPPLIERS_ORDERS = 1;
    public static final int GRID_PURCHASES_MAIN_VENDOR_ACCOUNTS = 2;
    public static final int GRID_PURCHASES_MAIN_ACCEPTANCES = 3;
    public static final int GRID_PURCHASES_MAIN_RETURN_TO_SUPPLIERS = 4;
    public static final int GRID_PURCHASES_MAIN_INVOICES_RECEIVED = 5;
    public static final int GRID_PURCHASES_MAIN_PURCHASING_MANAGEMENT = 6;

    public static final int GRID_SALES_MAIN_CUSTOMERS_ORDERS = 7;
    public static final int GRID_SALES_MAIN_INVOICES_TO_BUYERS = 8;
    public static final int GRID_SALES_MAIN_SHIPMENT = 9;
    public static final int GRID_SALES_MAIN_AGENT_REPORTS = 10;
    public static final int GRID_SALES_MAIN_BUYERS_RETURNS = 11;
    public static final int GRID_SALES_MAIN_ISSUED_INVOICES = 12;
    public static final int GRID_SALES_MAIN_PROFITABILITY_BY_GOODS = 13;
    public static final int GRID_SALES_MAIN_PROFITABILITY_BY_EMPLOYEES = 14;
    public static final int GRID_SALES_MAIN_PROFITABILITY_BY_CUSTOMERS = 15;
    public static final int GRID_SALES_MAIN_GOODS_FOR_SALE = 16;
    public static final int GRID_SALES_MAIN_SALES_FUNNEL_BY_ORDERS = 17;
    public static final int GRID_SALES_MAIN_SALES_FUNNEL_BY_CONTRACTORS = 18;

    public static final int GRID_GOODS_MAIN_GOODS = 19;
    public static final int GRID_GOODS_MAIN_POSTING = 20;
    public static final int GRID_GOODS_MAIN_INVENTORY = 21;
    public static final int GRID_GOODS_MAIN_INTERNAL_ORDER = 22;
    public static final int GRID_GOODS_MAIN_LOSS = 23;
    public static final int GRID_GOODS_MAIN_MOVEMENT = 24;
    public static final int GRID_GOODS_MAIN_PRICE_LIST = 25;
    public static final int GRID_GOODS_MAIN_REMAIN = 26;
    public static final int GRID_GOODS_MAIN_REVENUE = 27;
    public static final int GRID_GOODS_MAIN_SERIAL = 28;

    public static final int GRID_CONTRACTORS_MAIN_CONTRACTORS = 29;
    public static final int GRID_CONTRACTORS_MAIN_CONTRACTS = 30;

    public static final int GRID_MONEY_MAIN_PAYMENTS = 31;
    public static final int GRID_MONEY_MAIN_CASH_FLOW = 32;
    public static final int GRID_MONEY_MAIN_PROFIT_LOSS = 33;
    public static final int GRID_MONEY_MAIN_MUTUAL_SETTLEMENTS_WITH_CONTRACTORS = 34;
    public static final int GRID_MONEY_MAIN_MUTUAL_SETTLEMENTS_WITH_EMPLOEES = 35;
    public static final int GRID_MONEY_MAIN_BALANCE_ADJUSTMENT = 36;

    public static final int GRID_RETAIL_MAIN_STORES = 37;
    public static final int GRID_RETAIL_MAIN_SHIFT = 38;
    public static final int GRID_RETAIL_MAIN_SALES = 39;
    public static final int GRID_RETAIL_MAIN_RETURNS = 40;
    public static final int GRID_RETAIL_MAIN_MAKING = 41;
    public static final int GRID_RETAIL_MAIN_PAYOUT = 42;
    public static final int GRID_RETAIL_MAIN_POINTS = 43;
    public static final int GRID_RETAIL_MAIN_PREPAYOUT = 44;
    public static final int GRID_RETAIL_MAIN_PREPAYMENT_RETURN = 45;
    public static final int GRID_RETAIL_MAIN_CLOUD_CHECKS = 46;
    public static final int GRID_RETAIL_MAIN_BONUS_PROGRAM = 47;

    public static final int GRID_PRODUCTION_MAIN_FLOWCHART = 48;
    public static final int GRID_PRODUCTION_MAIN_ORDERS = 49;
    public static final int GRID_PRODUCTION_MAIN_TECHNOLOGICAL_OPERATIONS = 50;
    public static final int GRID_PRODUCTION_MAIN_PRODUCTION_TARGETS = 51;
    public static final int GRID_PRODUCTION_MAIN_TECHNICAL_PROCESS = 52;
    public static final int GRID_PRODUCTION_MAIN_STAGE_PRODUCTION = 53;

    public static final int GRID_INDICATORS_MAIN_DOCUMENTS = 54;
    public static final int GRID_INDICATORS_MAIN_RECYCLEBIN = 55;
    public static final int GRID_INDICATORS_MAIN_AUDIT = 56;
    public static final int GRID_INDICATORS_MAIN_FILES = 57;








    //СПИСОК URL САЙТА. ВСЕ АДРЕСА ДОЛЖНЫ ЦЕНТРАЛИЗОВАННО ХРАНИТЬСЯ!
    //АВТОРИЗАЦИЯ
    public static final String LOGIN = "login";
    //ВЫХОД
    public static final String LOGOUT = "logout";
    //РЕГИСТРАЦИЯ
    public static final String REGISTRATION =  "registration";


    //Раздел "Показатели"
    //URL 2 уровня
    public static final String INDICATORS = BODY_URL + SIGN_UP_URL + "indicators";
    public static final String INDICATORS_DASHBOARD_VIEW = BODY_URL + SIGN_UP_URL + "dashboard-view";
    public static final String INDICATORS_OPERATIONS_VIEW = BODY_URL + SIGN_UP_URL + "operations-view";
    public static final String INDICATORS_TRASHCAN_VIEW = BODY_URL + SIGN_UP_URL + "trashcan-view";
    public static final String INDICATORS_AUDIT_VIEW = BODY_URL + SIGN_UP_URL + "audit-view";
    public static final String INDICATORS_DOCUMENTS_VIEW = BODY_URL + SIGN_UP_URL + "documents-view";


    //Раздел "Закупки"
    //URL 2 уровня
    public static final String PURCHASES = BODY_URL + SIGN_UP_URL + "purchases";
    public static final String PURCHASES_SUPPLIERS_ORDERS_VIEW = BODY_URL + SIGN_UP_URL + "suppliers-orders-view";
    public static final String PURCHASES_SUPPLIERS_INVOICES_VIEW = BODY_URL + SIGN_UP_URL + "suppliers-invoices-view";
    public static final String PURCHASES_ADMISSIONS_VIEW = BODY_URL + SIGN_UP_URL + "admissions-view";
    public static final String PURCHASES_ACCEPTS_VIEW = BODY_URL + SIGN_UP_URL + "accepts-modal";
    public static final String PURCHASES_RETURNS_TO_SUPPLIERS_VIEW = BODY_URL + SIGN_UP_URL + "returns-to-suppliers-view";
    public static final String PURCHASES_INVOICE_RECEIVED_VIEW = BODY_URL + SIGN_UP_URL + "invoice-received-view";
    public static final String PURCHASES_PURCHASING_MANAGEMENT_VIEW = BODY_URL + SIGN_UP_URL + "purchasing-management-view";
    //URL 3 уровня
    public static final String PURCHASES_PURCHASES__NEW_ORDER_PURCHASES = PURCHASES + SIGN_UP_URL + "new-order-purchases";
    public static final String PURCHASES_PURCHASES__ADD_NEW_INVOICES_TO_SUPPLIER = PURCHASES + SIGN_UP_URL + "add-new-invoices-to-supplier";


    //Раздел "Продажи"
    //URL 2 уровня
    public static final String SELLS = BODY_URL + SIGN_UP_URL + "sells";
    public static final String SELLS_CUSTOMERS_ORDERS_VIEW = BODY_URL + SIGN_UP_URL + "customers-orders-view";
    public static final String SELLS_INVOICES_TO_BUYERS_VIEW = BODY_URL + SIGN_UP_URL + "invoices-to-buyers-view";
    public static final String SELLS_SHIPMENT_VIEW = BODY_URL + SIGN_UP_URL + "shipment-view";
    public static final String SELLS_AGENT_REPORTS_VIEW = BODY_URL + SIGN_UP_URL + "agent-reports-view";
    public static final String SELLS_BUYERS_RETURNS_VIEW = BODY_URL + SIGN_UP_URL + "buyers-returns-view";
    public static final String SELLS_ISSUED_INVOICE_VIEW = BODY_URL + SIGN_UP_URL + "issued-invoice-view";
    public static final String SELLS_PROFITABILITY_VIEW = BODY_URL + SIGN_UP_URL + "profitability-view";
    public static final String SELLS_GOODS_FOR_SALE_VIEW = BODY_URL + SIGN_UP_URL + "goods-for-sale-view";
    public static final String SELLS_SALES_SUB_SALES_FUNNEL_VIEW = BODY_URL + SIGN_UP_URL + "sales-sub-sales-funnel-view";
    //URL 3 уровня
    public static final String SELLS_SELLS__CUSTOMER_ORDER_EDIT = SELLS + SIGN_UP_URL + "customer-order-edit";
    public static final String SELLS_SELLS__ADD_NEW_INVOICES_TO_BUYERS = SELLS + SIGN_UP_URL + "add-new-invoices-to-buyers";
    public static final String SELLS_SELLS__SHIPMENT_EDIT= SELLS + SIGN_UP_URL + "shipment-edit";


    //Раздел "Товары"
    //URL 2 уровня
    public static final String GOODS = BODY_URL + SIGN_UP_URL + "goods";
    public static final String GOODS__VIEW = BODY_URL + SIGN_UP_URL + "";
    public static final String GOODS_POSTING_VIEW = BODY_URL + SIGN_UP_URL + "positing-view";
    public static final String GOODS_INVENTORY_VIEW = BODY_URL + SIGN_UP_URL + "inventory-view";
    public static final String GOODS_INTERNAL_ORDER_VIEW = BODY_URL + SIGN_UP_URL + "internal-order-view";
    public static final String GOODS_LOSS_VIEW = BODY_URL + SIGN_UP_URL + "loss-view";
    public static final String GOODS_MOVEMENT_VIEW = BODY_URL + SIGN_UP_URL + "movement-view";
    public static final String GOODS_GOODS_PRICE_VIEW = BODY_URL + SIGN_UP_URL + "goods-price-view";
    public static final String GOODS_REMAIN_VIEW = BODY_URL + SIGN_UP_URL + "remain-view";
    public static final String GOODS_REVENUE_VIEW = BODY_URL + SIGN_UP_URL + "revenue-view";
    public static final String GOODS_SERIAL_NUMBERS_VIEW = BODY_URL + SIGN_UP_URL + "serial-numbers-view";
    public static final String GOODS_CUSTOMERS_PRODUCTS_VIEW = BODY_URL + SIGN_UP_URL + "customers-products-view";
    //URL 3 уровня
    public static final String GOODS_GOODS__LOSS_CREATE = GOODS + SIGN_UP_URL + "loss-create";
    public static final String GOODS_GOODS__PRICE_LIST_CREATE = GOODS + SIGN_UP_URL + "price-list-create";
    public static final String GOODS_GOODS__PRICE_LIST_EDIT = GOODS + SIGN_UP_URL + "price-list-edit";
    public static final String GOODS_GOODS__ADD_MOVING = GOODS + SIGN_UP_URL + "add-moving";
    public static final String GOODS_GOODS__POSTING_CREATE = GOODS + SIGN_UP_URL + "posting-create";
    public static final String GOODS_GOODS__REVENUE_EDIT = GOODS + SIGN_UP_URL + "revenue-edit";
    public static final String GOODS_GOODS__EDIT_VIEW = GOODS + SIGN_UP_URL + "edit";

    public static final String GOODS_KITS__EDIT_VIEW = GOODS + SIGN_UP_URL + "kit-edit";

    //Раздел "Контрагенты"
    //URL 2 уровня
    public static final String CONTRACTORS = BODY_URL + SIGN_UP_URL + "contractors";
    public static final String CONTRACTORS_CONTRACTORS_VIEW = BODY_URL + SIGN_UP_URL + "contractors-view";
    public static final String CONTRACTORS_CONTRACTS_VIEW = BODY_URL + SIGN_UP_URL + "contracts-view";


    //Раздел "Деньги"
    //URL 2 уровня
    public static final String MONEY = BODY_URL + SIGN_UP_URL + "money";
    public static final String MONEY_MONEY_PAYMENTS_VIEW = BODY_URL + SIGN_UP_URL + "money-payments-view";
    public static final String MONEY_MONEY_SUB_CASE_FLOW_VIEW = BODY_URL + SIGN_UP_URL + "money-cash-flow-view";
    public static final String MONEY_MONEY_SUB_PROFIT_LOSS_VIEW = BODY_URL + SIGN_UP_URL + "money-profit-loss-view";
    public static final String MONEY_MONEY_SUB_MUTUAL_SETTLEMENTS_VIEW = BODY_URL + SIGN_UP_URL + "money-mutual-settlements-view";
    public static final String MONEY_BALANCE_ADJUSTMENT_VIEW = BODY_URL + SIGN_UP_URL + "balance-adjustment-view";


    //Раздел "Розница"
    //URL 2 уровня
    public static final String RETAIL = BODY_URL + SIGN_UP_URL + "retail";
    public static final String RETAIL_RETAIL_STORES_VIEW = BODY_URL + SIGN_UP_URL + "retail-stores-view";
    public static final String RETAIL_RETAIL_SHIFT_VIEW = BODY_URL + SIGN_UP_URL + "retail-shift-view";
    public static final String RETAIL_RETAIL_SALES_VIEW = BODY_URL + SIGN_UP_URL + "retail-sales-view";
    public static final String RETAIL_RETAIL_RETURNS_VIEW = BODY_URL + SIGN_UP_URL + "retail-returns-view";
    public static final String RETAIL_RETAIL_MAKING_VIEW = BODY_URL + SIGN_UP_URL + "retail-making-view";
    public static final String RETAIL_PAYOUT_VIEW = BODY_URL + SIGN_UP_URL + "payout-view";
    public static final String RETAIL_RETAIL_POINTS_VIEW = BODY_URL + SIGN_UP_URL + "retail-points-view";
    //Два раза написана реализация вкладки "Операции с баллами".
    //Реализация RetailOperationWithPointsTabView НЕ используется! Если применяться не будет, то нужно класс удалить.
    public static final String RETAIL_RETAIL_OPERATION_WITH_POINTS_VIEW = BODY_URL + SIGN_UP_URL + "retail-operation-with-points-view"; // <----
    public static final String RETAIL_PREPAYMENT_VIEW = BODY_URL + SIGN_UP_URL + "prepayment-view";
    public static final String RETAIL_PREPAYMENT_RETURN_VIEW = BODY_URL + SIGN_UP_URL + "prepayment-return-view";
    public static final String RETAIL_RETAIL_CLOUD_CHECK_VIEW = BODY_URL + SIGN_UP_URL + "retail-cloud-check-view";
    public static final String RETAIL_BONUS_PROGRAM_VIEW = BODY_URL + SIGN_UP_URL + "bonus-program-view";


    //Раздел "Производство"
    //URL 2 уровня
    public static final String PRODUCTION = BODY_URL + SIGN_UP_URL + "production";
    public static final String PRODUCTION_FLOWCHARTS_VIEW = BODY_URL + SIGN_UP_URL + "flowcharts-view";
    public static final String PRODUCTION_ORDERS_OF_PRODUCTION_VIEW = BODY_URL + SIGN_UP_URL + "orders-of-production-view";
    public static final String PRODUCTION_TECHNOLOGICAL_VIEW = BODY_URL + SIGN_UP_URL + "technological-view";
    public static final String PRODUCTION_PRODUCTION_TARGETS_VIEW = BODY_URL + SIGN_UP_URL + "production-targets-view";
    public static final String PRODUCTION_TECHNICAL_PROCESS_VIEW = BODY_URL + SIGN_UP_URL + "technical-process-view";
    public static final String PRODUCTION_STAGES_VIEW = BODY_URL + SIGN_UP_URL + "stages-view";


    //Раздел "Задачи"
    //URL 2 уровня
    public static final String TASKS = BODY_URL + SIGN_UP_URL + "tasks";


    //Раздел "Приложения"
    //URL 2 уровня
    public static final String APPS = BODY_URL + SIGN_UP_URL + "apps";
    public static final String APPS_EMBED_APPS_VIEW = BODY_URL + SIGN_UP_URL + "embed-apps";


    //Раздел "Уведомления"


    //Раздел "FAQ"


    //Раздел "Профиль"
    //URL 2 уровня
    public static final String PROFILE = BODY_URL + SIGN_UP_URL + "profile";
    public static final String PROFILE_COMPANY_VIEW = BODY_URL + SIGN_UP_URL + "company-view";
    public static final String PROFILE_EMPLOYEE_VIEW = BODY_URL + SIGN_UP_URL + "employee-view";
    public static final String PROFILE_WAREHOUSE_VIEW = BODY_URL + SIGN_UP_URL + "warehouse-view";
    public static final String PROFILE_CURRENCY_VIEW = BODY_URL + SIGN_UP_URL + "currency-view";
    public static final String PROFILE_UNIT_VIEW = BODY_URL + SIGN_UP_URL + "unit-view";
    public static final String PROFILE_SCENARIO_VIEW = BODY_URL + SIGN_UP_URL + "scenario_view";
    //URL 3 уровня
    public static final String PROFILE_PROFILE__USER_SETTINGS = PROFILE + SIGN_UP_URL + "user-settings";
    public static final String PROFILE_PROFILE__SETTINGS = PROFILE + SIGN_UP_URL + "settings";
    //URL 4 уровня
    public static final String PROFILE_PROFILE__SETTINGS__COMPANY_SETTINGS = PROFILE_PROFILE__SETTINGS + SIGN_UP_URL + "company-settings";
    public static final String PROFILE_PROFILE__SETTINGS__DISCOUNT_SETTINGS = PROFILE_PROFILE__SETTINGS + SIGN_UP_URL + "discount-settings";
    public static final String PROFILE_PROFILE__SETTINGS__EXPORT_SETTINGS = PROFILE_PROFILE__SETTINGS + SIGN_UP_URL + "export-settings";
    public static final String PROFILE_PROFILE__SETTINGS__IMPORT_SETTINGS = PROFILE_PROFILE__SETTINGS + SIGN_UP_URL + "import-settings";
    public static final String PROFILE_PROFILE__SETTINGS__SCENARIO_SETTINGS = PROFILE_PROFILE__SETTINGS + SIGN_UP_URL + "scenario-settings";
    public static final String PROFILE_PROFILE__SETTINGS__EMPLOYEES_SETTINGS = PROFILE_PROFILE__SETTINGS + SIGN_UP_URL + "employees-settings";
    public static final String PROFILE_PROFILE__SETTINGS__WAREHOUSES_SETTINGS = PROFILE_PROFILE__SETTINGS + SIGN_UP_URL + "warehouses-settings";
    public static final String PROFILE_PROFILE__SETTINGS__CURRENCY_SETTINGS = PROFILE_PROFILE__SETTINGS + SIGN_UP_URL + "currency-settings";
    public static final String PROFILE_PROFILE__SETTINGS__UNITS_SETTINGS = PROFILE_PROFILE__SETTINGS + SIGN_UP_URL + "units-settings";
    public static final String PROFILE_PROFILE__SETTINGS__LEGAL_ENTITIES_SETTINGS = PROFILE_PROFILE__SETTINGS + SIGN_UP_URL + "legal-entities-settings";
    public static final String PROFILE_PROFILE__SETTINGS__SALES_CHANNEL_SETTINGS = PROFILE_PROFILE__SETTINGS + SIGN_UP_URL + "sales-channel-settings";
    public static final String PROFILE_PROFILE__SETTINGS__COUNTRIES_SETTINGS = PROFILE_PROFILE__SETTINGS + SIGN_UP_URL + "countries-settings";
    public static final String PROFILE_PROFILE__SETTINGS__PROJECTS_SETTINGS = PROFILE_PROFILE__SETTINGS + SIGN_UP_URL + "projects-settings";

    //Страницы ошибок HTTP
    //URL 2 уровня, 404 ERROR
    public static final String ERROR_404 = BODY_URL + SIGN_UP_URL + "error-404";




    public static final String TOKEN_ATTRIBUTE_NAME = "token";

}
