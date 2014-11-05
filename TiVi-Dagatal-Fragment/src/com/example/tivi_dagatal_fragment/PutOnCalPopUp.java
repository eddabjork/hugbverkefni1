package com.example.tivi_dagatal_fragment;

import Data.DbUtils;
import Dtos.Show;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

public class PutOnCalPopUp extends DialogFragment{
	
	private static Show show;
	private static int title;

	public static PutOnCalPopUp newInstance(int titleToDisplay, Show showToSave) {
		PutOnCalPopUp frag = new PutOnCalPopUp();
		show = showToSave;
		title = titleToDisplay;
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    	return new AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.temp_icon)
                .setTitle(title)
                .setPositiveButton(R.string.neg_answer,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            
                        }
                    }
                )
                .setNegativeButton(R.string.pos_answer,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        	addToCal(show);
                        }
                    }
                )
                .create();
    }
    
    public void addToCal(Show show){
    	DbUtils dbHelper = new DbUtils(getActivity());
		dbHelper.putShowOnCal(show);
		MainActivity.cache.remove("calendarEpisodes");
		Log.v("cache", "Calendar episodes removed from cache");
    }
}
