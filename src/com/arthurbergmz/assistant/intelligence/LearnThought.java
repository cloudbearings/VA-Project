package com.arthurbergmz.assistant.intelligence;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import com.arthurbergmz.assistant.Assistant;
import com.arthurbergmz.assistant.Config;
import com.arthurbergmz.assistant.intelligence.grammar.EnUSGrammar;
import com.arthurbergmz.assistant.intelligence.grammar.PtBRGrammar;
import com.arthurbergmz.assistant.intelligence.memorybank.Memory;
import com.arthurbergmz.assistant.intelligence.memorybank.Thought;
import com.arthurbergmz.assistant.language.Language;
import com.arthurbergmz.assistant.utils.StringUtils;

public class LearnThought implements Thought {
	
	@Override
	public String[] getRelatedSentences(){
		return StringUtils.EMPTY_ARRAY;
	}
	
	@Override
	public boolean execute(String label, Assistant a){
		String str = label.toLowerCase();
		String[] grammar = (Config.LANGUAGE.equals(Language.PORTUGUESE_BRAZIL) ? PtBRGrammar.LEARNING.getGrammar() : EnUSGrammar.LEARNING.getGrammar());
		for(int i = 0, j = grammar.length; i < j; i++) str = str.replace(grammar[i].toLowerCase(), "");
		str = str.trim();
		for(Memory memory : a.getMemoryBank()){
			if(StringUtils.checkSimilarity(str, memory.key()) <= 1D){
				a.say("Eu j� sei o que � " + str + ".");
				return true;
			}
		}
		System.out.println(" -> \"" + str + "\"");
		Transferable t = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(this);
		if(t == null){
			a.say("Copie a defini��o de " + str + " para que eu possa armazen�-la.");
		}else if(!t.isDataFlavorSupported(DataFlavor.stringFlavor)){
			a.say("Tenha certeza de que o que voc� tem em sua �rea de transfer�ncia � um texto para que eu possa armazenar a defini��o de " + str + ".");
		}else{
			try{
				final String key = str.trim();
				final String definition = (String) t.getTransferData(DataFlavor.stringFlavor);
				a.registerMemory(new Memory(){
					@Override
					public String key() {
						return key;
					}
					@Override
					public String value() {
						return definition;
					}
				});
				a.say("Acho que entendi! Armazenando no banco de mem�rias" + ((definition.length() > 255) ? "..." : " a defini��o de " + key + ": " + definition));
			}catch (UnsupportedFlavorException e){
				e.printStackTrace();
				return false;
			}catch (IOException e){
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	@Override
	public int getCommandId() {
		return 3;
	}
	
}
