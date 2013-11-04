package de.albert.bihler.andrvoc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;


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
	
	public Vokabel(String string){
		altList= new ArrayList<String>(Arrays.asList(string.split("#")));
		translation=altList.get(0);
		name=altList.get(1);
		altList.remove(0);
		randomizeList();
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
	
	public Vokabel (List<String> l)
	{
		translation=l.get(0);
		name=l.get(1);
		l.remove(0);
		List<String> list = new ArrayList<String>();
		list.addAll(l);
		altList = list;
		randomizeList();
	}
	
	public void randomizeList()
	{
		Random rand = new Random();
	    for(int i = 1; i<= 10; i++)
	    {
	        int r1 = rand.nextInt(altList.size());
	        int r2 = rand.nextInt(altList.size());
	        String s1 = altList.get(r1);
	        String s2 = altList.get(r2);
	        altList.set(r1, s2);
	        altList.set(r2, s1);
	    }
	}
}
