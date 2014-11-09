/**
 * Nafn: 		Steinunn Fri�geirsd�ttir
 * Dagsetning: 	30. okt�ber 2014
 * Markmi�: 	FragmentEpisopde er fragment sem birtir uppl�singar
 * 				um einstaka ��tti.
 */
package com.example.tivi_dagatal_fragment;

import Clients.IMDbClient;
import Dtos.Episode;
import Dtos.Show;
import android.app.ActionBar.LayoutParams;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class FragmentEpisode extends Fragment{
	private ScrollView scrollView;
	private Episode episode;
	private ProgressDialog progressDialog;
	
	@Override
	//Eftir: B�i� a� birta uppl�singar um einstaka ��tt
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_episode, container, false);
		scrollView = new ScrollView(getActivity());
		new ShowEpisodeTask().execute(episode);
		//view = scrollView;
		Log.v("er a� b�a til ", "episode");
        return view;
	}
	
	
	public void setEpisode(Episode episode){
		this.episode = episode;
		Log.v("Var a� s�kja episode", episode.getTitle());
	}	
	
	/**
     * Nafn: 		Steinunn Fri�geirsd�ttir
     * Dagsetning: 	6. november 2014
     * Markmi�: 	?????????????????????????????????????????????????????????????????????????????????????
     */   
	private class ShowEpisodeTask extends AsyncTask<Object, Integer, Episode> {
		//Notkun: doInBackground(queries)
		//Eftir:  ���avinnslu � bakgrunni er loki�
		//        ????????????????????????????????????????????????????????????????????????????????????????
		protected Episode doInBackground(Object... objects) {         
			IMDbClient imdb = new IMDbClient();
			Episode episode = (Episode)objects[0];
			return episode;
		}
		
		// Notkun: onPreExecute()
		// Eftir:  progressDialog hefur veri� stillt sem � a� s�na � me�an notandi er a� b��a
		protected void onPreExecute() {  
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle(getResources().getString(R.string.process_title_popular));  
            progressDialog.setMessage(getResources().getString(R.string.process_msg_popular)); 
            progressDialog.setCancelable(false);  
            progressDialog.setIndeterminate(false);  
            progressDialog.show();  
        }  
		
		//Engin virkni
		protected void onProgressUpdate(Integer... progress) {
			//setProgressPercent(progress[0]);
		}
		
		//Notkun: onPostExecute(searchShows)
		//Eftir:  ????????????????????????????????????????????????????????????????
		protected void onPostExecute(Episode episode) {
			progressDialog.dismiss();
			/*LayoutParams lparams = new LayoutParams(LayoutParams.MATCH_PARENT,
			LayoutParams.MATCH_PARENT);
			LinearLayout llv = new LinearLayout(getActivity());
			llv.setOrientation(LinearLayout.VERTICAL);
			TextView textView = new TextView(getActivity());
			textView.setText("Titill ��ttarins er " + episode.getTitle());
			llv.addView(textView);
			//B�ta linearlayoutinu � scrollview
			scrollView.addView(llv);*/
			//Birta n�ja viewi�
			//setContentView(sv);
			
			//TODO: Bretya �llu � xml strengi
			LayoutParams lparams = new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
			TextView title = (TextView) getView().findViewById(R.id.title);
			title.setText("Titill ��ttar: " + episode.getTitle());
			
			TextView rating = (TextView) getView().findViewById(R.id.rating);
			rating.setText("Einkunn: " + episode.getImdbId()); //TODO: TEMP
			
			TextView season = (TextView) getView().findViewById(R.id.season);
			season.setText("Ser�a: " + episode.getSeason()); 
			
			TextView number = (TextView) getView().findViewById(R.id.number);
			number.setText("��ttur: " + episode.getNumber());
			
			TextView firstAired = (TextView) getView().findViewById(R.id.firstAired);
			firstAired.setText("Fyrst s�ndur: " + episode.getFirstAired()); //TODO: setja upp � e�lilegt form
			
			TextView overview = (TextView) getView().findViewById(R.id.overview);
			overview.setText("Um s�gu�r�� ��ttar: " + episode.getOverview()); 
		}
		
		public TextView addTextView(String text){
			TextView textView = new TextView(getActivity());
			textView.setText(text);
			return textView;
		}
	}
	
	// Notkun: showDialog(show)
	// Eftir:  pop-up hefur veri� birt sem b��ur upp� a� vista show � dagatali 
	void showDialog(Show show) {
	    DialogFragment newFragment = PutOnCalPopUp.newInstance(R.string.popup_put_cal, show);
	    newFragment.show(getFragmentManager(), "dialog");
	}
}	

