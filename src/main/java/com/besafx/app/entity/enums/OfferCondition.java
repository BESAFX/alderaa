package com.besafx.app.entity.enums;

public enum OfferCondition {
    Draft("مسودة"),
    Sent("أرسل"),
    Viewed("شوهد"),
    Agreed("تمت الموافقة"),
    Rejected("مرفوض"),
    Canceled("ملغي");

    private String name;

    OfferCondition(String name) {
        this.name = name;
    }

    public static OfferCondition findByName(String name) {
        for (OfferCondition v : values()) {
            if (v.getName().equals(name)) {
                return v;
            }
        }
        return null;
    }

    public static OfferCondition findByValue(String value) {
        for (OfferCondition v : values()) {
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
