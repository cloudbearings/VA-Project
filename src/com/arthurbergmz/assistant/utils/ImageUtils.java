package com.arthurbergmz.assistant.utils;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class ImageUtils {
	
	public static Image loadResource(String file){
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream input = classLoader.getResourceAsStream(file);
		try{
			return ImageIO.read(input);
		}catch (IOException e){
			e.printStackTrace();
		}
		return null;
	}
	
}
