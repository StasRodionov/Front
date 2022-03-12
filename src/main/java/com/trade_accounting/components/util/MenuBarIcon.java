package com.trade_accounting.components.util;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.contextmenu.HasMenuItems;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;

public class MenuBarIcon {

    public static MenuItem createIconItem(MenuBar menu, VaadinIcon iconName, String ariaLabel) {
        Icon icon = new Icon(iconName);
        MenuItem item = menu.addItem(icon);
        item.getElement().setAttribute("aria-label", ariaLabel);

        return item;
    }

        public static MenuItem createIconItem(HasMenuItems menu, VaadinIcon iconName, String label, String ariaLabel) {
            return createIconItem(menu, iconName, label, ariaLabel, false);
        }

        public static MenuItem createIconItem(HasMenuItems menu, VaadinIcon iconName, String label, String ariaLabel, boolean isChild) {
            Icon icon = new Icon(iconName);

            if (isChild) {
                icon.getStyle().set("width", "var(--lumo-icon-size-s)");
                icon.getStyle().set("height", "var(--lumo-icon-size-s)");
                icon.getStyle().set("marginRight", "var(--lumo-space-s)");
            }

            MenuItem item = menu.addItem(icon, e -> {
            });

            if (ariaLabel != null) {
                item.getElement().setAttribute("aria-label", ariaLabel);
            }

            if (label != null) {
                item.add(new Text(label));
            }

            return item;
        }
}
