/**
     * Nafn: 	   Edda BjÃƒÂ¶rk KonrÃƒÂ¡ÃƒÂ°sdÃƒÂ³ttir
     * Dagsetning: 30. oktÃƒÂ³ber 2014
     * MarkmiÃƒÂ°:   NÃƒÂ¡ ÃƒÂ­ upplÃƒÂ½singar um ÃƒÂ¾ÃƒÂ¡ttarÃƒÂ¶ÃƒÂ° og sÃƒÂ½na ÃƒÅ¾ÃƒÂ¦ttirnir mÃƒÂ­nir lista
     * 			   meÃƒÂ° upplÃƒÂ½singum
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
	
	public ShowInfoTask(Integer id, Fragment frag, Activity activity, List<String> open){
		this.id = id;
		this.frag = frag;
		this.myActivity = activity;
		this.open = open;
	}
	
	// Notkun: onPreExecute()
	// Eftir:  progressDialog hefur verið stillt sem birtist á meðan notandi bíður
	protected void onPreExecute() {  
		progressDialog = LayoutUtils.showProgressDialog(R.string.show_process_title, 
				R.string.show_process_msg, myActivity);	
    }  
	
	//Notkun:		 show = doInBackground(shows)
	//EftirskilyrÃƒÂ°i: show er ÃƒÂ¾ÃƒÂ¡tturinn sem inniheldur upplÃƒÂ½singar
	//				 sem nÃƒÂ¡ÃƒÂ° er ÃƒÂ­ ÃƒÂºtfrÃƒÂ¡ shows
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
	//EftirskilyrÃƒÂ°i: BÃƒÂºiÃƒÂ° er aÃƒÂ° sÃƒÂ¦kja upplÃƒÂ½singar um ÃƒÂ¾ÃƒÂ¡ttinn show
	//				 og sÃƒÂ½na ÃƒÂ­ ÃƒÅ¾ÃƒÂ¦ttirnir mÃƒÂ­nir lista.
	protected void onPostExecute(Show show) {
		LayoutUtils.setUpInfoLayout(show, open, myActivity, id, true);
        progressDialog.dismiss();
	}
}
