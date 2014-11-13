package com.example.tivi_dagatal_fragment;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.util.Log;

public class Utils {

	public static String parseAirTime(String airTime){
		SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm");
	    SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a");
	    String time = airTime.substring(0, airTime.length()-2);
	    String ampm = airTime.substring(airTime.length()-2);
	    String newAirTime = time + " " + ampm.toUpperCase();
	    Date date = new Date();
	    try{
	    	date = parseFormat.parse(newAirTime);
	    } catch(Exception e){
	    	Log.e("parse error", "could not parse date");
	    }
	    return displayFormat.format(date);
	}
	
	public static String translateWeekday(String weekday, Activity context){
		Log.v("weekday", weekday);
		//Log.v("monday", R.string.mon_en+"");
		if (weekday.equals(context.getResources().getString(R.string.mon_en))) 
			return context.getResources().getString(R.string.mon_is);
		else if (weekday.equals(context.getResources().getString(R.string.tue_en))) 
			return context.getResources().getString(R.string.tue_is);
		else if (weekday.equals(context.getResources().getString(R.string.wed_en))) 
			return context.getResources().getString(R.string.wed_is);
		else if (weekday.equals(context.getResources().getString(R.string.thu_en))) 
			return context.getResources().getString(R.string.thu_is);
		else if (weekday.equals(context.getResources().getString(R.string.fri_en))) 
			return context.getResources().getString(R.string.fri_is);
		else if (weekday.equals(context.getResources().getString(R.string.sat_en))) 
			return context.getResources().getString(R.string.sat_is);
		else if (weekday.equals(context.getResources().getString(R.string.sun_en))) 
			return context.getResources().getString(R.string.sun_is);
		else return weekday;
	}
	
	public static void addFragmentToStack(FragmentManager manager,Fragment fragment){
		FragmentTransaction fragmentTransaction = manager.beginTransaction();
		fragmentTransaction.replace(R.id.content_frame, fragment);
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();
	}
}
