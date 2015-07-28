package com.arthurbergmz.assistant.utils;

import java.text.Normalizer;
import java.util.regex.Pattern;


public class StringUtils {
	
	public static final String ALPHANUMERIC_SEQUENCE = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	public static final Pattern NORMAL_PATTERN = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
	public static final String[] EMPTY_ARRAY = new String[]{};
	
	private static String normalizeString(String str){
		return NORMAL_PATTERN.matcher(Normalizer.normalize(str.toLowerCase(), Normalizer.Form.NFD)).replaceAll("").replaceAll("[^A-Za-z0-9]", "");
	}
	
	// Levenshtein distance algorithm
	public static double checkSimilarity(String str1, String str2){
		str1 = normalizeString(str1);
		str2 = normalizeString(str2);
		double[] costs = new double[str2.length()+1];
		for(int j = 0; j < costs.length; j++) costs[j] = (double) j;
		for(int i = 1; i <= str1.length(); i++){
			costs[0] = (double) i;
			double k = (double) i - 1;
			for(int l = 1; l <= str2.length(); l++){
				double m = Math.min(1 + Math.min(costs[l], costs[l-1]), ((str1.charAt(i-1) == str2.charAt(l-1)) ? k : (k + 1)));
				k = costs[l];
				costs[l] = m;
			}
		}
		return costs[str2.length()];
	}
	
	public static boolean isEmpty(String str){
		return ((str == null) || (str.length() == 0));
	}
	
	public static String generateUID(int length){
		StringBuilder builder = new StringBuilder();
		for(int i = 0, j = (ALPHANUMERIC_SEQUENCE.length() - 1); i < length; i++) builder.append(ALPHANUMERIC_SEQUENCE.charAt(MathUtils.RANDOM.nextInt(j)));
		return builder.toString();
	}
	
	public static String getRandomString(String[] stringArray){
		int length = stringArray.length;
		if(stringArray.length < 1) return "";
		if(stringArray.length == 1) return stringArray[0];
		return stringArray[MathUtils.RANDOM.nextInt(length - 1)];
	}
	
}
