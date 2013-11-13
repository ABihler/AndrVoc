package de.albert.bihler.andrvoc.model;

import java.util.List;

public class Lesson {

    private long id;
    private String name;
    private String language;
    private int version;
    private List<Vokabel> vocabulary;

    public long getId() {
	return id;
    }

    public void setId(long id) {
	this.id = id;
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

}
