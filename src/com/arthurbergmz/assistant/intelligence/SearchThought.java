package com.arthurbergmz.assistant.intelligence;

import com.arthurbergmz.assistant.Assistant;
import com.arthurbergmz.assistant.Config;
import com.arthurbergmz.assistant.intelligence.grammar.EnUSGrammar;
import com.arthurbergmz.assistant.intelligence.grammar.PtBRGrammar;
import com.arthurbergmz.assistant.intelligence.memorybank.Memory;
import com.arthurbergmz.assistant.intelligence.memorybank.Thought;
import com.arthurbergmz.assistant.language.Language;
import com.arthurbergmz.assistant.utils.StringUtils;

public class SearchThought implements Thought {
	
	@Override
	public String[] getRelatedSentences(){
		return StringUtils.EMPTY_ARRAY;
	}
	
	@Override
	public boolean execute(String label, Assistant a){
		String str = label.toLowerCase();
		String[] grammar = (Config.LANGUAGE.equals(Language.PORTUGUESE_BRAZIL) ? PtBRGrammar.SEARCH.getGrammar() : EnUSGrammar.SEARCH.getGrammar());
		for(int i = 0, j = grammar.length; i < j; i++) str = str.replace(grammar[i].toLowerCase(), "");
		str = str.trim();
		for(Memory memory : a.getMemoryBank()){
			if(StringUtils.checkSimilarity(str, memory.key()) <= 1D){
				a.say(str + ": " + memory.value());
				return true;
			}
		}
		System.out.println(" -> \"" + str + "\"");
		a.say("Eu ainda não sei o que é e ainda não posso procurar na internet por " + str);
		return true;
	}

	@Override
	public int getCommandId() {
		return 1;
	}
	
}
