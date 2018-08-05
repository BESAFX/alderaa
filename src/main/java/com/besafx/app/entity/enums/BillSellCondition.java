package com.besafx.app.entity.enums;

public enum BillSellCondition {
    Draft("مسودة"),
    Sent("أرسلت"),
    Viewed("شوهدت"),
    Done("مدفوعة"),
    Canceled("ملغاة");

    private String name;

    BillSellCondition(String name) {
        this.name = name;
    }

    public static BillSellCondition findByName(String name) {
        for (BillSellCondition v : values()) {
            if (v.getName().equals(name)) {
                return v;
            }
        }
        return null;
    }

    public static BillSellCondition findByValue(String value) {
        for (BillSellCondition v : values()) {
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
