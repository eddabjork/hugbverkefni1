package com.example.tivi_dagatal;

import java.util.List;

import Clients.TraktClient;
import Data.DbUtils;
import Dtos.Show;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
		//handleIntent(getIntent());
		//SearchStuff("Friends");		
	}

	public void SearchStuff(View view){

				EditText wordText = (EditText) findViewById(R.id.leitarbox);
				String word = wordText.getText().toString();
				
				TraktClient search = new TraktClient();
		    	List<Show> searchShows = search.searchShow(word);
		    	LayoutParams lparams = new LayoutParams(LayoutParams.MATCH_PARENT,
		                LayoutParams.MATCH_PARENT);
            	ScrollView sv = new ScrollView(this);
		    	LinearLayout llv = new LinearLayout(this);
		    	llv.setOrientation(LinearLayout.VERTICAL);
		    	for (final Show show : searchShows){
		        	TextView textView = new TextView(this);
		    	    textView.setText(show.getTitle());
		    	    textView.setTextSize(30);
		    	    textView.setLayoutParams(lparams);
		    	    Button button = new Button(this);
		    	    button.setText("B�ta � ��ttirnir m�nir");
		    	    button.setLayoutParams(lparams);
		            button.setOnClickListener(new View.OnClickListener() {
		                public void onClick(View view) {
		                	dbHelper.saveShow(show);
		                }
		            });
		    	    llv.addView(textView);
		    	    llv.addView(button);
		    	}
		    	//Add the linearLayout view to the scroll view
		    	sv.addView(llv);
			    //Show the new view
			    setContentView(sv);		
	}
	
	
    /*private void handleIntent(Intent intent) {
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            // handles a click on a search suggestion; launches activity to show word
            Intent wordIntent = new Intent(this, SearchResultsActivity.class);
            wordIntent.setData(intent.getData());
            startActivity(wordIntent);
        } else if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            // handles a search query
            String query = intent.getStringExtra(SearchManager.QUERY);
            //SearchStuff(query);
        }
    }*/
	
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
		if (id == R.id.leita){
			SearchStuff(x);
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
	
	public void onSearch(View view){
    	Intent intent = new Intent(this, SearchResultsActivity.class);
        startActivity(intent);
    }
	
	public void onHome(View view){
    	Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
