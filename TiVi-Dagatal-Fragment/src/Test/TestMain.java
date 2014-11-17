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
		String expected = mTestMain.getResources().getString(R.string.thu_is);
		String test = mTestMain.getResources().getString(R.string.thu_en);
		assertEquals(expected, VariousUtils.translateWeekday(test, mTestMain));
	}
	
	public void testParseAirTime(){
		
	}
}
