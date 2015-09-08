package org.openehr.am.template;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by pieter.bos on 07/09/15.
 */
public class Translator {

    /* from language to path -> translation*/
    Map<String, Map<String, Translation>> translations;

    public Translator() {
        translations = new ConcurrentHashMap<String, Map<String, Translation>>();
    }

    public void addTranslation(String language, String path, Translation translation) {
        Map<String, Translation> translations = this.translations.get(language);
        if(translations == null) {
            synchronized(this) {
                if (this.translations.get(language) == null) {
                    translations = new ConcurrentHashMap<String, Translation>();
                    this.translations.put(language, translations);
                } else {
                    translations = this.translations.get(language);
                }
            }
        }
        translations.put(path, translation);
    }

    public String getText(String language, String path) {
        Translation translation = getTranslation(language, path);
        return translation == null ? null : translation.getText();
    }

    public String getDescription(String language, String path) {
        Translation translation = getTranslation(language, path);
        return translation == null ? null : translation.getDescription();
    }

    public Translation getTranslation(String language, String path) {
        Map<String, Translation> currentLanguage = translations.get(language);
        if(currentLanguage == null) {
            return null;
        }
        return currentLanguage.get(path);
    }
}
