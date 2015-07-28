package com.arthurbergmz.assistant.language;

import com.arthurbergmz.assistant.language.translations.BasicTranslation;
import com.arthurbergmz.assistant.language.translations.EnUSTranslation;
import com.arthurbergmz.assistant.language.translations.PtBRTranslation;

public enum Language {
	
	ENGLISH_USA("en-US", "Joey", new EnUSTranslation()), PORTUGUESE_BRAZIL("pt-BR", "Ricardo", new PtBRTranslation());
	
	private String a, b;
	private BasicTranslation c;
	
	private Language(String languageCode, String speaker, BasicTranslation basicTranslation){
		this.a = languageCode;
		this.b = speaker;
		this.c = basicTranslation;
	}
	
	public String getLanguageCode(){
		return this.a;
	}
	
	public String getSpeaker(){
		return this.b;
	}
	
	public BasicTranslation getTranslation(){
		return this.c;
	}
	
}
