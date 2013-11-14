package de.albert.bihler.andrvoc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Vokabel {

    private final String correctTranslation;
    private final String originalWord;
    private final List<String> alternativeTranslations;

    public Vokabel(String string) {
        alternativeTranslations = new ArrayList<String>(Arrays.asList(string.split("#")));
        originalWord = alternativeTranslations.get(0);
        correctTranslation = alternativeTranslations.get(1);
        alternativeTranslations.remove(0);
        Collections.shuffle(alternativeTranslations);
    }

    public String getOriginalWord() {
        return originalWord;
    }

    public String getCorrectTranslation() {
        return correctTranslation;
    }

    public List<String> getAlternativeTranslations() {
        return alternativeTranslations;
    }
}
