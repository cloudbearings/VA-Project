package com.arthurbergmz.assistant.intelligence.grammar;

public enum EnUSGrammar implements Grammar {
	
	SEARCH(1, "Search for", "Search about", "Search"), TELL_ABOUT(2, "Tell me what is the", "Say to me what is the", "Say what is the", "Tell me what are the",
			"Say to me what are the", "Say what are the", "Tell me what is", "Say to me what is", "Say what is", "Tell me what are", "Say to me what are", "Say what are"),
			LEARNING(3, "Learn what is", "Learn what are", "Definition of", "Meaning of");
	
	private int a;
	private String[] b;
	
	private EnUSGrammar(int cmd, String... str){
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
