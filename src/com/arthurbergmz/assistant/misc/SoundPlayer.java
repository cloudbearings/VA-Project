package com.arthurbergmz.assistant.misc;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.arthurbergmz.assistant.Initializer;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.FactoryRegistry;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

public class SoundPlayer extends PlaybackListener implements Runnable {
	
	private String filePath;
	private AdvancedPlayer player;
	private Thread playerThread;
	
	public SoundPlayer(String filePath){
		this.filePath = filePath;
	}
	
	public void play(){
		try {
			this.player = new AdvancedPlayer(new URL(this.filePath).openStream(), FactoryRegistry.systemRegistry().createAudioDevice());
			this.player.setPlayBackListener(this);
			this.playerThread = new Thread(this);
			this.playerThread.start();
		}catch (MalformedURLException e){
			e.printStackTrace();
		}catch (JavaLayerException e){
			e.printStackTrace();
		}catch (IOException e){
			e.printStackTrace();
		}
	}
	
	@Override
	public void run(){
		try{
			this.player.play();
		}catch (JavaLayerException e){
			e.printStackTrace();
		}
	}
	
	@Override
	public void playbackStarted(PlaybackEvent e){
		Initializer.ASSISTANT.getWindow().talk();
	}
	
	@Override
	public void playbackFinished(PlaybackEvent e){
		Initializer.ASSISTANT.getWindow().stopTalking();
	}
	
}