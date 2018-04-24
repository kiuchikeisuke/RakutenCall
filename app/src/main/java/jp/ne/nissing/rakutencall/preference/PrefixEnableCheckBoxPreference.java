package jp.ne.nissing.rakutencall.preference;

import jp.ne.nissing.rakutencall.R;
import android.content.Context;
import android.preference.*;
import android.util.AttributeSet;
import android.widget.Toast;

public class PrefixEnableCheckBoxPreference extends CheckBoxPreference {

    private Context mContext = null;
    
    public PrefixEnableCheckBoxPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        this.setKey(SharedPreferenceManager.KEY_PREFIX_ENABLE);
        
        init();
    }
    
    private void init(){
        this.setSummaryOn(R.string.checkbox_prefix_enable_summary_on);
        this.setSummaryOff(R.string.checkbox_prefix_enable_summary_off);
        
        this.setChecked(SharedPreferenceManager.getInstance(mContext).getPrefixEnable());
        
        this.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if(newValue instanceof Boolean == false){
                    return false;
                }
                Boolean isChecked = (Boolean) newValue;
                
                SharedPreferenceManager.getInstance(mContext).setPrefixEnable(isChecked);
                if(isChecked){
                    Toast.makeText(mContext, R.string.toast_prefix_enable_on, Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(mContext, R.string.toast_prefix_enable_off, Toast.LENGTH_LONG).show();
                }
                
                return true;
            }
        });
    }

}
