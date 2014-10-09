package com.example.tivi_dagatal;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class MyEpisodesList extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_episodes_list);
		
		addShow(5);
	}
	
	public void addShow(int id) {
		String img_src = "bla";
		String name_show = "New Girl";
		
		//tengjast við þætti
		
		LinearLayout mainLayout = (LinearLayout)findViewById(R.id.episode_layout);
		
		LinearLayout ll_1 = new LinearLayout(this);
		ll_1.setOrientation(LinearLayout.HORIZONTAL);
		
		ImageView image = new ImageView(this);
		image.setImageResource(R.drawable.ic_launcher);
		
		LinearLayout ll_2 = new LinearLayout(this);
		ll_2.setOrientation(LinearLayout.VERTICAL);
		
		TextView title = new TextView(this);
		title.setText(name_show);
		
		LinearLayout ll_3 = new LinearLayout(this);
		ll_3.setOrientation(LinearLayout.HORIZONTAL);
		
		Button btn_cal = new Button(this);
		btn_cal.setText("Setja á dagatal");
		btn_cal.setTextSize(10);
		btn_cal.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 30));

		Button btn_info = new Button(this);
		btn_info.setText("Upplýsingar");
		btn_info.setTextSize(10);
		btn_info.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 30));

		
		Button btn_delete = new Button(this);
		btn_delete.setText("Eyða");
		btn_delete.setTextSize(10);
		btn_delete.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 30));

		
		ll_3.addView(btn_cal);
		ll_3.addView(btn_info);
		ll_3.addView(btn_delete);
		ll_2.addView(title);
		ll_2.addView(ll_3);
		ll_1.addView(image);
		ll_1.addView(ll_2);
		
		mainLayout.addView(ll_1);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_episodes_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_my_episodes_list,
					container, false);
			return rootView;
		}
	}
}
