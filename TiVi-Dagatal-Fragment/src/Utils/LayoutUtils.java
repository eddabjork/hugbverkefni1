package Utils;

import android.app.Activity;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

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
	public static void showNoResult(LinearLayout layout, Activity context){
		TextView nothing = new TextView(context);
		nothing.setTextSize(20);
		nothing.setText(context.getResources().getString(R.string.nothing_found));
		layout.addView(nothing);
	}

}
