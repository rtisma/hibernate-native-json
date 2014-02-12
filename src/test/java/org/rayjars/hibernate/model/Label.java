package org.rayjars.hibernate.model;

import java.io.Serializable;

public class Label implements Serializable{

    private String value;
    private String lang;


    public Label() {
    }

    public Label(String value, String lang) {
        this.value = value;
        this.lang = lang;
    }

    public String getValue() {
        return value;
    }

    public Label setValue(String value) {
        this.value = value;
        return this;
    }

    public Label setLang(String lang) {
        this.lang = lang;
        return this;
    }

    public String getLang() {
        return lang;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Label label = (Label) o;

        if (lang != null ? !lang.equals(label.lang) : label.lang != null) return false;
        if (value != null ? !value.equals(label.value) : label.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = value != null ? value.hashCode() : 0;
        result = 31 * result + (lang != null ? lang.hashCode() : 0);
        return result;
    }
}
