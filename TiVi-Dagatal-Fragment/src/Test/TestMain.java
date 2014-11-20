/**
 * Nafn: 		Kristin Fjola Tomasdottir
 * Dagsetning: 	17.oktober 2014
 * Markmid: 	Klasinn thjonar theim tilgangi ad profa ymisskonar foll og 
 * 				virkni i forritinu med JUnit profunum.
 */
package Test;

import java.util.ArrayList;
import java.util.List;

import Dtos.Show;
import Utils.VariousUtils;
import android.test.ActivityInstrumentationTestCase2;

import com.example.tivi_dagatal_fragment.MainActivity;
import com.example.tivi_dagatal_fragment.R;

public class TestMain extends ActivityInstrumentationTestCase2<MainActivity> {
	
	private MainActivity mTestMain;
	
	public TestMain() {
        super(MainActivity.class);
    }
	 
	//Notkun: setUp()
	//Eftir:  activity hefur verid sett upp til ad profa forritid med
	@Override
    protected void setUp() throws Exception {
        super.setUp();
        mTestMain = getActivity();
    }
	
	//Notkun: testPreconditions()
	//Eftir:  athugad hefur verid hvort activity-id til ad profa med
	//		  se ekki null 
	public void testPreconditions() {
	    assertNotNull("mTestMain is null", mTestMain);
	}
	
	//Notkun: testTranslateWeekday()
	//Eftir:  Thad hafa verid framkvaemdar profanir a fallinu
	public void testTranslateWeekday(){
		String test = mTestMain.getResources().getString(R.string.mon_en);
		String expected = mTestMain.getResources().getString(R.string.mon_is);
		assertEquals(expected, VariousUtils.translateWeekday(test, mTestMain));
		test = mTestMain.getResources().getString(R.string.tue_en);
		expected = mTestMain.getResources().getString(R.string.tue_is);
		assertEquals(expected, VariousUtils.translateWeekday(test, mTestMain));
		test = mTestMain.getResources().getString(R.string.wed_en);
		expected = mTestMain.getResources().getString(R.string.wed_is);
		assertEquals(expected, VariousUtils.translateWeekday(test, mTestMain));
		test = mTestMain.getResources().getString(R.string.thu_en);
		expected = mTestMain.getResources().getString(R.string.thu_is);
		assertEquals(expected, VariousUtils.translateWeekday(test, mTestMain));
		test = mTestMain.getResources().getString(R.string.fri_en);
		expected = mTestMain.getResources().getString(R.string.fri_is);
		assertEquals(expected, VariousUtils.translateWeekday(test, mTestMain));
		test = mTestMain.getResources().getString(R.string.sat_en);
		expected = mTestMain.getResources().getString(R.string.sat_is);
		assertEquals(expected, VariousUtils.translateWeekday(test, mTestMain));
		test = mTestMain.getResources().getString(R.string.sun_en);
		expected = mTestMain.getResources().getString(R.string.sun_is);
		assertEquals(expected, VariousUtils.translateWeekday(test, mTestMain));
	}
	
	//Notkun: testParseAirTime()
	//Eftir:  Thad hafa verid framkvaemdar profanir a fallinu
	public void testParseAirTime(){
		String test = "8:00pm";
		String expected = "20:00";
		assertEquals(expected, VariousUtils.parseAirTime(test));
		test = "10:00pm";
		expected = "22:00";
		assertEquals(expected, VariousUtils.parseAirTime(test));
		test = "10:00am";
		expected = "10:00";
		assertEquals(expected, VariousUtils.parseAirTime(test));
		test = "11:00am";
		expected = "11:00";
		assertEquals(expected, VariousUtils.parseAirTime(test));
		test = "03:00pm";
		expected = "15:00";
		assertEquals(expected, VariousUtils.parseAirTime(test));
	}
	
	//Notkun: testParseAirTime()
	//Eftir:  Thad hafa verid framkvaemdar profanir a fallinu
	public void testFlushCache(){
		List<Show> popularShows = new ArrayList<Show>();
		popularShows.add(new Show("The Walking Dead"));
		mTestMain.getCache().put("popularShows", popularShows);
		VariousUtils.flushCache("popularShows");
		assertNull("popularShows cache is not empty", mTestMain.getCache().get("popularShows"));
	}
}
