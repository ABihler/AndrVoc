package de.albert.bihler.andrvoc.model;

import java.util.List;

public class Lesson {

	private String name;
	private int version;
	private List<Vokabel> vocabulary;

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
