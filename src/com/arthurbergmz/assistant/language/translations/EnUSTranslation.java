package com.arthurbergmz.assistant.language.translations;

public class EnUSTranslation implements BasicTranslation {
	
	private static final String[] onLackOfAnswersArray = new String[]{"I don't know what it is.", "I don't know it.", "I don't know what it means.", "I didn't understand. What do you mean?"};
	
	@Override
	public String onInitialize() {
		return "Initializing consciousness. Getting ready to give answers...";
	}
	
	@Override
	public String[] onLackOfAnswers(){
		return onLackOfAnswersArray;
	}
	
	@Override
	public String onError() {
		return "ERROR.";
	}
	
	@Override
	public String dontKnowSomething() {
		return "Sorry, I don't what is the meaning of";
	}
	
}
