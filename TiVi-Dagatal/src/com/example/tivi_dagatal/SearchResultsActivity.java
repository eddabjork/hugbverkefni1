/** Nafn:		Steinunn Friðgeirsdóttir
 * Dagsetning:	3. október 2014
 * Markmið:		Klasinn tekur við leitarorði og birtir leitarniðurstöður
 * 				frá client. Niðurstöðurnar eru settar í view sem er
 * 				búið til jafn óðum.
 **/
package com.example.tivi_dagatal;

import java.util.List;

import Clients.TraktClient;
import Data.DbUtils;
import Dtos.Show;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
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
	}

	//Notkun: SearchStuff(view)
	//Eftirskilyrði: Búið er að búa til nýtt view sem inniheldur
	//				 leitarniðurstöður frá því orði sem var í leitarboxi
	//				 ásamt tökkum til að bæta birtum þáttaröðum á lista.
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
		    button.setText("Bï¿½ta ï¿½ ï¿½ï¿½ttirnir mï¿½nir");
		    button.setLayoutParams(lparams);
	        button.setOnClickListener(new View.OnClickListener() {
	        	//Notkun: dbHelper.saveShow(show)
	        	//Eftirskilyrði: show hefur verið bætt í gagnasafn
	            public void onClick(View view) {
	            	dbHelper.saveShow(show);
	            }
	        });
	        //Bæta titli og takka í linearlayout
		    llv.addView(textView);
		    llv.addView(button);
		}
		//Bæta linearlayoutinu í scrollview
		sv.addView(llv);
	    //Birta nýja viewið
	    setContentView(sv);		
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
	//Eftirskilyrði: Leitaractivity hefur verið startað.
	public void onSearch(View view){
    	Intent intent = new Intent(this, SearchResultsActivity.class);
        startActivity(intent);
    }
	
	//Notkun: onSearch(view)
	//Eftirskilyrði: Heimaskjánum hefur verið startað.
	public void onHome(View view){
    	Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
