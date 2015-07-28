package com.arthurbergmz.assistant.intelligence.memorybank;

import com.arthurbergmz.assistant.Assistant;

public interface Thought {
	
	public String[] getRelatedSentences();
	public boolean execute(String label, Assistant a);
	public int getCommandId();
	
}
