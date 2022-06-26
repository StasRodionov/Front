package com.trade_accounting.components.settings;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.extern.slf4j.Slf4j;

import static com.trade_accounting.config.SecurityConstants.PROFILE_PROFILE__SETTINGS__DISCOUNT_SETTINGS;

@Route(value = PROFILE_PROFILE__SETTINGS__DISCOUNT_SETTINGS, layout = SettingsView.class)
@PageTitle("Учетная запись")
@Slf4j
public class DiscountSettingsView extends VerticalLayout {


}
