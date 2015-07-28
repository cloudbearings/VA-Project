package com.arthurbergmz.assistant.intelligence;

import com.arthurbergmz.assistant.Assistant;
import com.arthurbergmz.assistant.Config;
import com.arthurbergmz.assistant.intelligence.grammar.EnUSGrammar;
import com.arthurbergmz.assistant.intelligence.grammar.PtBRGrammar;
import com.arthurbergmz.assistant.intelligence.memorybank.Memory;
import com.arthurbergmz.assistant.intelligence.memorybank.Thought;
import com.arthurbergmz.assistant.language.Language;
import com.arthurbergmz.assistant.utils.StringUtils;

public class WhatIsSomethingThought implements Thought {
	
	@Override
	public String[] getRelatedSentences(){
		return StringUtils.EMPTY_ARRAY;
	}
	
	@Override
	public boolean execute(String label, Assistant a){
		String str = label.toLowerCase();
		String[] grammar = (Config.LANGUAGE.equals(Language.PORTUGUESE_BRAZIL) ? PtBRGrammar.TELL_ABOUT.getGrammar() : EnUSGrammar.TELL_ABOUT.getGrammar());
		for(int i = 0, j = grammar.length; i < j; i++) str = str.replace(grammar[i].toLowerCase(), "");
		str = str.trim();
		System.out.println(" -> \"" + str + "\"");
		Memory memory = null;
		for(Memory m : a.getMemoryBank()){
			if(StringUtils.checkSimilarity(str, m.key()) <= 1D){
				memory = m;
				break;
			}
		}
		if(memory == null){
			a.say(Config.LANGUAGE.getTranslation().dontKnowSomething() + " " + str);
		}else{
			a.say(str + ": " + memory.value());
		}
		return true;
	}

	@Override
	public int getCommandId() {
		return 2;
	}
	
}
