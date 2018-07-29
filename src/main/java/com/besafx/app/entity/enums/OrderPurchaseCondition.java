package com.besafx.app.entity.enums;

public enum OrderPurchaseCondition {
    Draft("مسودة"),
    Opened("فتح"),
    Sent("أرسلت"),
    Billed("مفوتر"),
    Canceled("ملغاة");

    private String name;

    OrderPurchaseCondition(String name) {
        this.name = name;
    }

    public static OrderPurchaseCondition findByName(String name) {
        for (OrderPurchaseCondition v : values()) {
            if (v.getName().equals(name)) {
                return v;
            }
        }
        return null;
    }

    public static OrderPurchaseCondition findByValue(String value) {
        for (OrderPurchaseCondition v : values()) {
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
