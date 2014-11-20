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
import Threads.DownloadImageTask;
import Threads.IMDbRatingTask;
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
	
	//Notkun: setUpInfoLayout(show,open,context,startIdFrom,noBannerUrl,showSeasons)
	//Eftirskilyrði: Búið er að setja upp útlit fyrir upplýsingar um þáttaraðir fyrir
	// 				 þáttinn show.
	public static void setUpInfoLayout(Show show, final List<String> open, final Activity context,
									Integer startIdFrom, String noBannerUrl, boolean showSeasons) {
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
			if(show.getDataTitle() == null) {		// no internet connection
				showNotConnectedMsg(context);
				showNoResult(infoLayout, context, false);
			} else if (show.getDataTitle().equals("not_found")) {	// no results from web client
				showNoResult(infoLayout, context, false);
			} else {
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
					new DownloadImageTask(banner, context).execute(url);
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
					int width = VariousUtils.getScreenWidth(context);
					int pd = width/72;
					LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
					layout.setMargins(pd, pd, pd, 0); //left, top, right, bottom
					TextView relatedButton = addRelatedButton(show, context, layout);
					infoLayout.addView(relatedButton);
					
					addSeasons(show, context, infoLayout);
				}
			}
			scrollView.addView(infoLayout);
			infoMain.addView(makeLine(context));
			infoMain.addView(scrollView);
		}
		
		startAnimator(context, infoLayout, infoMain, open, _infoButton);
	}
	
	//Notkun: addSeason(show, context, infoLayout)
	//Eftir:  b�i� er a� b�ta season t�kkum vi� info layout.
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
	
	//Notkun: text = addRelatedButton(show, context, gradeLayout)
	//Eftir:  Text er n�na clickable takki sem opnar Svipar�ir ��ttir myndina. 
	public static TextView addRelatedButton(final Show show, final Activity context, LayoutParams gradeLayout){
		
		TextView relatedShows = new TextView(context);
		relatedShows.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				FragmentRelated fragmentRelated = new FragmentRelated();
				fragmentRelated.setShow(show);
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
	
	//Notkun: id = getNextId()
	//Eftirskilyrði: id er næsta lausa auðkenni
	private static int getNextId() {
		id = (id == null) ? 0 : id+1;
		start_id_from = (start_id_from == null) ? 0 : start_id_from;
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
	// Eftir:  bÃƒÂºiÃƒÂ° er aÃƒÂ° birta texta ÃƒÂ­ view sem segir notanda aÃƒÂ° engar niÃƒÂ°urstÃƒÂ¶ÃƒÂ°ur fundust
	public static void showNoResult(ScrollView view, Activity context){
		TextView nothing = new TextView(context);
		nothing.setTextSize(20);
		nothing.setText(context.getResources().getString(R.string.nothing_found));
		view.addView(nothing);
	}
	
	// Notkun: showNoResult(layout, context)
	// Eftir:  bÃƒÂºiÃƒÂ° er aÃƒÂ° birta texta ÃƒÂ­ layout sem segir notanda aÃƒÂ° engar niÃƒÂ°urstÃƒÂ¶ÃƒÂ°ur fundust  
	public static void showNoResult(LinearLayout layout, Activity context, boolean big){
		TextView nothing = new TextView(context);
		if(big) nothing.setTextSize(20);
		else nothing.setTextSize(15);
		nothing.setText(context.getResources().getString(R.string.nothing_found));
		layout.addView(nothing);
	}
	
	//Notkun:		 line = makeLine();
  	//EftirskilyrÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â°i: line er nÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Âºna view hlutur sem er einfÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¶ld, ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¾unn, grÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ lÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â­na.
	public static View makeLine(Context context){
		 View v = new View(context);
		 
		 v.setLayoutParams(new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, 1, (float) 0.80));
		 v.setBackgroundColor(Color.rgb(203,203,203));
		 return v;
	}
	
	//Notkun: progressDialog = showProgressDialog(title, msg, context)
	//Eftir:  progressDialog hefur verid birt meÃ¯Â¿Â½ titlinum title og skilabodinu msg
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
	
	//Notkun: textView = getTextView(type,context,show)
	//Eftirskilyrði: textView er TextView fyrir ,,hlutinn'' type fyrir þáttinn
	//				 show
	public static TextView getTextView(String type, Activity context, Show show) {
		int width = VariousUtils.getScreenWidth(context);
		int pd = width/72;
		LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		layout.setMargins(pd, pd, pd, 0); //left, top, right, bottom
		TextView txtView = new TextView(context);
		txtView.setLayoutParams(layout);
		
		if(type == "genres") {
			String genre = TextUtils.join(", ",show.getGenres().toArray());
			txtView.setText(context.getResources().getString(R.string.genres)+" "+genre);
		} else if(type == "imdb_grade") {
			txtView.setText(context.getResources().getString(R.string.imdb_grade));
			new IMDbRatingTask(txtView, context).execute(show);
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
	
	//Notkun: startAnimator(context,infoLayout,infoMain,open,infoButton)
	//Eftirskilyrði: Búið er að ,,animate''-a infoMain
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
	
/******************TODO: Jóhanna start****************************/
	
	public static LinearLayout getRegListLayout (List<Show> searchShows, Activity context, DbUtils dbHelper) {
		int width = VariousUtils.getScreenWidth(context);
		int pd = (int) width/32;
		
		LinearLayout mainLayout = new LinearLayout(context);
		mainLayout.setOrientation(LinearLayout.VERTICAL);
		if(searchShows.isEmpty()){
			LayoutUtils.showNoResult(mainLayout, context, true);
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
				
				mainLayout.addView(episodeLayout);
				mainLayout.addView(makeLine(context));
			}
		}
		return mainLayout;
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
	
	public static LinearLayout getMyEpsListLayout(List<Show> showList, Activity context, DbUtils dbHelper) {
		int width = VariousUtils.getScreenWidth(context);
		int pd = (int) width/32;
		
		LinearLayout mainLayout = new LinearLayout(context);
		mainLayout.setOrientation(LinearLayout.VERTICAL);

		for (final Show show : showList){
			TextView title = getTitle(show, context);
			title.setPadding(pd,0,0,0);
				
			ImageButton calendarButton = getCalButton(show, context);
			calendarButton.setPadding(pd,pd,pd,pd);
			
			ImageButton deleteButton = getDeleteButton(show, context);
			deleteButton.setPadding(pd,pd,pd,pd);
			
			ImageButton infoButton = getInfoButton(show, context);
			infoButton.setPadding(pd,pd,pd,pd);
			
			RelativeLayout episodeLayout = getMyEpsEpisodeLayout(title, calendarButton, deleteButton, infoButton, context);
			LinearLayout infoMain = getInfoMainLayout(show, context, infoButton);
			
			mainLayout.addView(episodeLayout);
			mainLayout.addView(infoMain);
			mainLayout.addView(makeLine(context));
		}
		//TODO: lala hér er kallað á getInfoMainLayout
		return mainLayout;
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
		final ImageButton infoButton = new ImageButton(context);
		infoButton.setImageResource(R.drawable.down_arrow);
		infoButton.setBackgroundColor(Color.TRANSPARENT);
		infoButton.setId(R.id.infoButton);
		return infoButton;
	}
	
	public static LinearLayout getInfoMainLayout(final Show show, Activity context, final ImageButton infoButton){
		//TODO: þarf að fylgjast með hvar þú ert
		final FragmentList fraglist = new FragmentList();
		
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

		final ScrollView scrollView = new ScrollView(context);

		final LinearLayout infoLayout = new LinearLayout(context);
		infoLayout.setOrientation(LinearLayout.VERTICAL);
		infoLayout.setLayoutParams(layoutParams);
		infoLayout.setGravity(Gravity.CENTER);

		final LinearLayout infoMain = new LinearLayout(context);
		infoMain.setOrientation(LinearLayout.VERTICAL);
		infoMain.setLayoutParams(layoutParams);
		infoMain.setVisibility(View.GONE);
		infoMain.setId(getNextId());
		
		View.OnClickListener infoButtonListener = new View.OnClickListener() {
			@Override 
			public void onClick(View view) {
				show.setInfoLayout(infoLayout);
				show.setInfoMain(infoMain);
				show.setScrollView(scrollView);
				show.setInfoButton(infoButton);
				fraglist.new ShowInfoTask().execute(show);
			}
		};
		infoButton.setOnClickListener(infoButtonListener);
		return infoMain;
	}
	/******************Jóhanna end****************************/
}
