package de.albert.bihler.andrvoc.model;

import java.text.Collator;
import java.util.List;
import java.util.Locale;

public class Lesson implements Comparable<Lesson> {

    public static final String LESSON_MODE = "lessonMode";
    public static final int LESSON_MODE_NORMAL = 0;
    public static final int LESSON_MODE_WEAKEST_WORDS = 1;
    public static final int LESSON_MODE_OLDEST_WORDS = 2;

    private long id;
    private String uuid;
    private String name;
    private String language;
    private int version;
    private List<Vokabel> vocabulary;
    private long serverId;
    private Collator collator;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public List<Vokabel> getVocabulary() {
        return vocabulary;
    }

    public void setVocabulary(List<Vokabel> vocabulary) {
        this.vocabulary = vocabulary;
    }

    public long getServerId() {
        return serverId;
    }

    public void setServerId(long serverId) {
        this.serverId = serverId;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public int compareTo(Lesson compareLesson) {

        collator = Collator.getInstance(Locale.GERMAN);
        collator.setStrength(Collator.SECONDARY);
        return collator.compare(getName(), compareLesson.getName());
    }

}
