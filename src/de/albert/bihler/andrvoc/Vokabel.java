package de.albert.bihler.andrvoc;


public class Vokabel {
	public String name;
	public String translation;
	public String alt_translation;
		
	public Vokabel() {
		name="excuse me";
		translation="Entschuldigen Sie";
		alt_translation="Entschuldigen Sie, bitte";
	}
	
	public Vokabel(String v, String t) {
		name=v;
		translation=t;
	}
	
	public Vokabel(String v, String t, String a) {
		name=v;
		translation=t;
		alt_translation=a;
	}

}
