package com.arthurbergmz.assistant.utils;

import java.io.UnsupportedEncodingException;

import com.amazonaws.auth.BasicAWSCredentials;
import com.arthurbergmz.assistant.Assistant;
import com.arthurbergmz.assistant.intelligence.HelloThought;
import com.arthurbergmz.assistant.misc.Debug;
import com.arthurbergmz.assistant.misc.SoundPlayer;
import com.ivona.services.tts.IvonaSpeechCloudClient;
import com.ivona.services.tts.model.CreateSpeechRequest;
import com.ivona.services.tts.model.Input;
import com.ivona.services.tts.model.Voice;

public class AssistantUtils {
	
	private static IvonaSpeechCloudClient speechCloud;
	
	static{
		speechCloud = new IvonaSpeechCloudClient(new BasicAWSCredentials("YOUR AMAZON ACCESS KEY", "YOUR AMAZON SECRET KEY"));
        speechCloud.setEndpoint("https://tts.eu-west-1.ivonacloud.com");
	}
	
	public static void initializeDefaultThoughts(Assistant a){
		a.registerThought(new HelloThought());
		Debug.print("Pensamentos padrões definidos!");
	}
	
	public static void requestSpeech(String str){
		CreateSpeechRequest createSpeechRequest = new CreateSpeechRequest();
		Input input = new Input();
		Voice voice = new Voice();
		voice.setName("Ricardo");
		input.setData(str);
		createSpeechRequest.setInput(input);
		createSpeechRequest.setVoice(voice);
		try{
			new SoundPlayer(speechCloud.getCreateSpeechUrl(createSpeechRequest).toString()).play();
		}catch (UnsupportedEncodingException e){
			e.printStackTrace();
		}
	}
	
}
