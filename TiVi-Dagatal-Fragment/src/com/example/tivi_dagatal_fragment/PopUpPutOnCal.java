/**
 * Nafn: 		Kristin Fjola Tomasdottir
 * Dagsetning: 	30. oktober 2014
 * Markmid: 	Pop-up sem bidur notanda uppa ad vista akvedna thattarod dagatalinu sinu. 
 */
package com.example.tivi_dagatal_fragment;

import Data.DbUtils;
import Dtos.Show;
import Utils.VariousUtils;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class PopUpPutOnCal extends DialogFragment{
	
	private static Show show;

	// Notkun: putOnCalPopUp = newInstance(showToSave)
	// Eftir:  putOnCalPopUp er pop-up sem bidur notenda uppa
	// 		   ad vista thattinn showToSave i dagatali
	public static PopUpPutOnCal newInstance(Show showToSave) {
		PopUpPutOnCal frag = new PopUpPutOnCal();
		show = showToSave;
        return frag;
    }

	// Notkun: dialog = onCreateDialog(savedInstanceState)
	// Eftir:  dialog er pop-up sem bidur notenda uppa ad
	//		   vista thatt i dagatali
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder
			.setIcon(R.drawable.app_icon)
	        .setTitle(R.string.app_name)
	        .setMessage(R.string.popup_put_cal)
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
        		//Eftir: thaetti er baett a dagatalslista
	        	public void onClick(DialogInterface dialog, int whichButton) {
	                	addToCal(show);
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
    
	// Notkun: addToCal(show)
	// Eftir:  show hefur verid baett i dagatalid
    public void addToCal(Show show){
    	DbUtils dbHelper = new DbUtils(getActivity());
		dbHelper.putShowOnCal(show);
		VariousUtils.flushCache("calendarEpisodes");
    }
}
