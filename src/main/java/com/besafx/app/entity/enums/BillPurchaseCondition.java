package com.besafx.app.entity.enums;

public enum BillPurchaseCondition {
    Opened("فتح"),
    Done("مدفوعة"),
    Canceled("ملغاة");

    private String name;

    BillPurchaseCondition(String name) {
        this.name = name;
    }

    public static BillPurchaseCondition findByName(String name) {
        for (BillPurchaseCondition v : values()) {
            if (v.getName().equals(name)) {
                return v;
            }
        }
        return null;
    }

    public static BillPurchaseCondition findByValue(String value) {
        for (BillPurchaseCondition v : values()) {
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
