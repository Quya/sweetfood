package com.quya.common.utils;

import java.util.Random;

	/**
	 * 产生n位随机字符串
	 * @author wulihai
	 *
	 */
public final class GenerateCoding {
	private static String contentStr="0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static Random random = null;
	private static char[] numbersAndLetters = null;
	private static Object initLock = new Object(); 
	
	public static  String randomString(int length) {

	         if (length < 1) {
	             return null;
	         }
	         if (random == null) {
	        	 //同步锁定
	             synchronized (initLock) {
	                 if (random == null) {
	                	 random = new Random();
	                     numbersAndLetters = (contentStr).toCharArray();
	                 }
	             }
	         }
	         char [] randBuffer = new char[length];
	         for (int i=0; i<randBuffer.length; i++) {
	             randBuffer[i] = numbersAndLetters[random.nextInt(35)];
	         }
	         return new String(randBuffer);
	}

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String str=randomString(6);
		System.out.println(str);
	}

}
