package com.trade_accounting.components.util.configure.components.select;

public class SelectConfigurer {
    public static SelectExt<String> configureDeleteSelect(Action onDelete){
        return new SelectExt.SelectBuilder<String>()
                .item(SelectConstants.SELECT_ACTION_SELECT_ITEM)
                .defaultValue(SelectConstants.SELECT_ACTION_SELECT_ITEM)
                .itemWithAction(SelectConstants.DELETE_SELECT_ITEM, onDelete)
                .width(SelectConstants.SELECT_WIDTH_130PX)
                .build();
    }

    public static SelectExt<String> configureDeleteSelect(){
        return new SelectExt.SelectBuilder<String>()
                .item(SelectConstants.SELECT_ACTION_SELECT_ITEM)
                .defaultValue(SelectConstants.SELECT_ACTION_SELECT_ITEM)
                .item(SelectConstants.CHANGE_SELECT_ITEM)
                .item(SelectConstants.DELETE_SELECT_ITEM)
                .width(SelectConstants.SELECT_WIDTH_130PX)
                .build();
    }

    public static SelectExt<String> configureBulkEditAndDeleteSelect(Action onBulkEdit, Action onDelete){
        return new SelectExt.SelectBuilder<String>()
                .item(SelectConstants.SELECT_ACTION_SELECT_ITEM)
                .defaultValue(SelectConstants.SELECT_ACTION_SELECT_ITEM)
                .itemWithAction(SelectConstants.BULK_EDIT_SELECT_ITEM, onBulkEdit)
                .itemWithAction(SelectConstants.DELETE_SELECT_ITEM, onDelete)
                .width(SelectConstants.SELECT_WIDTH_130PX)
                .build();
    }

    public static SelectExt<String> configureBulkEditAndDeleteSelect(){
        return new SelectExt.SelectBuilder<String>()
                .item(SelectConstants.SELECT_ACTION_SELECT_ITEM)
                .defaultValue(SelectConstants.SELECT_ACTION_SELECT_ITEM)
                .item(SelectConstants.BULK_EDIT_SELECT_ITEM)
                .item(SelectConstants.DELETE_SELECT_ITEM)
                .width(SelectConstants.SELECT_WIDTH_130PX)
                .build();
    }

    public static SelectExt<String> configurePrintSelect(){
        return new SelectExt.SelectBuilder<String>()
                .item(SelectConstants.PRINT_SELECT_ITEM)
                .defaultValue(SelectConstants.PRINT_SELECT_ITEM)
                .item(SelectConstants.ADD_TEMPLATE_SELECT_ITEM)
                .width(SelectConstants.SELECT_WIDTH_110PX)
                .build();
    }

    public static SelectExt<String> configureStatusSelect(){
        return new SelectExt.SelectBuilder<String>()
                .item(SelectConstants.STATUS_SELECT_ITEM)
                .width(SelectConstants.SELECT_WIDTH_110PX)
                .build();
    }
}
