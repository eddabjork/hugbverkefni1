/**
 * Nafn: 		Krist�n Fj�la T�masd�ttir
 * Dagsetning: 	30. okt�ber 2014
 * Markmi�: 	Pop-up sem b��ur notanda upp� a� vista �kve�na ��ttar�� � dagatalinu s�nu. 
 */
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

	// Notkun: putOnCalPopUp = newInstance(titleToDisplay, showToSave)
	// Eftir:  putOnCalPopUp er pop-up me� titlinu titleToDisplay sem b��ur upp� 
	// 		   a� vista ��ttinn showToSave � dagatali
	public static PutOnCalPopUp newInstance(int titleToDisplay, Show showToSave) {
		PutOnCalPopUp frag = new PutOnCalPopUp();
		show = showToSave;
		title = titleToDisplay;
        return frag;
    }

	// Notkun: dialog = onCreateDialog(savedInstanceState)
	// Eftir:  dialog er pop-up sem b��ur upp� a� vista ��tt � dagatali
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    	return new AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.app_icon)
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
    
	// Notkun: addToCal(show)
	// Eftir:  show hefur veri� b�tt � dagatali�
    public void addToCal(Show show){
    	DbUtils dbHelper = new DbUtils(getActivity());
		dbHelper.putShowOnCal(show);
		MainActivity.cache.remove("calendarEpisodes");
		Log.v("cache", "Calendar episodes removed from cache");
    }
}
