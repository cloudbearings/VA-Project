package com.arthurbergmz.assistant.language.translations;

public interface BasicTranslation {
	
	public String onInitialize();
	public String[] onLackOfAnswers();
	public String onError();
	public String dontKnowSomething();
	
}
