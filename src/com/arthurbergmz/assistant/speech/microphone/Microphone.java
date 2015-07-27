package com.arthurbergmz.assistant.speech.microphone;

import java.io.File;

import javax.sound.sampled.AudioFileFormat.Type;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine.Info;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

public class Microphone {
	
	private File file;
	private Type fileType;
	private AudioFormat audioFormat;
	private TargetDataLine targetDataLine;
	private CaptureState captureState;
	
	public Microphone(Type fileType){
		this.fileType = fileType;
		this.audioFormat = new AudioFormat(8000F, 16, 1, true, false); //new AudioFormat(44100F, 32, 2, true, false);
		this.captureState = CaptureState.CLOSED;
		this.targetDataLine = null;
	}
	
	public void setCaptureState(CaptureState captureState){
		this.captureState = captureState;
	}
	
	public CaptureState getCaptureState(){
		return this.captureState;
	}
	
	public void setFile(File file){
		this.file = file;
	}
	
	public File getFile(){
		System.out.println("FILE SIZE: " + this.file.length());
		return this.file;
	}
	
	public void setFileType(Type fileType){
		this.fileType = fileType;
	}
	
	public Type getFileType(){
		return this.fileType;
	}
	
	public void setTargetDataLine(TargetDataLine targetDataLine){
		this.targetDataLine = targetDataLine;
	}
	
	public TargetDataLine getTargetDataLine(){
		return this.targetDataLine;
	}
	
	public AudioFormat getAudioFormat(){
		return this.audioFormat;
	}
	
	private boolean initTargetDataLine(){
		try{
			this.targetDataLine = (TargetDataLine) AudioSystem.getLine(new Info(TargetDataLine.class, this.audioFormat));
		}catch (LineUnavailableException e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean start(String audioFile){
		return this.start(new File(audioFile));
	}
	
	public boolean start(File audioFile){
		this.captureState = CaptureState.CAPTURING;
		this.file = audioFile;
		if(this.targetDataLine == null) if(!this.initTargetDataLine()) return false;
		new Thread(new CaptureThread(this)).start();
		return true;
	}
	
	public boolean open(){
		if(this.targetDataLine == null) if(!this.initTargetDataLine()) return false;
		if(!this.targetDataLine.isActive() && !this.targetDataLine.isOpen() && !this.targetDataLine.isRunning()){
			try{
				this.captureState = CaptureState.PROCESSING;
				this.targetDataLine.open(this.audioFormat);
				this.targetDataLine.start();
			}catch (LineUnavailableException e){
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
	
	public boolean close(){
		if((this.targetDataLine == null) || this.captureState.equals(CaptureState.CLOSED)) return false;
		this.targetDataLine.stop();
		this.targetDataLine.close();
		this.captureState = CaptureState.CLOSED;
		return true;
	}
	
}
