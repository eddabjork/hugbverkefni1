package Utils;

import android.app.Activity;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.tivi_dagatal_fragment.R;

public class LayoutUtils {
	
	// Notkun: showNoResult(view, context)
	// Eftir:  b�i� er a� birta texta � view sem segir notanda a� engar ni�urst��ur fundust
	public static void showNoResult(ScrollView view, Activity context){
		TextView nothing = new TextView(context);
		nothing.setTextSize(20);
		nothing.setText(context.getResources().getString(R.string.nothing_found));
		view.addView(nothing);
	}
	
	// Notkun: showNoResult(layout, context)
	// Eftir:  b�i� er a� birta texta � layout sem segir notanda a� engar ni�urst��ur fundust  
	public static void showNoResult(LinearLayout layout, Activity context){
		TextView nothing = new TextView(context);
		nothing.setTextSize(20);
		nothing.setText(context.getResources().getString(R.string.nothing_found));
		layout.addView(nothing);
	}

}
