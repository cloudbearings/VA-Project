package com.arthurbergmz.assistant.intelligence.utils;

import com.arthurbergmz.assistant.Assistant;

public interface Thought {
	
	public String[] getRelatedSentences();
	public String execute(String label, Assistant a);
	
}
