package com.arthurbergmz.assistant;

import java.util.ArrayList;
import java.util.List;

import com.arthurbergmz.assistant.intelligence.utils.Thought;
import com.arthurbergmz.assistant.misc.Debug;
import com.arthurbergmz.assistant.utils.AssistantUtils;
import com.arthurbergmz.assistant.utils.StringUtils;

public class Assistant {
	
	private String name;
	private String userName;
	private List<Thought> thoughts;
	private List<String> lastQuestions;
	private List<Thought> lastThoughts;
	private AssistantWindow window;
	private int anger;
	
	public Assistant(){
		this.window = new AssistantWindow();
		this.name = null;
		this.userName = null;
		this.anger = 0;
		this.thoughts = new ArrayList<Thought>();
		this.lastQuestions = new ArrayList<String>(30);
		this.lastThoughts = new ArrayList<Thought>(30);
		AssistantUtils.initializeDefaultThoughts(this);
		Debug.print("Assistente criado!");
	}
	
	public AssistantWindow getWindow(){
		return this.window;
	}
	
	public String setName(String name){
		this.name = name;
		return this.name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public boolean hasName(){
		return !StringUtils.isEmpty(this.name);
	}
	
	public String setUserName(String name){
		this.userName = name;
		return this.userName;
	}
	
	public String getUserName(){
		return this.userName;
	}
	
	public boolean hasUserName(){
		return !StringUtils.isEmpty(this.userName);
	}
	
	public int getAnger(){
		return this.anger;
	}
	
	public boolean registerThought(Thought thought){
		return this.thoughts.add(thought);
	}
	
	public List<Thought> getPossibleThoughts(){
		return this.thoughts;
	}
	
	public List<String> getLastQuestions(){
		return this.lastQuestions;
	}
	
	public List<Thought> getLastThoughts(){
		return this.lastThoughts;
	}
	
	public List<String> getLastQuestions(int max){
		List<String> questions = new ArrayList<String>();
		int questionsMax = this.lastQuestions.size();
		for(int i = 0; i < max; i++){
			if(i >= questionsMax) break;
			questions.add(this.lastQuestions.get(i));
		}
		return questions;
	}
	
	public List<Thought> getLastThoughts(int max){
		List<Thought> thoughts = new ArrayList<Thought>();
		int thoughtsMax = this.lastThoughts.size();
		for(int i = 0; i < max; i++){
			if(i >= thoughtsMax) break;
			thoughts.add(this.lastThoughts.get(i));
		}
		return thoughts;
	}
	
	public void ask(String str){
		System.out.println(str);
		//TODO
		this.say(str);
	}
	
	public void say(String str){
		AssistantUtils.requestSpeech(str);
	}
	
}
