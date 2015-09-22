package org.openehr.am.template;

import org.openehr.am.archetype.ontology.ArchetypeTerm;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by pieter.bos on 07/09/15.
 */
public class Translator {

    /** map storing for every path that has an override in translation, its translation */
    private Map<String, ArchetypeTerm> overridenTranslations;

    /* language -> (path -> translation) */
    private Map<String, Map<String, ArchetypeTerm>> nodeTranslations;
    // language -> (path -> (code -> translation))
    private Map<String, Map<String, Map<String, ArchetypeTerm>>> codeTranslations;

    public Translator() {
        nodeTranslations = new ConcurrentHashMap<String, Map<String, ArchetypeTerm>>();
        codeTranslations = new ConcurrentHashMap<String, Map<String, Map<String, ArchetypeTerm>>>();
        overridenTranslations = new ConcurrentHashMap<String, ArchetypeTerm>();
    }

    public String getText(String language, String path) {
        ArchetypeTerm translation = getTranslation(language, path);
        return translation == null ? null : translation.getText();
    }

    public String getDescription(String language, String path) {
        ArchetypeTerm translation = getTranslation(language, path);
        return translation == null ? null : translation.getDescription();
    }

    /**
     * Return the translation for a given path in a given language
     * @param language
     * @param path
     * @return
     */
    public ArchetypeTerm getTranslation(String language, String path) {
        if(this.overridenTranslations.containsKey(path)) {
            return overridenTranslations.get(path);
        }
        Map<String, ArchetypeTerm> currentLanguage = nodeTranslations.get(language);
        if(currentLanguage == null) {
            return null;
        }
        return currentLanguage.get(path);
    }

    /**
     * Return the translation for a code at a given path in a given language. For use in DvCodedText with local terminology, since this cannot easily be handled by the above case
     * - unless we always pass the path plus the node id we want to translate. That is actually an option worth considering
     */
    public ArchetypeTerm getTranslation(String language, String path, String code) {
        Map<String, Map<String, ArchetypeTerm>> languageToCodes = codeTranslations.get(language);
        if(languageToCodes == null) {
            return null;
        }

        Map<String, ArchetypeTerm> codeToTranslation = languageToCodes.get(path);
        return codeToTranslation == null ? null : codeToTranslation.get(code);
    }

    public void addTranslation(String language, String path, ArchetypeTerm archetypeTerm) {
        Map<String, ArchetypeTerm> translations = this.nodeTranslations.get(language);
        if(translations == null) {
            synchronized(this) {
                if (this.nodeTranslations.get(language) == null) {
                    translations = new ConcurrentHashMap<String, ArchetypeTerm>();
                    this.nodeTranslations.put(language, translations);
                } else {
                    translations = this.nodeTranslations.get(language);
                }
            }
        }
        translations.put(path, archetypeTerm);
    }

    public void addTranslation(String language, String path, String code, ArchetypeTerm archetypeTerm) {
        Map<String, Map<String,ArchetypeTerm>> translations = this.codeTranslations.get(language);
        if(translations == null) {
            synchronized(this) {
                if ((translations = this.codeTranslations.get(language)) == null) {
                    translations = new ConcurrentHashMap<String, Map<String, ArchetypeTerm>>();
                    this.codeTranslations.put(language, translations);
                }
            }
        }
        Map<String, ArchetypeTerm> codeToTermMap = translations.get(path);
        if(codeToTermMap == null) {
            synchronized(this) {
                if ((codeToTermMap = translations.get(path)) == null) {
                    codeToTermMap = new ConcurrentHashMap<String, ArchetypeTerm>();
                    translations.put(path, codeToTermMap);
                }
            }
        }
        codeToTermMap.put(code, archetypeTerm);
    }

    public void addOverridenTranslation(String path, ArchetypeTerm archetypeTerm) {
        overridenTranslations.put(path, archetypeTerm);
    }
}
