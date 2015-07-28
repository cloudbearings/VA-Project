package com.arthurbergmz.assistant.intelligence.grammar;

public enum PtBRGrammar implements Grammar {
	
	SEARCH(1, "Procure por", "Procure sobre", "Procure"), TELL_ABOUT(2, "Me diga o que � a", "Me diga o que � o", "Me diga o que �", "Diga o que � a", "Diga o que � o", "Diga o que �",
			"O que � a", "O que � o", "O que �", "O que s�o as", "O que s�o os", "O que s�o", "O que significa"),
			LEARNING(3, "Aprenda o que �", "Aprenda o que s�o", "Defini��o de", "Significado de");
	
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
