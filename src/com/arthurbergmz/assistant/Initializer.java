package com.arthurbergmz.assistant;

import java.io.File;
import java.nio.file.Files;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import javaFlacEncoder.FLACFileWriter;
import jhook.Keyboard;
import jhook.KeyboardListener;

import com.arthurbergmz.assistant.speech.GoogleSpeech;
import com.arthurbergmz.assistant.speech.GoogleSpeechListener;
import com.arthurbergmz.assistant.speech.GoogleSpeechResponse;
import com.arthurbergmz.assistant.speech.GoogleSpeechLanguage;
import com.arthurbergmz.assistant.speech.microphone.Microphone;
import com.arthurbergmz.assistant.utils.AssistantUtils;

public class Initializer {
	
	public static Assistant ASSISTANT;
	public static GoogleSpeech GOOGLE_SPEECH;
	public static final ExecutorService THREAD_EXECUTOR = Executors.newSingleThreadExecutor(new ThreadFactory(){
		public Thread newThread(Runnable runnable) {
			return new Thread(runnable, "VirtualAssistant Async Task");
		}
	});
	
	public static void main(String[] args){
		ASSISTANT = new Assistant();
		AssistantUtils.requestSpeech("Inicializando consciência. Me preparando para responder...");
		allowSpeaking();
	}
	
	public static void allowSpeaking(){
		GOOGLE_SPEECH = new GoogleSpeech("YOUR CHROMIUM API KEY");
		GOOGLE_SPEECH.setLanguage(GoogleSpeechLanguage.PORTUGUESE_BRAZIL);
		GOOGLE_SPEECH.registerListener(new GoogleSpeechListener(){
			public void onResponse(GoogleSpeechResponse response){
				String str = response.getResponse();
				ASSISTANT.ask(str);
			}
		});
		System.out.println("Você pode começar a falar!");
		Keyboard kb = new Keyboard();
		kb.addListener(new KeyboardListener(){
			private boolean isHoldingButton = false;
			private Microphone mic = new Microphone(FLACFileWriter.FLAC);
			private File file = null;
			public void keyPressed(boolean keyDown, int keyCode){
				if(keyCode == 18 && keyDown && !this.isHoldingButton){
					this.isHoldingButton = true;
					System.out.println("Gravando...");
					try{
						this.file = new File("tmpVirtualAssistant.flac");
						this.mic.start(this.file);
					}catch(Exception e){
						e.printStackTrace();
					}
				}else if(keyCode == 18 && !keyDown && this.isHoldingButton){
					this.isHoldingButton = false;
					try{
						this.mic.close();
						byte[] data = Files.readAllBytes(this.mic.getFile().toPath());
						Initializer.GOOGLE_SPEECH.recognize(data, (int)this.mic.getAudioFormat().getSampleRate());
						this.mic.getFile().delete();
					}catch (Exception e){
						e.printStackTrace();
					}
				}
			}
		});
	}
	
}
