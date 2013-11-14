package de.albert.bihler.andrvoc.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Vokabel {

    private long id;
    private String correctTranslation;
    private String originalWord;
    private long lessonId;
    private List<String> alternativeTranslations;

    public Vokabel() {
    }

    @Deprecated
    /**
     * Konstruktor zum Erzeugen von Vokabel-Objekten aus einem String-"Array" mit Rauten. 
     * Wurde am Anfang benutzt, um die Objekte aus XML-Dateien zu erzeugen. 
     * Konstruktor sollte nicht mehr benutzt werden, weil die Vokabeln jetzt aus der Datenbank kommen.
     * @param string
     */
    public Vokabel(String string) {
        alternativeTranslations = new ArrayList<String>(Arrays.asList(string.split("#")));
        originalWord = alternativeTranslations.get(0);
        correctTranslation = alternativeTranslations.get(1);
        alternativeTranslations.remove(0);
        Collections.shuffle(alternativeTranslations);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOriginalWord() {
        return originalWord;
    }

    public void setOriginalWord(String word) {
        this.originalWord = word;
    }

    public String getCorrectTranslation() {
        return correctTranslation;
    }

    public void setCorrectTranslation(String correctTranslation) {
        this.correctTranslation = correctTranslation;
    }

    public long getLessonId() {
        return lessonId;
    }

    public void setLessonId(long lessonId) {
        this.lessonId = lessonId;
    }

    public List<String> getAlternativeTranslations() {
        return alternativeTranslations;
    }

    public void setAlternativeTranslations(List<String> alternativeTranslations) {
        this.alternativeTranslations = alternativeTranslations;
    }
}
