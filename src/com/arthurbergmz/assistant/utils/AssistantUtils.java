package com.arthurbergmz.assistant.utils;

import java.io.UnsupportedEncodingException;
import java.util.List;

import com.amazonaws.auth.BasicAWSCredentials;
import com.arthurbergmz.assistant.Assistant;
import com.arthurbergmz.assistant.Config;
import com.arthurbergmz.assistant.Initializer;
import com.arthurbergmz.assistant.intelligence.HelloThought;
import com.arthurbergmz.assistant.intelligence.LearnThought;
import com.arthurbergmz.assistant.intelligence.SearchThought;
import com.arthurbergmz.assistant.intelligence.WhatIsSomethingThought;
import com.arthurbergmz.assistant.intelligence.grammar.EnUSGrammar;
import com.arthurbergmz.assistant.intelligence.grammar.PtBRGrammar;
import com.arthurbergmz.assistant.intelligence.memorybank.Thought;
import com.arthurbergmz.assistant.misc.Debug;
import com.arthurbergmz.assistant.misc.SoundPlayer;
import com.ivona.services.tts.IvonaSpeechCloudClient;
import com.ivona.services.tts.model.CreateSpeechRequest;
import com.ivona.services.tts.model.Input;
import com.ivona.services.tts.model.Voice;

public class AssistantUtils {
	
	private static final IvonaSpeechCloudClient SPEECH_CLOUD;
	private static final Runnable ASSISTANT_SPEAKING;
	private static final Runnable STOP_ASSISTANT_SPEAKING;
	
	static{
		SPEECH_CLOUD = new IvonaSpeechCloudClient(new BasicAWSCredentials(Config.AMAZON_ACCESS_KEY, Config.AMAZON_SECRET_KEY));
		SPEECH_CLOUD.setEndpoint(Config.AMAZON_IVONA_ENDPOINT);
		ASSISTANT_SPEAKING = new Runnable(){
			@Override
			public void run() {
				Initializer.ASSISTANT.getWindow().talk();
			}
		};
		STOP_ASSISTANT_SPEAKING = new Runnable(){
			@Override
			public void run() {
				Initializer.ASSISTANT.getWindow().stopTalking();
			}
		};
	}
	
	public static void initializeDefaultThoughts(Assistant a){
		a.registerThought(new HelloThought());
		a.registerThought(new SearchThought());
		a.registerThought(new WhatIsSomethingThought());
		a.registerThought(new LearnThought());
		Debug.print("Pensamentos padrões definidos!");
	}
	
	public static void requestSpeech(String str){
		CreateSpeechRequest createSpeechRequest = new CreateSpeechRequest();
		Input input = new Input();
		Voice voice = new Voice();
		voice.setName(Config.LANGUAGE.getSpeaker());
		input.setData(str);
		createSpeechRequest.setInput(input);
		createSpeechRequest.setVoice(voice);
		try{
			new SoundPlayer(SPEECH_CLOUD.getCreateSpeechUrl(createSpeechRequest).toString()).play(ASSISTANT_SPEAKING, STOP_ASSISTANT_SPEAKING);
		}catch (UnsupportedEncodingException e){
			e.printStackTrace();
		}
	}
	
	public static Thought getMostSimilarThought(String str, List<Thought> thoughts){
		str = str.toLowerCase();
		Thought thought = null;
		int cmd = 0;
		double distance = 10D;
		if(Config.LANGUAGE.getLanguageCode().equals("pt-BR")){
			PtBRGrammar[] grammar = PtBRGrammar.values();
			for(int i = 0, j = grammar.length; i < j; i++){
				PtBRGrammar g = grammar[i];
				String[] actions = g.getGrammar();
				for(int k = 0, l = actions.length; k < l; k++){
					if(str.startsWith(actions[k].toLowerCase())){
						cmd = g.getCommandId();
						break;
					}
				}
			}
		}else{
			EnUSGrammar[] grammar = EnUSGrammar.values();
			for(int i = 0, j = grammar.length; i < j; i++){
				EnUSGrammar g = grammar[i];
				String[] actions = g.getGrammar();
				for(int k = 0, l = actions.length; k < l; k++){
					if(str.startsWith(actions[k].toLowerCase())){
						cmd = g.getCommandId();
						break;
					}
				}
			}
		}
		for(int i = 0, j = thoughts.size(); i < j; i++){
			Thought temporaryThought = thoughts.get(i);
			if((cmd != 0) && (temporaryThought.getCommandId() == cmd)) return temporaryThought;
			String[] sentences = temporaryThought.getRelatedSentences();
			for(int k = 0, l = sentences.length; k < l; k++){
				double temporaryDistance = StringUtils.checkSimilarity(str, sentences[k]);
				if((temporaryDistance < 4) && (temporaryDistance < distance)){
					thought = temporaryThought;
					distance = temporaryDistance;
					break;
				}
			}
		}
		return thought;
	}
	
}
