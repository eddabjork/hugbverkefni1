/**
 * Nafn: 		Kristin Fjola Tomasdottir
 * Dagsetning: 	13.oktober 2014
 * Markmid: 	Klasinn geymir allskonar hjalparfoll sem haegt er ad nota
 * 				i odrum klosum i forritinu.
 */
package Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.tivi_dagatal_fragment.FragmentList;
import com.example.tivi_dagatal_fragment.MainActivity;
import com.example.tivi_dagatal_fragment.R;
import com.example.tivi_dagatal_fragment.R.string;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

public class VariousUtils {

	//Notkun: parsedAirTime = parseAirTime(airTime)
	//Eftir:  parsedAirTime er hh:MM:ss format af airTime
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
	
	//Notkun: translatedWeekday = translateWeekday(weekday, context)
	//Eftir:  translatedWeekday er islensk thyding af weekday
	public static String translateWeekday(String weekday, Activity context){
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
	
	//Notkun: addFragmentToStack(manager, fragment)
	//Fyrir:  búið er að birta fragment ofaná stack með síðasta
	//		  fragment undir.
	public static void addFragmentToStack(FragmentManager manager,Fragment fragment){
		FragmentTransaction fragmentTransaction = manager.beginTransaction();
		fragmentTransaction.replace(R.id.content_frame, fragment);
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();
	}
	
	//Notkun:		 flushCache(key)
	//Eftirskilyrði: það sem var með key sem lykil hefur verið eytt úr cache-minni
	public static void flushCache(String key){
		MainActivity.getCache().remove(key);
		Log.v("cache", key + " removed from cache");
	}
	
	//Notkun:		 flushCacheAfter12Hours(key)
	//Eftirskilyrði: það sem var með key sem lykil hefur verið eytt úr cache-minni
	public static void flushCacheAfter12Hours(String key){
		long time = System.currentTimeMillis();
		long twelveHours = (long) (60000*60*12);
		if((time - MainActivity.getAppStartTime()) > twelveHours){
			flushCache(key);
		}
	}
	
	//Notkun: ans = isConnectedToInternet(context)
	//Eftir:  ans er true ef notandi er tengdur netinu, annars false
	public static boolean isConnectedToInternet(Activity context){
		ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);		 
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();	
		return isConnected;	
	}
	
	//Notkun: width = getScreenWidth(context)
	//Eftir:  width er breiddin a taekinu sem er ad nota appid
	public static int getScreenWidth(Activity context){
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		return width;
	}
}
