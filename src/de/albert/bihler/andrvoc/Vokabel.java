package de.albert.bihler.andrvoc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


public class Vokabel {
	public String name;
	public String translation;
	public String alt_translation;
	public List<String> altList;
		
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
	
	public Vokabel (String v, String t, List<String> l)
	{
		name=v;
		translation=t;
				
		List<String> list = new ArrayList<String>();
		list.addAll(l);
		list.add(v);
		altList = list;
	}
}
