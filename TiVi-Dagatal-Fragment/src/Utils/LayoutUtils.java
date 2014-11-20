/**
 * Nafn: 		Kristin Fjola Tomasdottir
 * Dagsetning: 	13.oktober 2014
 * Markmid: 	Klasinn geymir hjalparfoll tengd utliti sem haegt er ad nota
 * 				i odrum klosum i forritinu.
 */
package Utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Data.DbUtils;
import Dtos.Season;
import Dtos.Show;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tivi_dagatal_fragment.Animator;
import com.example.tivi_dagatal_fragment.FragmentList;
import com.example.tivi_dagatal_fragment.FragmentRelated;
import com.example.tivi_dagatal_fragment.PopUpDelete;
import com.example.tivi_dagatal_fragment.PopUpPutOnCal;
import com.example.tivi_dagatal_fragment.R;

public class LayoutUtils {
	private static Integer id;
	private static Integer start_id_from;
	
	public static void setUpInfoLayout(Show show, FragmentRelated fragmentRelated, final List<String> open, 
										final Activity context, Integer startIdFrom, String noBannerUrl, boolean showSeasons) {
		start_id_from = startIdFrom;
		
		LinearLayout infoLayout = show.getInfoLayout();
		LinearLayout infoMain = show.getInfoMain();
		ScrollView scrollView = show.getScrollView();
		ImageButton _infoButton = show.getInfoButton();
		
		final FragmentList fraglist = new FragmentList();
		
		if(!open.contains(""+show.getInfoMain().getId())){
			infoLayout.removeAllViews();
			infoMain.removeAllViews();
			scrollView.removeAllViews();
			// if show.getDataTitle == null then there is no internet connection
			if(show.getDataTitle() != null) {
				//banner
				if(!show.getBanner().equals(noBannerUrl)) {
					ImageView banner = new ImageView(context);
					DisplayMetrics metrics = context.getResources().getDisplayMetrics();
					int densityDpi = metrics.densityDpi;
					if(densityDpi < 480) {
						banner.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
					} else {
						banner.setScaleType(ImageView.ScaleType.CENTER_CROP);
					}
					LinearLayout.LayoutParams bannerParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
					banner.setLayoutParams(bannerParams);
					String url = show.getBanner();
					fraglist.new DownloadImageTask(banner).execute(url);
					banner.buildDrawingCache();
					infoLayout.addView(banner);
				}
				
				//genres
				if(show.getGenres().size() != 0) {
					TextView genres = getTextView("genres", context, show);
					infoLayout.addView(genres);
				}
				
				//einkunn a imdb
				TextView grade = getTextView("imdb_grade", context, show);				
				infoLayout.addView(grade);
				
				//sjonvarpsstodvar
				if(show.getNetwork() != null && !show.getNetwork().equals("")) {
					TextView network = getTextView("network", context, show);
					infoLayout.addView(network);
				}					
				
				//a hvada degi thatturinn er syndur
				if(show.getAirDay() != null && !show.getAirDay().equals("")) {
					TextView airday = getTextView("air_day", context, show);
					infoLayout.addView(airday);
				
					//klukkan hvad syndur
					TextView airtime = getTextView("air_time", context, show);
					infoLayout.addView(airtime);
				}
				
				//soguthradur
				if(show.getOverview() != null && !show.getOverview().equals("")) {
					TextView overview = getTextView("overview", context, show);				
					infoLayout.addView(overview);
				}
				
				if(showSeasons){
					LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
					layout.setMargins(15, 15, 15, 0); //left, top, right, bottom
					TextView relatedButton = addRelatedButton(show, context, fragmentRelated, layout);
					infoLayout.addView(relatedButton);
					
					addSeasons(show, context, infoLayout);
				}
				
			} else {
				showNotConnectedMsg(context);
				showNoResult(infoLayout, context, false);
			}
			scrollView.addView(infoLayout);
			infoMain.addView(makeLine(context));
			infoMain.addView(scrollView);
		}
		
		startAnimator(context, infoLayout, infoMain, open, _infoButton);
	}
	
	public static void addSeasons(final Show show, Activity context, LinearLayout infoLayout){
		//seriur
		final FragmentList fraglist = new FragmentList();
		List<Season> seasons = show.getSeasons();
		Collections.reverse(seasons);
		for(final Season season : seasons) {
			TextView seasonbutton = new TextView(context);
			seasonbutton.setText(context.getResources().getString(R.string.serie) + " " + season.getSeasonNumber());
			seasonbutton.setGravity(Gravity.CENTER);
			seasonbutton.setTextSize(20);
			
			final ImageButton infoButton = new ImageButton(context);
			infoButton.setId(3);
			infoButton.setImageResource(R.drawable.down_arrow);
			infoButton.setBackgroundColor(Color.TRANSPARENT);
			int width = VariousUtils.getScreenWidth(context);
			int pd = (int) width/70;
			infoButton.setPadding(pd,pd,pd,pd);
			
			RelativeLayout seasonLayout = new RelativeLayout(context);
			RelativeLayout.LayoutParams titleParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			RelativeLayout.LayoutParams infoParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			titleParams.addRule(RelativeLayout.CENTER_IN_PARENT);
			titleParams.addRule(RelativeLayout.CENTER_VERTICAL);
			infoParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			infoParams.addRule(RelativeLayout.CENTER_VERTICAL);
			
			seasonLayout.addView(seasonbutton, titleParams);
			seasonLayout.addView(infoButton, infoParams);
			infoLayout.addView(seasonLayout);
			
			final LinearLayout episodes = new LinearLayout(context);
			episodes.setOrientation(LinearLayout.VERTICAL);
			episodes.setVisibility(View.GONE);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
			episodes.setLayoutParams(layoutParams);
			episodes.setGravity(Gravity.CENTER);
			episodes.setId(getNextId());
			infoLayout.addView(episodes);
			season.setEpisodesView(episodes);				
			
			View.OnClickListener serieButtonListener = new View.OnClickListener() {
				public void onClick(View view) {
					Map<Show, Season> map = new HashMap<Show, Season>();
					show.setInfoButton(infoButton);
					map.put(show, season);
					fraglist.new SeasonEpisodesTask().execute(map);
				}
			};
			infoButton.setOnClickListener(serieButtonListener);
			seasonbutton.setOnClickListener(serieButtonListener);
		}
		
	}
	
	
	public static TextView addRelatedButton(Show show, final Activity context, final FragmentRelated fragmentRelated, LayoutParams gradeLayout){
		final Show _show = show;
		
		TextView relatedShows = new TextView(context);
		relatedShows.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				fragmentRelated.setShow(_show);
				context.setTitle(context.getResources().getString(R.string.related_shows));
		        FragmentManager fragmentManager = context.getFragmentManager();
		        VariousUtils.addFragmentToStack(fragmentManager, fragmentRelated);
			}
		});
		String udata = context.getResources().getString(R.string.related_shows);
		SpannableString content = new SpannableString(udata);
		content.setSpan(new UnderlineSpan(), 0, udata.length(), 0);
		relatedShows.setText(content);
		relatedShows.setTextColor(context.getResources().getColor(R.color.app_red));
		relatedShows.setTypeface(null, Typeface.BOLD);
		relatedShows.setLayoutParams(gradeLayout);
		
		return relatedShows;
	}
	
	
	private static int getNextId() {
		id = (id == null) ? 0 : id+1;
		return start_id_from+id;
	}

	//Notkun: showNotConnectedMsg(context)
	//Eftir:  skilabod um ad notandi se ekki tengdur netinu hefur verid synt
	public static void showNotConnectedMsg(Context context){
		CharSequence text = context.getResources().getString(R.string.not_online);
		int duration = Toast.LENGTH_LONG;
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}
	
	// Notkun: showNoResult(view, context)
	// Eftir:  bÃºiÃ° er aÃ° birta texta Ã­ view sem segir notanda aÃ° engar niÃ°urstÃ¶Ã°ur fundust
	public static void showNoResult(ScrollView view, Activity context){
		TextView nothing = new TextView(context);
		nothing.setTextSize(20);
		nothing.setText(context.getResources().getString(R.string.nothing_found));
		view.addView(nothing);
	}
	
	// Notkun: showNoResult(layout, context)
	// Eftir:  bÃºiÃ° er aÃ° birta texta Ã­ layout sem segir notanda aÃ° engar niÃ°urstÃ¶Ã°ur fundust  
	public static void showNoResult(LinearLayout layout, Activity context, boolean big){
		TextView nothing = new TextView(context);
		if(big) nothing.setTextSize(20);
		else nothing.setTextSize(15);
		nothing.setText(context.getResources().getString(R.string.nothing_found));
		layout.addView(nothing);
	}
	
	//Notkun:		 line = makeLine();
  	//EftirskilyrÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â°i: line er nÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Âºna view hlutur sem er einfÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¶ld, ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¾unn, grÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¡ lÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â­na.
	public static View makeLine(Context context){
		 View v = new View(context);
		 
		 v.setLayoutParams(new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, 1, (float) 0.80));
		 v.setBackgroundColor(Color.rgb(203,203,203));
		 return v;
	}
	
	//Notkun: progressDialog = showProgressDialog(title, msg, context)
	//Eftir:  progressDialog hefur verid birt meï¿½ titlinum title og skilabodinu msg
	public static ProgressDialog showProgressDialog(Integer title, Integer msg, Activity context){
		ProgressDialog progressDialog = new ProgressDialog(context, R.style.ProgressDialog);
        progressDialog.setTitle(context.getResources().getString(title));  
        progressDialog.setMessage(context.getResources().getString(msg)); 
        progressDialog.setCancelable(false);  
        progressDialog.setIndeterminate(false);  
        progressDialog.show();  
        // change color of divider
        int dividerId = progressDialog.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
		View divider = progressDialog.findViewById(dividerId);
		divider.setBackgroundColor(context.getResources().getColor(R.color.app_red));
		return progressDialog;
	}
	
/******************Jóhanna start****************************/
	
	public static LinearLayout getRegListLayout (List<Show> searchShows, Activity context, DbUtils dbHelper) {
		int width = VariousUtils.getScreenWidth(context);
		int pd = (int) width/32;
		
		LinearLayout llv = new LinearLayout(context);
		llv.setOrientation(LinearLayout.VERTICAL);
		if(searchShows.isEmpty()){
			LayoutUtils.showNoResult(llv, context, true);
		}
		else{
			for (final Show show : searchShows){
				TextView title = getTitle(show, context);
				title.setPadding(pd,0,0,0);
				
				ImageButton addButton = getAddButton(show, context, dbHelper);
				addButton.setPadding(pd,pd,pd,pd);
				
				ImageButton infoButton = getInfoButton(show, context);
				infoButton.setPadding(pd,pd,pd,pd);
				
				RelativeLayout episodeLayout = getRegEpisodeLayout(title, addButton, infoButton, context);
				
				llv.addView(episodeLayout);
				llv.addView(makeLine(context));
			}
		}
		return llv;
	}
	
	public static TextView getTitle(Show show, Activity context){
		TextView title = new TextView(context);
		title.setText(show.getTitle());
		title.setId(R.id.title);
		return title;
	}
	
	public static ImageButton getAddButton(final Show show, final Activity context, final DbUtils dbHelper){
		final ImageButton addButton = new ImageButton(context);
		// 0 -> onList=false; 1 -> onList=true
		addButton.setTag(0);
		addButton.setImageResource(R.drawable.off_list);
		
		addButton.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent me) {
				if (me.getAction() == MotionEvent.ACTION_DOWN) {
					addButton.setColorFilter(Color.argb(150, 155, 155, 155));
					return true;
				} else if (me.getAction() == MotionEvent.ACTION_UP) {
					addButton.setColorFilter(Color.argb(0, 155, 155, 155));
					int status =(Integer) view.getTag();
	            	if(status == 0) {
	            		dbHelper.saveShow(show);
	            		view.setTag(1);
						addButton.setImageResource(R.drawable.on_list);
	            		showDialogCal(show, context);
	            	} else {
	            		dbHelper.deleteShow(show);
	            		view.setTag(0);
						addButton.setImageResource(R.drawable.off_list);
	            	}
					return true;
				}
				return false;
			}
		});
		addButton.setBackgroundColor(Color.TRANSPARENT);
		addButton.setId(R.id.addButton);
		return addButton;
	}
	
	// Notkun: showDialogCal(show)
	// Eftir:  pop-up hefur veri� birt sem b��ur upp� a� vista show � dagatali 
	public static void showDialogCal(Show show, Activity context) {
		DialogFragment newFragment = PopUpPutOnCal.newInstance(show);
	    newFragment.show(context.getFragmentManager(), "dialog");
	}
	
	public static RelativeLayout getRegEpisodeLayout(TextView title, ImageButton addButton, ImageButton infoButton, Activity context){
		RelativeLayout episodeLayout = new RelativeLayout(context);
		
		RelativeLayout.LayoutParams titleParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		RelativeLayout.LayoutParams addParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		RelativeLayout.LayoutParams infoParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

		titleParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		titleParams.addRule(RelativeLayout.CENTER_VERTICAL);
		infoParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		infoParams.addRule(RelativeLayout.CENTER_VERTICAL);
		addParams.addRule(RelativeLayout.LEFT_OF, R.id.infoButton);
		addParams.addRule(RelativeLayout.CENTER_VERTICAL);
		
		episodeLayout.addView(title, titleParams);
		episodeLayout.addView(addButton, addParams);
		episodeLayout.addView(infoButton, infoParams);
		
		return episodeLayout;
	}
	
	public static LinearLayout getMyEpsListLayout(List<Show> searchShows, Activity context, DbUtils dbHelper) {
		int width = VariousUtils.getScreenWidth(context);
		int pd = (int) width/32;
		
		LinearLayout llv = new LinearLayout(context);
		llv.setOrientation(LinearLayout.VERTICAL);

		for (final Show show : searchShows){
			TextView title = getTitle(show, context);
			title.setPadding(pd,0,0,0);
				
			ImageButton calendarButton = getCalButton(show, context);
			calendarButton.setPadding(pd,pd,pd,pd);
			
			ImageButton deleteButton = getDeleteButton(show, context);
			deleteButton.setPadding(pd,pd,pd,pd);
			
			ImageButton infoButton = getInfoButton(show, context);
			infoButton.setPadding(pd,pd,pd,pd);
				
			RelativeLayout episodeLayout = getMyEpsEpisodeLayout(title, calendarButton, deleteButton, infoButton, context);
			
			llv.addView(episodeLayout);
			llv.addView(makeLine(context));
		}
		//TODO: vantar helling
		return llv;
	}
	
	public static RelativeLayout getMyEpsEpisodeLayout(TextView title, ImageButton calendarButton, ImageButton deleteButton, ImageButton infoButton, Activity context){
		RelativeLayout episodeLayout = new RelativeLayout(context);
		
		RelativeLayout.LayoutParams titleParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		RelativeLayout.LayoutParams calParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		RelativeLayout.LayoutParams delParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		RelativeLayout.LayoutParams infoParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		
		titleParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		titleParams.addRule(RelativeLayout.CENTER_VERTICAL);
		calParams.addRule(RelativeLayout.LEFT_OF, R.id.deleteButton);
		calParams.addRule(RelativeLayout.CENTER_VERTICAL);
		delParams.addRule(RelativeLayout.LEFT_OF, R.id.infoButton);
		delParams.addRule(RelativeLayout.CENTER_VERTICAL);
		infoParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		infoParams.addRule(RelativeLayout.CENTER_VERTICAL);
		
		episodeLayout.addView(title, titleParams);
		episodeLayout.addView(calendarButton, calParams);
		episodeLayout.addView(deleteButton, delParams);
		episodeLayout.addView(infoButton, infoParams);
		
		return episodeLayout;
	}
	
	public static ImageButton getCalButton(final Show show, final Activity context){
		final ImageButton calendarButton = new ImageButton(context);
		DbUtils dbHelper = new DbUtils(context);
		// 0 -> onCal=false; 1 -> onCal=true
		boolean onCal = dbHelper.isOnCal(show);
		if(onCal) {
			calendarButton.setImageResource(R.drawable.on_cal);
			calendarButton.setTag(1);
		}
		else {
			calendarButton.setImageResource(R.drawable.off_cal);
			calendarButton.setTag(0);
		}
		calendarButton.setBackgroundColor(Color.TRANSPARENT);
		calendarButton.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent me) {
				if (me.getAction() == MotionEvent.ACTION_DOWN) {
					calendarButton.setColorFilter(Color.argb(150, 155, 155, 155));
					return true;
				} else if (me.getAction() == MotionEvent.ACTION_UP) {
					calendarButton.setColorFilter(Color.argb(0, 155, 155, 155));
					final int status =(Integer) view.getTag();
					if(status == 1) {
						removeFromCal(show, context);
						view.setTag(0);
						calendarButton.setImageResource(R.drawable.off_cal);
					}
					else {
						addToCal(show, context);
						view.setTag(1);
						calendarButton.setImageResource(R.drawable.on_cal);
					}
					return true;
				}
				return false;
			}
		});
		calendarButton.setId(R.id.calButton);
		return calendarButton;
	}
	
	//Notkun:		 addToCal(show);
	public static void addToCal(Show show, Activity context){
		DbUtils dbHelper = new DbUtils(context);
		dbHelper.putShowOnCal(show);
		VariousUtils.flushCache("calendarEpisodes");
	}
	
	//Notkun:		 removeFromCal(show);
	public static void removeFromCal(Show show, Activity context){
		DbUtils dbHelper = new DbUtils(context);
		dbHelper.takeShowOffCal(show);
		VariousUtils.flushCache("calendarEpisodes");
	}
	
	public static ImageButton getDeleteButton(final Show show, final Activity context){
		ImageButton deleteButton = new ImageButton(context);
		deleteButton.setImageResource(R.drawable.delete);
		deleteButton.setBackgroundColor(Color.TRANSPARENT);
		deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	showDialogDel(show, context);
            }
        });
		deleteButton.setId(R.id.deleteButton);
		return deleteButton;
	}
	
	// Notkun: showDialogDel(show)
	// Eftir:  pop-up hefur verid birt sem spyr hvort notandi vilji eyda thaetti
	public static void showDialogDel(Show show, Activity context) {
	    DialogFragment newFragment = PopUpDelete.newInstance(show);
	    newFragment.show(context.getFragmentManager(), "dialog");
	}
	
	public static ImageButton getInfoButton(final Show show, Activity context){
		ImageButton infoButton = new ImageButton(context);
		infoButton.setImageResource(R.drawable.down_arrow);
		infoButton.setBackgroundColor(Color.TRANSPARENT);
		infoButton.setId(R.id.infoButton);
		return infoButton;
		//TODO: vantar helling
	}

	/******************Jóhanna end****************************/
	
	public static TextView getTextView(String type, Activity context, Show show) {
		LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		layout.setMargins(15, 15, 15, 0); //left, top, right, bottom
		
		TextView txtView = new TextView(context);
		txtView.setLayoutParams(layout);
		
		if(type == "genres") {
			String genre = TextUtils.join(", ",show.getGenres().toArray());
			txtView.setText(context.getResources().getString(R.string.genres)+" "+genre);
		} else if(type == "imdb_grade") {
			Map<Show, TextView> map = new HashMap<Show, TextView>();
			map.put(show, txtView);
			FragmentList fraglist = new FragmentList();
			fraglist.new IMDbRatingTask().execute(map);
			txtView.setText(context.getResources().getString(R.string.imdb_grade));
		} else if(type == "network") {
			txtView.setText(context.getResources().getString(R.string.network)+ " " + show.getNetwork());
		} else if(type == "air_day") {
			String airDay = VariousUtils.translateWeekday(show.getAirDay(), context);
			txtView.setText((context.getResources().getString(R.string.airday))+" "+airDay);
		} else if(type == "air_time") {
			String airTime = VariousUtils.parseAirTime(show.getAirTime());
			txtView.setText((context.getResources().getString(R.string.airtime))+" "+ airTime);
		} else if(type == "overview") {
			LinearLayout.LayoutParams overviewLayout = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			overviewLayout.setMargins(15, 15, 15, 0); //left, top, right, bottom
			txtView.setLayoutParams(overviewLayout);
			txtView.setText(context.getResources().getString(R.string.overview)+"\n"+show.getOverview());
		}
		
		return txtView;
	}
	
	public static void startAnimator(Activity context, LinearLayout infoLayout, LinearLayout infoMain, 
									List<String> open, ImageButton infoButton) {
		Animator.setHeightForWrapContent(context, infoLayout);
		Animator animation = null;
        if(open.contains(""+infoMain.getId())) {
            animation = new Animator(infoMain, 500, 1);
            open.remove(""+infoMain.getId());
            infoButton.setImageResource(R.drawable.down_arrow);
        } else {
            animation = new Animator(infoMain, 500, 0);
            open.add(""+infoMain.getId());
            infoButton.setImageResource(R.drawable.up_arrow);
        }
        infoMain.startAnimation(animation);
	}
}
