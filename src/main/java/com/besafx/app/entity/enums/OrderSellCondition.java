package com.besafx.app.entity.enums;

public enum OrderSellCondition {
    Pending("في الإنتظار"),
    Agreed("تمت الموافقة"),
    Rejected("مرفوضة"),
    Canceled("ملغاة");

    private String name;

    OrderSellCondition(String name) {
        this.name = name;
    }

    public static OrderSellCondition findByName(String name) {
        for (OrderSellCondition v : values()) {
            if (v.getName().equals(name)) {
                return v;
            }
        }
        return null;
    }

    public static OrderSellCondition findByValue(String value) {
        for (OrderSellCondition v : values()) {
            if (v.name().equals(value)) {
                return v;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }
}
