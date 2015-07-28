package com.arthurbergmz.assistant.intelligence.grammar;

public enum PtBRGrammar implements Grammar {
	
	SEARCH(1, "Procure por", "Procure sobre", "Procure"), TELL_ABOUT(2, "Me diga o que é a", "Me diga o que é o", "Me diga o que é", "Diga o que é a", "Diga o que é o", "Diga o que é",
			"O que é a", "O que é o", "O que é", "O que são as", "O que são os", "O que são", "O que significa"),
			LEARNING(3, "Aprenda o que é", "Aprenda o que são", "Definição de", "Significado de");
	
	private int a;
	private String[] b;
	
	private PtBRGrammar(int cmd, String... str){
		this.a = cmd;
		this.b = str;
	}
	
	@Override
	public int getCommandId() {
		return this.a;
	}
	
	@Override
	public String[] getGrammar() {
		return this.b;
	}
	
}
