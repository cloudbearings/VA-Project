package com.arthurbergmz.assistant.speech.microphone;

import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

public class CaptureThread implements Runnable {
	
	private Microphone mic;
	
	public CaptureThread(Microphone microphone){
		this.mic = microphone;
	}
	
	public void run(){
		this.mic.open();
		try{
			AudioSystem.write(new AudioInputStream(this.mic.getTargetDataLine()), this.mic.getFileType(), this.mic.getFile());
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
