/**
 * Nafn: 		Kristín Fjóla Tómasdóttir
 * Dagsetning: 	13. nóvember 2014
 * Markmið: 	Pop-up sem spyr notanda hvort hann vilji eyða þætti. 
 */
package com.example.tivi_dagatal_fragment;

import Data.DbUtils;
import Dtos.Show;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;


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
    	return new AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.app_icon)
                .setTitle(R.string.popup_del)
                .setPositiveButton(R.string.neg_answer,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // do nothing
                        }
                    }
                )
                .setNegativeButton(R.string.pos_answer,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        	removeFromMyShows(show);
                        }
                    }
                )
                .create();
    }
    
	//Notkun:	removeFromMyEpisodes(show);
  	//Eftir:	Búið er að eyða show úr gagnagrunni
	public void removeFromMyShows(Show show){
		DbUtils dbHelper = new DbUtils(getActivity());
		dbHelper.deleteShow(show);
		MainActivity.cache.remove("calendarEpisodes");
		Log.v("cache", "Calendar episodes removed from cache");
		FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                       .replace(R.id.content_frame, new FragmentList())
                       .commit();
	}
}
