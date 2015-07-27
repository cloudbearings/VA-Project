package com.arthurbergmz.assistant.intelligence;

import java.util.Random;

import com.arthurbergmz.assistant.Assistant;
import com.arthurbergmz.assistant.intelligence.utils.Thought;

public class HelloThought implements Thought {
	
	private static String[] a = new String[]{"Olá", "Oi"};
	private static String[] b = new String[]{"Oi.", "Olá.", "Oi, estou ouvindo.", "Olá, estou aqui."};
	
	@Override
	public String[] getRelatedSentences(){
		return a;
	}
	
	@Override
	public String execute(String label, Assistant a){
		return b[new Random().nextInt(b.length-1)];
	}
	
}
