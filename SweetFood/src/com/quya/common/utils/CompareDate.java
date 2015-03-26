package com.quya.common.utils;

public class CompareDate {
	
	public CompareDate(){	
	}

	public static boolean CompareDates(String startTime,String endTime,String start,String end){
		if((startTime.compareTo(start)<=0) && 
			     endTime.compareTo(start)>=0){
			return true;
			}else if((startTime.compareTo(start)>=0)&&
			     startTime.compareTo(end)<=0){
			return true;
			}else{
			return false;
			}
	}
}
