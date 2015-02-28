package jp.ne.nissing.rakutencall.preference;

import jp.ne.nissing.rakutencall.R;
import android.content.Context;
import android.preference.*;
import android.util.AttributeSet;
import android.widget.Toast;

public class InternationalNumCheckBoxPreference extends CheckBoxPreference {

    private Context mContext = null;
    
    public InternationalNumCheckBoxPreference(Context context,
            AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        this.setKey(SharedPreferenceManager.KEY_INTERNATIONAL_NUM);
        
        init();
    }

    private void init() {
        this.setSummaryOn(R.string.checkbox_international_num_on);
        this.setSummaryOff(R.string.checkbox_international_num_off);
        
        this.setChecked(SharedPreferenceManager.getInstance(mContext).getInternationalNum());
        
        this.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if(newValue instanceof Boolean == false){
                    return false;
                }
                Boolean isChecked = (Boolean) newValue;
                SharedPreferenceManager.getInstance(mContext).setInternationalNum(isChecked);
                
                if(isChecked){
                    Toast.makeText(mContext, R.string.toast_international_num_on, Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(mContext, R.string.toast_international_num_off, Toast.LENGTH_LONG).show();
                }
                
                return true;
            }
        });
    }

}
