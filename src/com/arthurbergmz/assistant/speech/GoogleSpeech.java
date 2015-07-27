package com.arthurbergmz.assistant.speech;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.InvalidParameterException;
import java.util.Scanner;

import javaFlacEncoder.FLACFileWriter;

import javax.net.ssl.HttpsURLConnection;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import org.json.JSONException;
import org.json.JSONObject;

import com.arthurbergmz.assistant.misc.Debug;
import com.arthurbergmz.assistant.misc.SafeList;
import com.arthurbergmz.assistant.utils.StringUtils;

public class GoogleSpeech {
	
	private static final String GOOGLE_SPEECH_DUPLEX = "https://www.google.com/speech-api/full-duplex/v1/";
	private static final byte[] FINAL_CHUNK = new byte[]{'0', '\r', '\n', '\r', '\n'};
	
	private String apiKey;
	private GoogleSpeechLanguage language;
	private SafeList<GoogleSpeechListener> listeners;
	
	public GoogleSpeech(String apiKey){
		this.apiKey = apiKey;
		this.language = GoogleSpeechLanguage.AUTO;
		this.listeners = new SafeList<GoogleSpeechListener>();
	}
	
	public void setApiKey(String apiKey){
		this.apiKey = apiKey;
	}
	
	public String getApiKey(){
		return this.apiKey;
	}
	
	public void setLanguage(GoogleSpeechLanguage language){
		this.language = language;
	}
	
	public GoogleSpeechLanguage getLanguage(){
		return this.language;
	}
	
	/**
	 * There is a limit of 15 seconds of audio.
	 */
	public void recognize(TargetDataLine targetDataLine, AudioFormat audioFormat){
		String uid = StringUtils.generateUID(32);
		String aUrl = GOOGLE_SPEECH_DUPLEX + "up?lang=" + this.language.getLanguageCode() + "&lm=dictation&client=chromium&pair=" + uid + "&key=" + this.apiKey + "&continuous"; // &Interim
		String bUrl = GOOGLE_SPEECH_DUPLEX + "down?maxresults=1&pair=" + uid;
		this.downChannel(bUrl);
		this.upChannel(targetDataLine, audioFormat, aUrl);
	}
	
	/**
	 * There is a limit of 15 seconds of audio.
	 */
	public void recognize(byte[] data, int sampleRate){
		if(data.length >= 1048576){
			byte[][] dataArray = this.chunkData(data);
			for(byte[] arr : dataArray) this.recognize(arr, sampleRate);
		}
		String uid = StringUtils.generateUID(32);
		String aUrl = GOOGLE_SPEECH_DUPLEX + "up?lang=" + this.language.getLanguageCode() + "&lm=dictation&client=chromium&pair=" + uid + "&key=" + this.apiKey; // &continuous&Interim
		String bUrl = GOOGLE_SPEECH_DUPLEX + "down?maxresults=1&pair=" + uid;
		this.downChannel(bUrl);
		this.upChannel(this.chunkData(data), sampleRate, aUrl);
	}
	
	private void upChannel(final TargetDataLine targetDataLine, final AudioFormat audioFormat, final String url){
		if(!targetDataLine.isOpen()){
			try{
				targetDataLine.open(audioFormat);
			}catch (LineUnavailableException e){
				e.printStackTrace();
				return;
			}
			targetDataLine.start();
		}
		new Thread("Upstream Thread"){
			@Override
			public void run(){
				GoogleSpeech.this.openConnection(targetDataLine, audioFormat, url);
			}
		}.start();;
	}
	
	private void upChannel(final byte[][] data, final int sampleRate, final String url){
		new Thread("Upstream File Thread"){
			@Override
			public void run(){
				GoogleSpeech.this.openConnection(data, sampleRate, url);
			}
		}.start();
	}
	
	private void downChannel(final String url){
		new Thread("Downstream Thread"){
			@Override
			public void run(){
				Scanner inputStream = GoogleSpeech.this.openConnection(url);
				if(inputStream == null) throw new NullPointerException("inputStream is null");
				while(inputStream.hasNextLine()){
					String response = inputStream.nextLine();
					if(response.length() > 17){
						try{
							GoogleSpeech.this.notifyListeners(GoogleSpeech.this.parseResponse(response));
						}catch (JSONException e){
							e.printStackTrace();
						}
					}
				}
				inputStream.close();
				Debug.print("DownChannel ok.");
			}
		}.start();
	}
	
	private Scanner openConnection(String urlString){
		try{
			URLConnection c = new URL(urlString).openConnection();
			if(!(c instanceof HttpsURLConnection)) throw new InvalidParameterException("This operation must run on a HTTPS connection!");
			HttpsURLConnection connection = (HttpsURLConnection) c;
			connection.setAllowUserInteraction(false);
			connection.setInstanceFollowRedirects(true);
			connection.setRequestMethod("GET");
			int i = connection.getResponseCode();
			if(i == HttpsURLConnection.HTTP_OK){
				return new Scanner(connection.getInputStream(), "UTF-8");
			}else{
				throw new UnsupportedOperationException("Server returned: " + i);
			}
		}catch (MalformedURLException e){
			e.printStackTrace();
		}catch (IOException e){
			e.printStackTrace();
		}
		return null;
	}
	
	private void openConnection(TargetDataLine targetDataLine, AudioFormat audioFormat, String urlString){
		try{
			URLConnection c = new URL(urlString).openConnection();
			if(!(c instanceof HttpsURLConnection)) throw new InvalidParameterException("This operation must run on a HTTPS connection!");
			HttpsURLConnection connection = (HttpsURLConnection) c;
			connection.setAllowUserInteraction(false);
			connection.setInstanceFollowRedirects(true);
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setChunkedStreamingMode(0);
			connection.setRequestProperty("Transfer-Encoding", "chunked");
			connection.setRequestProperty("Content-Type", ("audio/x-flac; rate=" + ((int) audioFormat.getSampleRate())));
			DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
			AudioSystem.write(new AudioInputStream(targetDataLine), FLACFileWriter.FLAC, outputStream);
			outputStream.write(FINAL_CHUNK);
			outputStream.flush();
			outputStream.close();
			int i = connection.getResponseCode();
			if(!(i >= 200 && i <= 300)) throw new UnsupportedOperationException("Server returned: " + i);
		}catch (MalformedURLException e){
			e.printStackTrace();
		}catch (IOException e){
			e.printStackTrace();
		}
	}
	
	private Scanner openConnection(byte[][] data, int sampleRate, String urlString){
		try{
			URLConnection c = new URL(urlString).openConnection();
			if(!(c instanceof HttpsURLConnection)) throw new InvalidParameterException("This operation must run on a HTTPS connection!");
			HttpsURLConnection connection = (HttpsURLConnection) c;
			connection.setAllowUserInteraction(false);
			connection.setInstanceFollowRedirects(true);
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setChunkedStreamingMode(0);
			connection.setRequestProperty("Transfer-Encoding", "chunked");
			connection.setRequestProperty("Content-Type", ("audio/x-flac; rate=" + sampleRate));
			DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
			for(byte[] arr : data){
				outputStream.write(arr);
				try{
					Thread.sleep(1000L);
				}catch (InterruptedException e){
					e.printStackTrace();
				}
			}
			outputStream.write(FINAL_CHUNK);
			outputStream.flush();
			outputStream.close();
			int i = connection.getResponseCode();
			if(i >= 200 && i <= 300){
				return new Scanner(connection.getInputStream());
			}else{
				throw new UnsupportedOperationException("Server returned: " + i);
			}
		}catch (MalformedURLException e){
			e.printStackTrace();
		}catch (IOException e){
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean registerListener(GoogleSpeechListener listener){
		return this.listeners.add(listener);
	}
	
	public boolean unregisterListener(GoogleSpeechListener listener){
		return this.listeners.remove(listener);
	}
	
	private synchronized void notifyListeners(GoogleSpeechResponse response){
		if(StringUtils.isEmpty(response.getResponse())) return;
		for(GoogleSpeechListener listener : this.listeners.getRawList()) listener.onResponse(response);
	}
	
	private GoogleSpeechResponse parseResponse(String raw) throws JSONException{
		GoogleSpeechResponse response = new GoogleSpeechResponse();
		JSONObject json = (StringUtils.isEmpty(raw) ? new JSONObject() : new JSONObject(raw));
		if(json.has("result")){
			JSONObject responseObject = json.getJSONArray("result").getJSONObject(0).getJSONArray("alternative").getJSONObject(0);
			response.setResponse((responseObject.has("transcript") ? responseObject.getString("transcript") : null));
			response.setConfidence((responseObject.has("confidence") ? responseObject.getLong("confidence") : 0L));
			//TODO other possible responses
		}
		return response;
	}
	
	private byte[][] chunkData(byte[] data){
		if(data.length >= 1048576){
			int chunks = (int)((data.length/524288) + 1);
			byte[][] arr = new byte[chunks][];
			for(int i = 0, j = 0; i < data.length && j < arr.length; i += 524288, j++){
				int k = ((data.length - i < 524288) ? (data.length - i) : 524288);
				arr[j] = new byte[k];
				System.arraycopy(data, i, arr[j], 0, k);
			}
			return arr;
		}else{
			byte[][] arr = new byte[1][data.length];
			System.arraycopy(data, 0, arr[0], 0, data.length);
			return arr;
		}
	}
	
}
