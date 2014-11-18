package Test;

import org.junit.*;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;

import com.example.tivi_dagatal_fragment.MainActivity;
import com.example.tivi_dagatal_fragment.R;

import Utils.VariousUtils;

public class TestMain extends ActivityInstrumentationTestCase2<MainActivity> {
	
	private MainActivity mTestMain;
	
	public TestMain() {
        super(MainActivity.class);
    }
	 
	@Override
    protected void setUp() throws Exception {
        super.setUp();
        mTestMain = getActivity();
    }
	
	public void testPreconditions() {
	    assertNotNull("mTestMain is null", mTestMain);
	}
	
	public void testTranslateWeekday(){
		String test = mTestMain.getResources().getString(R.string.thu_en);
		String expected = mTestMain.getResources().getString(R.string.thu_is);
		assertEquals(expected, VariousUtils.translateWeekday(test, mTestMain));
	}
	
	public void testParseAirTime(){
		String test = "8:00pm";
		String expected = "20:00";
		assertEquals(expected, VariousUtils.parseAirTime(test));
	}
	
	public void testFlushCache(){
		VariousUtils.flushCache("popularShows");
		assertNull(MainActivity.getCache().get("popularShows"));
	}
}
