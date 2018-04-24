package jp.ne.nissing.rakutencall.preference;

import jp.ne.nissing.rakutencall.R;
import android.content.Context;
import android.preference.*;
import android.util.AttributeSet;
import android.widget.Toast;

public class AdBuddizCheckBoxPreference extends CheckBoxPreference {

    private final Context mContext;
    
    public AdBuddizCheckBoxPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        this.setKey(SharedPreferenceManager.KEY_ADBUDDIZ);
        
        init();
    }

    private void init() {
        this.setSummaryOn(R.string.checkbox_adbuddiz_summary_on);
        this.setSummaryOff(R.string.checkbox_adbuddiz_summary_off);
        
        this.setChecked(SharedPreferenceManager.getInstance(mContext).getIsAdbuddizHide());
        
        this.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if(newValue instanceof Boolean == false){
                    return false;
                }
                
                Boolean isChecked = (Boolean) newValue;
                
                SharedPreferenceManager.getInstance(mContext).setIsAdbuddizHide(isChecked);
                if(isChecked){
                    Toast.makeText(mContext, R.string.toast_adbuddiz_on, Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(mContext, R.string.toast_adbuddiz_off, Toast.LENGTH_SHORT).show();
                }
                
                return true;
            }
        });
    }

}
