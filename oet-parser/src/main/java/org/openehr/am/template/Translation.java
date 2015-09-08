package org.openehr.am.template;

/**
 * Created by pieter.bos on 07/09/15.
 */
public class Translation {

    private String text;
    private String description;

    public Translation(String text, String description) {
        this.text = text;
        this.description = description;
    }

    public String getText() {
        return text;
    }

    public String getDescription() {
        return description;
    }
}
