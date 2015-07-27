package com.arthurbergmz.assistant;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JWindow;

import com.arthurbergmz.assistant.utils.ImageUtils;

public class AssistantWindow {
	
	private JLabel logo;
	private boolean speaking;
	
	public AssistantWindow(){
		this.speaking = false;
		JWindow window = new JWindow();
		window.setAlwaysOnTop(true);
		window.setLayout(null);
		this.logo = new JLabel();
		this.logo.setIcon(new ImageIcon(ImageUtils.loadResource("Interface00.png")));
		this.logo.setHorizontalAlignment(JLabel.CENTER);
		this.logo.setOpaque(false);
		this.logo.setBounds(0, 0, 240, 714);
		window.add(this.logo);
		window.setBackground(new Color(1F, 1F, 1F, 0F));
		window.setSize(240, 768);
		window.setLocationRelativeTo(null);
		window.setVisible(true);
	}
	
	public void talk(){
		this.speaking = true;
		//TODO Need to work harder on it
		Initializer.THREAD_EXECUTOR.execute(new Runnable(){
			@Override
			public void run() {
				while(AssistantWindow.this.speaking){
					try{
						Thread.sleep(50L);
						AssistantWindow.this.logo.setIcon(new ImageIcon(ImageUtils.loadResource("Interface01.png")));
						Thread.sleep(50L);
						AssistantWindow.this.logo.setIcon(new ImageIcon(ImageUtils.loadResource("Interface00.png")));
						Thread.sleep(50L);
						AssistantWindow.this.logo.setIcon(new ImageIcon(ImageUtils.loadResource("Interface02.png")));
						Thread.sleep(50L);
						AssistantWindow.this.logo.setIcon(new ImageIcon(ImageUtils.loadResource("Interface01.png")));
						Thread.sleep(50L);
						AssistantWindow.this.logo.setIcon(new ImageIcon(ImageUtils.loadResource("Interface00.png")));
					}catch (Exception e){
						e.printStackTrace();
					}
				}
			}
		});
	}
	
	public void stopTalking(){
		this.speaking = false;
	}
	
}
