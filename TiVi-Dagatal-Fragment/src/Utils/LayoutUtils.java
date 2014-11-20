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

import Dtos.Season;
import Dtos.Show;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.app.FragmentManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

import com.example.tivi_dagatal_fragment.Animator;
import com.example.tivi_dagatal_fragment.FragmentList;
import com.example.tivi_dagatal_fragment.FragmentRelated;
import com.example.tivi_dagatal_fragment.R;
import com.example.tivi_dagatal_fragment.FragmentList.DownloadImageTask;
import com.example.tivi_dagatal_fragment.FragmentList.IMDbRatingTask;
import com.example.tivi_dagatal_fragment.FragmentList.SeasonEpisodesTask;

public class LayoutUtils {
	private static Integer id;
	private static Integer start_id_from;
	
	public static void setUpInfoLayout(Show show, FragmentRelated fragmentRelated, final List<String> open, 
										final Activity context, Integer startIdFrom, String noBannerUrl, boolean showSeasons) {
		final Show _show = show;
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
				
				LinearLayout.LayoutParams gradeLayout = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				gradeLayout.setMargins(15, 15, 15, 0); //left, top, right, bottom
				
				//genres
				if(show.getGenres().size() != 0) {
					TextView genres = new TextView(context);
					genres.setLayoutParams(gradeLayout);
					String genre = TextUtils.join(", ",show.getGenres().toArray());
					genres.setText(context.getResources().getString(R.string.genres)+" "+genre);
					infoLayout.addView(genres);
				}
				
				//einkunn a imdb
				TextView grade = new TextView(context);
				Map<Show, TextView> map = new HashMap<Show, TextView>();
				map.put(show, grade);
				fraglist.new IMDbRatingTask().execute(map);
				grade.setLayoutParams(gradeLayout);
				grade.setText(context.getResources().getString(R.string.imdb_grade));
				
				infoLayout.addView(grade);
				
				//sjonvarpsstodvar
				if(show.getNetwork() != null && !show.getNetwork().equals("")) {
					TextView network = new TextView(context);
					network.setLayoutParams(gradeLayout);
					network.setText(context.getResources().getString(R.string.network)+ " " + show.getNetwork());
					infoLayout.addView(network);
				}					
				
				//a hvada degi thatturinn er syndur
				if(show.getAirDay() != null && !show.getAirDay().equals("")) {
					String airDay = VariousUtils.translateWeekday(show.getAirDay(), context);
					TextView airday = new TextView(context);
					airday.setLayoutParams(gradeLayout);
					airday.setText((context.getResources().getString(R.string.airday))+" "+airDay);
					infoLayout.addView(airday);
				
					//klukkan hvad syndur
					String airTime = VariousUtils.parseAirTime(show.getAirTime());
					TextView airtime = new TextView(context);
					airtime.setLayoutParams(gradeLayout);
					airtime.setText((context.getResources().getString(R.string.airtime))+" "+ airTime);
					
					infoLayout.addView(airtime);
				}
				
				//soguthradur
				if(show.getOverview() != null && !show.getOverview().equals("")) {
					TextView overview = new TextView(context);
					LinearLayout.LayoutParams overviewLayout = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
					overviewLayout.setMargins(15, 15, 15, 0); //left, top, right, bottom
					overview.setLayoutParams(overviewLayout);
					overview.setText(context.getResources().getString(R.string.overview)+"\n"+show.getOverview());					
					infoLayout.addView(overview);
				}
				
				if(showSeasons){
					TextView relatedButton = addRelatedButton(show, context, fragmentRelated, gradeLayout);
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
		
		Animator.setHeightForWrapContent(context, infoLayout);
		Animator animation = null;
        if(open.contains(""+infoMain.getId())) {
            animation = new Animator(infoMain, 500, 1);
            open.remove(""+infoMain.getId());
            _infoButton.setImageResource(R.drawable.down_arrow);
        } else {
            animation = new Animator(infoMain, 500, 0);
            open.add(""+infoMain.getId());
            _infoButton.setImageResource(R.drawable.up_arrow);
        }
        infoMain.startAnimation(animation);
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
	// Eftir:  búið er að birta texta í view sem segir notanda að engar niðurstöður fundust
	public static void showNoResult(ScrollView view, Activity context){
		TextView nothing = new TextView(context);
		nothing.setTextSize(20);
		nothing.setText(context.getResources().getString(R.string.nothing_found));
		view.addView(nothing);
	}
	
	// Notkun: showNoResult(layout, context)
	// Eftir:  búið er að birta texta í layout sem segir notanda að engar niðurstöður fundust  
	public static void showNoResult(LinearLayout layout, Activity context, boolean big){
		TextView nothing = new TextView(context);
		if(big) nothing.setTextSize(20);
		else nothing.setTextSize(15);
		nothing.setText(context.getResources().getString(R.string.nothing_found));
		layout.addView(nothing);
	}
	
	//Notkun:		 line = makeLine();
  	//EftirskilyrÃƒÆ’Ã‚Â°i: line er nÃƒÆ’Ã‚Âºna view hlutur sem er einfÃƒÆ’Ã‚Â¶ld, ÃƒÆ’Ã‚Â¾unn, grÃƒÆ’Ã‚Â¡ lÃƒÆ’Ã‚Â­na.
	public static View makeLine(Context context){
		 View v = new View(context);
		 
		 v.setLayoutParams(new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, 1, (float) 0.80));
		 v.setBackgroundColor(Color.rgb(203,203,203));
		 return v;
	}
	
	//Notkun: progressDialog = showProgressDialog(title, msg, context)
	//Eftir:  progressDialog hefur verid birt me� titlinum title og skilabodinu msg
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
}
