/**
 * Nafn: 		Kristín Fjóla Tómasdóttir
 * Dagsetning: 	13. nóvember 2014
 * Markmið: 	Pop-up sem spyr notanda hvort hann vilji eyða þætti. 
 */
package com.example.tivi_dagatal_fragment;

import Data.DbUtils;
import Dtos.Show;
import Utils.VariousUtils;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


public class PopUpDelete extends DialogFragment{
	
	private static Show show;

	// Notkun: popUpDelete = newInstance(showToDelete)
	// Eftir:  popUpDelete er pop-up sem spyr notanda hvort
	//		   hann vilji eyða þættinum showToDelete úr gagnagrunni		
	public static PopUpDelete newInstance(Show showToDelete) {
		PopUpDelete frag = new PopUpDelete();
		show = showToDelete;
        return frag;
    }

	// Notkun: dialog = onCreateDialog(savedInstanceState)
	// Eftir:  dialog er pop-up sem spyr notanda hvort hann vilji eyða þætti
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    	AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder
			.setIcon(R.drawable.app_icon)
	        .setTitle(R.string.app_name)
	        .setMessage(R.string.popup_del)
	        .setPositiveButton(R.string.neg_answer,
	            new DialogInterface.OnClickListener() {
	        		//Notkun: onClick(dialog, whichButton)
	        		//Eftir: ekkert
	                public void onClick(DialogInterface dialog, int whichButton) {
	                    // do nothing
	                }
	            }
	        )
	        .setNegativeButton(R.string.pos_answer,
	            new DialogInterface.OnClickListener() {
	        	//Notkun: onClick(dialog, whichButton)
        		//Eftir: þaetti hefur verid eytt ur 'Thaettirnir minir' lista
	        	public void onClick(DialogInterface dialog, int whichButton) {
	        			removeFromMyShows(show);
	                }
	            }
	        );
		Dialog d = builder.show();
		// change title color
		int textViewId = d.getContext().getResources().getIdentifier("android:id/alertTitle", null, null);
		TextView tv = (TextView) d.findViewById(textViewId);
		tv.setTextColor(getResources().getColor(R.color.app_red));
		// change divider color
		int dividerId = d.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
		View divider = d.findViewById(dividerId);
		divider.setBackgroundColor(getResources().getColor(R.color.app_red));
		return d;
    }
    
	//Notkun:	removeFromMyEpisodes(show);
  	//Eftir:	Búið er að eyða show úr gagnagrunni
	public void removeFromMyShows(Show show){
		DbUtils dbHelper = new DbUtils(getActivity());
		dbHelper.deleteShow(show);
		VariousUtils.flushCache("calendarEpisodes");
		FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                       .replace(R.id.content_frame, new FragmentList())
                       .commit();
	}
}
