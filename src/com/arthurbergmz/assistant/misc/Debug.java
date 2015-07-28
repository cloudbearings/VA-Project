package com.arthurbergmz.assistant.misc;

public class Debug {
	
	public static boolean ACTIVATED = false;
	
	public static void print(String msg){
		if(ACTIVATED) System.out.println(msg);
	}
	
}
