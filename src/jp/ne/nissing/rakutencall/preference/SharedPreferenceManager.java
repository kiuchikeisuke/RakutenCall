package jp.ne.nissing.rakutencall.preference;

import jp.ne.nissing.rakutencall.preference.phoneappdata.PhoneActivityData;
import android.content.*;
import android.text.TextUtils;

public class SharedPreferenceManager {
    private static SharedPreferenceManager instance = null;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    static final String PREF_KEY = "jp.ne.nissing.rakutencall";
    static final String KEY_PACKAGE = "package_name";
    static final String KEY_ACTIVITY = "activity_name";
    static final String KEY_PREFIX_NUM = "prefix_num";
    static final String KEY_FREE_DIAL = "free_dial";
    static final String KEY_ADBUDDIZ = "show_adbuddiz";
    static final String KEY_PREFIX_ENABLE = "prefix_enable";
    static final String KEY_SPECIAL_NUM_IGNORE = "special_num";
    static final String KEY_INTERNATIONAL_NUM = "international_num";
    
    static final String DEFAULT_VALUE_PACKAGE = "com.android.phone";
    static final String DEFALUT_VALUE_ACTIVITY = "com.android.phone.OutgoingCallBroadcaster";
    static final String DEFAULT_VALUE_PREFIX_NUM = "003768";
    static final boolean DEFAULT_VALUE_FREE_DIAL_IGNORE = true;
    static final boolean DEFAULT_VALUE_ADBUDDIZ = false;
    static final boolean DEFAULT_VALUE_PREFIX_ENABLE = true;
    static final boolean DEFAULT_VALUE_SPECIAL_NUM_IGNORE = true;
    static final boolean DEFAULT_VALUE_INTERNATIONAL_NUM = true;

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

    public void setDefaultPrefixNum(String prefixNum){
        editor.putString(KEY_PREFIX_NUM, TextUtils.isEmpty(prefixNum) ? DEFAULT_VALUE_PREFIX_NUM : prefixNum);
        editor.commit();
    }

    public String getDefaultPrefixNum(){
        return pref.getString(KEY_PREFIX_NUM, DEFAULT_VALUE_PREFIX_NUM);
    }
    
    public void setFreeDialIgnoreCheck(boolean value){
        editor.putBoolean(KEY_FREE_DIAL, value);
        editor.commit();
    }
    
    public boolean getFreeDialIgnoreCheck(){
        return pref.getBoolean(KEY_FREE_DIAL, DEFAULT_VALUE_FREE_DIAL_IGNORE);
    }

    public void setIsAdbuddizHide(boolean value){
        editor.putBoolean(KEY_ADBUDDIZ, value);
        editor.commit();
    }
    
    public boolean getIsAdbuddizHide() {
        return pref.getBoolean(KEY_ADBUDDIZ, DEFAULT_VALUE_ADBUDDIZ);
    }
    
    public boolean getPrefixEnable(){
        return pref.getBoolean(KEY_PREFIX_ENABLE, DEFAULT_VALUE_PREFIX_ENABLE);
    }
    
    public void setPrefixEnable(boolean value){
        editor.putBoolean(KEY_PREFIX_ENABLE, value);
        editor.commit();
    }
    
    public void setSpecialNumIgnore(boolean value){
        editor.putBoolean(KEY_SPECIAL_NUM_IGNORE, value);
        editor.commit();
    }
    
    public boolean getSpecialNumIgnore(){
        return pref.getBoolean(KEY_SPECIAL_NUM_IGNORE, DEFAULT_VALUE_SPECIAL_NUM_IGNORE);
    }
    
    public void setInternationalNum(boolean value){
        editor.putBoolean(KEY_INTERNATIONAL_NUM, value);
        editor.commit();
    }
    
    public boolean getInternationalNum(){
        return pref.getBoolean(KEY_INTERNATIONAL_NUM, DEFAULT_VALUE_INTERNATIONAL_NUM);
    }
}
