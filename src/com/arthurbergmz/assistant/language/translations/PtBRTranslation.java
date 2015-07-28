package com.arthurbergmz.assistant.language.translations;

public class PtBRTranslation implements BasicTranslation {
	
	private static final String[] onLackOfAnswersArray = new String[]{"Eu n�o sei o que � isso.", "Eu n�o sei.", "N�o sei o que isto significa.", "N�o entendi. O que voc� quis dizer?"};
	
	@Override
	public String onInitialize() {
		return "Inicializando consci�ncia. Me preparando para responder...";
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
		return "Ainda n�o sei o que �";
	}
	
}
