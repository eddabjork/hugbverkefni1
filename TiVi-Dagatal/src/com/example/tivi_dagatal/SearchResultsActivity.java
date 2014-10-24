/** Nafn:		Steinunn Friï¿½geirsdï¿½ttir
 * Dagsetning:	3. oktï¿½ber 2014
 * Markmiï¿½:		Klasinn tekur viï¿½ leitarorï¿½i og birtir leitarniï¿½urstï¿½ï¿½ur
 * 				frï¿½ client. Niï¿½urstï¿½ï¿½urnar eru settar ï¿½ view sem er
 * 				bï¿½iï¿½ til jafn ï¿½ï¿½um.
 **/
package com.example.tivi_dagatal;

import java.util.List;

import Clients.TraktClient;
import Data.DbUtils;
import Dtos.Show;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class SearchResultsActivity extends ActionBarActivity {
	DbUtils dbHelper = new DbUtils(this);
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_search_results);
		
		setEnterKeyListener();
	}
	
	// veit ekki hvernig þetta er í síma, en mjög hægt í emulator :(
	public void setEnterKeyListener(){
		EditText editText = (EditText) findViewById(R.id.leitarbox);
		editText.setOnKeyListener(new OnKeyListener()
		{
		    public boolean onKey(View v, int keyCode, KeyEvent event)
		    {
		        if (event.getAction() == KeyEvent.ACTION_DOWN)
		        {
		            switch (keyCode)
		            {
		                case KeyEvent.KEYCODE_DPAD_CENTER:
		                case KeyEvent.KEYCODE_ENTER:
		                	SearchStuff(v);
		                    return true;
		                default:
		                    break;
		            }
		        }
		        return false;
		    }
		});
	}

	//Notkun: SearchStuff(view)
	//Eftirskilyrï¿½i: Bï¿½iï¿½ er aï¿½ bï¿½a til nï¿½tt view sem inniheldur
	//				 leitarniï¿½urstï¿½ï¿½ur frï¿½ ï¿½vï¿½ orï¿½i sem var ï¿½ leitarboxi
	//				 ï¿½samt tï¿½kkum til aï¿½ bï¿½ta birtum ï¿½ï¿½ttarï¿½ï¿½um ï¿½ lista.
	public void SearchStuff(View view){
		EditText wordText = (EditText) findViewById(R.id.leitarbox);
		String word = wordText.getText().toString();		
		
		// measure call time
		long startTime = System.nanoTime();
		
		new SearchShowsTask().execute(word);
		
 		long endTime = System.nanoTime();
 		long duration_milli = (endTime - startTime)/1000000;  //divide by 1000000 to get milliseconds.
 		Log.v("async call time:", duration_milli+"");
         
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search_results, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		View x = new View(this);
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		if (id == R.id.search){
			onSearch(x);
			return true;
		}
		if (id == R.id.action_home){
			onHome(x);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	//Notkun: onSearch(view)
	//Eftirskilyrï¿½i: Leitaractivity hefur veriï¿½ startaï¿½.
	public void onSearch(View view){
    	Intent intent = new Intent(this, SearchResultsActivity.class);
        startActivity(intent);
    }
	
	//Notkun: onSearch(view)
	//Eftirskilyrï¿½i: Heimaskjï¿½num hefur veriï¿½ startaï¿½.
	public void onHome(View view){
    	Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
	
	private class SearchShowsTask extends AsyncTask<String, Integer, List<Show>> {
		protected List<Show> doInBackground(String... queries) {         
			TraktClient search = new TraktClient();	    	 
			List<Show> searchShows = search.searchShow(queries[0]);  
			return searchShows;
		}
		
		protected void onProgressUpdate(Integer... progress) {
			//setProgressPercent(progress[0]);
		}
		
		protected void onPostExecute(List<Show> searchShows) {
			LayoutParams lparams = new LayoutParams(LayoutParams.MATCH_PARENT,
			LayoutParams.MATCH_PARENT);
			ScrollView sv = new ScrollView(SearchResultsActivity.this);
			LinearLayout llv = new LinearLayout(SearchResultsActivity.this);
			llv.setOrientation(LinearLayout.VERTICAL);
			for (final Show show : searchShows){
				TextView textView = new TextView(SearchResultsActivity.this);
				textView.setText(show.getTitle());
				textView.setTextSize(30);
				textView.setLayoutParams(lparams);
				Button button = new Button(SearchResultsActivity.this);
				button.setText(getResources().getString(R.string.search_add));
				button.setLayoutParams(lparams);
				button.setOnClickListener(new View.OnClickListener() {
					//Notkun: dbHelper.saveShow(show)
					//Eftirskilyrï¿½i: show hefur veriï¿½ bï¿½tt ï¿½ gagnasafn eÃ°a tekiÃ° Ãºr Ã¾vÃ­
					public void onClick(View view) {
		            	if(((Button)view).getText().toString() == getResources().getString(R.string.search_add)) {
		            		dbHelper.saveShow(show);
		            		((Button) view).setText(getResources().getString(R.string.take_off_list));
		            	} else {
		            		dbHelper.deleteShow(show);
		            		((Button) view).setText(getResources().getString(R.string.search_add));
		            	}
		            }
		        });
				//Bï¿½ta titli og takka ï¿½ linearlayout
				llv.addView(textView);
				llv.addView(button);
			}
			//Bï¿½ta linearlayoutinu ï¿½ scrollview
			sv.addView(llv);
			//Birta nï¿½ja viewiï¿½
			setContentView(sv);	
		}
	}

}
