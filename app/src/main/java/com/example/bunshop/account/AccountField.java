package com.example.bunshop.account;

public class AccountField {
    private String caption;
    private String field;

    public AccountField(String caption, String field) {
        this.caption = caption;
        this.field = field;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
