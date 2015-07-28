package com.arthurbergmz.assistant.intelligence;

import com.arthurbergmz.assistant.Assistant;
import com.arthurbergmz.assistant.Config;
import com.arthurbergmz.assistant.intelligence.memorybank.Thought;
import com.arthurbergmz.assistant.language.Language;
import com.arthurbergmz.assistant.utils.StringUtils;

public class HelloThought implements Thought {
	
	private static String[] a_enUS = new String[]{"Hello", "Hi"};
	private static String[] a_ptBR = new String[]{"Olá", "Oi"};
	private static String[] b_enUS = new String[]{"Hi.", "Hello.", "Hi, I'm listening to you!", "Hello, I'm here!"};
	private static String[] b_ptBR = new String[]{"Oi.", "Olá.", "Oi, estou ouvindo!", "Olá, estou aqui!"};
	
	@Override
	public String[] getRelatedSentences(){
		return (Config.LANGUAGE.equals(Language.PORTUGUESE_BRAZIL) ? a_ptBR : a_enUS);
	}
	
	@Override
	public boolean execute(String label, Assistant a){
		String str = StringUtils.getRandomString((Config.LANGUAGE.equals(Language.PORTUGUESE_BRAZIL) ? b_ptBR : b_enUS));
		System.out.println(" -> \"" + str + "\"");
		a.say(str);
		return true;
	}

	@Override
	public int getCommandId() {
		return 0;
	}
	
}
