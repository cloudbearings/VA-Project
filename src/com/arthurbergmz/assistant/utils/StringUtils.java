package com.arthurbergmz.assistant.utils;


public class StringUtils {
	
	public static final String ALPHANUMERIC_SEQUENCE = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	
	// Levenshtein distance algorithm
	public static int checkSimilarity(String str1, String str2){
		str1 = str1.toLowerCase().replaceAll("[^A-Za-z0-9 ]", "");
		str2 = str2.toLowerCase().replaceAll("[^A-Za-z0-9 ]", "");
		int[] costs = new int[str2.length()+1];
		for(int j = 0; j < costs.length; j++) costs[j] = j;
		for(int i = 1; i <= str1.length(); i++){
			costs[0] = i;
			int k = i - 1;
			for(int l = 1; l <= str2.length(); l++){
				int m = Math.min(1 + Math.min(costs[l], costs[l-1]), ((str1.charAt(i-1) == str2.charAt(l-1)) ? k : (k + 1)));
				k = costs[l];
				costs[l] = m;
			}
		}
		return costs[str2.length()];
	}
	
	public static boolean isEmpty(String str){
		if(str == null || str.length() == 0) return true;
		return false;
	}
	
	public static String generateUID(int length){
		StringBuilder builder = new StringBuilder();
		int j = ALPHANUMERIC_SEQUENCE.length()-1;
		for(int i = 0; i < length; i++) builder.append(ALPHANUMERIC_SEQUENCE.charAt(MathUtils.RANDOM.nextInt(j)));
		return builder.toString();
	}
	
}
