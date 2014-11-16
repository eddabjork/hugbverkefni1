/**
 * Nafn: 		Kristin Fjola Tomasdottir
 * Dagsetning: 	13.oktober 2014
 * Markmid: 	Klasinn geymir hjalparfoll tengd utliti sem haegt er ad nota
 * 				i odrum klosum i forritinu.
 */
package Utils;

import android.app.Activity;
import android.content.Context;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tivi_dagatal_fragment.R;

public class LayoutUtils {
	
	// Notkun: showNoResult(view, context)
	// Eftir:  búið er að birta texta í view sem segir notanda að engar niðurstöður fundust
	public static void showNoResult(ScrollView view, Activity context){
		TextView nothing = new TextView(context);
		nothing.setTextSize(20);
		nothing.setText(context.getResources().getString(R.string.nothing_found));
		view.addView(nothing);
	}
	
	// Notkun: showNoResult(layout, context)
	// Eftir:  búið er að birta texta í layout sem segir notanda að engar niðurstöður fundust  
	public static void showNoResult(LinearLayout layout, Activity context, boolean big){
		TextView nothing = new TextView(context);
		if(big) nothing.setTextSize(20);
		else nothing.setTextSize(15);
		nothing.setText(context.getResources().getString(R.string.nothing_found));
		layout.addView(nothing);
	}

	//Notkun: showNotConnectedMsg(context)
	//Eftir:  skilabod um ad notandi se ekki tengdur netinu hefur verid synt
	public static void showNotConnectedMsg(Context context){
		CharSequence text = context.getResources().getString(R.string.not_online);
		int duration = Toast.LENGTH_LONG;
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}
}
