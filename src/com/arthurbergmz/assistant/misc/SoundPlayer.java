package com.arthurbergmz.assistant.misc;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.FactoryRegistry;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

public class SoundPlayer extends PlaybackListener implements Runnable {
	
	private String filePath;
	private AdvancedPlayer player;
	private Thread playerThread;
	private Runnable onStart;
	private Runnable onEnd;
	
	public SoundPlayer(String filePath){
		this.filePath = filePath;
	}
	
	public void play(){
		this.play(null, null);
	}
	
	public void play(Runnable onStart, Runnable onEnd){
		try {
			this.onStart = onStart;
			this.onEnd = onEnd;
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
		if(this.onStart != null) this.onStart.run();
	}
	
	@Override
	public void playbackFinished(PlaybackEvent e){
		if(this.onEnd != null) this.onEnd.run();
	}
	
}