package com.arthurbergmz.assistant.speech;

import java.util.List;
import java.util.ArrayList;

public class GoogleSpeechResponse {
	
	private String response;
	private long confidence;
	private List<String> possibleResponses;
	
	public GoogleSpeechResponse(){
		this.response = null;
		this.confidence = 0L;
		this.possibleResponses = new ArrayList<String>(5);
	}
	
	public void setResponse(String response){
		this.response = response;
	}
	
	public String getResponse(){
		return this.response;
	}
	
	public void setConfidence(long confidence){
		this.confidence = confidence;
	}
	
	public long getConfidence(){
		return this.confidence;
	}
	
	public List<String> getPossibleResponses(){
		List<String> responses = new ArrayList<String>(this.possibleResponses);
		responses.add(0, this.response);
		return responses;
	}
	
	public List<String> getOtherPossibleResponses(){
		return this.possibleResponses;
	}
	
}
