package com.arthurbergmz.assistant.language.translations;

public class PtBRTranslation implements BasicTranslation {
	
	private static final String[] onLackOfAnswersArray = new String[]{"Eu não sei o que é isso.", "Eu não sei.", "Não sei o que isto significa.", "Não entendi. O que você quis dizer?"};
	
	@Override
	public String onInitialize() {
		return "Inicializando consciência. Me preparando para responder...";
	}
	
	@Override
	public String[] onLackOfAnswers(){
		return onLackOfAnswersArray;
	}
	
	@Override
	public String onError() {
		return "ERRO.";
	}
	
	@Override
	public String dontKnowSomething() {
		return "Ainda não sei o que é";
	}
	
}
