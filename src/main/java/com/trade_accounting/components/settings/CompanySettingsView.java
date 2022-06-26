package com.trade_accounting.components.settings;

import com.trade_accounting.components.AppView;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.extern.slf4j.Slf4j;

import static com.trade_accounting.config.SecurityConstants.PROFILE_PROFILE__SETTINGS__COMPANY_SETTINGS;

@Route(value = PROFILE_PROFILE__SETTINGS__COMPANY_SETTINGS, layout = AppView.class)
@PageTitle("Учетная запись")
@Slf4j
public class CompanySettingsView extends VerticalLayout {


}
