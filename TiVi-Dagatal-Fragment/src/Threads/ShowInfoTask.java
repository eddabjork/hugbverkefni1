/**
     * Nafn: 	   Edda Bjork Konradsdottir
     * Dagsetning: 30. oktober 2014
     * Markmid:    Na i upplysingar um thattarod og syna thaettirnir minir lista
     * 			   med upplysingum
     * */

package Threads;

import java.util.ArrayList;
import java.util.List;

import Clients.TraktClient;
import Dtos.Show;
import Utils.LayoutUtils;
import Utils.VariousUtils;
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.example.tivi_dagatal_fragment.FragmentEpisode;
import com.example.tivi_dagatal_fragment.FragmentList;
import com.example.tivi_dagatal_fragment.R;

public class ShowInfoTask extends AsyncTask<Show, Integer, Show> {
	private List<String> open = new ArrayList<String>();
	private Integer id;
	private Fragment frag = new FragmentEpisode();
	private static Activity myActivity;
	private ProgressDialog progressDialog;
	private boolean isMyEpisodes;
	
	//Notkun: task = ShowInfoTask(id, frag, activity, open)
	//Eftir:  task er nytt ShowInfoTask
	public ShowInfoTask(Integer id, Fragment frag, Activity activity, List<String> open, boolean isMyEpisodes){
		this.id = id;
		this.frag = frag;
		this.myActivity = activity;
		this.open = open;
		this.isMyEpisodes = isMyEpisodes;
	}
	
	// Notkun: onPreExecute()
	// Eftir:  progressDialog hefur verid stillt sem birtist a medan notandi bidur
	protected void onPreExecute() {  
		progressDialog = LayoutUtils.showProgressDialog(R.string.show_process_title, 
				R.string.show_process_msg, myActivity);	
    }  
	
	//Notkun:		 show = doInBackground(shows)
	//Eftir:		 show er thatturinn sem inniheldur upplysingar
	//				 sem nad er i utfra shows
	protected Show doInBackground(Show... shows) {
		Show show = new Show();
		if(VariousUtils.isConnectedToInternet(myActivity)){
			TraktClient client = new TraktClient();
			if(!open.contains(""+shows[0].getInfoMain().getId())) show = client.getShowInfo(shows[0]);
		}
		show.setInfoLayout(shows[0].getInfoLayout());
		show.setInfoMain(shows[0].getInfoMain());
		show.setScrollView(shows[0].getScrollView());
		show.setInfoButton(shows[0].getInfoButton());
		return show;
		
	}
	
	//Notkun:		 onPostExecute(show)
	//Eftir: 		 Buid er ad saekja upplysingar um thattinn show
	//				 og syna a thaettirnir minir lista.
	protected void onPostExecute(Show show) {
		LayoutUtils.setUpInfoLayout(show, open, myActivity, id, isMyEpisodes);
        progressDialog.dismiss();
	}
}
