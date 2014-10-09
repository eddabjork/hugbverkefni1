package com.example.tivi_dagatal;

import java.util.List;

import Clients.TraktClient;
import Dtos.Show;
import android.app.ActionBar.LayoutParams;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class SearchResultsActivity extends ActionBarActivity {

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
    	LinearLayout ll = new LinearLayout(this);
    	ll.setOrientation(LinearLayout.VERTICAL);
    	for (Show show : searchShows){
        	TextView textView = new TextView(this);
    	    textView.setText(show.getTitle());
    	    textView.setId(0);
    	    textView.setLayoutParams(lparams);
    	    ll.addView(textView);
    	}
    	//Add the linearLayout view to the scroll view
    	sv.addView(ll);
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
		return super.onOptionsItemSelected(item);
	}
	
	/*public void onSearch(View view){
    	Intent intent = new Intent(this, SearchResultsActivity.class);
        startActivity(intent);
    }*/

}
