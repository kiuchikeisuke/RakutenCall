package jp.ne.nissing.rakutencall.preference;

import jp.ne.nissing.rakutencall.R;
import android.content.Context;
import android.preference.*;
import android.util.AttributeSet;
import android.widget.Toast;

public class SpecialNumIgnoreCheckBoxPreference extends CheckBoxPreference {

    private Context mContext = null;
    
    public SpecialNumIgnoreCheckBoxPreference(Context context,
            AttributeSet attrs) {
        super(context, attrs);
        mContext = context; 
        this.setKey(SharedPreferenceManager.KEY_SPECIAL_NUM_IGNORE);
        
        init();
    }

    private void init() {
        this.setSummaryOn(R.string.checkbox_special_num_ignore_on);
        this.setSummaryOff(R.string.checkbox_special_num_ignore_off);
        
        this.setChecked(SharedPreferenceManager.getInstance(mContext).getSpecialNumIgnore());
        
        this.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if(newValue instanceof Boolean == false){
                    return false;
                }
                Boolean isChecked = (Boolean) newValue;
                SharedPreferenceManager.getInstance(mContext).setSpecialNumIgnore(isChecked);
                if(isChecked){
                    Toast.makeText(mContext, R.string.toast_special_num_ignore_on, Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(mContext, R.string.toast_special_num_ignore_off, Toast.LENGTH_LONG).show();
                }
                
                return true;
            }
        });
    }
}
