package jp.ne.nissing.rakutencall;

import android.content.*;

public class SharedPreferenceManager {
	private static SharedPreferenceManager instance = null;
	private SharedPreferences pref;
	private SharedPreferences.Editor editor;
    private static final String PREF_KEY = "jp.ne.nissing.rakutencall";
    private static final String KEY_PACKAGE = "package_name";
    private static final String KEY_ACTIVITY = "activity_name";
	private static final String DEFAULT_VALUE_PACKAGE = "com.android.phone";
	private static final String DEFALUT_VALUE_ACTIVITY = "com.android.phone.OutgoingCallBroadcaster";
    
	public static SharedPreferenceManager getInstance(Context context){
		if(instance == null){
			instance = new SharedPreferenceManager(context);
		}
		return instance;
	}
	
	private SharedPreferenceManager(Context context){
		pref = context.getSharedPreferences(PREF_KEY,Context.MODE_PRIVATE);
		editor = pref.edit();
	}
	
	public void setDefaultPhoneApp(String packageName,String activityName){
		editor.putString(KEY_PACKAGE, packageName);
		editor.putString(KEY_ACTIVITY, activityName);
		editor.commit();
	}
	
	public PhoneActivityData getDefaultPhoneApp(){
		String packageName = pref.getString(KEY_PACKAGE, DEFAULT_VALUE_PACKAGE);
		String activityName = pref.getString(KEY_ACTIVITY, DEFALUT_VALUE_ACTIVITY);
		return new PhoneActivityData(null, null, packageName, activityName);
	}
}
